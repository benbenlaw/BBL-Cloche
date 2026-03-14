package com.benbenlaw.cloche.recipe;

import com.benbenlaw.cloche.Cloche;
import com.benbenlaw.cloche.recipe.cloche.ClocheRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ClocheRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZER = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Cloche.MOD_ID);

    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, Cloche.MOD_ID);

    //Cloche
    public static final Supplier<RecipeSerializer<ClocheRecipe>> CLOCHE_SERIALIZER =
            SERIALIZER.register("cloche", () -> ClocheRecipe.SERIALIZER);

    public static final Supplier<RecipeType<ClocheRecipe>> CLOCHE_TYPE =
            TYPES.register("cloche", () -> ClocheRecipe.TYPE);

}