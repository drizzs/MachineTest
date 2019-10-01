package com.drizzs.machinetest.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

import java.util.Objects;

import static net.minecraft.item.crafting.ShapedRecipe.deserializeItem;

public class CrucibleRecipeSerializer<T extends CrucibleRecipes> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T>  {

    private final IFactory<T> factory;

    private final int meltTime;

    public CrucibleRecipeSerializer(IFactory<T> factory, int melttime) {
        this.factory = factory;
        this.meltTime = melttime;

    }




        @Override
    public T read(ResourceLocation id, JsonObject json) {
            JsonElement ingredientJson = JSONUtils.isJsonArray(json, "ingredient") ? JSONUtils.getJsonArray(json, "ingredient") : JSONUtils.getJsonObject(json, "ingredient");
            Ingredient ingredient = Ingredient.deserialize(ingredientJson);
            if (!json.has("output"))
                throw new com.google.gson.JsonSyntaxException("Missing output, expected to find a object");
            JsonObject outputJson = JSONUtils.getJsonObject(json, "output");
            String ItemKey = JSONUtils.getString(outputJson, "Item");
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(ItemKey));
            if (item == null)
                throw new com.google.gson.JsonSyntaxException("Crucible recipe output is null! Recipe is: " + id.toString());
            ItemStack output = deserializeItem(JSONUtils.getJsonObject(json, "result"));
            int meltTime = JSONUtils.getInt(json, "MeltTime", this.meltTime);
            return this.factory.create(id, output, meltTime, ingredient);
    }

    @Nullable
    @Override
    public T read(ResourceLocation id, PacketBuffer buffer) {
        Ingredient ingredient = Ingredient.read(buffer);

        Item item = ForgeRegistries.ITEMS.getValue(buffer.readResourceLocation());
        if (item == null)
            throw new com.google.gson.JsonSyntaxException("Crucible recipe result is null! Recipe is: " + id.toString());
        int amount = buffer.readVarInt();
        ItemStack output = buffer.readItemStack();
        int meltTime = buffer.readVarInt();
        return this.factory.create(id, output, meltTime, ingredient);
    }

    @Override
    public void write(PacketBuffer buffer, T recipe) {
        buffer.writeResourceLocation(recipe.getId());
        buffer.writeVarInt(recipe.getInputs().size());
        for (Ingredient input : recipe.getInputs()) {
            input.write(buffer);
        }
        recipe.getIngredients().get(0).write(buffer);

        buffer.writeResourceLocation(Objects.requireNonNull(recipe.getRecipeOutput().getItem().getRegistryName()));

        buffer.writeVarInt(recipe.getMeltTime());
    }


    public interface IFactory<T extends CrucibleRecipes> {
        T create(ResourceLocation id, ItemStack output,int meltTime, Ingredient... inputs);
    }
}
