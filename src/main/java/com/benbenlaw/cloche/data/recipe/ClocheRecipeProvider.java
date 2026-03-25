package com.benbenlaw.cloche.data.recipe;

import com.benbenlaw.cloche.Cloche;
import com.benbenlaw.cloche.recipe.cloche.ClocheRecipe;
import com.benbenlaw.core.recipe.ChanceResult;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class ClocheRecipeProvider implements RecipeBuilder {

    public String group;
    public Ingredient seed;
    public Ingredient soil;
    public Optional<Ingredient> catalyst;
    public int duration;
    public NonNullList<ChanceResult> results;
    protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    public Optional<ItemStackTemplate> shearsResult;

    public ClocheRecipeProvider(Ingredient seed, Ingredient soil, Ingredient catalyst, int duration, NonNullList<ChanceResult> results, ItemStackTemplate shearsResult) {
        this.seed = seed;
        this.soil = soil;
        this.catalyst = Optional.ofNullable(catalyst);
        this.duration = duration;
        this.results = results;
        this.shearsResult = Optional.ofNullable(shearsResult);
    }

    public static ClocheRecipeProvider clocheRecipeBuilder(Ingredient seed, Ingredient soil, Ingredient catalyst, int duration, NonNullList<ChanceResult> results, ItemStackTemplate shearsResult) {
        return new ClocheRecipeProvider(seed, soil, catalyst, duration, results, shearsResult);
    }
    @Override
    public @NotNull RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public @NotNull RecipeBuilder group(@Nullable String groupName) {
        this.group = groupName;
        return this;
    }

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        ItemStack stack = results.getFirst().template().create();
        return ResourceKey.create(
                Registries.RECIPE,
                Cloche.identifier("cloche/" + stack.getItem().builtInRegistryHolder().key().identifier().getPath())
        );
    }

    @Override
    public void save(@NotNull RecipeOutput recipeOutput, @NotNull String id) {
        save(recipeOutput, ResourceKey.create(Registries.RECIPE, Cloche.identifier("cloche/" + id)));
    }


    @Override
    public void save(RecipeOutput recipeOutput, @NotNull ResourceKey<Recipe<?>> resourceKey) {
        Advancement.Builder builder = Advancement.Builder.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceKey))
                .rewards(AdvancementRewards.Builder.recipe(resourceKey))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(builder::addCriterion);
        ClocheRecipe clocheRecipe = new ClocheRecipe(seed, soil, catalyst, duration, results, shearsResult);
        recipeOutput.accept(resourceKey, clocheRecipe, builder.build(resourceKey.identifier().withPrefix("recipes/cloche/")));

    }



}
