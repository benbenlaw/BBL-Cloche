package com.benbenlaw.cloche.data.recipe;

import com.benbenlaw.core.recipe.ChanceResult;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;

public class ResultLists {

    public static final NonNullList<ChanceResult> WHEAT_RESULTS = NonNullList.create();
    public static final NonNullList<ChanceResult> POTATO_RESULTS = NonNullList.create();
    public static final NonNullList<ChanceResult> BEETROOT_RESULTS = NonNullList.create();
    public static final NonNullList<ChanceResult> MELON_RESULTS = NonNullList.create();
    public static final NonNullList<ChanceResult> PUMPKIN_RESULTS = NonNullList.create();
    public static final NonNullList<ChanceResult> CHORUS_FRUIT_RESULTS = NonNullList.create();
    public static final NonNullList<ChanceResult> CRIMSON_FUNGUS_RESULTS = NonNullList.create();
    public static final NonNullList<ChanceResult> WARPED_FUNGUS_RESULTS = NonNullList.create();
    public static final NonNullList<ChanceResult> OAK_SAPLING_RESULTS = NonNullList.create();
    public static final NonNullList<ChanceResult> SPRUCE_SAPLING_RESULTS = NonNullList.create();
    public static final NonNullList<ChanceResult> BIRCH_SAPLING_RESULTS = NonNullList.create();
    public static final NonNullList<ChanceResult> JUNGLE_SAPLING_RESULTS = NonNullList.create();
    public static final NonNullList<ChanceResult> ACACIA_SAPLING_RESULTS = NonNullList.create();
    public static final NonNullList<ChanceResult> DARK_OAK_SAPLING_RESULTS = NonNullList.create();
    public static final NonNullList<ChanceResult> CHERRY_SAPLING_RESULTS = NonNullList.create();
    public static final NonNullList<ChanceResult> MANGROVE_SAPLING_RESULTS = NonNullList.create();
    public static final NonNullList<ChanceResult> PALE_OAK_SAPLING_RESULTS = NonNullList.create();
    public static final NonNullList<ChanceResult> CACTUS_RESULTS = NonNullList.create();


    static {
        WHEAT_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.WHEAT), 1.0f));
        WHEAT_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.WHEAT_SEEDS), 0.2f));

        POTATO_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.POTATO), 1.0f));
        POTATO_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.POISONOUS_POTATO), 0.1f));

        BEETROOT_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.BEETROOT), 1.0f));
        BEETROOT_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.BEETROOT_SEEDS), 0.2f));

        MELON_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.MELON), 1.0f));

        PUMPKIN_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.PUMPKIN), 1.0f));

        CHORUS_FRUIT_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.CHORUS_FRUIT), 1.0f));

        CRIMSON_FUNGUS_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.CRIMSON_STEM, 2), 1.0f));
        CRIMSON_FUNGUS_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.CRIMSON_FUNGUS), 0.5f));
        CRIMSON_FUNGUS_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.SHROOMLIGHT), 0.1f));
        CRIMSON_FUNGUS_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.NETHER_WART_BLOCK), 0.4f));

        WARPED_FUNGUS_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.WARPED_STEM, 2), 1.0f));
        WARPED_FUNGUS_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.WARPED_FUNGUS), 0.5f));
        WARPED_FUNGUS_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.SHROOMLIGHT), 0.1f));
        WARPED_FUNGUS_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.WARPED_WART_BLOCK), 0.4f));

        OAK_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.OAK_LOG, 2), 1.0f));
        OAK_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.OAK_SAPLING), 0.2f));
        OAK_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.APPLE), 0.2f));
        OAK_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.STICK), 0.1f));

        SPRUCE_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.SPRUCE_LOG, 2), 1.0f));
        SPRUCE_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.SPRUCE_SAPLING), 0.2f));
        SPRUCE_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.STICK), 0.1f));

        BIRCH_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.BIRCH_LOG, 2), 1.0f));
        BIRCH_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.BIRCH_SAPLING), 0.2f));
        BIRCH_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.STICK), 0.1f));

        JUNGLE_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.JUNGLE_LOG, 2), 1.0f));
        JUNGLE_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.JUNGLE_SAPLING), 0.2f));
        JUNGLE_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.COCOA_BEANS), 0.2f));
        JUNGLE_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.STICK), 0.1f));

        ACACIA_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.ACACIA_LOG, 2), 1.0f));
        ACACIA_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.ACACIA_SAPLING), 0.2f));
        ACACIA_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.STICK), 0.1f));

        DARK_OAK_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.DARK_OAK_LOG, 2), 1.0f));
        DARK_OAK_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.DARK_OAK_SAPLING), 0.2f));
        DARK_OAK_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.APPLE), 0.2f));
        DARK_OAK_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.STICK), 0.1f));

        CHERRY_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.CHERRY_LOG, 2), 1.0f));
        CHERRY_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.CHERRY_SAPLING), 0.2f));
        CHERRY_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.STICK), 0.1f));

        MANGROVE_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.MANGROVE_LOG, 2), 1.0f));
        MANGROVE_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.MANGROVE_PROPAGULE), 0.2f));
        MANGROVE_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.STICK), 0.1f));

        PALE_OAK_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.PALE_OAK_LOG, 2), 1.0f));
        PALE_OAK_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.PALE_OAK_SAPLING), 0.2f));
        PALE_OAK_SAPLING_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.STICK), 0.1f));

        CACTUS_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.CACTUS, 1), 0.9f));
        CACTUS_RESULTS.add(new ChanceResult(new ItemStackTemplate(Items.CACTUS_FLOWER, 1), 0.1f));

    }
}