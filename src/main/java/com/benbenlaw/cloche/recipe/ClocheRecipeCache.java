package com.benbenlaw.cloche.recipe;

import com.benbenlaw.cloche.recipe.cloche.ClocheRecipe;
import net.minecraft.resources.Identifier;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ClocheRecipeCache {

    public static Map<Identifier, ClocheRecipe> cachedRecipes = new HashMap<>();

    public static void setRecipe(Map<Identifier, ClocheRecipe> recipes) {
        cachedRecipes = recipes;
    }

    public static Collection<ClocheRecipe> getRecipes() {
        return cachedRecipes.values();
    }
}
