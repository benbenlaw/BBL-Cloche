package com.benbenlaw.cloche.event.client;

import com.benbenlaw.cloche.Cloche;
import com.benbenlaw.cloche.block.ClocheBlockEntities;
import com.benbenlaw.cloche.block.entity.renderer.ClocheBlockEntityRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = Cloche.MOD_ID, value = Dist.CLIENT)
public class ClientListener {

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ClocheBlockEntities.CLOCHE_BLOCK_ENTITY.get(), ClocheBlockEntityRenderer::new);
    }
}
