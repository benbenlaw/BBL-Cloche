package com.benbenlaw.cloche.data;

import com.benbenlaw.cloche.Cloche;
import com.benbenlaw.cloche.block.ClocheBlocks;
import com.benbenlaw.cloche.data.recipe.ClocheRecipeProvider;
import com.benbenlaw.cloche.data.recipe.ResultLists;
import com.benbenlaw.cloche.item.ClocheItems;
import com.benbenlaw.core.recipe.ChanceResult;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ClocheRecipesProvider extends RecipeProvider {

    public ClocheRecipesProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
            super(packOutput, provider);
        }

        @Override
        protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.@NotNull Provider provider, @NotNull RecipeOutput recipeOutput) {
            return new ClocheRecipesProvider(provider, recipeOutput);
        }

        @Override
        public @NotNull String getName() {
            return Cloche.MOD_ID + " Recipes";
        }
    }
    
    
    @Override
    protected void buildRecipes() {
        
        // Shears Upgrade - Crafting Recipe
        shaped(RecipeCategory.MISC, ClocheItems.SHEARS_UPGRADE.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', Tags.Items.TOOLS_SHEAR)
                .define('B', Items.IRON_INGOT)
                .group("cloche")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(output);

        // Main Output Upgrade - Crafting Recipe
        shaped(RecipeCategory.MISC, ClocheItems.MAIN_OUTPUT_UPGRADE.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', Items.GOLDEN_CARROT)
                .define('B', Items.IRON_INGOT)
                .group("cloche")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(output);

        // No Seed Upgrade - Crafting Recipe
        shaped(RecipeCategory.MISC, ClocheItems.NO_SEEDS_UPGRADE.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', Tags.Items.SEEDS)
                .define('B', Items.IRON_INGOT)
                .group("cloche")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(output);

        // No Other Drops Upgrade
        shapeless(RecipeCategory.MISC, ClocheItems.NO_OTHER_DROPS_UPGRADE.get())
                .requires(ClocheItems.NO_SEEDS_UPGRADE.get())
                .requires(ClocheItems.MAIN_OUTPUT_UPGRADE.get())
                .unlockedBy("has_item", has(ClocheItems.NO_SEEDS_UPGRADE.get()))
                .save(output);

        // Cloche - Crafting Recipe
        shaped(RecipeCategory.MISC, ClocheBlocks.CLOCHE.get())
                .pattern("III")
                .pattern("WDW")
                .pattern("III")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('W', Tags.Items.BUCKETS_WATER)
                .define('D',  ItemTags.DIRT)
                .group("cloche")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output);
        

        // Cloche Wheat
        ClocheRecipeProvider.clocheRecipeBuilder(Ingredient.of(Items.WHEAT_SEEDS), tag(ItemTags.DIRT),
                        null, 1200, ResultLists.WHEAT_RESULTS, null)
                .save(output, "cloche/wheat");

        // Cloche Potato
        ClocheRecipeProvider.clocheRecipeBuilder(Ingredient.of(Items.POTATO), tag(ItemTags.DIRT),
                        null, 1200, ResultLists.POTATO_RESULTS, null)
                .save(output, "cloche/potato");

        // Cloche Beetroot
        ClocheRecipeProvider.clocheRecipeBuilder(Ingredient.of(Items.BEETROOT_SEEDS),tag(ItemTags.DIRT),
                        null, 1200, ResultLists.BEETROOT_RESULTS, null)
                .save(output, "cloche/beetroot");

        // Cloche Melon
        ClocheRecipeProvider.clocheRecipeBuilder(Ingredient.of(Items.MELON_SEEDS), tag(ItemTags.DIRT),
                        null, 1200, ResultLists.MELON_RESULTS, null)
                .save(output, "cloche/melon");

        // Cloche Pumpkin
        ClocheRecipeProvider.clocheRecipeBuilder(Ingredient.of(Items.PUMPKIN_SEEDS), tag(ItemTags.DIRT),
                        null, 1200, ResultLists.PUMPKIN_RESULTS, null)
                .save(output, "cloche/pumpkin");

        // Cloche Chorus Fruit
        ClocheRecipeProvider.clocheRecipeBuilder(Ingredient.of(Items.CHORUS_FRUIT), Ingredient.of(Items.END_STONE),
                        null, 1200, ResultLists.CHORUS_FRUIT_RESULTS, null)
                .save(output, "cloche/chorus_fruit");

        // Oak Sapling
        ClocheRecipeProvider.clocheRecipeBuilder(Ingredient.of(Items.OAK_SAPLING), tag(ItemTags.DIRT),
                        null, 1200, ResultLists.OAK_SAPLING_RESULTS, new ItemStackTemplate(Items.OAK_LEAVES, 2))
                .save(output, "cloche/oak_sapling");

        // Spruce Sapling
        ClocheRecipeProvider.clocheRecipeBuilder(Ingredient.of(Items.SPRUCE_SAPLING), tag(ItemTags.DIRT),
                        null, 1200, ResultLists.SPRUCE_SAPLING_RESULTS, new ItemStackTemplate(Items.SPRUCE_LEAVES, 2))
                .save(output, "cloche/spruce_sapling");

        // Birch Sapling
        ClocheRecipeProvider.clocheRecipeBuilder(Ingredient.of(Items.BIRCH_SAPLING), tag(ItemTags.DIRT),
                        null, 1200, ResultLists.BIRCH_SAPLING_RESULTS, new ItemStackTemplate(Items.BIRCH_LEAVES, 2))
                .save(output, "cloche/birch_sapling");

        // Jungle Sapling
        ClocheRecipeProvider.clocheRecipeBuilder(Ingredient.of(Items.JUNGLE_SAPLING), tag(ItemTags.DIRT),
                        null, 1200, ResultLists.JUNGLE_SAPLING_RESULTS, new ItemStackTemplate(Items.JUNGLE_LEAVES, 2))
                .save(output, "cloche/jungle_sapling");

        // Acacia Sapling
        ClocheRecipeProvider.clocheRecipeBuilder(Ingredient.of(Items.ACACIA_SAPLING), tag(ItemTags.DIRT),
                        null, 1200, ResultLists.ACACIA_SAPLING_RESULTS, new ItemStackTemplate(Items.ACACIA_LEAVES, 2))
                .save(output, "cloche/acacia_sapling");

        // Cherry Sapling
        ClocheRecipeProvider.clocheRecipeBuilder(Ingredient.of(Items.CHERRY_SAPLING), tag(ItemTags.DIRT),
                        null, 1200, ResultLists.CHERRY_SAPLING_RESULTS, new ItemStackTemplate(Items.CHERRY_LEAVES, 2))
                .save(output, "cloche/cherry_sapling");

        //Mangrove Sapling
        ClocheRecipeProvider.clocheRecipeBuilder(Ingredient.of(Items.MANGROVE_PROPAGULE), tag(ItemTags.DIRT),
                        null, 1200, ResultLists.MANGROVE_SAPLING_RESULTS, new ItemStackTemplate(Items.MANGROVE_LEAVES, 2))
                .save(output, "cloche/mangrove_sapling");

        // Dark Oak Sapling
        ClocheRecipeProvider.clocheRecipeBuilder(Ingredient.of(Items.DARK_OAK_SAPLING), tag(ItemTags.DIRT),
                        null, 1200, ResultLists.DARK_OAK_SAPLING_RESULTS, new ItemStackTemplate(Items.DARK_OAK_LEAVES, 2))
                .save(output, "cloche/dark_oak_sapling");

        // Crimson Fungus
        ClocheRecipeProvider.clocheRecipeBuilder(Ingredient.of(Items.CRIMSON_FUNGUS), tag(ItemTags.DIRT),
                        null, 1200, ResultLists.CRIMSON_FUNGUS_RESULTS, null)
                .save(output, "cloche/crimson_fungus");

        // Warped Fungus
        ClocheRecipeProvider.clocheRecipeBuilder(Ingredient.of(Items.WARPED_FUNGUS), tag(ItemTags.DIRT),
                        null, 1200, ResultLists.WARPED_FUNGUS_RESULTS, null)
                .save(output, "cloche/warped_fungus");

        //Pale Oak Sapling
        ClocheRecipeProvider.clocheRecipeBuilder(Ingredient.of(Items.PALE_OAK_SAPLING), tag(ItemTags.DIRT),
                        null, 1200, ResultLists.PALE_OAK_SAPLING_RESULTS, new ItemStackTemplate(Items.PALE_OAK_LEAVES, 2))
                .save(output, "cloche/pale_oak_sapling");

        //Cactus
        ClocheRecipeProvider.clocheRecipeBuilder(Ingredient.of(Items.CACTUS), tag(ItemTags.DIRT),
                        null, 1200, ResultLists.CACTUS_RESULTS, null)
                .save(output, "cloche/cactus");

        // Single Item Recipes
        createSingleItemRecipe(new ItemStackTemplate(Items.DANDELION), tag(ItemTags.DIRT), 1200, "dandelion");
        createSingleItemRecipe(new ItemStackTemplate(Items.POPPY), tag(ItemTags.DIRT), 1200, "poppy");
        createSingleItemRecipe(new ItemStackTemplate(Items.BLUE_ORCHID), tag(ItemTags.DIRT), 1200, "blue_orchid");
        createSingleItemRecipe(new ItemStackTemplate(Items.ALLIUM), tag(ItemTags.DIRT), 1200, "allium");
        createSingleItemRecipe(new ItemStackTemplate(Items.AZURE_BLUET), tag(ItemTags.DIRT), 1200, "azure_bluet");
        createSingleItemRecipe(new ItemStackTemplate(Items.RED_TULIP), tag(ItemTags.DIRT), 1200, "red_tulip");
        createSingleItemRecipe(new ItemStackTemplate(Items.ORANGE_TULIP), tag(ItemTags.DIRT), 1200, "orange_tulip");
        createSingleItemRecipe(new ItemStackTemplate(Items.WHITE_TULIP), tag(ItemTags.DIRT), 1200, "white_tulip");
        createSingleItemRecipe(new ItemStackTemplate(Items.PINK_TULIP), tag(ItemTags.DIRT), 1200, "pink_tulip");
        createSingleItemRecipe(new ItemStackTemplate(Items.OXEYE_DAISY), tag(ItemTags.DIRT), 1200, "oxeye_daisy");
        createSingleItemRecipe(new ItemStackTemplate(Items.CORNFLOWER), tag(ItemTags.DIRT), 1200, "cornflower");
        createSingleItemRecipe(new ItemStackTemplate(Items.LILY_OF_THE_VALLEY), tag(ItemTags.DIRT), 1200, "lily_of_the_valley");
        createSingleItemRecipe(new ItemStackTemplate(Items.WITHER_ROSE), tag(ItemTags.DIRT), 1200, "wither_rose");
        createSingleItemRecipe(new ItemStackTemplate(Items.PINK_PETALS), tag(ItemTags.DIRT), 1200, "pink_petals");
        createSingleItemRecipe(new ItemStackTemplate(Items.SPORE_BLOSSOM), tag(ItemTags.DIRT), 1200, "spore_blossom");
        createSingleItemRecipe(new ItemStackTemplate(Items.FERN), tag(ItemTags.DIRT), 1200, "fern");
        createSingleItemRecipe(new ItemStackTemplate(Items.SHORT_GRASS), tag(ItemTags.DIRT), 1200, "short_grass");
        createSingleItemRecipe(new ItemStackTemplate(Items.SUNFLOWER), tag(ItemTags.DIRT), 1200, "sunflower");
        createSingleItemRecipe(new ItemStackTemplate(Items.LILAC), tag(ItemTags.DIRT), 1200, "lilac");
        createSingleItemRecipe(new ItemStackTemplate(Items.ROSE_BUSH), tag(ItemTags.DIRT), 1200, "rose_bush");
        createSingleItemRecipe(new ItemStackTemplate(Items.PEONY), tag(ItemTags.DIRT), 1200, "peony");
        createSingleItemRecipe(new ItemStackTemplate(Items.GLOW_LICHEN), tag(Tags.Items.STONES), 1200, "glow_lichen");
        createSingleItemRecipe(new ItemStackTemplate(Items.LILY_PAD), tag(Tags.Items.BUCKETS_WATER), 1200, "lily_pad");
        createSingleItemRecipe(new ItemStackTemplate(Items.SEAGRASS), tag(Tags.Items.BUCKETS_WATER), 1200, "seagrass");
        createSingleItemRecipe(new ItemStackTemplate(Items.SEA_PICKLE), tag(Tags.Items.BUCKETS_WATER), 1200, "sea_pickle");
        createSingleItemRecipe(new ItemStackTemplate(Items.KELP), tag(Tags.Items.BUCKETS_WATER), 1200, "kelp");
        createSingleItemRecipe(new ItemStackTemplate(Items.VINE), tag(ItemTags.JUNGLE_LOGS), 1200, "vine");
        createSingleItemRecipe(new ItemStackTemplate(Items.COCOA_BEANS), tag(ItemTags.JUNGLE_LOGS), 1200, "cocoa_beans");
        createSingleItemRecipe(new ItemStackTemplate(Items.BAMBOO), tag(ItemTags.DIRT), 1200, "bamboo");
        createSingleItemRecipe(new ItemStackTemplate(Items.SUGAR_CANE), tag(ItemTags.DIRT), 1200, "sugar_cane");
        createSingleItemRecipe(new ItemStackTemplate(Items.SWEET_BERRIES), tag(ItemTags.DIRT), 1200, "sweet_berries");
        createSingleItemRecipe(new ItemStackTemplate(Items.NETHER_WART), Ingredient.of(Items.SOUL_SAND), 1200, "nether_wart");
        createSingleItemRecipe(new ItemStackTemplate(Items.CARROT), tag(ItemTags.DIRT), 1200, "carrot");
        createSingleItemRecipe(new ItemStackTemplate(Items.BROWN_MUSHROOM), tag(ItemTags.DIRT), 1200, "brown_mushroom");
        createSingleItemRecipe(new ItemStackTemplate(Items.RED_MUSHROOM), tag(ItemTags.DIRT), 1200, "red_mushroom");
        createSingleItemRecipe(new ItemStackTemplate(Items.FIREFLY_BUSH), tag(ItemTags.DIRT), 1200, "firefly_bush");
        createSingleItemRecipe(new ItemStackTemplate(Items.TORCHFLOWER), tag(ItemTags.DIRT), 1200, "torchflower");
        createSingleItemRecipe(new ItemStackTemplate(Items.CLOSED_EYEBLOSSOM), tag(ItemTags.DIRT), 1200, "closed_eyeblossom");
        createSingleItemRecipe(new ItemStackTemplate(Items.OPEN_EYEBLOSSOM), tag(ItemTags.DIRT), 1200, "open_eyeblossom");
        createSingleItemRecipe(new ItemStackTemplate(Items.WILDFLOWERS), tag(ItemTags.DIRT), 1200, "wildflowers");
        createSingleItemRecipe(new ItemStackTemplate(Items.GOLDEN_DANDELION), Ingredient.of(Items.GOLD_BLOCK), 1200, "golden_dandelion");
    }

    public void createSingleItemRecipe(ItemStackTemplate item, Ingredient soil, int duration, String name) {

        NonNullList<ChanceResult> SINGLE_ITEM_RESULTS = NonNullList.create();
        SINGLE_ITEM_RESULTS.add(new ChanceResult(item, 1.0f));

        ClocheRecipeProvider.clocheRecipeBuilder(Ingredient.of(item.typeHolder().value()), soil,
                        null, duration, SINGLE_ITEM_RESULTS, null)
                .save(output, "cloche/" + name);

    }
}
