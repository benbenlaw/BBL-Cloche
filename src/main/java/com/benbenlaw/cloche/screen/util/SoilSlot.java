//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.benbenlaw.cloche.screen.util;

import com.benbenlaw.cloche.recipe.ClocheRecipeCache;
import com.benbenlaw.cloche.recipe.cloche.ClocheRecipe;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

import java.util.List;

public class SoilSlot extends ResourceHandlerSlot {

    public SoilSlot(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int handlerSlot, int xPosition, int yPosition) {
        super(handler, slotModifier, handlerSlot, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {

        List<Ingredient> seeds = ClocheRecipeCache.getRecipes().stream().map(ClocheRecipe::soil).filter(seed -> seed.test(itemStack)).toList();

        for (Ingredient seed : seeds) {
            if (seed.test(itemStack)) {
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
