package com.benbenlaw.cloche.item.util;

import com.benbenlaw.cloche.item.ClocheDataComponent;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record CropData(int outputMultiplier, int speedModifier) {

    public static final Codec<CropData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.intRange(0, 500).fieldOf("output_multiplier").forGetter(CropData::outputMultiplier),
        Codec.intRange(0, 100).fieldOf("speed_modifier").forGetter(CropData::speedModifier)
    ).apply(instance, CropData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CropData> STREAM_CODEC = StreamCodec.of(
        (buf, cropData) -> {
            buf.writeInt(cropData.outputMultiplier);
            buf.writeInt(cropData.speedModifier);
        },
        buf -> {
            int outputMultiplier = buf.readInt();
            int speedModifier = buf.readInt();
            return new CropData(outputMultiplier, speedModifier);
        }
    );


    public static CropData fromItemStack(ItemStack stack) {
        if (stack.has(ClocheDataComponent.CROP_DATA)) {
            return stack.get(ClocheDataComponent.CROP_DATA);
        } else {
            return new CropData(0, 0);
        }
    }

}
