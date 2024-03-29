package com.drizzs.machinetest.tileentity;

import com.drizzs.machinetest.MachineTest;
import com.drizzs.machinetest.recipes.CrucibleRecipes;
import com.drizzs.machinetest.recipes.RecipeRegistry;
import com.drizzs.machinetest.util.MachineTags;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

import static com.drizzs.machinetest.tileentity.TileEntityHandler.CRUCIBLETILE;

public class CrucibleTile extends TileEntity implements ITickableTileEntity, IRecipeHolder, IRecipeHelperPopulator {

    protected NonNullList<ItemStack> inventory = NonNullList.withSize(3, ItemStack.EMPTY);
    private boolean hasFuel;
    private boolean tooHot;
    private int maxTemp;
    private int minTemp;
    private int fuelTemp;
    private int meltTime;
    private int meltTimeTotal;

    protected final IIntArray crucibleData = new IIntArray() {

        public int get(int index) {
            switch (index) {
                case 0:
                    return CrucibleTile.this.meltTime;
                case 1:
                    return CrucibleTile.this.meltTimeTotal;
                case 2:
                    return CrucibleTile.this.minTemp;
                case 3:
                    return CrucibleTile.this.maxTemp;
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
                case 2:
                    CrucibleTile.this.minTemp = value;
                    break;
                case 3:
                    CrucibleTile.this.maxTemp = value;
                    break;

            }

        }

        public int size() {
            return 4;
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

    private boolean tooHot() {
        if (getFuelTemp() > this.maxTemp) {
            tooHot = true;
        } else {
            tooHot = false;
        }
        return tooHot;
    }


    private boolean hasFuel() {
        hasFuel = getFuelTemp() >= 150;

        return hasFuel;
    }

    private void smeltRecipe(@Nullable IRecipe<?> iRecipe) {
        if (!(iRecipe instanceof CrucibleRecipes))
            return;

        if (this.canSmelt(iRecipe)) {
            CrucibleRecipes recipe = (CrucibleRecipes) iRecipe;
            ItemStack ingredientStack = this.inventory.get(0);
            ItemStack recipeOutStack = recipe.getRecipeOutput();
            ItemStack recipeAlternateOut = recipe.getAlternateOutput();
            ItemStack outStack = this.inventory.get(0);
            ingredientStack.shrink(1);
            if (outStack.isEmpty() && !this.tooHot()) {
                this.inventory.set(1, recipeOutStack.copy());
            } else if (outStack.isEmpty() && this.tooHot()) {
                this.inventory.set(1, recipeAlternateOut.copy());
            } else if (outStack.getItem() == recipeOutStack.getItem())
                this.inventory.set(1, recipeOutStack);

            if (!this.world.isRemote)
                this.setRecipeUsed(recipe);

        }
    }

    private boolean canSmelt(@Nullable IRecipe<?> iRecipe) {
        if (!(iRecipe instanceof CrucibleRecipes))
            return false;

        if (!this.inventory.get(0).isEmpty()) {
            CrucibleRecipes recipe = (CrucibleRecipes) iRecipe;
            ItemStack outStack = recipe.getRecipeOutput();
            if (outStack.isEmpty())
                return false;
            else {
                if (this.inventory.isEmpty())
                    return true;

            }
        }
        return false;
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
        tag.putInt("MinTemp", minTemp);
        tag.putInt("MaxTemp", maxTemp);
        tag.putBoolean("HasFuel", hasFuel);
        tag.putBoolean("TooHot", tooHot);

        ItemStackHelper.saveAllItems(tag, this.inventory);
        tag.putShort("RecipesUsedSize", (short) this.recipeAmounts.size());
        int i = 0;

        for (Map.Entry<ResourceLocation, Integer> entry : this.recipeAmounts.entrySet()) {
            tag.putString("RecipeLocation" + i, entry.getKey().toString());
            tag.putInt("RecipeAmount" + i, entry.getValue());
            ++i;
        }

        return tag;
    }

    public void read(CompoundNBT tag) {
        super.read(tag);
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(tag, this.inventory);
        this.hasFuel = tag.getBoolean("HasFuel");
        this.tooHot = tag.getBoolean("TooHot");
        this.fuelTemp = tag.getInt("FuelTemp");
        this.minTemp = tag.getInt("MinTemp");
        this.maxTemp = tag.getInt("MaxTemp");
        this.meltTime = tag.getInt("MeltTime");
        this.meltTimeTotal = tag.getInt("MeltTimeTotal");

        int i = tag.getShort("RecipesUsedSize");
        for (int j = 0; j < i; ++j) {
            ResourceLocation resourcelocation = new ResourceLocation(tag.getString("RecipeLocation" + j));
            int k = tag.getInt("RecipeAmount" + j);
            this.recipeAmounts.put(resourcelocation, k);
        }
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
        return new ItemStackHandler(3) {
            @Override
            protected int getStackLimit(int slot, @Nonnull ItemStack stack)
            {
                return 1;
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

    @Override
    public void fillStackedContents(RecipeItemHelper helper) {
        for (ItemStack itemstack : this.inventory)
            helper.accountStack(itemstack);
    }

    @Override
    public void setRecipeUsed(@Nullable IRecipe<?> recipe) {
        if (recipe != null)
            this.recipeAmounts.compute(recipe.getId(), (location, integer) -> 1 + (integer == null ? 0 : integer));
    }

    @Nullable
    @Override
    public IRecipe<?> getRecipeUsed() {
        return null;
    }


    //Inventory

    public int getSizeInventory() {
        return this.inventory.size();
    }

    public void clear() {
        this.inventory.clear();
    }

    public void getExperience(PlayerEntity player) {
        List<IRecipe<?>> list = Lists.newArrayList();

        for(Map.Entry<ResourceLocation, Integer> entry : this.recipeAmounts.entrySet()) {
            player.world.getRecipeManager().getRecipe(entry.getKey()).ifPresent((experience) -> {
                list.add(experience);
                experienceHandler(player, entry.getValue(), (CrucibleRecipes.getExperience()));
            });
        }

        player.unlockRecipes(list);
        this.recipeAmounts.clear();
    }

    private static void experienceHandler(PlayerEntity player, int amount, float exp) {
        if (exp == 0.0F) {
            amount = 0;
        } else if (exp < 1.0F) {
            int i = MathHelper.floor((float)amount * exp);
            if (i < MathHelper.ceil((float)amount * exp) && Math.random() < (double)((float)amount * exp - (float)i)) {
                ++i;
            }

            amount = i;
        }

        while(amount > 0) {
            int j = ExperienceOrbEntity.getXPSplit(amount);
            amount -= j;
            player.world.addEntity(new ExperienceOrbEntity(player.world, player.posX, player.posY + 0.5D, player.posZ + 0.5D, j));
        }

    }
}
