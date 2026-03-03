package com.benbenlaw.cloche.recipe.cloche;

import com.benbenlaw.core.block.entity.handler.item.InputItemHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public class ClocheRecipeInput implements RecipeInput {

    private final InputItemHandler handler;

    public ClocheRecipeInput(InputItemHandler handler) {
        this.handler = handler;
    }

    @Override
    public ItemStack getItem(int i) {
        return handler.getResource(i).toStack();
    }

    @Override
    public int size() {
        return handler.size();
    }
}