package com.benbenlaw.cloche.screen.util;

import com.benbenlaw.cloche.recipe.ClocheRecipeCache;
import com.benbenlaw.cloche.recipe.ClocheRecipes;
import com.benbenlaw.cloche.recipe.cloche.ClocheRecipe;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CatalystSlot extends ResourceHandlerSlot {

    private final Level level;

    public CatalystSlot(Level level, ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int handlerSlot, int xPosition, int yPosition) {
        super(handler, slotModifier, handlerSlot, xPosition, yPosition);
        this.level = level;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {

        List<ClocheRecipe> recipes = level.isClientSide()
                ? ClocheRecipeCache.getRecipes().stream().toList()
                : Objects.requireNonNull(level.getServer()).getRecipeManager().getRecipes().stream()
                .filter(recipe -> recipe.value().getType() == ClocheRecipes.CLOCHE_TYPE.get())
                .filter(recipe -> recipe.value() instanceof ClocheRecipe)
                .map(recipe -> (ClocheRecipe) recipe.value())
                .toList();

        List<Optional<Ingredient>> seeds = recipes.stream()
                .map(ClocheRecipe::catalyst)
                .filter(seed -> seed.isPresent() && seed.get().test(itemStack))
                .toList();

        for (Optional<Ingredient> seed : seeds) {
            if (seed.isEmpty()) continue;
            if (seed.get().test(itemStack)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int getMaxStackSize(ItemStack itemStack) {
        return 1;
    }

    @Override
    public boolean mayPickup(Player player) {
        return true;
    }
}