package com.OL925.ThinkTech.common.event;

import static com.OL925.ThinkTech.common.init.ThTList.ExplosiveGenerator;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.enums.ItemList;

public class ExplosionEventHandler {

    @SubscribeEvent
    public void onExplosion(ExplosionEvent.Detonate event) {
        World world = event.world;

        double explosionX = event.explosion.explosionX;
        double explosionY = event.explosion.explosionY;
        double explosionZ = event.explosion.explosionZ;

        float explosionRadius = event.explosion.explosionSize;
        AxisAlignedBB explosionBoundingBox = AxisAlignedBB.getBoundingBox(
            explosionX - explosionRadius,
            explosionY - explosionRadius,
            explosionZ - explosionRadius,
            explosionX + explosionRadius,
            explosionY + explosionRadius,
            explosionZ + explosionRadius);

        List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, explosionBoundingBox);

        for (EntityItem item : items) {
            ItemStack stack = item.getEntityItem();
            if (stack.getItem() == Items.potato && stack.stackSize == 64
                || stack.getItem() == ItemList.Electric_Piston_HV.get(1)
                    .getItem()) {

                item.setDead();

                ItemStack newDrop = ExplosiveGenerator.get(1);

                EntityItem newItem = new EntityItem(world, item.posX, item.posY, item.posZ, newDrop);
                world.spawnEntityInWorld(newItem);
            }
        }
    }
}
