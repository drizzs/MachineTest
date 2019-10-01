package com.drizzs.machinetest.tileentity;

import com.drizzs.machinetest.MachineTest;
import com.drizzs.machinetest.recipes.CrucibleRecipes;
import com.drizzs.machinetest.recipes.RecipeRegistry;
import com.drizzs.machinetest.util.MachineTags;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

import static com.drizzs.machinetest.tileentity.TileEntityHandler.CRUCIBLETILE;

public class CrucibleTile extends TileEntity implements ITickableTileEntity {

    private boolean hasFuel = false;
    private int fuelTemp = 0;
    private int meltTime = 0;
    private int meltTimeTotal = 0;
    protected NonNullList<ItemStack> inventory = NonNullList.withSize(3, ItemStack.EMPTY);

    protected final IIntArray crucibleData = new IIntArray() {
        public int get(int index) {
            switch (index) {
                case 0:
                    return CrucibleTile.this.meltTime;
                case 1:
                    return CrucibleTile.this.meltTimeTotal;
                default:
                    return 0;
            }
        }

        public void set(int index, int value) {
            switch (index) {
                case 0:
                    CrucibleTile.this.meltTime = value;
                    break;
                case 1:
                    CrucibleTile.this.meltTimeTotal = value;
                    break;
            }
        }

        public int size() {
            return 2;
        }

    };

    private final Map<ResourceLocation, Integer> recipeAmounts = Maps.newHashMap();
    private final IRecipeType<CrucibleRecipes> recipeType;

    public CrucibleTile() {
        super(CRUCIBLETILE);
        recipeType = RecipeRegistry.CRUCIBLETYPE;
    }

    public int getFuelTemp() {
        return fuelTemp;
    }

    private void setFuelTemp() {
        BlockPos pos1 = pos.down();
        assert this.world != null;
        World world = this.world.getWorld();
        BlockState state = world.getBlockState(pos1);
        Block block = state.getBlock();

        if (block.isIn(MachineTags.Blocks.LOWHEATSOURCE)) {
            fuelTemp = 250;
        } else if (block.isIn(MachineTags.Blocks.MEDIUMLOWHEATSOURCE)) {
            fuelTemp = 650;
        } else if (block.isIn(MachineTags.Blocks.MEDIUMHEATSOURCE)) {
            fuelTemp = 1000;
        } else if (block.isIn(MachineTags.Blocks.MEDIUMHIGHHEATSOURCE)) {
            fuelTemp = 1500;
        } else if (block.isIn(MachineTags.Blocks.HIGHHEATSOURCE)) {
            fuelTemp = 2000;
        } else if (block.isIn(MachineTags.Blocks.EXTREMELYHIGHHEATSOURCE)) {
            fuelTemp = 3000;
        } else {
            fuelTemp = 0;
        }
    }


    private boolean hasFuel() {
        hasFuel = getFuelTemp() >= 150;

        return hasFuel;
    }


    @Override
    public void tick() {

        World world = this.getWorld();
        setFuelTemp();

        if (this.hasFuel()) {
            if (!world.isRemote) {

            }
        }

    }

    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        tag.putInt("MeltTime", meltTime);
        tag.putInt("MeltTimeTotal", meltTimeTotal);
        tag.putInt("FuelTemp", fuelTemp);
        tag.putBoolean("HasFuel", hasFuel);
        return tag;
    }

    public void read(CompoundNBT tag) {
        super.read(tag);
        this.hasFuel = tag.getBoolean("HasFuel");
        this.fuelTemp = tag.getInt("FuelTemp");
        this.meltTime = tag.getInt("MeltTime");
        this.meltTimeTotal = tag.getInt("MeltTimeTotal");
    }

    private int getMeltTimeTotal() {
        return this.world.getRecipeManager().getRecipe(this.recipeType, this, this.world).map(CrucibleRecipes::getMeltTime).orElse(200);
    }

    //Inventory Handler

    private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (this.hasFuel()) {
                return handler.cast();
            }
        }
        return super.getCapability(cap, side);
    }

    private IItemHandler createHandler() {
        return new ItemStackHandler(1) {

            @Override
            protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
                return Math.min(0, 1);
            }

            @Override
            public int getSlotLimit(int slot) {
                return 3;
            }

            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return stack.getItem().isIn(MachineTags.Items.CRUCIBLE);
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (!stack.getItem().isIn(MachineTags.Items.CRUCIBLE)) {
                    return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }
        };

    }

}
