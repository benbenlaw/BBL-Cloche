package com.benbenlaw.cloche.data;

import com.benbenlaw.cloche.Cloche;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.jetbrains.annotations.NotNull;

public class ClocheLangProvider extends LanguageProvider {

    public ClocheLangProvider(PackOutput output) {
        super(output, Cloche.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.cloche", "Cloche");

        //Blocks
        add("block.cloche.cloche", "Cloche");

        //Items
        add("item.cloche.no_seeds_upgrade", "No Seeds Upgrade");
        add("item.cloche.main_output_upgrade", "Main Output Upgrade");
        add("item.cloche.shears_upgrade", "Shears Upgrade");
        add("item.cloche.no_other_drops_upgrade", "No Other Drops Upgrade");
        add("item.cloche.mutation_upgrade", "Mutation Upgrade");

        //Tooltips - Info
        add("tooltip.cloche.no_seeds_upgrade", "Prevents seeds from being produced");
        add("tooltip.cloche.main_output_upgrade", "Doubles the main output (Stacks with other Main Output Upgrades)");
        add("tooltip.cloche.shears_upgrade", "Add the Shears drop to the outputs");
        add("tooltip.cloche.no_other_drops_upgrade", "Prevents all other drops except the main output");
        add("tooltip.cloche.mutation_upgrade", "Has a 25% chance to mutate the input seed! Mutation make the crops grow faster and produce more!");
        add("tooltip.cloche.mutation_upgrade_speed", "Reduces growth time by %s%%");
        add("tooltip.cloche.mutation_upgrade_output", "Give a %s%% chance to roll results again");
        add("tooltip.cloche.seed_slot", "Seed Slot");
        add("tooltip.cloche.soil_slot", "Soil Slot");
        add("tooltip.cloche.upgrade_slot", "Upgrade Slot");
        add("tooltip.cloche.catalyst_slot_no_catalysts", "No loaded recipes use catalysts");
        add("tooltip.cloche.catalyst_slot", "Catalysts Slot");
        add("tooltip.cloche.cloche", "Used to grow crops, can be upgraded with various upgrades");

        //JEI
        add("jei.cloche.chance", "Chance: %s");
        add("jei.cloche.main_output", "Effected by Main Output Upgrade");
        add("jei.cloche.shears_result", "Drop added by Shears Upgrade");
        add("jei.cloche.seeds_results", "Removed by No Seeds Upgrade");


    }

    @Override
    public @NotNull String getName() {
        return Cloche.MOD_ID + " Language Provider";
    }
}
