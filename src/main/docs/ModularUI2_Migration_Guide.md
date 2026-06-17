# ModularUI 2 (MUI2) 多方块 GUI 迁移与开发参考手册

> **目标读者**：AI Agent。本文档旨在提供足够详尽的上下文，使 Agent 能独立完成 MUI1→MUI2 迁移，以及从零编写 MUI2 GUI。

---

## 目录

1. [架构概览](#1-架构概览)
2. [MUI1 vs MUI2：双路径共存机制](#2-mui1-vs-mui2双路径共存机制)
3. [MTEMultiBlockBaseGui 完整方法参考](#3-mtemultiblockbasegui-完整方法参考)
4. [同步值（SyncValue）类型大全](#4-同步值syncvalue类型大全)
5. [布局与容器 Widget](#5-布局与容器-widget)
6. [基础 Widget 目录](#6-基础-widget-目录)
7. [迁移模式详解](#7-迁移模式详解)
8. [完整代码示例](#8-完整代码示例)
9. [常见问题与反模式](#9-常见问题与反模式)
10. [快速导入速查](#10-快速导入速查)

---

## 1. 架构概览

### 1.1 类继承链

```
MetaTileEntity                          (gregtech.api.metatileentity)
  ├── useMui2() → false (默认)
  ├── buildUI() → null (待子类复写)
  │
  └── CommonMetaTileEntity
        │
        └── MTEMultiBlockBase           (gregtech.api.metatileentity.implementations)
              ├── useMui2() → true ★ 多方块强制走 MUI2
              ├── buildUI() → final → getGui().build(...)
              ├── getGui()  → new MTEMultiBlockBaseGui<>(this)
              │
              ├── MTEEnhancedMultiBlockBase
              │     └── MTEExtendedPowerMultiBlockBase
              │           └── GTPPMultiBlockBase<T>  (gtPlusPlus)
              │                 ├── getGui() → 默认 (可复写)
              │                 │
              │                 └── ThT_ImplosionGenerator  ← ThinkTech 多方块
              │                         └── getGui() → new ThT_ImplosionGeneratorGui(this)
              │
              └── (其他多方块)
```

### 1.2 GUI 类继承链

```
MTEMultiBlockBaseGui<T extends MTEMultiBlockBase>   (gregtech.common.gui.modularui.multiblock.base)
  ├── 字段:
  │     ├── protected final T multiblock          ← 引用 MTE 实例
  │     ├── protected final IGregTechTileEntity baseMetaTileEntity
  │     ├── protected Map<String, IPanelHandler> panelMap  ← 弹出面板注册表
  │     └── protected List<UITexture> machineModeIcons
  │
  ├── build() → 构建完整 GUI 面板
  │     ├── setMachineModeIcons()
  │     ├── registerSyncValues(syncManager)       ★ 复写以注册自定义同步
  │     ├── getBasePanel()                        ★ 复写以定制基础面板
  │     ├── initPanelMap()
  │     └── panel.child(createMainColumn())
  │
  └── createMainColumn()
        ├── createTerminalRow()
        │     └── createTerminalTextWidget()  ★ 复写以添加信息文本
        ├── createPanelGap()
        │     ├── createLeftPanelGapRow()     ★ 复写以添加按钮
        │     └── createRightPanelGapRow()    ★ 复写以添加按钮
        └── createInventoryRow()
```

### 1.3 关键流程：GUI 打开时发生了什么

```
玩家右键机器
  → CommonMetaTileEntity.openGui(player)
    → if (GTGuis.GLOBAL_SWITCH_MUI2 && useMui2())  // 多方块 useMui2()=true
      → MetaTileEntityGuiHandler.open(player, this)
        → buildUI(guiData, syncManager, uiSettings)  ← 被调用
          → getGui().build(...)  ← 最终入口
            → registerSyncValues(syncManager)        ← 1. 注册所有数据同步
            → getBasePanel()                         ← 2. 创建空白面板骨架
            → panel.child(createMainColumn(...))     ← 3. 构建所有子组件
```

---

## 2. MUI1 vs MUI2：双路径共存机制

### 2.1 代码路径分支

```java
// CommonMetaTileEntity.java:615
public void openGui(EntityPlayer player) {
    if ((GTGuis.GLOBAL_SWITCH_MUI2 && useMui2()) || forceUseMui2()) {
        // === MUI2 路径 ===
        MetaTileEntityGuiHandler.open(player, this);
        // → 最终调用 buildUI() → getGui().build(...)
    } else {
        // === MUI1 路径 (旧) ===
        GTUIInfos.openGTTileEntityUI(getBaseMetaTileEntity(), player);
        // → 调用 addUIWidgets(ModularWindow.Builder, UIBuildContext)
        // → 内部调用 drawTexts(DynamicPositionedColumn, SlotWidget)
    }
}
```

### 2.2 多方块永远走 MUI2

```java
// MTEMultiBlockBase.java:3363
@Override
protected boolean useMui2() {
    return true;  // 多方块固定返回 true
}
```

**推论**：所有继承自 `MTEMultiBlockBase` 的 ThinkTech 多方块的 `addUIWidgets()` 和 `drawTexts()` 方法**永远不会被调用**，它们是死代码，必须迁移。

### 2.3 迁移信号

当你在 MTE 类中发现以下代码时，说明需要迁移：

| 信号 | 含义 |
|------|------|
| `drawTexts(DynamicPositionedColumn, SlotWidget)` 复写 | MUI1 文本显示，死代码 |
| `drawTextsNoPlayerInventory(DynamicPositionedColumn)` 复写 | MUI1 无背包版，死代码 |
| `addUIWidgets(ModularWindow.Builder, UIBuildContext)` 复写 | MUI1 主入口，死代码 |
| `import com.gtnewhorizons.modularui.common.widget.*` | MUI1 导入，需移除 |
| `FakeSyncWidget.*Syncer` | MUI1 同步器，需替换 |

---

## 3. MTEMultiBlockBaseGui 完整方法参考

### 3.1 build() — GUI 构建入口

```java
// 行143-149，final 方法，不可复写
public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
    setMachineModeIcons();
    registerSyncValues(syncManager);             // ① 先注册同步
    ModularPanel panel = getBasePanel(guiData, syncManager, uiSettings);  // ② 创建面板
    initPanelMap(panel, syncManager);            // ③ 初始化弹出面板
    return panel.child(createMainColumn(panel, syncManager));  // ④ 添加主列
}
```

### 3.2 getBasePanel() — 可复写以调整面板尺寸

```java
// 行162-169
protected ModularPanel getBasePanel(PosGuiData guiData, PanelSyncManager syncManager,
                                     UISettings uiSettings) {
    return new GTBaseGuiBuilder(multiblock, guiData, syncManager, uiSettings)
        .setWidth(getBasePanelWidth())          // 默认 176
        .setHeight(getBasePanelHeight())         // 默认 166
        .doesAddGregTechLogo(false)              // 默认 false（多方块用自定义 logo）
        .doesBindPlayerInventory(false)          // 默认 false（多方块自行创建库存行）
        .build();
}
```

**复写场景**：需要自定义面板宽度/高度时。

```java
@Override
protected ModularPanel getBasePanel(PosGuiData guiData, PanelSyncManager syncManager,
                                     UISettings uiSettings) {
    return new GTBaseGuiBuilder(multiblock, guiData, syncManager, uiSettings)
        .setWidth(220)   // 自定义宽度
        .setHeight(200)  // 自定义高度
        .doesBindPlayerInventory(false)
        .build();
}
```

### 3.3 GTBaseGuiBuilder — 面板构建器 API

```java
// GTBaseGuiBuilder 完整 API
public class GTBaseGuiBuilder {
    public GTBaseGuiBuilder(IMetaTileEntity mte, PosGuiData guiData,
                            PanelSyncManager syncManager, UISettings uiSettings) { }

    public GTBaseGuiBuilder setWidth(int width) { }              // 面板宽度 (默认176)
    public GTBaseGuiBuilder setHeight(int height) { }            // 面板高度 (默认166)
    public GTBaseGuiBuilder doesBindPlayerInventory(boolean b){} // 绑定玩家背包
    public GTBaseGuiBuilder doesAddTitle(boolean b) { }          // 添加标题标签
    public GTBaseGuiBuilder doesAddCoverTabs(boolean b) { }      // 添加覆盖板标签
    public GTBaseGuiBuilder doesAddGhostCircuitSlot(boolean b){} // 添加幽灵电路槽
    public GTBaseGuiBuilder doesAddGregTechLogo(boolean b) { }   // 添加 GT logo
    public GTBaseGuiBuilder moveGregtechLogoPos(int x, int y) {} // 移动 logo 位置
    public ModularPanel build() { }                               // 构建并返回面板
}
```

### 3.4 createTerminalTextWidget() — ★最常复写★

左侧终端显示区域的文本容器。返回 `ListWidget<IWidget, ?>`，可链式 `.child(...)` 追加。

```java
// 行265-305，精简版
protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager,
                                                           ModularPanel parent) {
    IntSyncValue startupCheckSyncer = new IntSyncValue(multiblock::getmStartUpCheck);
    StringSyncValue machineModeSyncer = new StringSyncValue(multiblock::getMachineModeName);
    syncManager.syncValue("startupCheck", startupCheckSyncer);
    syncManager.syncValue("machineModeName", machineModeSyncer);

    return new ListWidget<>()
        .fullWidth()
        .crossAxisAlignment(Alignment.CrossAxis.START)

        // 机器模式（条件显示）
        .childIf(multiblock.supportsMachineModeSwitch(),
            () -> IKey.dynamic(() -> StatCollector.translateToLocalFormatted(
                    "gt.interact.desc.mb.mode", machineModeSyncer.getStringValue()))
                .asWidget().marginBottom(2).fullWidth())

        // "启动中" 行
        .child(IKey.lang("GT5U.multiblock.startup")
            .color(Color.WHITE.main).asWidget()
            .textAlign(Alignment.CenterLeft)
            .setEnabledIf(w -> startupCheckSyncer.getValue() > 0)
            .marginBottom(2).fullWidth())

        // "运行中" 行（条件显示）
        .childIf(multiblock.hasRunningText(),
            () -> new TextWidget<>(StatCollector.translateToLocalFormatted("gt.interact.desc.mb.running"))
                .color(Color.WHITE.main)
                .setEnabledIf(w -> multiblock.getErrorDisplayID() == 0
                                && baseMetaTileEntity.isActive())
                .marginBottom(2).fullWidth())

        // 关机时长 / 关机原因 / 结构错误 / 配方结果 / 配方信息
        .child(createShutdownDurationWidget(syncManager))
        .child(createShutdownReasonWidget(syncManager))
        .child(createStructureErrorWidget(syncManager))
        .child(createRecipeResultWidget())
        .childIf(multiblock.showRecipeTextInGUI(), () -> createRecipeInfoTextWidget(syncManager))
        .childIf(multiblock.showRecipeTextInGUI(), () -> createRecipeInfoWidget(syncManager));
}
```

**复写模板**：

```java
@Override
protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager,
                                                           ModularPanel parent) {
    // 1. 注册自定义同步值（也可在 registerSyncValues 中注册后在此获取）
    LongSyncValue mySyncer = new LongSyncValue(
        multiblock::getSomeValue, multiblock::setSomeValue);
    syncManager.syncValue("myKey", mySyncer);

    // 2. 调用父类保留标准显示，追加自定义文本行
    return super.createTerminalTextWidget(syncManager, parent)

        // 静态文本（固定显示）
        .child(IKey.lang("mymod.my.key").color(Color.WHITE.main).asWidget()
            .marginBottom(2).fullWidth())

        // 动态文本
        .child(IKey.dynamic(() -> "EU/t: " + numberFormat.format(mySyncer.getValue()))
            .color(Color.WHITE.main).asWidget()
            .textAlign(Alignment.CenterLeft)
            .setEnabledIf(w -> multiblock.getErrorDisplayID() == 0)
            .marginBottom(2).fullWidth())

        // 条件显示的文本
        .childIf(multiblock.someCondition(),
            () -> IKey.dynamic(() -> "Additional Info").color(Color.WHITE.main).asWidget()
                .marginBottom(2).fullWidth());
}
```

#### 可链式调用的 Widget 方法速查

```java
.asWidget()                          // IKey → Widget 转换
.color(int argb)                     // 设置文本颜色
.textAlign(Alignment.CenterLeft)     // 对齐: CenterLeft, Center, CenterRight
.marginBottom(int pixels)            // 底部边距
.marginTop(int pixels)               // 顶部边距
.marginLeft(int pixels)              // 左侧边距
.marginRight(int pixels)             // 右侧边距
.fullWidth()                         // 占满父容器宽度
.setEnabledIf(Predicate<IWidget>)   // 条件显示(隐藏时保留空间)
.childIf(boolean, Supplier<Widget>)  // 条件添加子组件（条件不满足时不创建）
.widgetTheme(WidgetThemeKey)         // 应用主题
.setPos(int x, int y)               // 绝对/相对位置
.size(int w, int h)                  // 固定尺寸
.coverChildrenHeight()               // 高度自适应子组件
```

### 3.5 createButtonColumn() — 右侧按钮列

```java
// 行1231
protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
    return Flow.column()
        .child(createPowerSwitchButton())                   // 电源开关
        .childIf(shouldDisplayVoidExcess(),
            () -> createVoidExcessButton(syncManager))      // 销毁模式
        .child(createMuffleButton())                        // 消音按钮
        .childIf(shouldDisplayInputSeparation(),
            () -> createInputSeparationButton(syncManager)) // 输入分离
        .childIf(multiblock.supportsMachineModeSwitch(),
            () -> createModeSwitchButton(syncManager))      // 机器模式
        .childIf(shouldDisplayBatchMode(),
            () -> createBatchModeButton(syncManager))       // 批处理模式
        .childIf(shouldDisplayRecipeLock(),
            () -> createLockToSingleRecipeButton(syncManager)) // 配方锁定
        .child(createStructureUpdateButton(syncManager))    // 结构更新
        .child(createPowerPanelButton(syncManager, panel)); // 能源面板
}
```

**复写模板**：

```java
@Override
protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
    return super.createButtonColumn(panel, syncManager)
        .child(createMyCustomButton(syncManager));
}
```

### 3.6 createLeftPanelGapRow() / createRightPanelGapRow() — 间隙按钮行

```java
// 行713/724
protected Flow createLeftPanelGapRow(ModularPanel panel, PanelSyncManager syncManager) {
    return Flow.row();  // 默认空
}
protected Flow createRightPanelGapRow(ModularPanel panel, PanelSyncManager syncManager) {
    return Flow.row();  // 默认空
}
```

**复写示例**（来自 LHC）：
```java
@Override
protected Flow createRightPanelGapRow(ModularPanel panel, PanelSyncManager syncManager) {
    return super.createRightPanelGapRow(panel, syncManager)
        .child(createProbTableButton(syncManager, panel))
        .child(createCalculatorButton(syncManager, panel));
}
```

### 3.7 registerSyncValues() — 数据同步注册

```java
// 行1268-1438，部分列举
protected void registerSyncValues(PanelSyncManager syncManager) {
    syncManager.syncValue("structureErrors", multiblock.getStructureErrorsSyncer());
    syncManager.syncValue("errorID",
        new IntSyncValue(multiblock::getErrorDisplayID, multiblock::setErrorDisplayID));
    syncManager.syncValue("machineActive",
        new BooleanSyncValue(baseMetaTileEntity::isActive, baseMetaTileEntity::setActive));
    syncManager.syncValue("totalRunTime",
        new LongSyncValue(multiblock::getTotalRunTime, multiblock::setTotalRunTime));
    syncManager.syncValue("progressTime",
        new IntSyncValue(() -> multiblock.mProgresstime, val -> multiblock.mProgresstime = val));
    syncManager.syncValue("maxProgressTime",
        new IntSyncValue(() -> multiblock.mMaxProgresstime, val -> multiblock.mMaxProgresstime = val));
    syncManager.syncValue("checkRecipeResult", ...);
    syncManager.syncValue("powerSwitch", ...);
    syncManager.syncValue("batchMode", ...);
    syncManager.syncValue("recipeLock", ...);
    syncManager.syncValue("voidExcess", ...);
    syncManager.syncValue("maintCount", ...);
    syncManager.syncValue("mufflerSyncer", ...);
    // ... 更多
}
```

### 3.8 条件显示控制方法（可复写）

| 方法 | 默认返回值 | 控制内容 |
|------|-----------|----------|
| `shouldDisplayVoidExcess()` | `multiblock.supportsVoidProtection()` | 销毁模式按钮 |
| `shouldDisplayInputSeparation()` | `multiblock.supportsInputSeparation()` | 输入分离按钮 |
| `shouldDisplayBatchMode()` | `multiblock.supportsBatchMode()` | 批处理模式按钮 |
| `shouldDisplayRecipeLock()` | `multiblock.supportsSingleRecipeLocking()` | 配方锁定按钮 |
| `showOutputRates()` | `false` | 配方输出在 NEI 中的速率 |

### 3.9 尺寸相关方法

| 方法 | 默认返回值 | 用途 |
|------|-----------|------|
| `getBasePanelWidth()` | `176` | 面板宽度 |
| `getBasePanelHeight()` | `166` | 面板高度 |
| `getTerminalRowWidth()` | `200` | 终端行宽度 |
| `getTerminalRowHeight()` | `getTerminalWidgetHeight()` | 终端行高度 |
| `getTerminalWidgetWidth()` | `150` | 终端文本区宽度 |
| `getTerminalWidgetHeight()` | `132` | 终端文本区高度 |
| `getTextBoxToInventoryGap()` | `80` | 文本与库存间距 |

---

## 4. 同步值（SyncValue）类型大全

### 4.1 基础同步值

服务端→客户端单向同步（默认），客户端→服务端需加 `.allowC2S()`。

| 类型 | 构造器 | 用途 |
|------|--------|------|
| `IntSyncValue(IntSupplier getter)` | 只读 | 只从服务端读 |
| `IntSyncValue(IntSupplier getter, IntConsumer setter)` | 读写 | 标准双向 |
| `LongSyncValue(LongSupplier getter)` | 只读 | 只从服务端读 |
| `LongSyncValue(LongSupplier getter, LongConsumer setter)` | 读写 | 标准双向 |
| `DoubleSyncValue(DoubleSupplier getter)` | 只读 | 只从服务端读 |
| `DoubleSyncValue(DoubleSupplier getter, DoubleConsumer setter)` | 读写 | 标准双向 |
| `BooleanSyncValue(BooleanSupplier getter)` | 只读 | 只从服务端读 |
| `BooleanSyncValue(BooleanSupplier getter, BooleanConsumer setter)` | 读写 | 标准双向 |
| `StringSyncValue(Supplier<String> getter)` | 只读 | 只从服务端读 |
| `StringSyncValue(Supplier<String> getter, Consumer<String> setter)` | 读写 | 标准双向 |

### 4.2 高级同步值

| 类型 | 说明 |
|------|------|
| `EnumSyncValue<T, ?>` | 枚举类型同步。`new EnumSyncValue<>(MyEnum.class, getter, setter).allowC2S()` |
| `GenericSyncValue<T, ?>` | 任意对象同步，需要提供 Adapter 进行序列化/反序列化 |
| `GenericListSyncHandler<T>` | 列表同步，需要序列化/反序列化每项元素 |
| `BigIntSyncValue` | 大整数同步（TecTech 风格） |
| `InteractionSyncHandler` | 一次性操作同步（按钮点击等，不需要持久化值的场景） |

### 4.3 注册与获取

```java
// 注册（通常放在 registerSyncValues 或 createTerminalTextWidget 中）
LongSyncValue myVal = new LongSyncValue(
    multiblock::getMyValue,    // Server→Client getter
    multiblock::setMyValue      // Client→Server setter (可选)
);
syncManager.syncValue("myValueKey", myVal);  // Key 必须唯一

// 获取（在 createTerminalTextWidget 中从上文获取）
LongSyncValue myVal = syncManager.findSyncHandler("myValueKey", LongSyncValue.class);
long currentValue = myVal.getValue();  // 客户端可获取最新同步值
```

### 4.4 `isMachineOn()` / `getErrorDisplayID()` 等价检查对照表

检查 `multiblock.getErrorDisplayID()` 是判断机器是否正常工作的标准方式：

| `getErrorDisplayID()` | 含义 |
|----------------------|------|
| `0` | 机器正常，无错误 |
| `1` | 结构不完整 |
| `2` | 维护问题 |
| 其他 | 特定错误码 |

```java
// 标准可见性条件：机器已成型 且 无错误
.setEnabledIf(w -> multiblock.mMachine)
// 或：机器正常运转
.setEnabledIf(w -> multiblock.getErrorDisplayID() == 0)
// 或组合
.setEnabledIf(w -> baseMetaTileEntity.isActive() && multiblock.getErrorDisplayID() == 0)
```

---

## 5. 布局与容器 Widget

### 5.1 Flow — 弹性布局

```java
// 纵向排列
Flow.column()
    .child(widget1)
    .child(widget2)
    .coverChildrenHeight()  // 高度自适应子组件
    .fullWidth()            // 宽度撑满父容器

// 横向排列
Flow.row()
    .child(widget1)
    .child(widget2)
    .horizontalCenter()     // 水平居中
    .topRel(0.25f)          // 相对父容器的顶部偏移 25%
    .size(72, 18)           // 固定尺寸

// 方法速查
Flow.column()         // 创建纵向流布局
Flow.row()            // 创建横向流布局
  .child(IWidget)     // 添加子组件
  .coverChildrenHeight()    // 高度 = 子组件高度之和
  .coverChildrenWidth()     // 宽度 = 子组件宽度之和
  .fullWidth()              // 100% 父容器宽度
  .centerLeft()             // 左中定位
  .centerRight()            // 右中定位
  .horizontalCenter()       // 水平居中
  .verticalCenter()         // 垂直居中
  .top(float)               // 绝对顶部偏移
  .topRel(float)            // 相对顶部偏移 (0.0~1.0)
  .leftRel(float)           // 相对左侧偏移
  .size(int w, int h)       // 固定尺寸
  .pos(int x, int y)        // 绝对定位
  .marginBottom(int)        // 外边距
  .marginTop(int)           // 外边距
```

### 5.2 ListWidget — 纵向列表

```java
new ListWidget<>()
    .fullWidth()
    .crossAxisAlignment(Alignment.CrossAxis.START)  // 子项左对齐/居中/右对齐
    .child(IKey.lang("key").asWidget())              // 添加静态文本子项
    .child(IKey.dynamic(() -> text).asWidget())      // 添加动态文本子项
    .childIf(condition, () -> widget)                // 条件添加
```

### 5.3 Grid — 网格布局

```java
// 模板字符串方式
SlotGroupWidget.builder()
    .matrix("III", "III")       // 2行×3列
    .key('I', index -> new ItemSlot()
        .slot(new ModularSlot(inventoryHandler, index).slotGroup("my_group")))
    .build()

// 编程式（等宽）
Grid().gridOfWidthHeight(width, height, (x, y, $index) -> {
    return new ButtonWidget<>().size(16, 16);
})

// 编程式（可变宽，元素列表映射）
Grid().gridOfWidthElements(5, itemList, ($x, $y, $index, item) -> {
    return new ItemDisplayWidget().item(item);
})
```

### 5.4 Panel / Popup — 弹出面板

```java
// 在 initPanelMap 或 build 中注册弹出面板
IPanelHandler popupHandler = IPanelHandler.synced("myPopupPanel",
    GTGuis.createPopUpPanel("myPopupPanel")
        .child(new TextWidget<>("Hello"))
        .child(CommonButtons.panelCloseButton())
);

// 在按钮中触发弹窗
ButtonWidget button = new ButtonWidget<>()
    .onMousePressed(mouseButton -> {
        if (mouseButton == MouseButton.LEFT) {
            popupHandler.openPanel(this.syncManager);
        }
        return InteractiveWidget.Result.ACCEPT;
    });
```

---

## 6. 基础 Widget 目录

### 6.1 文本类 Widget

```java
// 静态文本（lang key）
IKey.lang("gt.interact.desc.mb.mode")
    .color(Color.WHITE.main)
    .asWidget()
    .textAlign(Alignment.CenterLeft)
    .marginBottom(2)
    .fullWidth()

// 动态文本
IKey.dynamic(() -> "EU/t: " + numberFormat.format(someValueSyncer.getValue()))
    .color(Color.WHITE.main)
    .asWidget()
    .setEnabledIf(w -> someCondition)

// TextWidget（带 syncer 的文本，自动跟随同步值变化）
new TextWidget<>(StatCollector.translateToLocalFormatted("some.key"))
    .color(Color.WHITE.main)
    .setEnabledIf(widget -> multiblock.getErrorDisplayID() == 0 && baseMetaTileEntity.isActive())
    .marginBottom(2)
    .fullWidth()
```

### 6.2 按钮类 Widget

```java
// 标准按钮
new ButtonWidget<>()
    .size(16, 16)
    .overlay(UITexture.xxx)           // 覆盖图标
    .addTooltip(IKey.lang("tooltip.key"))
    .onMousePressed(mouseButton -> {
        // 处理点击
        someSyncer.setValue(newVal);
        return InteractiveWidget.Result.ACCEPT;
    })
    .setEnabledIf(w -> someCondition)

// 开关按钮
new ToggleButton()
    .size(16, 16)
    .value(booleanSyncer)
    .overlay(true, activeTexture)     // 开的图标
    .overlay(false, inactiveTexture)  // 关的图标
    .tooltip(true, "enabled key")     // 开的提示
    .tooltip(false, "disabled key")   // 关的提示
    .addTooltip(IKey.dynamic(...))    // 动态提示（自动跟随状态更新）

// 使用同步 Key 的开关按钮（更简洁）
new ToggleButton()
    .syncHandler("mySyncerKey")       // 通过 syncManager 的 key 查找 BooleanSyncValue
    .overlay(true, activeTex)
    .overlay(false, inactiveTex)
    .tooltip(true, "on_tooltip")
    .tooltip(false, "off_tooltip")

// 通用开关按钮工厂
CommonButtons.createToggleButton(booleanSyncer, overlayTex, "tooltip.key")
CommonButtons.createToggleButtonDynamicTooltip(booleanSyncer, overlayTex,
    tooltip -> tooltip.addLine(IKey.dynamic(() -> ...)))

// 点击执行一次性操作的按钮
ButtonWidget button = new ButtonWidget<>()
    .overlay(overlayTex)
    .onMousePressed(mouseButton -> {
        myInteractionSyncer.setValue(true);  // 触发服务端执行
        return InteractiveWidget.Result.ACCEPT;
    });

// InteractionSyncValue 示例
InteractionSyncHandler interactionSyncer = new InteractionSyncHandler(
    () -> { /* 服务端执行的逻辑 */ });
syncManager.syncValue("doAction", interactionSyncer);
```

### 6.3 槽位类 Widget

```java
// 物品槽位
new ItemSlot()
    .slot(new ModularSlot(inventoryHandler, index)   // 绑定库存处理器和槽位索引
        .slotGroup("my_slot_group")                   // 槽位组（用于配方NEI映射）
        .filter(stack -> stack.getItem() == ...))     // 物品过滤
    .widgetTheme(GTWidgetThemes.OVERLAY_ITEM_SLOT_INGOT)

// 物品槽位（内容显示用，不可交互）
new ItemSlot()
    .slot(new ModularSlot(inventoryHandler, index)
        .accessibility(false, true)   // canPut=false, canTake=true
        .slotGroup("my_slot_group"))
    .widgetTheme(GTWidgetThemes.ITEM_SLOT_DISPLAY)

// 流体槽位（大容量显示）
new FluidSlot()
    .syncHandler(new FluidSlotSyncHandler(fluidTank)
        .canFillSlot(false))           // 只能抽出不能填入
    .alwaysShowFull(false)             // 不总是显示满的
    .size(18, 54)
    .background(GTGuiTextures.DISPLAY_SLOT_GRAY)
    .overlay(GTGuiTextures.DISPLAY_SLOT_GRAY)

// 幽灵电路槽位
CommonWidgets.createCircuitSlot(syncManager, mte)
```

### 6.4 显示类 Widget

```java
// 物品显示（图标 + 描述）
new ItemDisplayWidget()
    .disableHoverBackground()
    .size(20)
    .background(IDrawable.EMPTY)
    .overlay(overlay)
    .item(itemStack)
    .tooltip(IKey.lang("description.key"))
    .tooltipDynamic(() -> { /* 动态 tooltip */ })

// 流体显示
new FluidDisplayWidget()
    .disableThemeBackground(true)
    .disableHoverThemeBackground(true)
    .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
    .displayAmount(false)
    .value(fluidStack)
    .size(14, 14)
    .marginRight(2)

// 进度条
new GTProgressWidget()
    .neiTransferRect(recipeMap)                          // 绑定NEI传输
    .value(new DoubleSyncValue(() -> (double)mProgresstime / mMaxProgresstime))
    .texture(GTGuiTextures.PROGRESSBAR_ARROW, direction) // 进度条纹理
    .pos(58, 24)
    .size(20, 18)
```

---

## 7. 迁移模式详解

### 模式 A：简单文本显示迁移（★推荐首选★）

**适用**：仅需在 GUI 左侧终端区追加自定义信息行。

**旧 MUI1 代码（死代码）**：
```java
// ThT_ImplosionGenerator.java - 旧代码
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

@Override
protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
    super.drawTexts(screenElements, inventorySlot);
    screenElements
        .widget(new TextWidget().setStringSupplier(
            () -> "Value:" + numberFormat.format(displayValue))
            .setDefaultColor(COLOR_TEXT_WHITE.get())
            .setEnabled(widget -> getErrorDisplayID() == 0))
        .widget(new FakeSyncWidget.LongSyncer(
            () -> displayValue, val -> displayValue = val));
}
```

**迁移步骤**：

1. 创建 `MyMachineGui.java`
2. 修改 `MyMachine.java`

```java
// === 步骤1: MyMachineGui.java ===
package com.OL925.ThinkTech.gui;

import com.OL925.ThinkTech.common.MTE.MyMachine;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

public class MyMachineGui extends MTEMultiBlockBaseGui<MyMachine> {

    private static final NumberFormatMUI numberFormat = new NumberFormatMUI();

    public MyMachineGui(MyMachine multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(
            PanelSyncManager syncManager, ModularPanel parent) {

        LongSyncValue mySyncer = new LongSyncValue(
            MyMachine::getDisplayValue,    // MTE 需提供 public static getter
            MyMachine::setDisplayValue);   // MTE 需提供 public static setter
        syncManager.syncValue("myDisplayValue", mySyncer);

        return super.createTerminalTextWidget(syncManager, parent)
            .child(
                IKey.dynamic(() -> "Value:" + numberFormat.format(mySyncer.getValue()))
                    .color(Color.WHITE.main)
                    .asWidget()
                    .textAlign(Alignment.CenterLeft)
                    .setEnabledIf(w -> multiblock.getErrorDisplayID() == 0)
                    .marginBottom(2)
                    .fullWidth());
    }
}
```

```java
// === 步骤2: MyMachine.java 改动 ===
// 添加导入
import com.OL925.ThinkTech.gui.MyMachineGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

// 添加 getter/setter（如果 displayValue 是 private static）
public static long getDisplayValue() { return displayValue; }
public static void setDisplayValue(long val) { displayValue = val; }

// 复写 getGui
@Nonnull
@Override
protected MTEMultiBlockBaseGui<?> getGui() {
    return new MyMachineGui(this);
}

// 删除: import com.gtnewhorizons.modularui.common.widget.*
// 删除: drawTexts() 方法体
```

### 模式 B：覆写 registerSyncValues + createTerminalTextWidget

**适用**：除了显示文本外，还需要注册多个同步值供后续使用。

```java
public class MyMachineGui extends MTEMultiBlockBaseGui<MyMachine> {

    private static final NumberFormatMUI numberFormat = new NumberFormatMUI();

    public MyMachineGui(MyMachine multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);  // ★ 必须调用父类

        syncManager.syncValue("myEnergyOut",
            new LongSyncValue(multiblock::getEnergyOut, multiblock::setEnergyOut));
        syncManager.syncValue("myTier",
            new IntSyncValue(multiblock::getTier, multiblock::setTier));
        syncManager.syncValue("myEnabled",
            new BooleanSyncValue(multiblock::isEnabled, multiblock::setEnabled).allowC2S());
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(
            PanelSyncManager syncManager, ModularPanel parent) {
        // 从 syncManager 中获取已注册的同步值
        LongSyncValue energySyncer = syncManager.findSyncHandler("myEnergyOut", LongSyncValue.class);
        IntSyncValue tierSyncer = syncManager.findSyncHandler("myTier", IntSyncValue.class);

        return super.createTerminalTextWidget(syncManager, parent)
            .child(IKey.dynamic(() -> "EU/t: " + numberFormat.format(energySyncer.getValue()))
                .color(Color.WHITE.main).asWidget()
                .setEnabledIf(w -> multiblock.getErrorDisplayID() == 0)
                .marginBottom(2).fullWidth())
            .child(IKey.dynamic(() -> "Tier: " + tierSyncer.getValue())
                .color(Color.WHITE.main).asWidget()
                .setEnabledIf(w -> multiblock.getErrorDisplayID() == 0)
                .marginBottom(2).fullWidth());
    }
}
```

### 模式 C：添加自定义按钮

```java
public class MyMachineGui extends MTEMultiBlockBaseGui<MyMachine> {

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("myToggle",
            new BooleanSyncValue(multiblock::isMyFeatureEnabled, multiblock::setMyFeatureEnabled)
                .allowC2S());
    }

    // 在右侧按钮列中添加
    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        BooleanSyncValue toggleSyncer = syncManager.findSyncHandler("myToggle", BooleanSyncValue.class);

        return super.createButtonColumn(panel, syncManager)
            .child(
                CommonButtons.createToggleButtonDynamicTooltip(
                    toggleSyncer,
                    UITexture.fullImage("mymod", "gui/toggle_icon"),
                    tooltip -> tooltip.addLine(IKey.dynamic(() ->
                        "My Feature: " + (toggleSyncer.getValue() ? "ON" : "OFF")))));
    }
}
```

### 模式 D：完整自定义 GUI（不使用标准多方块布局）

**适用**：机器不需要标准多方块布局（无按钮列、无终端文本区），需要完全自定义的槽位和进度条布局。

**注意**：`MTEMultiBlockBase.buildUI()` 是 `final`，此模式仅适用于直接复写 `buildUI()` 的 `MetaTileEntity` 子类（如 `MTEBrickedBlastFurnace`）。

```java
// 在 MTE 类中直接复写 buildUI（注意：非多方块或特殊架构才可用）
@Override
public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
    syncManager.registerSlotGroup("item_inv", 0);

    return GTGuis.mteTemplatePanelBuilder(this, data, syncManager, uiSettings)
        .setHeight(200)
        .doesBindPlayerInventory(false)
        .build()
        .child(createMySlots())
        .child(createMyProgressBar());
}

// 或者创建独立的 GUI 类（不继承 MTEMultiBlockBaseGui）
public class MyCustomGui {
    private final MyMachine machine;

    public ModularPanel build(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return GTGuis.mteTemplatePanelBuilder(machine, data, syncManager, uiSettings)
            .build()
            .child(/* 完全自定义布局 */);
    }
}
```

---

## 8. 完整代码示例

### 8.1 本项目实例：ThT_ImplosionGenerator → ThT_ImplosionGeneratorGui

**文件结构**：
```
src/main/java/com/OL925/ThinkTech/
├── common/MTE/ThT_ImplosionGenerator.java   (MTE 类)
└── gui/ThT_ImplosionGeneratorGui.java       (MUI2 GUI 类)
```

**MTE 类改动要点**（ThT_ImplosionGenerator.java）：

```java
// 新增导入
import com.OL925.ThinkTech.gui.ThT_ImplosionGeneratorGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

// 移除的导入（MUI1 死代码）
// import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
// import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
// import com.gtnewhorizons.modularui.common.widget.SlotWidget;
// import com.gtnewhorizons.modularui.common.widget.TextWidget;

// 为静态字段添加 public getter/setter（供 GUI 类跨包访问）
// 原：private static long displayGenerating;
// 新增：
public static long getDisplayGenerating() { return displayGenerating; }
public static void setDisplayGenerating(long val) { displayGenerating = val; }

// 复写 getGui()
@Nonnull
@Override
protected MTEMultiBlockBaseGui<?> getGui() {
    return new ThT_ImplosionGeneratorGui(this);
}

// 删除：整个 drawTexts() 方法
```

**GUI 类完整代码**（ThT_ImplosionGeneratorGui.java）：
```java
package com.OL925.ThinkTech.gui;

import com.OL925.ThinkTech.common.MTE.ThT_ImplosionGenerator;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

public class ThT_ImplosionGeneratorGui extends MTEMultiBlockBaseGui<ThT_ImplosionGenerator> {

    private static final NumberFormatMUI numberFormat = new NumberFormatMUI();

    public ThT_ImplosionGeneratorGui(ThT_ImplosionGenerator multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(
            PanelSyncManager syncManager, ModularPanel parent) {

        LongSyncValue displayGeneratingSyncer = new LongSyncValue(
            ThT_ImplosionGenerator::getDisplayGenerating,
            ThT_ImplosionGenerator::setDisplayGenerating);
        syncManager.syncValue("displayGenerating", displayGeneratingSyncer);

        return super.createTerminalTextWidget(syncManager, parent)
            .child(
                IKey.dynamic(() -> "Last EU generating(eu/tick):"
                    + numberFormat.format(displayGeneratingSyncer.getValue()))
                    .color(Color.WHITE.main)
                    .asWidget()
                    .textAlign(Alignment.CenterLeft)
                    .setEnabledIf(w -> multiblock.getErrorDisplayID() == 0)
                    .marginBottom(2)
                    .fullWidth());
    }
}
```

### 8.2 高级参考：LapotronicSuperCapacitorGui（多行动态文本 + 弹出面板）

来自 GT5 源码 `gregtech/common/gui/modularui/multiblock/MTELapotronicSuperCapacitorGui.java`：

```java
public class MTELapotronicSuperCapacitorGui
        extends MTEMultiBlockBaseGui<MTELapotronicSuperCapacitor> {

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.syncValue("avgEuIn",
            new LongSyncValue(multiblock::getAverageEuIn, multiblock::setAverageEuIn));
        syncManager.syncValue("avgEuOut",
            new LongSyncValue(multiblock::getAverageEuOut, multiblock::setAverageEuOut));
        syncManager.syncValue("capacity",
            new BigIntSyncValue(multiblock::getCapacity, multiblock::setCapacity));
        syncManager.syncValue("wireless",
            new BooleanSyncValue(multiblock::isWireless_mode, multiblock::setWireless_mode)
                .allowC2S());
        syncManager.syncValue("passiveDrain",
            new IntSyncValue(multiblock::getPassiveDrain, multiblock::setPassiveDrain));
        syncManager.syncValue("passiveDrainPercent",
            new IntSyncValue(multiblock::getPassiveDrainPercent, multiblock::setPassiveDrainPercent));
        syncManager.syncValue("euOut",
            new LongSyncValue(multiblock::getEUOut, multiblock::setEUOut));
        syncManager.syncValue("euIn",
            new LongSyncValue(multiblock::getEUIn, multiblock::setEUIn));
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(
            PanelSyncManager syncManager, ModularPanel parent) {
        LongSyncValue avgOut = syncManager.findSyncHandler("avgEuOut", LongSyncValue.class);
        BigIntSyncValue capacity = syncManager.findSyncHandler("capacity", BigIntSyncValue.class);
        LongSyncValue euOut = syncManager.findSyncHandler("euOut", LongSyncValue.class);
        LongSyncValue euIn = syncManager.findSyncHandler("euIn", LongSyncValue.class);
        IntSyncValue passiveDrain = syncManager.findSyncHandler("passiveDrain", IntSyncValue.class);
        IntSyncValue passiveDrainPercent = syncManager.findSyncHandler("passiveDrainPercent", IntSyncValue.class);
        BooleanSyncValue wireless = syncManager.findSyncHandler("wireless", BooleanSyncValue.class);

        return super.createTerminalTextWidget(syncManager, parent)
            .child(IKey.dynamic(() -> {
                String cap = EnumChatFormatting.BLUE + formatNumber(capacity.getValue());
                return StatCollector.translateToLocalFormatted("...total_capacity...") + cap;
            }).asWidget())
            .child(IKey.dynamic(() -> {
                String avgOutFmt = EnumChatFormatting.GREEN + formatNumber(avgOut.getValue()) + " EU/t";
                return StatCollector.translateToLocalFormatted("...average_out...") + avgOutFmt;
            }).asWidget())
            .child(IKey.dynamic(() -> {
                String euOutFmt = EnumChatFormatting.GREEN + formatNumber(euOut.getValue()) + " EU/t";
                return StatCollector.translateToLocalFormatted("...current_out...") + euOutFmt;
            }).asWidget())
            .child(IKey.dynamic(() -> {
                String euInFmt = EnumChatFormatting.GREEN + formatNumber(euIn.getValue()) + " EU/t";
                return StatCollector.translateToLocalFormatted("...current_in...") + euInFmt;
            }).asWidget())
            .child(IKey.dynamic(() -> {
                String drainInfo = EnumChatFormatting.RED + formatNumber(passiveDrain.getValue()) + " EU/t";
                return StatCollector.translateToLocalFormatted("...passive_drain...") + drainInfo;
            }).asWidget())
            .child(IKey.dynamic(() -> {
                String wirelessFmt = EnumChatFormatting.LIGHT_PURPLE + formatNumber(wireless.getValue());
                return "..." + wirelessFmt;
            }).asWidget().setEnabledIf(w -> multiblock.isWireless_mode()))
            .child(IKey.dynamic(() -> EnumChatFormatting.RED + "...supplying...")
                .asWidget().setEnabledIf(w -> multiblock.isWireless_mode() && ...));
    }
}
```

### 8.3 高级参考：LargeHadronColliderGui（弹出面板）

```java
public class MTELargeHadronColliderGui extends MTEMultiBlockBaseGui<MTELargeHadronCollider> {

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        // ... 注册多个 sync 值
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createButtonColumn(panel, syncManager)
            .child(createOverviewButton(syncManager, panel));
    }

    @Override
    protected Flow createRightPanelGapRow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createRightPanelGapRow(panel, syncManager)
            .child(createProbTableButton(syncManager, panel))
            .child(createCalculatorButton(syncManager, panel));
    }

    // 创建弹出面板按钮
    private ButtonWidget<?> createOverviewButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler infoPanel = IPanelHandler.synced("infoPanel", () -> {
            ModularPanel panel = GTGuis.createPopUpPanel("infoPanel", true, true);
            // ... 构建弹出面板内容
            return panel;
        });

        return new ButtonWidget<>()
            .overlay(overlayTexture)
            .addTooltip("Open Info Panel")
            .onMousePressed(mouseButton -> {
                infoPanel.openPanel(syncManager);
                return Result.ACCEPT;
            });
    }
}
```

---

## 9. 常见问题与反模式

### 9.1 numberFormat 跨包访问

**问题**：`multiblock.numberFormat` 报 protected 访问错误。

**原因**：`numberFormat` 在 `MTEMultiBlockBase` 中是 `protected`，GUI 类在不同包中通过 `multiblock.numberFormat` 访问不被允许（Java 保护访问规则）。

**解决**：在 GUI 类中自行声明：
```java
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
private static final NumberFormatMUI numberFormat = new NumberFormatMUI();
```

### 9.2 静态字段同步

**问题**：MTE 使用 `static` 字段存储显示值（如 `private static long displayValue`），GUI 类无法直接访问。

**解决**：在 MTE 中添加 public static getter/setter：
```java
// 在 MTE 类中
public static long getDisplayValue() { return displayValue; }
public static void setDisplayValue(long val) { displayValue = val; }

// 在 GUI 类中（跨包通过类名访问）
LongSyncValue syncer = new LongSyncValue(
    MyMachine::getDisplayValue,
    MyMachine::setDisplayValue);
```

**注意**：`static` 字段意味着多个机器实例共享同一值（通常是个 bug）。迁移时保持原有行为，如有需要可后续改为实例字段。

### 9.3 不要忘记调用 super

所有 `registerSyncValues()`、`createTerminalTextWidget()`、`createButtonColumn()` 等复写方法中**必须调用 `super`**，否则会丢失标准多方块 GUI 功能（电源按钮、维护提示、结构错误显示等）。

### 9.4 buildUI() 是 final

`MTEMultiBlockBase.buildUI()` 被声明为 `final`：
```java
@Override
public final ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager,
                                   UISettings uiSettings) {
    return getGui().build(guiData, syncManager, uiSettings);
}
```

**不能复写 `buildUI()`**。只能通过复写 `getGui()` 来定制 GUI。

### 9.5 MUI1 代码是死代码

任何继承自 `MTEMultiBlockBase` 的类中，`drawTexts()`、`addUIWidgets()`、`drawTextsNoPlayerInventory()` 都**完全不会被调用**。不需要保留这些方法（即使是注释化的）。

### 9.6 颜色使用 MUI2 Color 类

```java
// MUI1 (错误，在新代码中不可用)
.setDefaultColor(COLOR_TEXT_WHITE.get())

// MUI2 (正确)
.color(Color.WHITE.main)
```

MUI2 `Color` 类中的常用值：
- `Color.WHITE.main` = `0xFFFFFFFF`（纯白）
- `Color.TEXT_COLOR_DARK` = `0xFF404040`（深灰文本）
- `Color.RED.main`（用于警告文本）
- `Color.GREEN.main`（用于正常状态文本）
- `Color.BLUE.main`（用于强调）

### 9.7 确保 MTE 方法可公开访问

GUI 类通过 `this.multiblock` 访问 MTE 实例。同步值的 getter/setter 中引用的 MTE 方法需要是 `public` 的（跨包访问）：

```
multiblock.getSomeValue()     ← 需要 public
multiblock.setSomeValue(val)  ← 需要 public
multiblock::getSomeValue      ← 方法引用，需要 public
MyMachine::getDisplayValue    ← 静态方法，需要 public
```

---

## 10. 快速导入速查

### 10.1 核心 MUI2 导入

```java
// 绘制与键
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.UITexture;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.DrawableStack;

// 面板与工厂
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.IPanelHandler.*;

// 布局
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;

// 同步值
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;

// Widget 基类
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.Widget;

// 布局 Widget
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.ListWidget;

// 基础 Widget
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.ProgressWidget;

// 槽位与显示 Widget
import com.cleanroommc.modularui.widgets.ItemDisplayWidget;
import com.cleanroommc.modularui.widgets.FluidDisplayWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

// 输入 Widget
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

// MUI1 兼容工具（仅在 GUI 类需格式化数字时使用）
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
```

### 10.2 GT 封装层导入

```java
// GUI 基类
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

// GT 工厂
import gregtech.api.modularui2.GTGuis;
import gregtech.common.modularui2.factory.GTBaseGuiBuilder;

// GT 主题与纹理
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;

// GT 通用组件
import gregtech.api.modularui2.common.CommonButtons;
import gregtech.api.modularui2.common.CommonWidgets;
import gregtech.common.modularui2.widget.builder.ItemSlotGridBuilder;

// GT 进度条
import gregtech.common.modularui2.widget.GTProgressWidget;
```

### 10.3 待删除的 MUI1 导入清单

迁移完成后，确认以下导入已从 MTE 类中移除：

```
com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn
com.gtnewhorizons.modularui.common.widget.FakeSyncWidget
com.gtnewhorizons.modularui.common.widget.SlotWidget
com.gtnewhorizons.modularui.common.widget.TextWidget
com.gtnewhorizons.modularui.common.widget.DrawableWidget
com.gtnewhorizons.modularui.common.widget.Scrollable
```

---

## 附录：迁移 Checklist（Agent 执行用）

执行 MUI1→MUI2 迁移时，按以下步骤逐项确认：

```
□ 1. 确认 MTE 继承自 MTEMultiBlockBase → useMui2() 必定返回 true
□ 2. 在 MTE 中找到需要迁移的代码：
      □ drawTexts() 复写
      □ addUIWidgets() 复写
      □ drawTextsNoPlayerInventory() 复写
      □ FakeSyncWidget.*Syncer 使用
□ 3. 确定迁移模式：
      □ 仅文本显示 → 模式 A（createTerminalTextWidget）
      □ 多同步值 + 文本 → 模式 B（registerSyncValues + createTerminalTextWidget）
      □ 自定义按钮 → 模式 C（createButtonColumn / createLeftPanelGapRow / createRightPanelGapRow）
      □ 完全自定义布局 → 模式 D（独立 GUI 类 + buildUI）
□ 4. 创建 GUI 类（继承 MTEMultiBlockBaseGui<T>）
      □ 声明 numberFormat 静态字段（如需格式化数字）
      □ 复写相应方法（registerSyncValues / createTerminalTextWidget / createButtonColumn / ...）
      □ 调用 super 方法保留标准布局
      □ .child() 追加自定义组件
□ 5. 修改 MTE 类
      □ 添加 getGui() 复写，返回 new XxxGui(this)
      □ 为私有静态字段添加 public getter/setter（如有）
      □ 添加必要导入（GUI 类、MTEMultiBlockBaseGui）
      □ 删除 MUI1 导入
      □ 删除死代码（drawTexts / addUIWidgets / FakeSyncWidget）
□ 6. 编译验证
      □ ./gradlew compileJava
```

---

> **项目完整示例参考**：
> - MTE：`src/main/java/com/OL925/ThinkTech/common/MTE/ThT_ImplosionGenerator.java`
> - GUI：`src/main/java/com/OL925/ThinkTech/gui/ThT_ImplosionGeneratorGui.java`
>
> **外部参考**（GT5 源码 jar）：
> - `gregtech/common/gui/modularui/multiblock/base/MTEMultiBlockBaseGui.java` — 基类全部方法
> - `gregtech/common/gui/modularui/multiblock/MTELapotronicSuperCapacitorGui.java` — registerSyncValues 完整示例
> - `gregtech/common/gui/modularui/multiblock/MTELargeHadronColliderGui.java` — 弹出面板完整示例
> - `gregtech/common/gui/modularui/multiblock/MTEBrickedBlastFurnaceGui.java` — 独立 GUI 示例
> - `gregtech/common/modularui2/factory/GTBaseGuiBuilder.java` — 面板构建器完整 API
