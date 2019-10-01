package com.drizzs.machinetest.recipes;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.List;

public class CrucibleRecipes implements IRecipe<IInventory> {

    private final ResourceLocation id;
    private final ImmutableList<Ingredient> inputs;
    private final ItemStack output;
    private final int meltTime;

    public CrucibleRecipes(ResourceLocation id, ItemStack outputs, int meltTime, Ingredient... inputs)
    {
        Preconditions.checkArgument(inputs.length <= 3);
        this.id = id;
        this.inputs = ImmutableList.copyOf(inputs);
        this.output = outputs;
        this.meltTime = meltTime;
    }


    public List<Ingredient> getInputs() {
        return this.inputs;
    }

    public int getMeltTime() {return this.meltTime; }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return this.output.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.output;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeRegistry.CRUCIBLESERIALIZER;
    }

    @Override
    public IRecipeType<?> getType() {
        return RecipeRegistry.CRUCIBLETYPE;
    }
}