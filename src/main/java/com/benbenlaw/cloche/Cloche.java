package com.benbenlaw.cloche;

import com.benbenlaw.cloche.block.ClocheBlockEntities;
import com.benbenlaw.cloche.block.ClocheBlocks;
import com.benbenlaw.cloche.block.ClocheCapabilities;
import com.benbenlaw.cloche.item.ClocheCreativeTab;
import com.benbenlaw.cloche.item.ClocheDataComponent;
import com.benbenlaw.cloche.item.ClocheItems;
import com.benbenlaw.cloche.networking.ClocheMessages;
import com.benbenlaw.cloche.recipe.ClocheRecipes;
import com.benbenlaw.cloche.screen.ClocheMenus;
import com.benbenlaw.cloche.screen.cloche.ClocheScreen;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.apache.logging.log4j.LogManager;

@Mod(Cloche.MOD_ID)
public class Cloche{

    public static final String MOD_ID = "cloche";
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();

    public Cloche(final IEventBus modEventBus, final ModContainer modContainer) {

        ClocheBlocks.BLOCKS.register(modEventBus);
        ClocheBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ClocheCreativeTab.CREATIVE_MODE_TABS.register(modEventBus);
        ClocheItems.ITEMS.register(modEventBus);
        ClocheDataComponent.COMPONENTS.register(modEventBus);
        ClocheRecipes.TYPES.register(modEventBus);
        ClocheRecipes.SERIALIZER.register(modEventBus);
        ClocheMenus.MENUS.register(modEventBus);

        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(this::commonSetup);

    }

    public static Identifier identifier(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        ClocheCapabilities.registerCapabilities(event);
    }

    @EventBusSubscriber(modid = Cloche.MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(ClocheMenus.CLOCHE_MENU.get(), ClocheScreen::new);
        }
    }

    public void commonSetup(RegisterPayloadHandlersEvent event) {
        ClocheMessages.registerNetworking(event);
    }

}
