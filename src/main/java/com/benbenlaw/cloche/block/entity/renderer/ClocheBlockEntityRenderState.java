package com.benbenlaw.cloche.block.entity.renderer;

import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.Display;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

public class ClocheBlockEntityRenderState extends BlockEntityRenderState {

    public ItemStack seed;
    public ItemStack soil;
    public Level blockEntityLevel;

    final BlockModelRenderState plantModel = new BlockModelRenderState();
    final ItemStackRenderState seedRenderer = new ItemStackRenderState();
    final ItemStackRenderState soilRenderer = new ItemStackRenderState();
}
