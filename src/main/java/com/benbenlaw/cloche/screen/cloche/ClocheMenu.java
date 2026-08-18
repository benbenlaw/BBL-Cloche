package com.benbenlaw.cloche.screen.cloche;

import com.benbenlaw.cloche.block.entity.ClocheBlockEntity;
import com.benbenlaw.cloche.recipe.cloche.ClocheRecipe;
import com.benbenlaw.cloche.recipe.ClocheRecipeCache;
import com.benbenlaw.cloche.screen.ClocheMenus;
import com.benbenlaw.cloche.screen.util.CatalystSlot;
import com.benbenlaw.cloche.screen.util.SeedSlot;
import com.benbenlaw.cloche.screen.util.SoilSlot;
import com.benbenlaw.cloche.util.ClocheTags;
import com.benbenlaw.core.screen.SimpleAbstractContainerMenu;
import com.benbenlaw.core.screen.util.slot.InputSlot;
import com.benbenlaw.core.screen.util.slot.WhitelistSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClocheMenu extends SimpleAbstractContainerMenu {

    protected ClocheBlockEntity blockEntity;
    protected Level level;
    protected ContainerData data;
    protected Player player;
    protected BlockPos blockPos;
    public int numberOfCatalysts = 0;

    public ClocheMenu(int containerID, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerID, inventory, extraData.readBlockPos(), new SimpleContainerData(2));
    }

    public ClocheMenu(int containerID, Inventory inventory, BlockPos blockPos, ContainerData data) {
        super(ClocheMenus.CLOCHE_MENU.get(), containerID, inventory, blockPos, 18);
        this.player = inventory.player;
        this.blockPos = blockPos;
        this.level = inventory.player.level();
        this.blockEntity = (ClocheBlockEntity) this.level.getBlockEntity(blockPos);
        this.data = data;

        ClocheBlockEntity entity = (ClocheBlockEntity) this.level.getBlockEntity(blockPos);

        checkContainerSize(inventory, 18);

        assert entity != null;

        this.addSlot(new SeedSlot(level, blockEntity.getInputHandler(), blockEntity.getInputHandler()::set, ClocheBlockEntity.SEED_SLOT, 8, 17));
        this.addSlot(new SoilSlot(level, blockEntity.getInputHandler(), blockEntity.getInputHandler()::set, ClocheBlockEntity.SOIL_SLOT, 8, 17 + 18));
        this.addSlot(new CatalystSlot(level, blockEntity.getInputHandler(), blockEntity.getInputHandler()::set, ClocheBlockEntity.CATALYST_SLOT, 8, 17 + 36));

        for (int i = 0; i < 3; i++) {
            this.addSlot(new InputSlot(blockEntity.getUpgradeHandler(), blockEntity.getUpgradeHandler()::set, i, 35 + i * 18, 53) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.is(ClocheTags.Items.UPGRADES);
                }
            }.size(1));
        }

        int outputSlot = 0;

        for (int col = 0; col < 4; col++) {
            this.addSlot(new InputSlot(blockEntity.getOutputHandler(), blockEntity.getOutputHandler()::set, outputSlot++, 98 + col * 18,17));
        }

        for (int col = 0; col < 4; col++) {
            this.addSlot(new InputSlot(blockEntity.getOutputHandler(), blockEntity.getOutputHandler()::set, outputSlot++,98 + col * 18,35));
        }

        for (int col = 0; col < 4; col++) {
            this.addSlot(new InputSlot(blockEntity.getOutputHandler(), blockEntity.getOutputHandler()::set, outputSlot++, 98 + col * 18, 53));
        }

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);
        addDataSlots(data);
    }

    public boolean isCrafting() {
        return data.get(0) > 0 ;
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);  // Max Progress
        int progressArrowSize = 24; // This is the height in pixels of your arrow
        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }
}
