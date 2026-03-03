package com.benbenlaw.cloche.screen.cloche;

import com.benbenlaw.cloche.block.entity.ClocheBlockEntity;
import com.benbenlaw.cloche.recipe.cloche.ClocheRecipe;
import com.benbenlaw.cloche.recipe.ClocheRecipeCache;
import com.benbenlaw.cloche.screen.ClocheMenus;
import com.benbenlaw.cloche.util.ClocheTags;
import com.benbenlaw.core.screen.SimpleAbstractContainerMenu;
import com.benbenlaw.core.screen.util.slot.InputSlot;
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

        this.addSlot(new InputSlot(blockEntity.getInputHandler(), blockEntity.getInputHandler()::set, ClocheBlockEntity.SEED_SLOT, 8, 17) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return isValidSeed(stack);
            }
        }.size(1));

        this.addSlot(new InputSlot(blockEntity.getInputHandler(), blockEntity.getInputHandler()::set, ClocheBlockEntity.SOIL_SLOT, 8, 17 + 18) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return isValidSoil(stack);
            }
        }.size(1));

        this.addSlot(new InputSlot(blockEntity.getInputHandler(), blockEntity.getInputHandler()::set, ClocheBlockEntity.CATALYST_SLOT, 8, 17 + 36) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return isValidCatalyst(stack);
            }
        }.size(1));

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

    public Map<String, List<Ingredient>> getRecipeOptions() {

        Map<String, List<Ingredient>> options = new HashMap<>();

        List<Ingredient> soilOptions = new ArrayList<>();
        List<Ingredient> seedOptions = new ArrayList<>();
        List<Ingredient> catalystOptions = new ArrayList<>();

        for (ClocheRecipe recipe : ClocheRecipeCache.getRecipes()) {
            soilOptions.add(recipe.soil());
            seedOptions.add(recipe.seed());
            recipe.catalyst().ifPresent(catalystOptions::add);
        }

        numberOfCatalysts = catalystOptions.size();

        options.put("soil", soilOptions);
        options.put("seed", seedOptions);
        options.put("catalyst", catalystOptions);

        return options;
    }

    public boolean isValidSoil(ItemStack stack) {
        return getRecipeOptions().get("soil").stream().anyMatch(ingredient -> ingredient.test(stack));
    }
    public boolean isValidSeed(ItemStack stack) {
        return getRecipeOptions().get("seed").stream().anyMatch(ingredient -> ingredient.test(stack));
    }
    public boolean isValidCatalyst(ItemStack stack) {
        return getRecipeOptions().get("catalyst").stream().anyMatch(ingredient -> ingredient.test(stack));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();

        // Player inventory → machine
        if (index < 36) {

            if (isValidSeed(stack)) {
                if (!this.moveItemStackTo(stack, 36, 37, false)) return ItemStack.EMPTY;
            }
            else if (isValidSoil(stack)) {
                if (!this.moveItemStackTo(stack, 37, 38, false)) return ItemStack.EMPTY;
            }
            else if (isValidCatalyst(stack)) {
                if (!this.moveItemStackTo(stack, 38, 39, false)) return ItemStack.EMPTY;
            }
            else if (stack.is(ClocheTags.Items.UPGRADES)) {
                if (!this.moveItemStackTo(stack, 39, 42, false)) return ItemStack.EMPTY;
            }
            else {
                return ItemStack.EMPTY; // 🚨 nothing accepts it → abort safely
            }

        }
        // Machine → player inventory
        else {
            if (!this.moveItemStackTo(stack, 0, 36, false)) return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        slot.onTake(player, stack);
        return copy;
    }

}
