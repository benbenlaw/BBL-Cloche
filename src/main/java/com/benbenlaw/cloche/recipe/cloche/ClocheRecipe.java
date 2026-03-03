package com.benbenlaw.cloche.recipe.cloche;

import com.benbenlaw.core.recipe.ChanceResult;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.benbenlaw.cloche.block.entity.ClocheBlockEntity.*;

public record ClocheRecipe(Ingredient seed, Ingredient soil, Optional<Ingredient> catalyst, int duration, NonNullList<ChanceResult> results, Optional<ItemStackTemplate> shearsResult) implements Recipe<RecipeInput> {

    @Override
    public boolean matches(RecipeInput container, @NotNull Level level) {

        boolean needCatalyst = !catalyst.isEmpty();
        if (needCatalyst) {
            if (catalyst.get().test(container.getItem(CATALYST_SLOT))) {
                return seed.test(container.getItem(SEED_SLOT)) && soil.test(container.getItem(SOIL_SLOT));
            } else {
                return false;
            }
        }

        return seed.test(container.getItem(SEED_SLOT)) && soil.test(container.getItem(SOIL_SLOT));

    }


    @Override
    public ItemStack assemble(RecipeInput recipeInput) {
        return results.getFirst().template().create();
    }


    @Override
    public @NotNull RecipeSerializer<? extends Recipe<RecipeInput>> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<? extends Recipe<RecipeInput>> getType() {
        return TYPE;
    }

    @Override
    public @NotNull PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

    public static final RecipeType<ClocheRecipe> TYPE = new RecipeType<>() {};

    public static final MapCodec<ClocheRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(
                    Ingredient.CODEC.fieldOf("seed").forGetter(ClocheRecipe::seed),
                    Ingredient.CODEC.fieldOf("soil").forGetter(ClocheRecipe::soil),
                    Ingredient.CODEC.optionalFieldOf("catalyst").forGetter(ClocheRecipe::catalyst),
                    Codec.INT.fieldOf("duration").forGetter(ClocheRecipe::duration),
                    Codec.list(ChanceResult.CODEC).fieldOf("results").flatXmap(chanceResults -> {
                        NonNullList<ChanceResult> nonNullList = NonNullList.create();
                        nonNullList.addAll(chanceResults);
                        return DataResult.success(nonNullList);
                    }, DataResult::success).forGetter(ClocheRecipe::getRollResults),
                    ItemStackTemplate.CODEC.optionalFieldOf("shears_result").forGetter(ClocheRecipe::shearsResult)
            ).apply(instance, ClocheRecipe::new)
    );


    private static final StreamCodec<RegistryFriendlyByteBuf, ClocheRecipe> STREAM_CODEC =
            StreamCodec.of(ClocheRecipe::write, ClocheRecipe::read);

    public static final RecipeSerializer<ClocheRecipe> SERIALIZER =
            new RecipeSerializer<>(CODEC, STREAM_CODEC);


    private static ClocheRecipe read(RegistryFriendlyByteBuf buffer) {
        Ingredient seed = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        Ingredient soil = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        Optional<Ingredient> catalyst =
                Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC.decode(buffer).or(Optional::empty);

        int duration = buffer.readInt();

        int size = buffer.readVarInt();
        NonNullList<ChanceResult> outputs = NonNullList.create();
        for (int i = 0; i < size; i++) {
            outputs.add(ChanceResult.read(buffer));
        }

        // ---- OPTIONAL TEMPLATE ----
        Optional<ItemStackTemplate> shearsResult;
        if (buffer.readBoolean()) {
            shearsResult = Optional.of(ItemStackTemplate.STREAM_CODEC.decode(buffer));
        } else {
            shearsResult = Optional.empty();
        }

        return new ClocheRecipe(seed, soil, catalyst, duration, outputs, shearsResult
        );
    }

    private static void write(RegistryFriendlyByteBuf buffer, ClocheRecipe recipe) {
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.seed);
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.soil);
        Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC.encode(buffer, recipe.catalyst);
        buffer.writeInt(recipe.duration);

        buffer.writeVarInt(recipe.results.size());
        for (ChanceResult output : recipe.results) {
            output.write(buffer);
        }

        // ---- OPTIONAL TEMPLATE ----
        if (recipe.shearsResult.isPresent()) {
            buffer.writeBoolean(true);
            ItemStackTemplate.STREAM_CODEC.encode(buffer, recipe.shearsResult.get());
        } else {
            buffer.writeBoolean(false);
        }
    }

    public List<ItemStack> getResults() {
        return getRollResults().stream()
                .map(result -> result.template().create())
                .collect(Collectors.toList());
    }

    public NonNullList<ChanceResult> getRollResults() {
        return this.results;
    }

    public List<ItemStack> rollResults(RandomSource rand) {
        List<ItemStack> results = new ArrayList<>();
        List<ChanceResult> rollResults = getRollResults();
        for (ChanceResult output : rollResults) {
            ItemStack stack = output.rollOutput(rand);
            if (!stack.isEmpty())
                results.add(stack);
        }
        return results;
    }
}

