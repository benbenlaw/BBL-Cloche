package com.benbenlaw.cloche.block.entity;

import com.benbenlaw.cloche.block.ClocheBlockEntities;
import com.benbenlaw.cloche.block.custom.ClocheBlock;
import com.benbenlaw.cloche.item.ClocheDataComponent;
import com.benbenlaw.cloche.item.ClocheItems;
import com.benbenlaw.cloche.item.util.CropData;
import com.benbenlaw.cloche.recipe.ClocheRecipeCache;
import com.benbenlaw.cloche.recipe.cloche.ClocheRecipe;
import com.benbenlaw.cloche.recipe.ClocheRecipes;
import com.benbenlaw.cloche.recipe.cloche.ClocheRecipeInput;
import com.benbenlaw.cloche.screen.cloche.ClocheMenu;
import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import com.benbenlaw.core.block.entity.handler.item.InputItemHandler;
import com.benbenlaw.core.block.entity.handler.item.OutputItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.fml.startup.Server;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ClocheBlockEntity extends SyncableBlockEntity implements MenuProvider {

    public final ContainerData data;
    public int maxProgress = Integer.MAX_VALUE;
    public int progress = 0;

    public static final int SEED_SLOT = 0;
    public static final int SOIL_SLOT = 1;
    public static final int CATALYST_SLOT = 2;
    public static final int UPGRADE_SLOT_1 = 0;
    public static final int UPGRADE_SLOT_2 = 1;
    public static final int UPGRADE_SLOT_3 = 2;
    public static final int[] OUTPUT_SLOTS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};

    private final InputItemHandler inputHandler = new InputItemHandler(this, 3,
            (i, stack) -> i == SEED_SLOT || i == SOIL_SLOT || i == CATALYST_SLOT) {
        @Override
        protected void onContentsChanged(int index, ItemStack previousContents) {
            updateCachedRecipe();
            super.onContentsChanged(index, previousContents);
        }
    };

    private final InputItemHandler upgradeHandler = new InputItemHandler(this, 3,
            (i, stack) -> i == UPGRADE_SLOT_1 || i == UPGRADE_SLOT_2 || i == UPGRADE_SLOT_3
    );

    private final OutputItemHandler outputHandler = new OutputItemHandler(this, 12, i -> true);

    private RecipeHolder<ClocheRecipe> cachedRecipe;

    public ClocheBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ClocheBlockEntities.CLOCHE_BLOCK_ENTITY.get(), blockPos, blockState);
        this.data = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> ClocheBlockEntity.this.progress;
                    case 1 -> ClocheBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> ClocheBlockEntity.this.progress = value;
                    case 1 -> ClocheBlockEntity.this.maxProgress = value;
                }
            }

            public int getCount() {
                return 2;
            }
        };
    }

    public void tick() {

        assert level != null;
        if (level.isClientSide()) return;

        boolean running = level.getBlockState(worldPosition).getValue(ClocheBlock.RUNNING);
        if (!running) {
            progress = 0;
            sync();
            return;
        }

        if (cachedRecipe == null) {
            updateCachedRecipe();
            return;
        }

        List<ItemStack> outputs = getActualOutputs(cachedRecipe.value());

        if (cachedRecipe != null && canInsertOutputs(outputs)) {
            CropData cropData = CropData.fromItemStack(inputHandler.getResource(SEED_SLOT).toStack());

            maxProgress = applySpeedModifier(cachedRecipe.value().duration(),cropData.speedModifier());
            progress++;
            if (progress >= maxProgress) craftItem();
        } else {
            progress = 0;
            sync();
        }
    }

    private void craftItem() {
        if (cachedRecipe == null || level == null) return;

        ClocheRecipe recipe = cachedRecipe.value();
        List<ItemStack> outputs = getActualOutputs(recipe);

        try (Transaction tx = Transaction.open(null)) {
            for (ItemStack stack : outputs) {
                int remaining = stack.getCount();

                for (int i = 0; i < outputHandler.size() && remaining > 0; i++) {
                    int inserted = outputHandler.insertInternalReturn(
                            i,
                            ItemResource.of(stack),
                            remaining,
                            tx
                    );
                    remaining -= inserted;
                }

                if (remaining > 0) {
                    return;
                }
            }

            tx.commit();
        }

        int mutationUpgradeCount = countUpgrade(ClocheItems.MUTATION_UPGRADE.get());
        if (mutationUpgradeCount > 0) {
            mutationEffect(mutationUpgradeCount);
        }

        progress = 0;
        sync();
    }

    private List<ItemStack> getActualOutputs(ClocheRecipe recipe) {
        if (level == null) return List.of();

        List<ItemStack> outputs = new ArrayList<>(recipe.rollResults(level.getRandom()));

        boolean hasNoSeedUpgrade = hasUpgrade(ClocheItems.NO_SEEDS_UPGRADE.get());
        boolean hasShearsUpgrade = hasUpgrade(ClocheItems.SHEARS_UPGRADE.get());
        boolean hasNoOtherDropsUpgrade = hasUpgrade(ClocheItems.NO_OTHER_DROPS_UPGRADE.get());
        int mainOutputUpgradeCount = countUpgrade(ClocheItems.MAIN_OUTPUT_UPGRADE.get());

        if (hasNoOtherDropsUpgrade && !outputs.isEmpty()) {
            ItemStack main = recipe.getRollResults().getFirst().template().create().copy();
            outputs.clear();
            outputs.add(main);
        }

        if (hasNoSeedUpgrade) {
            Ingredient seed = recipe.seed();
            outputs.removeIf(seed);
        }

        if (mainOutputUpgradeCount > 0 && !outputs.isEmpty()) {
            ItemStack main = outputs.getFirst();
            main.setCount(main.getCount() * (1 << mainOutputUpgradeCount));
        }

        CropData cropData = CropData.fromItemStack(inputHandler.getResource(SEED_SLOT).toStack());
        int outputMultiplier = cropData.outputMultiplier();

        if (outputMultiplier > 0 && !outputs.isEmpty()) {
            ItemStack main = outputs.getFirst();

            int guaranteedMultiplier = 1 + (outputMultiplier / 100);
            int remainderChance = outputMultiplier % 100;

            main.setCount(main.getCount() * guaranteedMultiplier);

            if (remainderChance > 0 && level.getRandom().nextInt(100) < remainderChance) {
                main.grow(main.getCount());
            }
        }

        if (hasShearsUpgrade) {
            if (recipe.shearsResult().isPresent()) {
                ItemStack shears = recipe.shearsResult().get().create();
                if (!shears.isEmpty()) {
                    outputs.add(shears.copy());
                }
            }
        }

        return mergeStacks(outputs);
    }

    private List<ItemStack> mergeStacks(List<ItemStack> stacks) {
        List<ItemStack> merged = new ArrayList<>();

        for (ItemStack stack : stacks) {
            boolean mergedExisting = false;

            for (ItemStack existing : merged) {
                if (ItemStack.isSameItemSameComponents(existing, stack)) {
                    existing.grow(stack.getCount());
                    mergedExisting = true;
                    break;
                }
            }

            if (!mergedExisting) {
                merged.add(stack.copy());
            }
        }

        return merged;
    }

    private void mutationEffect(int mutationUpgradeCount) {
        if (level == null || mutationUpgradeCount <= 0) return;

        int CHANCE_OF_MUTATION = 25; //Chance per upgrade as a percentage
        if (level.getRandom().nextInt(100) >= CHANCE_OF_MUTATION) return;

        ItemStack original = inputHandler.getResource(SEED_SLOT).toStack();
        if (original.isEmpty()) return;

        ItemStack mutated = original.copy();
        CropData cropData = CropData.fromItemStack(mutated);

        int outputMultiplier = cropData.outputMultiplier();
        int speedModifier = cropData.speedModifier();

        // Randomly pick which stat to improve
        if (level.getRandom().nextBoolean()) {
            outputMultiplier += 1;
        } else {
            speedModifier += 1;
        }

        // Cap at 100
        outputMultiplier = Math.min(outputMultiplier, 100);
        speedModifier = Math.min(speedModifier, 100);

        mutated.set(ClocheDataComponent.CROP_DATA.get(), new CropData(outputMultiplier, speedModifier));

        try (Transaction tx = Transaction.open(null)) {
            inputHandler.extractInternal(SEED_SLOT, ItemResource.of(original), original.getCount(), tx);
            inputHandler.insert(SEED_SLOT, ItemResource.of(mutated), mutated.getCount(), tx);
            tx.commit();
        }

        setChanged();
        sync();
    }


    private int applySpeedModifier(int baseDuration, int speedModifier) {
        double multiplier = 1.0 - (speedModifier / 100.0);
        multiplier = Math.max(multiplier, 0.05);
        return (int) Math.ceil(baseDuration * multiplier);
    }

    private boolean hasUpgrade(ItemLike upgrade) {
        ItemResource upgradeResource = ItemResource.of(upgrade);
        for (int i = 0; i < upgradeHandler.size(); i++) {
            ItemStack stack = upgradeHandler.getResource(i).toStack();
            if (!stack.isEmpty() && upgradeResource.matches(stack)) {
                return true;
            }
        }
        return false;
    }

    private int countUpgrade(ItemLike upgrade) {
        ItemResource resource = ItemResource.of(upgrade);
        int count = 0;

        for (int i = 0; i < upgradeHandler.size(); i++) {
            ItemStack stack = upgradeHandler.getResource(i).toStack();
            if (!stack.isEmpty() && resource.matches(stack)) {
                count += stack.getCount();
            }
        }

        return count;
    }

    private boolean canInsertOutputs(List<ItemStack> outputs) {
        try (Transaction tx = Transaction.open(null)) {
            for (ItemStack stack : outputs) {
                int remaining = stack.getCount();

                for (int i = 0; i < outputHandler.size() && remaining > 0; i++) {
                    int inserted = outputHandler.insertInternalReturn(
                            i,
                            ItemResource.of(stack),
                            remaining,
                            tx
                    );
                    remaining -= inserted;
                }

                if (remaining > 0) {
                    return false;
                }
            }
            return true;
        }
    }


    private void updateCachedRecipe() {
        if (level != null && level.getServer() != null) {
            cachedRecipe = level.getServer().getRecipeManager().getRecipeFor(ClocheRecipes.CLOCHE_TYPE.get(),
                    new ClocheRecipeInput(inputHandler), level
            ).orElse(null);
        }
    }

    public InputItemHandler getInputHandler() {
        return inputHandler;
    }

    public InputItemHandler getUpgradeHandler() {
        return upgradeHandler;
    }

    public OutputItemHandler getOutputHandler() {
        return outputHandler;
    }

    public ResourceHandler<ItemResource> getItemCapability() {
        return outputHandler;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int container, @NotNull Inventory inventory, @NotNull Player player) {
        return new ClocheMenu(container, inventory, this.getBlockPos(), data);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.cloche.cloche");
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {

        inputHandler.serialize(output.child("inputs"));
        upgradeHandler.serialize(output.child("upgrades"));
        outputHandler.serialize(output.child("outputs"));
        output.putInt("maxProgress", maxProgress);
        output.putInt("progress", progress);

        super.saveAdditional(output);

    }

    @Override
    protected void loadAdditional(ValueInput input) {

        inputHandler.deserialize(input.childOrEmpty("inputs"));
        upgradeHandler.deserialize(input.childOrEmpty("upgrades"));
        outputHandler.deserialize(input.childOrEmpty("outputs"));
        maxProgress = input.getIntOr("maxProgress", Integer.MAX_VALUE);
        progress = input.getIntOr("progress", 0);

        super.loadAdditional(input);
    }

    @Override
    public void preRemoveSideEffects(@NotNull BlockPos pos, @NotNull BlockState state) {
        dropInventoryContents(inputHandler);
        dropInventoryContents(upgradeHandler);
        dropInventoryContents(outputHandler);
    }



}
