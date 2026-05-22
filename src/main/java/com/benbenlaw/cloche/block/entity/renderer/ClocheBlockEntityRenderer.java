package com.benbenlaw.cloche.block.entity.renderer;

import com.benbenlaw.cloche.block.entity.ClocheBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import org.joml.Quaternionf;

public class ClocheBlockEntityRenderer implements BlockEntityRenderer<ClocheBlockEntity, ClocheBlockEntityRenderState> {

    private final ItemModelResolver itemModelResolver;
    private final BlockModelResolver blockModelResolver;

    public ClocheBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
        this.blockModelResolver = context.blockModelResolver();
    }

    @Override
    public ClocheBlockEntityRenderState createRenderState() {
        return new ClocheBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(ClocheBlockEntity blockEntity, ClocheBlockEntityRenderState renderState, float partialTick, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);

        renderState.seed = ItemUtil.getStack(blockEntity.getInputHandler(), 0);
        renderState.soil = ItemUtil.getStack(blockEntity.getInputHandler(), 1);

        renderState.blockEntityLevel = blockEntity.getLevel();

        // Soil renderer
        if (renderState.soil != null) {
            itemModelResolver.updateForTopItem(renderState.soilRenderer, renderState.soil,ItemDisplayContext.FIXED, blockEntity.getLevel(), null,0);
        }

        // Seed/crop renderer
        if (renderState.seed != null) {

            if (renderState.seed.is(ItemTags.SAPLINGS)) {
                BlockState saplingState =((BlockItem) renderState.seed.getItem()).getBlock().defaultBlockState();
                blockModelResolver.update(renderState.plantModel, saplingState, BlockDisplayContext.create());
            }

            else if (renderState.seed.getItem() instanceof BlockItem blockItem) {

                BlockState cropState = blockItem.getBlock().defaultBlockState();

                if (cropState.getBlock() instanceof CropBlock cropBlock) {

                    int maxAge = cropBlock.getMaxAge();
                    int age = Math.round((float) blockEntity.progress / blockEntity.maxProgress * maxAge);

                    Property<?> ageProperty = null;
                    for (Property<?> property : cropBlock.getStateDefinition().getProperties()) {
                        if (property instanceof IntegerProperty integerProperty) {
                            if (integerProperty.getPossibleValues().contains(age)) {
                                ageProperty = integerProperty;
                                break;
                            }
                        }
                    }

                    if (ageProperty instanceof IntegerProperty integerProperty) {
                        BlockState seedAsBlockCrop = cropState.setValue(integerProperty, age);
                        blockModelResolver.update(renderState.plantModel, seedAsBlockCrop, BlockDisplayContext.create());
                    }
                }
            }
        }
    }

    @Override
    public void submit(ClocheBlockEntityRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {


        // Soil
        if (renderState.soil != null) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.05, 0.5);
            poseStack.scale(1.85f, 0.1f, 1.85f);
            renderState.soilRenderer.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }

        // Plant
        if (renderState.seed != null) {
            poseStack.pushPose();
            poseStack.translate(0.1, 0.05, 0.1);
            poseStack.scale(0.8f, 0.8f, 0.8f);
            renderState.plantModel.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
    }
}