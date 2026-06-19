# ModularUI 2 (MUI2) 开发者指南

> **写给谁看**：你已经会用 GregTech / NHE 写多方块机器（继承 `MTEMultiBlockBase`、实现 `checkMachine` / `checkRecipe` 之类），但还**没怎么碰过 cleanroommc ModularUI 2 这套 GUI 框架**。本文从零讲起，带你建立正确的心智模型，再给到完整的 API 参考、迁移步骤、可运行示例和踩坑清单。
>
> **读完你能做到**：
> - 看懂任何一个 GT 多方块的 MUI2 GUI 代码，并能说出每一段在干什么。
> - 为自己的多方块机器从零写一个 GUI（加文本、加按钮、加槽位、加弹出面板）。
> - 把旧的 MUI1 (`com.gtnewhorizons.modularui`) 写法迁移到 MUI2。
> - 遇到 GUI 不刷新 / 数据不同步 / 编译报错时，知道从哪里排查。
>
> **配套文档**：仓库里另有一份 `ModularUI2_Migration_Guide.md`，是面向 AI Agent 的精简执行手册。本文是给人类看的完整版，两者内容有重叠，但本文更注重"为什么"和心智模型。

---

## 前置知识 / 约定

- 你会 Java 8 的 lambda、方法引用（`obj::method`）、`Supplier`/`Consumer`/`Predicate`。
- 你知道 GT 多方块机器在服务端跑逻辑，GUI 在客户端显示，两者之间靠网络包同步。
- 本文代码块里的 `// 注释` 是给人看的解释，不是必须照抄。
- 文件路径用 `path:line` 的格式标注源码位置，方便你在 IDE 里跳转。

---

## 目录

1. [概念与心智模型](#1-概念与心智模型)
2. [GUI 是怎么打开的：完整数据流](#2-gui-是怎么打开的完整数据流)
3. [SyncManager 与 SyncValue API 参考](#3-syncmanager-与-syncvalue-api-参考)
4. [布局与容器 Widget](#4-布局与容器-widget)
5. [Widget 目录](#5-widget-目录)
6. [GT 封装层 (`gregtech.api.modularui2`)](#6-gt-封装层-gregtechapimodularui2)
7. [`MTEMultiBlockBaseGui` 扩展点参考](#7-mtemultiblockbasegui-扩展点参考)
8. [迁移指南：MUI1 → MUI2](#8-迁移指南mui1--mui2)
9. [完整可运行示例](#9-完整可运行示例)
10. [调试与常见坑](#10-调试与常见坑)
11. [附录](#11-附录)

---

## 1. 概念与心智模型

> **TL;DR**：MUI2 是一个"声明式 + 数据绑定"的 GUI 框架。你用 Widget 搭界面（**长什么样**），用 SyncValue 把服务端数据接到客户端（**显示什么**），框架帮你处理刷新和网络同步。

### 1.1 三个层次：框架 → GT 封装 → 多方块基类

写 ThinkTech 多方块 GUI 时，你会接触到三层代码，分清它们很重要：

```
┌─────────────────────────────────────────────────────────────────┐
│ 第 1 层：cleanroommc ModularUI 框架（通用，跟 GT 无关）          │
│   包名：com.cleanroommc.modularui.*                              │
│   提供：Widget、Panel、SyncManager、SyncValue、Flow、Grid 等    │
│   你直接用的类：ModularPanel、IKey、ListWidget、ButtonWidget…  │
└─────────────────────────────────────────────────────────────────┘
                            ▲ 依赖
┌─────────────────────────────────────────────────────────────────┐
│ 第 2 层：GT 对 MUI2 的封装（GregTech 自己的便利工具）            │
│   包名：gregtech.api.modularui2.* / gregtech.common.modularui2.*│
│   提供：GTGuis、GTBaseGuiBuilder、GTGuiTextures、GTWidgetThemes│
│         CommonButtons、CommonWidgets、GTProgressWidget 等      │
│   作用：把"GT 机器都要做的那套事"（标题、logo、槽位主题、按钮）  │
│         封装成一行就能调用的工厂方法。                            │
└─────────────────────────────────────────────────────────────────┘
                            ▲ 依赖
┌─────────────────────────────────────────────────────────────────┐
│ 第 3 层：多方块 GUI 基类（GT5U 提供，本文重点）                  │
│   类：gregtech.common.gui.modularui.multiblock.base.            │
│       MTEMultiBlockBaseGui<T>                                   │
│   作用：定义了"标准多方块 GUI 长什么样"——左边终端文本、右边按钮 │
│         列、下方库存行——你只需要复写几个钩子往里面塞自定义内容。│
└─────────────────────────────────────────────────────────────────┘
```

**为什么这样分？** 第 1 层是干净的、和 Minecraft 版本无关的 UI 框架；第 2 层是 GT 团队为了少写重复代码做的糖；第 3 层是多方块专用的"模板方法"。你写 ThinkTech GUI 时，**绝大多数时候只跟第 3 层（`MTEMultiBlockBaseGui`）和第 2 层（`GTGuis`/`CommonButtons`）打交道**，偶尔用到第 1 层的 Widget。分清层次能帮你快速在 IDE 里找到想要的类。

### 1.2 MUI1 vs MUI2：两个不同的库，名字像而已

这是个**常见误解**，必须先讲清楚：

| | MUI1 | MUI2 |
|---|---|---|
| 包名 | `com.gtnewhorizons.modularui.*` | `com.cleanroommc.modularui.*`（框架）+ `gregtech.*modularui2.*`（GT 封装） |
| 风格 | 命令式：`screenElements.widget(new TextWidget().setStringSupplier(...))` | 声明式 + 数据绑定：`IKey.dynamic(() -> ...).asWidget().child(...)` |
| 同步 | `FakeSyncWidget.LongSyncer(...)` 这类专用同步器 | 统一的 `SyncValue` 体系（`LongSyncValue`、`IntSyncValue`…） |
| 主入口 | `addUIWidgets(ModularWindow.Builder, UIBuildContext)` | `buildUI(...)` → `getGui().build(...)` |
| 现状 | 在多方块里是**死代码**（永远不会被调用） | 多方块强制走这条路径 |

**关键事实**：`MTEMultiBlockBase.useMui2()` 固定返回 `true`，所以**所有继承 `MTEMultiBlockBase` 的多方块，`addUIWidgets()` / `drawTexts()` 都永远不会执行**。你看到的 MUI1 代码是历史遗留，迁移时直接删。

### 1.3 心智模型：Widget = 视图，SyncValue = 数据线

这是理解整个框架最重要的一张图：

```
        【服务端】                              【客户端】
   MetaTileEntity 实例                     GUI 界面（玩家看到）
   ┌───────────────┐                  ┌──────────────────────┐
   │ displayValue  │                  │  "Value: 1234" 文本   │
   │ (真实数据源)   │                  │  (Widget 显示)        │
   └───────┬───────┘                  └──────────▲───────────┘
           │ getter/setter                       │ 每帧读取
           ▼                                     │
   ┌───────────────────┐    网络包    ┌──────────┴───────────┐
   │   SyncValue       │ ──────────► │   SyncValue           │
   │ (服务端副本+序列化)│  自动同步   │ (客户端缓存值)         │
   └───────────────────┘             └──────────────────────┘
           ▲                                     ▲
           │ registerSyncValues 时注册            │ Widget 引用它来显示
           │                                     │
   ┌───────┴───────────┐                ┌────────┴────────────┐
   │  PanelSyncManager │  ← 用字符串 key 关联 →  │ IKey.dynamic(() ->  │
   │  (同步管理器)      │                │  syncer.getValue())  │
   └───────────────────┘                └──────────────────────┘
```

读这张图的要点：

1. **服务端拥有真实数据**。`displayValue` 字段在 MTE 实例里，只有服务端改它才算数。
2. **SyncValue 是一根"数据线"**。你给它一个 getter（怎么读服务端数据）和一个 setter（客户端改了怎么写回服务端），框架自动把它们在网络间同步。客户端读 `syncer.getValue()` 拿到的是**框架帮你同步过来的缓存值**。
3. **Widget 不直接读 MTE 字段**。Widget 读的是 SyncValue 的客户端缓存。这样框架才能知道"什么时候数据变了、需要重发网络包"。
4. **`IKey.dynamic(() -> ...)` 里的 lambda 每帧执行**。所以它读 `syncer.getValue()` 时，拿到的永远是最新的同步值，文本会自动刷新。

理解了这四点，后面所有 API 都是在回答两个问题：**"我有哪些 Widget 可以搭界面？"** 和 **"我有哪些 SyncValue 可以接数据？"**。

### 1.4 客户端 / 服务端：谁拥有真理？

记住一条铁律：**服务端是真理之源，客户端是视图。**

- 默认同步方向是 **S2C**（服务端 → 客户端）：服务端 getter 的值会被打包发给客户端，客户端能读，但不能写。
- 如果要让玩家在 GUI 里改一个值（比如点开关按钮），必须显式调用 `.allowC2S()`，并提供 setter。否则客户端写了也不会发回服务端，机器状态不会变。
- `InteractionSyncHandler` 是个特殊例外：它不存"值"，只用来"通知服务端执行一次性动作"（详见 3.6）。

### 1.5 声明式 + 链式 API

MUI2 大量使用链式调用，比如：

```java
IKey.dynamic(() -> "EU/t: " + syncer.getValue())
    .color(Color.WHITE.main)
    .asWidget()
    .textAlign(Alignment.CenterLeft)
    .setEnabledIf(w -> multiblock.getErrorDisplayID() == 0)
    .marginBottom(2)
    .fullWidth();
```

读这种代码的窍门：**从上往下读，每一步都是"给上一个对象加一个属性，然后返回（通常是）同一个对象"**。`IKey.dynamic(...)` 造一个键，`.color(...)` 给它上色，`.asWidget()` 把键变成可放置的 Widget，后面 `.textAlign` / `.setEnabledIf` / `.marginBottom` / `.fullWidth` 都是给这个 Widget 设属性。

不要被链子吓到——它本质就是一连串 setter，只是写在一行里更紧凑。

---

## 2. GUI 是怎么打开的：完整数据流

> **TL;DR**：玩家右键机器 → `openGui` 判断走 MUI2 → 调 `buildUI` → `getGui().build(...)` → 先注册同步值、再造面板骨架、最后填子组件。理解这个顺序，你就知道为什么"注册同步值必须在用它的 Widget 之前"。

### 2.1 类继承链（你的多方块在哪儿）

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

两个要点：
- `MTEMultiBlockBase.buildUI()` 是 `final` 的——**你不能复写它**。定制 GUI 的唯一入口是复写 `getGui()` 返回你自己的 GUI 类。
- 你的 GUI 类继承 `MTEMultiBlockBaseGui<T>`，泛型 T 是你的 MTE 类型。

### 2.2 GUI 类继承链

```
MTEMultiBlockBaseGui<T extends MTEMultiBlockBase>   (gregtech.common.gui.modularui.multiblock.base)
  ├── 字段:
  │     ├── protected final T multiblock          ← 引用 MTE 实例
  │     ├── protected final IGregTechTileEntity baseMetaTileEntity
  │     ├── protected Map<String, IPanelHandler> panelMap  ← 弹出面板注册表
  │     └── protected List<UITexture> machineModeIcons
  │
  ├── build() → 构建完整 GUI 面板（final，不可复写）
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

### 2.3 从右键到画面：一步步发生了什么

```
玩家右键机器
  → CommonMetaTileEntity.openGui(player)              [CommonMetaTileEntity.java:615]
    → if (GTGuis.GLOBAL_SWITCH_MUI2 && useMui2())     // 多方块 useMui2()=true
      → MetaTileEntityGuiHandler.open(player, this)
        → buildUI(guiData, syncManager, uiSettings)   // 被调用
          → getGui().build(...)                        // 最终入口
            → ① registerSyncValues(syncManager)        // 先注册所有数据同步
            → ② getBasePanel(...)                      // 创建空白面板骨架
            → ③ initPanelMap(panel, syncManager)       // 初始化弹出面板
            → ④ panel.child(createMainColumn(...))     // 构建所有子组件
```

**为什么顺序是"先注册同步、再建 Widget"？** 因为 Widget 在创建时可能要 `syncManager.findSyncHandler(key, ...)` 去取已经注册的 SyncValue。如果 Widget 先建、同步后注册，`findSyncHandler` 会返回 `null`，你就拿到一个 NPE。

所以一条简单规则：**自定义同步值，要么在 `registerSyncValues()` 里注册、在 `createTerminalTextWidget()` 里取；要么在 `createTerminalTextWidget()` 一开始就注册、紧接着用。** 不要试图在 `createMainColumn` 之后的某个角落才注册。

### 2.4 标准多方块 GUI 的"解剖图"

`createMainColumn()` 产出的主列长这样（简化）：

```
┌──────────────────────────────────────────────────────────┐
│  ┌─────────────────────┐  ┌─────┐                        │  ← createPanelGap 行
│  │                     │  │ 右  │                        │     (createLeft/RightPanelGapRow)
│  │   终端文本区        │  │ 间  │                        │
│  │   (createTerminal   │  │ 隙  │                        │
│  │    TextWidget)      │  │     │                        │
│  │                     │  │     │  ┌──────┐              │
│  │   "EU/t: 1234"      │  │     │  │ 按钮列│              │  ← createButtonColumn
│  │   "运行中"          │  │     │  │      │              │     (电源/销毁/模式…)
│  │   ...               │  │     │  │ ⚡   │              │
│  │                     │  │     │  │ 🗑   │              │
│  └─────────────────────┘  │     │  │ 🔇   │              │
│                           │     │  │ ...  │              │
│  ┌─────────────────────┐  │     │  └──────┘              │
│  │  库存行 / 槽位       │  │     │                        │  ← createInventoryRow
│  │  (createInventoryRow)│  │     │                        │
│  └─────────────────────┘  │     │                        │
└──────────────────────────────────────────────────────────┘
```

你复写的几个钩子，就是往这四个区域里塞东西：
- **`createTerminalTextWidget`**：往左边终端区加文本行（最常用）。
- **`createButtonColumn`**：往右边按钮列加按钮。
- **`createLeftPanelGapRow` / `createRightPanelGapRow`**：往中间间隙行加按钮（LHC 的"概率表""计算器"按钮就在这）。
- **`getBasePanel`**：改面板整体宽高。

---

## 3. SyncManager 与 SyncValue API 参考

> **TL;DR**：`PanelSyncManager` 是同步管理器，用字符串 key 注册 `SyncValue`；`SyncValue` 是数据线，分 S2C 单向和 `.allowC2S()` 双向两种；`InteractionSyncHandler` 用来"点按钮触发服务端执行"。

### 3.1 `PanelSyncManager` 是什么

每个 GUI 面板都有一个 `PanelSyncManager`，它维护一个 `Map<String, SyncHandler>`。你做三件事：

```java
// 1. 注册：把一个 SyncValue 用唯一 key 存进去
syncManager.syncValue("myKey", mySyncValue);

// 2. 取出：之后用同一个 key + 期望类型取回
LongSyncValue v = syncManager.findSyncHandler("myKey", LongSyncValue.class);

// 3. 注册槽位组（给物品槽位用，配合 NEI 配方映射）
syncManager.registerSlotGroup("item_inv", 0);
```

**key 必须唯一**。重复 key 会覆盖，导致两个 Widget 显示同一个值。建议用"功能名"命名，如 `"displayGenerating"`、`"myTier"`。

### 3.2 基础 SyncValue 类型一览

全部在 `com.cleanroommc.modularui.value.sync` 包下。默认 S2C 单向，加 `.allowC2S()` 才支持客户端写回。

| 类型 | 构造器（只读 / 读写） | 用途 |
|------|----------------------|------|
| `IntSyncValue` | `(IntSupplier)` / `(IntSupplier, IntConsumer)` | 32 位整数：等级、计数、进度时间 |
| `LongSyncValue` | `(LongSupplier)` / `(LongSupplier, LongConsumer)` | 64 位整数：EU/t、能量、运行时长 |
| `DoubleSyncValue` | `(DoubleSupplier)` / `(DoubleSupplier, DoubleConsumer)` | 浮点：进度比例 0.0~1.0 |
| `BooleanSyncValue` | `(BooleanSupplier)` / `(BooleanSupplier, BooleanConsumer)` | 开关：启用/禁用、模式 |
| `StringSyncValue` | `(Supplier<String>)` / `(Supplier<String>, Consumer<String>)` | 文本：机器模式名等 |

注册 + 取出的标准姿势：

```java
// 注册（在 registerSyncValues 或 createTerminalTextWidget 开头）
LongSyncValue myVal = new LongSyncValue(
    multiblock::getMyValue,     // 服务端 getter
    multiblock::setMyValue);    // 服务端 setter（不需要客户端改就省略）
syncManager.syncValue("myValueKey", myVal);

// 取出（在需要用它的 Widget 里）
LongSyncValue myVal = syncManager.findSyncHandler("myValueKey", LongSyncValue.class);
long current = myVal.getValue();   // 客户端读最新同步值
```

### 3.3 高级 SyncValue

| 类型 | 说明 | 典型用法 |
|------|------|---------|
| `EnumSyncValue<T, ?>` | 枚举同步。`new EnumSyncValue<>(MyEnum.class, getter, setter).allowC2S()` | 机器模式、档位切换 |
| `GenericSyncValue<T, ?>` | 任意对象同步，需要提供 `Adapter` 做序列化/反序列化 | 自定义数据结构 |
| `GenericListSyncHandler<T>` | 列表同步，每项元素都要能序列化 | 多项配方预览、错误列表 |
| `BigIntSyncValue` | 大整数同步（TecTech 风格） | 超大容量能量存储（Lapotron） |
| `InteractionSyncHandler` | 一次性操作同步，**不持久化值** | 点按钮触发服务端动作（3.6 详述） |

### 3.4 S2C vs C2S：什么时候要 `.allowC2S()`

决策树：

```
这个值只是给玩家看的吗？（如 EU/t 显示、当前进度）
  → 是：用只读构造器（只给 getter），不加 .allowC2S()。
        例：new LongSyncValue(multiblock::getEnergyOut)

玩家会在 GUI 里改它吗？（如开关按钮、模式切换、文本框输入）
  → 是：用读写构造器（getter + setter），并加 .allowC2S()。
        例：new BooleanSyncValue(multiblock::isEnabled, multiblock::setEnabled).allowC2S()
```

**忘了 `.allowC2S()` 的症状**：按钮点了，客户端看起来变了，但松开手又弹回去，或者机器状态根本没变——因为客户端的"写"没发回服务端。

### 3.5 取值的几种姿势

```java
// A. 注册时直接持有引用（最省事，推荐用在 createTerminalTextWidget 内联注册）
LongSyncValue v = new LongSyncValue(...);
syncManager.syncValue("k", v);
// 后面直接用 v.getValue()

// B. 在另一个方法里用 key 取回（跨方法时用）
LongSyncValue v = syncManager.findSyncHandler("k", LongSyncValue.class);
// 注意：findSyncHandler 可能返回 null（key 写错或还没注册），生产代码可加判空

// C. 用 syncHandler(key) 给 ToggleButton（它会自己找 BooleanSyncValue）
new ToggleButton().syncHandler("myToggleKey")...
```

### 3.6 `InteractionSyncHandler`：点按钮让服务端干活

有时你不需要存"值"，只想"玩家点一下，服务端执行点逻辑"。用 `InteractionSyncHandler`：

```java
// 注册：传一个服务端要执行的 Runnable
InteractionSyncHandler doAction = new InteractionSyncHandler(() -> {
    // 这段代码在【服务端】执行
    multiblock.doSomethingExpensive();
});
syncManager.syncValue("doAction", doAction);

// 触发：按钮点击时调 setValue(true)（值本身无意义，只是触发信号）
new ButtonWidget<>()
    .overlay(myTexture)
    .onMousePressed(mouseButton -> {
        doAction.setValue(true);   // 发送触发信号
        return InteractiveWidget.Result.ACCEPT;
    });
```

### 3.7 标准 key 与状态判断速查

GT 基类已经注册了一堆标准同步值，你可以直接 `findSyncHandler` 取用（key 名以源码为准，常见的有）：

| 标准 key | 类型 | 含义 |
|---------|------|------|
| `"errorID"` | `IntSyncValue` | 结构错误码 |
| `"machineActive"` | `BooleanSyncValue` | 机器是否激活 |
| `"progressTime"` / `"maxProgressTime"` | `IntSyncValue` | 进度 / 最大进度 |
| `"powerSwitch"` | `BooleanSyncValue` | 电源开关（C2S） |
| `"batchMode"` / `"recipeLock"` / `"voidExcess"` | `BooleanSyncValue` | 各种模式开关（C2S） |

**`getErrorDisplayID()` 含义**（判断机器是否正常的标准方式）：

| 值 | 含义 |
|----|------|
| `0` | 机器正常，无错误 |
| `1` | 结构不完整 |
| `2` | 维护问题 |
| 其他 | 特定错误码 |

常用可见性条件：

```java
.setEnabledIf(w -> multiblock.mMachine)                          // 机器已成型
.setEnabledIf(w -> multiblock.getErrorDisplayID() == 0)          // 无错误
.setEnabledIf(w -> baseMetaTileEntity.isActive()
                    && multiblock.getErrorDisplayID() == 0)      // 正在运行且无错误
```

---

## 4. 布局与容器 Widget

> **TL;DR**：`Flow.column()`/`Flow.row()` 做弹性排列，`ListWidget` 做纵向列表，`Grid` 做网格，`ModularPanel` + `IPanelHandler` 做弹出面板。所有 Widget 共享一套链式定位方法。

### 4.1 `Flow` — 弹性布局（最常用）

```java
// 纵向排列
Flow.column()
    .child(widget1)
    .child(widget2)
    .coverChildrenHeight()   // 高度自适应子组件之和
    .fullWidth();            // 宽度撑满父容器

// 横向排列
Flow.row()
    .child(widget1)
    .child(widget2)
    .horizontalCenter()      // 水平居中
    .topRel(0.25f)           // 相对父容器顶部偏移 25%
    .size(72, 18);           // 固定尺寸
```

`Flow` 方法速查：

| 方法 | 作用 |
|------|------|
| `Flow.column()` / `Flow.row()` | 创建纵向 / 横向流 |
| `.child(IWidget)` | 添加子组件 |
| `.coverChildrenHeight()` / `.coverChildrenWidth()` | 高度 / 宽度 = 子组件之和 |
| `.fullWidth()` | 100% 父容器宽度 |
| `.centerLeft()` / `.centerRight()` | 左中 / 右中定位 |
| `.horizontalCenter()` / `.verticalCenter()` | 水平 / 垂直居中 |
| `.top(float)` / `.topRel(float)` | 绝对 / 相对顶部偏移（相对 0.0~1.0） |
| `.leftRel(float)` | 相对左侧偏移 |
| `.size(int w, int h)` | 固定尺寸 |
| `.pos(int x, int y)` | 绝对定位 |
| `.marginBottom(int)` / `.marginTop(int)` | 外边距 |

### 4.2 `ListWidget` — 纵向列表（终端文本区就是它）

`createTerminalTextWidget` 返回的就是 `ListWidget<IWidget, ?>`，非常适合"一行一行往下加文本"。

```java
new ListWidget<>()
    .fullWidth()
    .crossAxisAlignment(Alignment.CrossAxis.START)   // 子项左对齐（START/CENTER/END）
    .child(IKey.lang("key").asWidget())               // 静态文本
    .child(IKey.dynamic(() -> text).asWidget())       // 动态文本
    .childIf(condition, () -> widget);                // 条件添加（条件不满足时不创建）
```

`Alignment.CrossAxis` 取值：`START`（左对齐）、`CENTER`（居中）、`END`（右对齐）。

### 4.3 `Grid` — 网格布局

两种写法：

**模板字符串式**（配合 `SlotGroupWidget`，适合槽位阵列）：

```java
SlotGroupWidget.builder()
    .matrix("III", "III")       // 2 行 × 3 列，字符代表槽位
    .key('I', index -> new ItemSlot()
        .slot(new ModularSlot(inventoryHandler, index).slotGroup("my_group")))
    .build();
```

**编程式**（适合按钮网格 / 物品展示网格）：

```java
// 等宽：指定宽高
Grid.gridOfWidthHeight(width, height, (x, y, index) -> {
    return new ButtonWidget<>().size(16, 16);
});

// 可变宽：传元素列表
Grid.gridOfWidthElements(5, itemList, (x, y, index, item) -> {
    return new ItemDisplayWidget().item(item);
});
```

### 4.4 `ModularPanel` 与弹出面板

弹出面板（比如 LHC 的"概览""概率表"弹窗）用 `IPanelHandler` 管理：

```java
// 1. 定义弹出面板处理器（lazy 构建，首次打开时才造内容）
IPanelHandler popup = IPanelHandler.synced("myPopup", () -> {
    ModularPanel panel = GTGuis.createPopUpPanel("myPopup", true, true);
    panel.child(new TextWidget<>("Hello"))
         .child(CommonButtons.panelCloseButton());   // 关闭按钮
    return panel;
});

// 2. 在按钮点击时打开
new ButtonWidget<>()
    .overlay(myIcon)
    .addTooltip(IKey.lang("tooltip.open_popup"))
    .onMousePressed(mouseButton -> {
        if (mouseButton == MouseButton.LEFT) {
            popup.openPanel(syncManager);
        }
        return InteractiveWidget.Result.ACCEPT;
    });
```

要点：
- `IPanelHandler.synced(name, supplier)` 的 supplier 在**首次打开**时执行，所以面板内容是惰性创建的。
- 弹出面板要自己加关闭按钮（`CommonButtons.panelCloseButton()`），否则玩家没法关。
- name 要唯一，和主面板的 panelMap 关联。

### 4.5 所有 Widget 共享的链式方法

下面这些方法几乎任何 Widget 都能调，记住这一表就够定位用了：

| 方法 | 作用 |
|------|------|
| `.pos(int x, int y)` / `.setPos(...)` | 绝对 / 相对位置 |
| `.size(int w, int h)` | 固定尺寸 |
| `.top(float)` / `.topRel(float)` | 顶部偏移（绝对 / 相对） |
| `.leftRel(float)` | 左侧相对偏移 |
| `.fullWidth()` | 占满父容器宽度 |
| `.coverChildrenHeight()` | 高度自适应子组件 |
| `.marginBottom/Top/Left/Right(int)` | 四向边距 |
| `.setEnabledIf(Predicate<IWidget>)` | 条件显示（隐藏时**保留**空间） |
| `.widgetTheme(WidgetThemeKey)` | 应用主题（颜色 / 背景纹理） |
| `.background(IDrawable)` / `.overlay(IDrawable)` | 背景 / 覆盖图 |
| `.addTooltip(IKey)` | 加静态 tooltip |
| `.asWidget()` | `IKey` → `Widget` 转换（IKey 专属） |

**`setEnabledIf` vs `childIf` 的区别**：
- `setEnabledIf(w -> cond)`：Widget 一直存在，只是根据条件显示/隐藏，**隐藏时占的位置还在**。
- `childIf(cond, () -> widget)`：条件不满足时**根本不创建**这个 Widget，不留位置。

---

## 5. Widget 目录

> **TL;DR**：按"文本 / 按钮 / 槽位 / 显示 / 输入 / 图形"六类整理，每类给"是什么 + 何时用 + 微示例"。

### 5.1 文本类

**`IKey.lang(key)` — 静态本地化文本**

```java
IKey.lang("gt.interact.desc.mb.mode")
    .color(Color.WHITE.main)
    .asWidget()
    .textAlign(Alignment.CenterLeft)
    .marginBottom(2)
    .fullWidth();
```

**`IKey.dynamic(supplier)` — 动态文本（每帧重新求值）**

```java
IKey.dynamic(() -> "EU/t: " + numberFormat.format(syncer.getValue()))
    .color(Color.WHITE.main)
    .asWidget()
    .setEnabledIf(w -> multiblock.getErrorDisplayID() == 0);
```

> ⚠️ `IKey.dynamic` 的 supplier **每帧执行**。不要在里面做重活（查数据库、遍历大列表、字符串大循环）。要展示复杂计算的结果，请在 MTE 里算好缓存到一个字段，再同步过来。

**`TextWidget` — 带可选同步的文本**

```java
new TextWidget<>(StatCollector.translateToLocalFormatted("some.key"))
    .color(Color.WHITE.main)
    .setEnabledIf(w -> multiblock.getErrorDisplayID() == 0 && baseMetaTileEntity.isActive())
    .marginBottom(2)
    .fullWidth();
```

`IKey.dynamic` vs `TextWidget` 的选择：动态内容用 `IKey.dynamic`；纯静态（构建时就定死）用 `TextWidget` 或 `IKey.lang`。

### 5.2 按钮类

**`ButtonWidget` — 通用按钮（自己处理点击）**

```java
new ButtonWidget<>()
    .size(16, 16)
    .overlay(UITexture.xxx)                    // 覆盖图标
    .addTooltip(IKey.lang("tooltip.key"))
    .onMousePressed(mouseButton -> {
        // mouseButton: MouseButton.LEFT / RIGHT
        someSyncer.setValue(newVal);           // 改同步值（C2S）
        return InteractiveWidget.Result.ACCEPT;
    })
    .setEnabledIf(w -> someCondition);
```

**`ToggleButton` — 开关按钮（绑定 BooleanSyncValue）**

```java
// 方式 A：直接传 BooleanSyncValue
new ToggleButton()
    .size(16, 16)
    .value(booleanSyncer)                      // 绑定同步值
    .overlay(true,  activeTexture)             // 开时的图标
    .overlay(false, inactiveTexture)           // 关时的图标
    .tooltip(true,  "enabled.key")             // 开时的提示
    .tooltip(false, "disabled.key");           // 关时的提示

// 方式 B：用 syncManager 的 key 查找（更简洁，适合在 createButtonColumn 里用）
new ToggleButton()
    .syncHandler("myToggleKey")                // 自动找 BooleanSyncValue
    .overlay(true,  activeTex)
    .overlay(false, inactiveTex)
    .tooltip(true,  "on_tooltip")
    .tooltip(false, "off_tooltip");
```

**`CycleButtonWidget` — 多值循环按钮**（在几个状态间循环切换，类似 ToggleButton 但多于两态）。

**GT 工厂方法（推荐，少写代码）**：

```java
// 静态 tooltip 的开关按钮
CommonButtons.createToggleButton(booleanSyncer, overlayTex, "tooltip.key");

// 动态 tooltip 的开关按钮（tooltip 跟着开关状态变）
CommonButtons.createToggleButtonDynamicTooltip(booleanSyncer, overlayTex,
    tooltip -> tooltip.addLine(IKey.dynamic(() ->
        "My Feature: " + (booleanSyncer.getValue() ? "ON" : "OFF"))));
```

### 5.3 槽位类

**`ItemSlot` — 物品槽位**

```java
new ItemSlot()
    .slot(new ModularSlot(inventoryHandler, index)    // 绑定库存 + 索引
        .slotGroup("my_slot_group")                   // 槽位组（配合 NEI 配方映射）
        .filter(stack -> stack.getItem() == ...))     // 物品过滤（可选）
    .widgetTheme(GTWidgetThemes.OVERLAY_ITEM_SLOT_INGOT);   // 应用主题纹理
```

**只读展示槽**（能看能取不能放）：

```java
new ItemSlot()
    .slot(new ModularSlot(inventoryHandler, index)
        .accessibility(false, true)    // canPut=false, canTake=true
        .slotGroup("my_slot_group"))
    .widgetTheme(GTWidgetThemes.ITEM_SLOT_DISPLAY);
```

**`FluidSlot` — 流体槽（大容量显示）**

```java
new FluidSlot()
    .syncHandler(new FluidSlotSyncHandler(fluidTank)
        .canFillSlot(false))            // 只能抽出不能填入
    .alwaysShowFull(false)              // 不总是显示满
    .size(18, 54)
    .background(GTGuiTextures.DISPLAY_SLOT_GRAY)
    .overlay(GTGuiTextures.DISPLAY_SLOT_GRAY);
```

**幽灵电路槽**（GT 提供，一行搞定）：

```java
CommonWidgets.createCircuitSlot(syncManager, mte);
```

### 5.4 显示类（不可交互）

**`ItemDisplayWidget` — 物品图标 + 描述**

```java
new ItemDisplayWidget()
    .disableHoverBackground()
    .size(20)
    .background(IDrawable.EMPTY)
    .overlay(overlay)
    .item(itemStack)
    .tooltip(IKey.lang("description.key"))
    .tooltipDynamic(() -> { /* 动态 tooltip */ });
```

**`FluidDisplayWidget` — 流体显示**

```java
new FluidDisplayWidget()
    .disableThemeBackground(true)
    .disableHoverThemeBackground(true)
    .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
    .displayAmount(false)        // 不显示数量
    .value(fluidStack)
    .size(14, 14)
    .marginRight(2);
```

**`GTProgressWidget` — 进度条**（GT 封装版，带 NEI 传输矩形）

```java
new GTProgressWidget()
    .neiTransferRect(recipeMap)                              // 绑定 NEI 传输
    .value(new DoubleSyncValue(() -> (double) mProgresstime / mMaxProgresstime))
    .texture(GTGuiTextures.PROGRESSBAR_ARROW, direction)    // 进度条纹理 + 方向
    .pos(58, 24)
    .size(20, 18);
```

### 5.5 输入类

**`TextFieldWidget` — 文本输入框**

配合 `StringSyncValue`（加 `.allowC2S()`）让玩家输入字符串。较少见，多用于"命名""设阈值"场景。

### 5.6 图形类

- **`UITexture`**：静态纹理资源。`UITexture.fullImage(modId, "path/to/texture")` 加载一张图。
- **`DynamicDrawable`**：每帧动态绘制的 drawable，用于复杂自绘。
- **`DrawableStack`**：多个 drawable 叠加（比如背景 + 图标 + 数字）。
- **主题**：通过 `GTGuiTextures`（GT 预置纹理）和 `GTWidgetThemes`（GT 预置主题）复用 GT 美术资源（见 6.4）。

### 5.7 注册你自己的纹理（ThinkTech 示例）

参考 `src/main/java/com/OL925/ThinkTech/gui/ThTUITexture.java`：

```java
package com.OL925.ThinkTech.gui;

import com.OL925.ThinkTech.Tags;
import com.gtnewhorizons.modularui.api.drawable.UITexture;

public class ThTUITexture {
    private static final String MODID = Tags.MODID;

    public static final UITexture PICTURE_Implosion_Generator = UITexture
        .fullImage(MODID, "gui/picture/implosionGeneratorUI");
}
```

要点：
- `Tags.MODID` 是你的 mod id（由 build.gradle 注入）。
- 纹理文件放在 `src/main/resources/assets/<modid>/textures/gui/picture/implosionGeneratorUI.png`。
- 定义成 `public static final`，全局复用。

---

## 6. GT 封装层 (`gregtech.api.modularui2`)

> **TL;DR**：`GTGuis` 是入口工厂，`GTBaseGuiBuilder` 造主面板，`CommonButtons`/`CommonWidgets` 是 GT 预制组件，`GTGuiTextures`/`GTWidgetThemes` 是 GT 美术资源库。

### 6.1 `GTGuis` — 工厂入口

```java
// 造一个标准 MTE 模板面板（带标题、玩家背包等可选项）
GTGuis.mteTemplatePanelBuilder(mte, guiData, syncManager, uiSettings)...

// 造一个弹出面板（modal=true 时背景遮挡不可点）
GTGuis.createPopUpPanel("panelName", true, true);
GTGuis.createPopUpPanel("panelName");   // 简化重载
```

### 6.2 `GTBaseGuiBuilder` — 主面板构建器

`getBasePanel()` 默认就用它。完整 API：

```java
public class GTBaseGuiBuilder {
    public GTBaseGuiBuilder(IMetaTileEntity mte, PosGuiData guiData,
                            PanelSyncManager syncManager, UISettings uiSettings) { }

    public GTBaseGuiBuilder setWidth(int width);               // 面板宽度（默认 176）
    public GTBaseGuiBuilder setHeight(int height);             // 面板高度（默认 166）
    public GTBaseGuiBuilder doesBindPlayerInventory(boolean b);// 绑定玩家背包
    public GTBaseGuiBuilder doesAddTitle(boolean b);           // 添加标题标签
    public GTBaseGuiBuilder doesAddCoverTabs(boolean b);       // 添加覆盖板标签
    public GTBaseGuiBuilder doesAddGhostCircuitSlot(boolean b);// 添加幽灵电路槽
    public GTBaseGuiBuilder doesAddGregTechLogo(boolean b);    // 添加 GT logo
    public GTBaseGuiBuilder moveGregtechLogoPos(int x, int y); // 移动 logo 位置
    public ModularPanel build();                               // 构建并返回面板
}
```

复写 `getBasePanel` 改尺寸的例子：

```java
@Override
protected ModularPanel getBasePanel(PosGuiData guiData, PanelSyncManager syncManager,
                                     UISettings uiSettings) {
    return new GTBaseGuiBuilder(multiblock, guiData, syncManager, uiSettings)
        .setWidth(220)
        .setHeight(200)
        .doesBindPlayerInventory(false)
        .build();
}
```

### 6.3 `CommonButtons` / `CommonWidgets` — GT 预制组件

| 工厂方法 | 产出 |
|---------|------|
| `CommonButtons.createToggleButton(syncer, tex, tooltipKey)` | 静态 tooltip 开关按钮 |
| `CommonButtons.createToggleButtonDynamicTooltip(syncer, tex, tooltipFn)` | 动态 tooltip 开关按钮 |
| `CommonButtons.panelCloseButton()` | 弹出面板的关闭按钮 |
| `CommonWidgets.createCircuitSlot(syncManager, mte)` | 幽灵电路槽 |

这些是"GT 机器都需要的标准按钮"的统一实现，优先用它们，避免自己重造一遍样式不一致。

### 6.4 `GTGuiTextures` / `GTWidgetThemes` — GT 美术资源

- **`GTGuiTextures`**：一堆预定义的 `UITexture`，如 `PROGRESSBAR_ARROW`、`DISPLAY_SLOT_GRAY`、各种槽位背景。
- **`GTWidgetThemes`**：预定义主题键，如 `OVERLAY_ITEM_SLOT_INGOT`（锭状物品槽覆盖）、`ITEM_SLOT_DISPLAY`（展示用槽位）、`BACKGROUND_TERMINAL`（终端背景）。

用主题的好处：纹理改动由 GT 统一维护，你的 GUI 自动跟着变好看。

### 6.5 `ItemSlotGridBuilder` — 槽位网格构建器

`gregtech.common.modularui2.widget.builder.ItemSlotGridBuilder`，比手写 `SlotGroupWidget.builder().matrix(...)` 更简洁地造一片物品槽网格。具体 API 见 GT5U 源码。

---

## 7. `MTEMultiBlockBaseGui` 扩展点参考

> **TL;DR**：这是本文的核心参考表。每个可复写方法列了"签名 / 默认行为 / 何时复写 / 警告"，最后给一个"我该复写哪个方法"的决策树。

### 7.1 可复写方法全表

| 方法 | 签名（简） | 默认行为 | 何时复写 | 警告 |
|------|-----------|---------|---------|------|
| `registerSyncValues` | `(PanelSyncManager) → void` | 注册标准同步值（errorID、progress、各模式开关等） | 需要新增同步值供后续 Widget 使用时 | **必须调 `super`**，否则丢标准同步 |
| `getBasePanel` | `(PosGuiData, PanelSyncManager, UISettings) → ModularPanel` | 用 `GTBaseGuiBuilder` 造 176×166 面板 | 需要改面板宽高 / 加标题 / 加 logo 时 | 不要在这里加子组件，子组件归 `createMainColumn` |
| `createTerminalTextWidget` ★ | `(PanelSyncManager, ModularPanel) → ListWidget` | 造左边终端文本区，含启动/运行/关机/结构错误等标准文本 | **最常用**：加自定义信息行（EU/t、等级、状态等） | 必须调 `super` 保留标准文本 |
| `createButtonColumn` | `(ModularPanel, PanelSyncManager) → Flow` | 造右边按钮列（电源、销毁、消音、模式、批处理、配方锁、结构更新、能源面板） | 加自定义按钮时 | 必须调 `super` |
| `createLeftPanelGapRow` | `(ModularPanel, PanelSyncManager) → Flow` | 空行 | 在中间间隙行左侧加按钮时 | — |
| `createRightPanelGapRow` | `(ModularPanel, PanelSyncManager) → Flow` | 空行 | 在中间间隙行右侧加按钮（LHC 的概率表/计算器）时 | — |
| `createMainColumn` | `(...) → ...` | 组装整个主列（终端行 + 间隙行 + 库存行） | 极少复写，会重排整个布局 | 复写后标准布局全没了，慎用 |
| `shouldDisplayVoidExcess` | `() → boolean` | `multiblock.supportsVoidProtection()` | 想强制显示/隐藏销毁模式按钮时 | — |
| `shouldDisplayInputSeparation` | `() → boolean` | `multiblock.supportsInputSeparation()` | 同上，输入分离按钮 | — |
| `shouldDisplayBatchMode` | `() → boolean` | `multiblock.supportsBatchMode()` | 同上，批处理按钮 | — |
| `shouldDisplayRecipeLock` | `() → boolean` | `multiblock.supportsSingleRecipeLocking()` | 同上，配方锁定按钮 | — |
| `showOutputRates` | `() → boolean` | `false` | 想在 NEI 显示配方输出速率时 | — |
| `getBasePanelWidth/Height` | `() → int` | `176` / `166` | 改尺寸的简便方式（比复写 `getBasePanel` 轻） | — |
| `getTerminalRowWidth/Height` | `() → int` | `200` / `getTerminalWidgetHeight()` | 改终端区尺寸 | — |
| `getTerminalWidgetWidth/Height` | `() → int` | `150` / `132` | 改终端文本区尺寸 | — |
| `getTextBoxToInventoryGap` | `() → int` | `80` | 改文本与库存间距 | — |

### 7.2 决策树：我该复写哪个方法？

```
你想做什么？
│
├─ 加一行文本显示（EU/t、等级、状态信息…）
│   → createTerminalTextWidget  ★ 80% 的情况
│
├─ 加一个按钮（开关、触发动作…）
│   ├─ 想放在右边按钮列里
│   │   → createButtonColumn
│   └─ 想放在中间间隙行
│       → createLeftPanelGapRow / createRightPanelGapRow
│
├─ 注册多个同步值（供多个地方用）
│   → registerSyncValues（配合上面的方法用）
│
├─ 改面板整体大小
│   ├─ 只改宽高 → getBasePanelWidth / getBasePanelHeight
│   └─ 还要改 logo / 标题 / 玩家背包绑定 → getBasePanel
│
├─ 加弹出面板（概览、计算器…）
│   → createButtonColumn 或 createRightPanelGapRow 里加触发按钮
│      + 用 IPanelHandler.synced 注册面板
│
├─ 显示/隐藏某个标准按钮
│   → shouldDisplay* 系列方法
│
└─ 完全自定义布局（不要标准多方块布局）
    → 不走 MTEMultiBlockBaseGui，直接在 MTE 里复写 buildUI
      （注意：仅对非 MTEMultiBlockBase 的类可行，多方块的 buildUI 是 final）
```

### 7.3 复写模板（照抄改改就能用）

**模板 1：只加文本（最常见）**

```java
public class MyMachineGui extends MTEMultiBlockBaseGui<MyMachine> {

    private static final NumberFormatMUI numberFormat = new NumberFormatMUI();

    public MyMachineGui(MyMachine multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(
            PanelSyncManager syncManager, ModularPanel parent) {

        // 注册同步值（也可以放 registerSyncValues 里）
        LongSyncValue mySyncer = new LongSyncValue(
            multiblock::getSomeValue, multiblock::setSomeValue);
        syncManager.syncValue("myValue", mySyncer);

        // 调 super 保留标准文本，再 .child() 追加自定义行
        return super.createTerminalTextWidget(syncManager, parent)
            .child(IKey.dynamic(() -> "Value: " + numberFormat.format(mySyncer.getValue()))
                .color(Color.WHITE.main)
                .asWidget()
                .textAlign(Alignment.CenterLeft)
                .setEnabledIf(w -> multiblock.getErrorDisplayID() == 0)
                .marginBottom(2)
                .fullWidth());
    }
}
```

**模板 2：registerSyncValues + 多处取用**

```java
@Override
protected void registerSyncValues(PanelSyncManager syncManager) {
    super.registerSyncValues(syncManager);   // ★ 必须调
    syncManager.syncValue("myEnergyOut",
        new LongSyncValue(multiblock::getEnergyOut, multiblock::setEnergyOut));
    syncManager.syncValue("myEnabled",
        new BooleanSyncValue(multiblock::isEnabled, multiblock::setEnabled).allowC2S());
}

@Override
protected ListWidget<IWidget, ?> createTerminalTextWidget(
        PanelSyncManager syncManager, ModularPanel parent) {
    LongSyncValue energy = syncManager.findSyncHandler("myEnergyOut", LongSyncValue.class);
    return super.createTerminalTextWidget(syncManager, parent)
        .child(IKey.dynamic(() -> "EU/t: " + numberFormat.format(energy.getValue()))
            .color(Color.WHITE.main).asWidget()
            .setEnabledIf(w -> multiblock.getErrorDisplayID() == 0)
            .marginBottom(2).fullWidth());
}
```

**模板 3：加按钮**

```java
@Override
protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
    BooleanSyncValue toggle = syncManager.findSyncHandler("myToggle", BooleanSyncValue.class);
    return super.createButtonColumn(panel, syncManager)
        .child(CommonButtons.createToggleButtonDynamicTooltip(
            toggle,
            UITexture.fullImage("mymod", "gui/toggle_icon"),
            tooltip -> tooltip.addLine(IKey.dynamic(() ->
                "My Feature: " + (toggle.getValue() ? "ON" : "OFF")))));
}
```

---

## 8. 迁移指南：MUI1 → MUI2

> **TL;DR**：多方块里所有 MUI1 代码都是死代码，识别信号 → 选迁移模式 → 改 MTE + 建 GUI 类 → 删死代码 → 编译。

### 8.1 识别信号：哪些代码是要迁移的死代码

在 MTE 类里看到这些，就是要迁移：

| 信号 | 含义 |
|------|------|
| `drawTexts(DynamicPositionedColumn, SlotWidget)` 复写 | MUI1 文本显示，死代码 |
| `drawTextsNoPlayerInventory(DynamicPositionedColumn)` 复写 | MUI1 无背包版，死代码 |
| `addUIWidgets(ModularWindow.Builder, UIBuildContext)` 复写 | MUI1 主入口，死代码 |
| `import com.gtnewhorizons.modularui.common.widget.*` | MUI1 导入，需移除 |
| `FakeSyncWidget.*Syncer` | MUI1 同步器，需替换为 `*SyncValue` |

### 8.2 MUI1 → MUI2 概念映射表

| MUI1 写法 | MUI2 写法 |
|-----------|-----------|
| `FakeSyncWidget.LongSyncer(() -> v, val -> v = val)` | `new LongSyncValue(() -> v, val -> v = val)` + `syncManager.syncValue("k", ...)` |
| `new TextWidget().setStringSupplier(() -> "...")` | `IKey.dynamic(() -> "...").asWidget()` |
| `TextWidget` + `.setDefaultColor(COLOR_TEXT_WHITE.get())` | `IKey.dynamic(...).color(Color.WHITE.main).asWidget()` |
| `DynamicPositionedColumn` 纵向排列 | `Flow.column()` 或 `ListWidget` |
| `screenElements.widget(...)` 追加 | `.child(...)` 链式追加 |
| `addUIWidgets(builder, ctx)` 主入口 | 复写 `createTerminalTextWidget` / `createButtonColumn` |
| `setEnabled(widget -> cond)` | `.setEnabledIf(w -> cond)` |

### 8.3 四种迁移模式

#### 模式 A：简单文本显示迁移 ★推荐首选

**适用**：只需在 GUI 左侧终端区追加自定义信息行。

**旧 MUI1 代码（死代码）**：

```java
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

1. 建 `MyMachineGui.java`（见 7.3 模板 1）。
2. 改 `MyMachine.java`：
   - 加 `import com.OL925.ThinkTech.gui.MyMachineGui;`
   - 加 `import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;`
   - 给私有静态字段加 public getter/setter（如果 GUI 要跨包访问）。
   - 复写 `getGui()` 返回 `new MyMachineGui(this)`。
   - 删掉 `drawTexts()` 整个方法和 MUI1 导入。

#### 模式 B：registerSyncValues + 多行文本

**适用**：除了显示文本，还要注册多个同步值供后续使用。见 7.3 模板 2。

#### 模式 C：添加自定义按钮

**适用**：要加开关 / 触发按钮。见 7.3 模板 3。

#### 模式 D：完全自定义布局

**适用**：机器不需要标准多方块布局（无按钮列、无终端文本区），需要完全自定义槽位和进度条。

**注意**：`MTEMultiBlockBase.buildUI()` 是 `final`，**多方块不能复写 `buildUI()`**。模式 D 只适用于直接继承 `MetaTileEntity`（非多方块）的类，或架构特殊的类（如 `MTEBrickedBlastFurnace`）。

```java
// 仅对非 MTEMultiBlockBase 的 MTE 可用
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
```

### 8.4 迁移检查清单（人类版）

```
□ 1. 确认 MTE 继承自 MTEMultiBlockBase（→ useMui2() 必为 true）
□ 2. 找出要迁移的死代码：drawTexts / addUIWidgets / drawTextsNoPlayerInventory / FakeSyncWidget
□ 3. 选迁移模式：
     □ 仅文本        → 模式 A（createTerminalTextWidget）
     □ 多同步值+文本 → 模式 B（registerSyncValues + createTerminalTextWidget）
     □ 自定义按钮    → 模式 C（createButtonColumn / gapRow）
     □ 完全自定义    → 模式 D（仅非多方块可用）
□ 4. 建 GUI 类（继承 MTEMultiBlockBaseGui<T>）：
     □ 声明 numberFormat 静态字段（如需格式化数字）
     □ 复写相应方法，调 super 保留标准功能
     □ .child() 追加自定义组件
□ 5. 改 MTE 类：
     □ 复写 getGui() 返回 new XxxGui(this)
     □ 给私有静态字段加 public getter/setter（如有）
     □ 加必要导入，删 MUI1 导入，删死代码
□ 6. 编译：./gradlew compileJava（或 IDE 里 build）
□ 7. 进游戏右键机器，确认 GUI 正常、数据刷新、按钮可用
```

---

## 9. 完整可运行示例

> **TL;DR**：四个从简到繁的例子，都带行内注释解释"为什么"。例 1 是 ThinkTech 真实代码。

### 9.1 例 1：极简——终端里加一行动态文本

**对应真实文件**：
- MTE：`src/main/java/com/OL925/ThinkTech/common/MTE/ThT_ImplosionGenerator.java:419`
- GUI：`src/main/java/com/OL925/ThinkTech/gui/ThT_ImplosionGeneratorGui.java`

**MTE 类改动**（`ThT_ImplosionGenerator.java`）：

```java
// 新增导入
import com.OL925.ThinkTech.gui.ThT_ImplosionGeneratorGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

// 原：private static long displayGenerating;
// 新增 getter/setter（GUI 类要跨包访问这个静态字段）
public static long getDisplayGenerating() { return displayGenerating; }
public static void setDisplayGenerating(long val) { displayGenerating = val; }

// 复写 getGui()，把 GUI 类接进来
@Nonnull
@Override
protected MTEMultiBlockBaseGui<?> getGui() {
    return new ThT_ImplosionGeneratorGui(this);
}

// 删除：整个 drawTexts() 方法和 MUI1 导入
```

**GUI 类**（`ThT_ImplosionGeneratorGui.java`，真实代码）：

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

    // 自己声明 numberFormat，不能复用 MTE 里的 protected 字段（跨包访问限制）
    private static final NumberFormatMUI numberFormat = new NumberFormatMUI();

    public ThT_ImplosionGeneratorGui(ThT_ImplosionGenerator multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        // 1. 注册同步值：把 MTE 的 displayGenerating 字段接成一根数据线
        LongSyncValue displayGeneratingSyncer = new LongSyncValue(
            ThT_ImplosionGenerator::getDisplayGenerating,   // 服务端读
            ThT_ImplosionGenerator::setDisplayGenerating);  // 客户端写回（这里其实只读也行）
        syncManager.syncValue("displayGenerating", displayGeneratingSyncer);

        // 2. 调 super 保留标准文本（启动/运行/结构错误等），再追加自定义行
        return super.createTerminalTextWidget(syncManager, parent)
            .child(
                IKey.dynamic(() -> "Last EU generating(eu/tick):"
                    + numberFormat.format(displayGeneratingSyncer.getValue()))
                    .color(Color.WHITE.main)              // MUI2 用 Color.WHITE.main，不是 COLOR_TEXT_WHITE.get()
                    .asWidget()                            // IKey → Widget
                    .textAlign(Alignment.CenterLeft)
                    .setEnabledIf(w -> multiblock.getErrorDisplayID() == 0)  // 机器正常才显示
                    .marginBottom(2)
                    .fullWidth());
    }
}
```

**为什么这样写**——逐行解读：
- `extends MTEMultiBlockBaseGui<ThT_ImplosionGenerator>`：泛型填你的 MTE 类型，这样 `this.multiblock` 就是强类型的，能直接调它的方法。
- `numberFormat` 声明成 `private static final`：`MTEMultiBlockBase` 里也有个 `numberFormat`，但它是 `protected`，GUI 类在另一个包里**不能**通过 `multiblock.numberFormat` 访问（Java 保护访问规则）。所以自己声明一个。
- `LongSyncValue(...::getDisplayGenerating, ...::setDisplayGenerating)`：方法引用。`getDisplayGenerating` 是 static，所以用 `类名::方法` 形式。
- `syncManager.syncValue("displayGenerating", ...)`：用唯一 key 注册。
- `super.createTerminalTextWidget(...).child(...)`：先拿父类造好的标准文本列表，再 `.child()` 往后追加。这样标准文本（运行中、关机原因等）都还在。

### 9.2 例 2：多同步值 + 多行格式化文本

仿 GT5 的 `MTELapotronicSuperCapacitorGui`（电容器 GUI，显示容量/输入/输出/无线模式等多行信息）：

```java
public class MyCapacitorGui extends MTEMultiBlockBaseGui<MyCapacitor> {

    private static final NumberFormatMUI numberFormat = new NumberFormatMUI();

    public MyCapacitorGui(MyCapacitor mb) { super(mb); }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);   // ★ 必须
        syncManager.syncValue("avgEuOut",
            new LongSyncValue(multiblock::getAverageEuOut, multiblock::setAverageEuOut));
        syncManager.syncValue("euOut",
            new LongSyncValue(multiblock::getEUOut, multiblock::setEUOut));
        syncManager.syncValue("wireless",
            new BooleanSyncValue(multiblock::isWireless, multiblock::setWireless).allowC2S());
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(
            PanelSyncManager syncManager, ModularPanel parent) {
        // 取出已注册的同步值
        LongSyncValue avgOut = syncManager.findSyncHandler("avgEuOut", LongSyncValue.class);
        LongSyncValue euOut  = syncManager.findSyncHandler("euOut",  LongSyncValue.class);
        BooleanSyncValue wireless = syncManager.findSyncHandler("wireless", BooleanSyncValue.class);

        return super.createTerminalTextWidget(syncManager, parent)
            // 平均输出
            .child(IKey.dynamic(() ->
                    "Avg Out: " + EnumChatFormatting.GREEN + numberFormat.format(avgOut.getValue()) + " EU/t")
                .color(Color.WHITE.main).asWidget()
                .setEnabledIf(w -> multiblock.getErrorDisplayID() == 0)
                .marginBottom(2).fullWidth())
            // 当前输出
            .child(IKey.dynamic(() ->
                    "Cur Out: " + EnumChatFormatting.GREEN + numberFormat.format(euOut.getValue()) + " EU/t")
                .color(Color.WHITE.main).asWidget()
                .setEnabledIf(w -> multiblock.getErrorDisplayID() == 0)
                .marginBottom(2).fullWidth())
            // 无线模式（仅无线开启时显示）
            .child(IKey.dynamic(() -> "Wireless mode: " + (wireless.getValue() ? "ON" : "OFF"))
                .color(Color.WHITE.main).asWidget()
                .setEnabledIf(w -> multiblock.getErrorDisplayID() == 0 && wireless.getValue())
                .marginBottom(2).fullWidth());
    }
}
```

**对应 MTE 改动**：加 4 个 public getter/setter，复写 `getGui()` 返回 `new MyCapacitorGui(this)`。

### 9.3 例 3：自定义开关按钮

```java
public class MyMachineGui extends MTEMultiBlockBaseGui<MyMachine> {

    public MyMachineGui(MyMachine mb) { super(mb); }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        // 开关需要客户端写回服务端，所以加 .allowC2S()
        syncManager.syncValue("myFeature",
            new BooleanSyncValue(multiblock::isMyFeature, multiblock::setMyFeature).allowC2S());
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        BooleanSyncValue feat = syncManager.findSyncHandler("myFeature", BooleanSyncValue.class);
        return super.createButtonColumn(panel, syncManager)   // ★ 保留标准按钮
            .child(CommonButtons.createToggleButtonDynamicTooltip(
                feat,
                UITexture.fullImage("mymod", "gui/feature_icon"),
                tooltip -> tooltip.addLine(IKey.dynamic(() ->
                    "My Feature: " + (feat.getValue() ? "ON" : "OFF")))));
    }
}
```

**关键点**：
- 开关类同步值**必须** `.allowC2S()`，否则按钮点了没反应（详见 10.4）。
- `super.createButtonColumn(...).child(...)`：先拿标准按钮列，再追加自己的。这样电源、销毁、模式等标准按钮都还在。

### 9.4 例 4：弹出信息面板

```java
public class MyMachineGui extends MTEMultiBlockBaseGui<MyMachine> {

    public MyMachineGui(MyMachine mb) { super(mb); }

    @Override
    protected Flow createRightPanelGapRow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createRightPanelGapRow(panel, syncManager)
            .child(createInfoButton(syncManager, panel));
    }

    private ButtonWidget<?> createInfoButton(PanelSyncManager syncManager, ModularPanel parent) {
        // 弹出面板：lazy 构建，首次打开时才造内容
        IPanelHandler infoPanel = IPanelHandler.synced("infoPanel", () -> {
            ModularPanel p = GTGuis.createPopUpPanel("infoPanel", true, true);
            p.child(new TextWidget<>("Machine Overview")
                    .color(Color.WHITE.main).pos(10, 8))
              .child(CommonButtons.panelCloseButton());   // 别忘了关闭按钮
            return p;
        });

        return new ButtonWidget<>()
            .overlay(UITexture.fullImage("mymod", "gui/info_icon"))
            .addTooltip(IKey.lang("tooltip.open_info"))
            .onMousePressed(mouseButton -> {
                if (mouseButton == MouseButton.LEFT) {
                    infoPanel.openPanel(syncManager);
                }
                return InteractiveWidget.Result.ACCEPT;
            });
    }
}
```

**关键点**：
- 弹出面板用 `IPanelHandler.synced(name, supplier)`，supplier 在首次打开时执行。
- 弹出面板**必须**自己加关闭按钮（`CommonButtons.panelCloseButton()`），否则玩家没法关。
- 触发用 `onMousePressed` + `infoPanel.openPanel(syncManager)`。

---

## 10. 调试与常见坑

> **TL;DR**：按"症状 → 原因 → 解决"组织，遇到问题先来这里查。

### 10.1 标准按钮 / 标准文本消失了

**症状**：复写后电源按钮、维护提示、结构错误文本全没了。

**原因**：复写 `registerSyncValues` / `createTerminalTextWidget` / `createButtonColumn` 时**忘了调 `super`**。

**解决**：每个复写方法第一行加 `super.xxx(...)`。

```java
@Override
protected void registerSyncValues(PanelSyncManager syncManager) {
    super.registerSyncValues(syncManager);   // ★ 这一行不能少
    // ... 你的自定义同步
}
```

### 10.2 `multiblock.numberFormat` 编译报错

**症状**：`numberFormat` 在 `MTEMultiBlockBase` 里是 `protected`，GUI 类在另一个包，通过 `multiblock.numberFormat` 访问报保护访问错误。

**原因**：Java 保护访问规则——`protected` 只允许同包或子类访问。GUI 类不是 MTE 的子类，且不同包。

**解决**：在 GUI 类里自己声明：

```java
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
private static final NumberFormatMUI numberFormat = new NumberFormatMUI();
```

### 10.3 私有静态字段跨包访问不了

**症状**：MTE 用 `private static long displayValue` 存显示值，GUI 类的 `LongSyncValue(...::getDisplayValue, ...)` 找不到方法。

**原因**：私有字段跨包不可见。

**解决**：在 MTE 里加 public static getter/setter：

```java
// MTE 类里
public static long getDisplayValue() { return displayValue; }
public static void setDisplayValue(long val) { displayValue = val; }

// GUI 类里用方法引用
LongSyncValue syncer = new LongSyncValue(
    MyMachine::getDisplayValue,    // 类名::静态方法
    MyMachine::setDisplayValue);
```

> ⚠️ `static` 字段意味着多个机器实例共享同一值（通常是个 bug）。迁移时先保持原行为，之后可考虑改成实例字段。

### 10.4 按钮点了没反应 / 状态弹回

**症状**：点开关按钮，客户端一瞬间变了，松手又回去；或者机器状态根本没变。

**原因**：同步值没加 `.allowC2S()`，客户端的"写"没发回服务端。

**解决**：

```java
// 错：只读，客户端写不回去
new BooleanSyncValue(multiblock::isEnabled, multiblock::setEnabled)

// 对：加 .allowC2S()
new BooleanSyncValue(multiblock::isEnabled, multiblock::setEnabled).allowC2S()
```

### 10.5 `findSyncHandler` 返回 null / NPE

**症状**：在 Widget 里取同步值，拿到 null，后续 `.getValue()` 报 NPE。

**原因**：取值的时机早于注册，或 key 写错。

**解决**：
- 确保注册在 `registerSyncValues()` 或 `createTerminalTextWidget()` 开头，**在用它造 Widget 之前**。
- 检查 key 字符串拼写一致。
- 生产代码可加判空：`if (syncer != null) ...`。

### 10.6 试图复写 `buildUI()` 编译失败

**症状**：多方块 MTE 里复写 `buildUI(...)`，IDE 报错或编译失败。

**原因**：`MTEMultiBlockBase.buildUI()` 是 `final`，不能复写。

**解决**：定制 GUI 的唯一入口是复写 `getGui()` 返回你自己的 GUI 类。

### 10.7 颜色用错了类

**症状**：`.setDefaultColor(COLOR_TEXT_WHITE.get())` 在 MUI2 里不可用 / 找不到方法。

**原因**：那是 MUI1 的 API。

**解决**：用 MUI2 的 `Color` 类：

```java
// 错（MUI1）
.setDefaultColor(COLOR_TEXT_WHITE.get())

// 对（MUI2）
.color(Color.WHITE.main)
```

常用颜色（`com.cleanroommc.modularui.utils.Color`）：
- `Color.WHITE.main` = `0xFFFFFFFF`（纯白）
- `Color.TEXT_COLOR_DARK` = `0xFF404040`（深灰文本）
- `Color.RED.main` / `Color.GREEN.main` / `Color.BLUE.main`（警告 / 正常 / 强调）

### 10.8 MTE 方法不是 public，跨包访问失败

**症状**：GUI 类里 `multiblock::getSomeValue` 报"方法不可见"。

**原因**：getter/setter 是 `protected` 或包级私有，GUI 类跨包访问不了。

**解决**：把 MTE 里被 GUI 引用的 getter/setter 改成 `public`。

```
multiblock.getSomeValue()      ← 需要 public
multiblock.setSomeValue(val)   ← 需要 public
multiblock::getSomeValue       ← 方法引用，同样需要 public
MyMachine::getDisplayValue     ← 静态方法，需要 public static
```

### 10.9 同步 key 重复

**症状**：两个 Widget 显示同一个值，或后注册的覆盖了先注册的。

**原因**：`syncManager.syncValue(key, ...)` 的 key 不唯一，后者覆盖前者。

**解决**：每个同步值用唯一的、有语义的 key 名，如 `"displayGenerating"`、`"myTier"`、`"myFeature"`，不要用 `"value"` 这种通用名。

### 10.10 动态文本卡顿

**症状**：GUI 打开时 / 数据更新时游戏掉帧。

**原因**：`IKey.dynamic(() -> ...)` 的 supplier **每帧执行**，里面做了重活（大循环、字符串拼接大量数据、查 NBT 等）。

**解决**：
- 在 MTE 里把计算结果缓存到一个字段，每 tick 更新一次，GUI 只读这个缓存字段。
- 避免在 supplier 里做字符串大循环拼接。
- 复杂文本可以预先算好分段，supplier 只做简单拼接。

### 10.11 Widget 显示了但数据没同步

**症状**：文本框一直显示初始值（0 / null），不随机器状态变化。

**原因**排查顺序：
1. 同步值注册了吗？（`syncManager.syncValue(key, ...)`）
2. key 唯一吗？有没有被覆盖？
3. getter 引用的 MTE 字段在服务端真的被更新了吗？（在 MTE 逻辑里加日志确认）
4. Widget 引用的 syncer 和注册的是同一个吗？（用 `findSyncHandler` 时 key 拼写对吗？）

### 10.12 MUI1 死代码留着不报错，但永远不执行

**症状**：MTE 里 `drawTexts()` 还在，编译通过，但 GUI 上看不到它的效果。

**原因**：多方块 `useMui2()=true`，`drawTexts()` 永远不被调用。

**解决**：迁移到 MUI2 后**删掉** `drawTexts()` / `addUIWidgets()` / `FakeSyncWidget` 等死代码，避免后人误以为它们还在工作。

---

## 11. 附录

### 11.1 完整导入速查

**MUI2 核心导入**（cleanroommc 框架）：

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

// MUI1 兼容工具（仅 GUI 类格式化数字时用）
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
```

**GT 封装层导入**：

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

**待删除的 MUI1 导入清单**（迁移后从 MTE 类移除）：

```
com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn
com.gtnewhorizons.modularui.common.widget.FakeSyncWidget
com.gtnewhorizons.modularui.common.widget.SlotWidget
com.gtnewhorizons.modularui.common.widget.TextWidget
com.gtnewhorizons.modularui.common.widget.DrawableWidget
com.gtnewhorizons.modularui.common.widget.Scrollable
```

### 11.2 SyncValue ↔ MTE 字段访问速查

| MTE 字段形式 | GUI 类怎么访问 |
|-------------|---------------|
| `public long value`（实例字段，public） | `multiblock::getValue` / `multiblock.value` |
| `private long value` + public getter/setter | `multiblock::getValue, multiblock::setValue` |
| `private static long value` + public static getter/setter | `MyMachine::getValue, MyMachine::setValue`（类名引用） |
| `protected` 字段（在 MTEMultiBlockBase 里） | **不能**跨包访问，GUI 类自己声明替代品（如 numberFormat） |

### 11.3 真实参考代码位置

**项目内**：
- MTE：`src/main/java/com/OL925/ThinkTech/common/MTE/ThT_ImplosionGenerator.java:419`（`getGui()` 复写）
- GUI：`src/main/java/com/OL925/ThinkTech/gui/ThT_ImplosionGeneratorGui.java`（极简完整示例）
- 自定义纹理：`src/main/java/com/OL925/ThinkTech/gui/ThTUITexture.java`

**GT5 源码 jar 内**（用 IDE 反编译/jar 查看器看）：
- `gregtech/common/gui/modularui/multiblock/base/MTEMultiBlockBaseGui.java` — 基类全部方法
- `gregtech/common/gui/modularui/multiblock/MTELapotronicSuperCapacitorGui.java` — `registerSyncValues` + 多行动态文本完整示例
- `gregtech/common/gui/modularui/multiblock/MTELargeHadronColliderGui.java` — 弹出面板 + 间隙行按钮完整示例
- `gregtech/common/gui/modularui/multiblock/MTEBrickedBlastFurnaceGui.java` — 独立 GUI（非标准多方块布局）示例
- `gregtech/common/modularui2/factory/GTBaseGuiBuilder.java` — 面板构建器完整 API

### 11.4 术语表

| 术语 | 含义 |
|------|------|
| **Widget** | GUI 上的一个元素（按钮、文本、槽位…）。MUI2 里所有可见元素都是 Widget。 |
| **Panel / ModularPanel** | 一个 GUI 面板容器，承载一棵 Widget 树。主面板 + 弹出面板都算。 |
| **SyncManager / PanelSyncManager** | 同步管理器，每个面板一个，用字符串 key 维护一组 SyncHandler。 |
| **SyncValue / SyncHandler** | 数据线。把服务端 getter/setter 接到框架，自动在网络间同步。 |
| **SlotGroup** | 槽位组，给一组物品槽起名，配合 NEI 配方映射用。 |
| **WidgetTheme** | Widget 主题，预定义的颜色 + 背景纹理组合（如 `GTWidgetThemes.ITEM_SLOT_DISPLAY`）。 |
| **IPanelHandler** | 弹出面板处理器，lazy 管理一个弹出面板的创建/打开/关闭。 |
| **S2C / C2S** | Server→Client / Client→Server，数据同步方向。默认 S2C，加 `.allowC2S()` 才支持 C2S。 |
| **MUI1 / MUI2** | 两套不同库：`com.gtnewhorizons.modularui`（旧，多方块里死代码）/ `com.cleanroommc.modularui` + GT 封装（新，正在用）。 |
| **MTE** | MetaTileEntity，GregTech 的机器方块实体。 |
| **多方块** | 继承 `MTEMultiBlockBase` 的 MTE，强制走 MUI2。 |

---

> **最后的话**：写 MUI2 GUI 的心智模型只有两件事——**用 Widget 搭视图，用 SyncValue 接数据**。剩下的都是 API 细节，遇到忘了的查本指南对应章节即可。第一个 GUI 从 9.1 例 1 照抄起步，跑通了再逐步加复杂度。祝顺利。
