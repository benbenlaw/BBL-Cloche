package com.benbenlaw.cloche.item;

import com.benbenlaw.cloche.Cloche;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ClocheItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Cloche.MOD_ID);

    public static final DeferredItem<Item> NO_SEEDS_UPGRADE = ITEMS.registerSimpleItem("no_seeds_upgrade");
    public static final DeferredItem<Item> MAIN_OUTPUT_UPGRADE = ITEMS.registerSimpleItem("main_output_upgrade");
    public static final DeferredItem<Item> SHEARS_UPGRADE = ITEMS.registerSimpleItem("shears_upgrade");
    public static final DeferredItem<Item> NO_OTHER_DROPS_UPGRADE = ITEMS.registerSimpleItem("no_other_drops_upgrade");
    public static final DeferredItem<Item> MUTATION_UPGRADE = ITEMS.registerSimpleItem("mutation_upgrade");

}
