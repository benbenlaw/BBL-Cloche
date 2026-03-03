package com.benbenlaw.cloche.event.recipe;

import com.benbenlaw.cloche.Cloche;
import com.benbenlaw.cloche.recipe.cloche.ClocheRecipe;
import com.benbenlaw.cloche.recipe.ClocheRecipeCache;
import com.benbenlaw.cloche.recipe.ClocheRecipes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = Cloche.MOD_ID)
public class DataPackSyncEvent {


    @SubscribeEvent
    public static void onDataPackSync(OnDatapackSyncEvent event) {
        event.sendRecipes(ClocheRecipes.CLOCHE_TYPE.get());
    }

    @SubscribeEvent
    public static void onRecipeReceived(RecipesReceivedEvent event) {
        RecipeMap recipeMap = event.getRecipeMap();

        Collection<RecipeHolder<ClocheRecipe>> clocheRecipes = recipeMap.byType(ClocheRecipes.CLOCHE_TYPE.get());
        Map<Identifier, ClocheRecipe> clocheRecipeMap = new HashMap<>();

        for (RecipeHolder<ClocheRecipe> recipeHolder : clocheRecipes) {
            clocheRecipeMap.put(recipeHolder.id().identifier(), recipeHolder.value());
        }

        ClocheRecipeCache.setRecipe(clocheRecipeMap);
    }
}
