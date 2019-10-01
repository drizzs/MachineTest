package com.drizzs.machinetest.tileentity;

import com.drizzs.machinetest.MachineTest;
import com.drizzs.machinetest.util.MachineTags;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.drizzs.machinetest.tileentity.TileEntityHandler.CRUCIBLETILE;

public class CrucibleTile extends TileEntity implements ITickableTileEntity

{

    private boolean hasFuel;
    private int fuelTemp;

    public CrucibleTile() {
        super(CRUCIBLETILE);
    }

    public int getFuelTemp() {
        return fuelTemp;
    }

    public void setFuelTemp() {
        BlockPos pos1 = pos.down();
        World world = this.world.getWorld();
        if(world.getBlockState(pos1).getBlock().isIn(MachineTags.Blocks.MEDIUMHEATSOURCE))
        {
            fuelTemp = 1000;
        }
        if(world.getBlockState(pos1).getBlock().isIn(MachineTags.Blocks.LOWHEATSOURCE))
        {
            fuelTemp = 250;
        }
        if(world.getBlockState(pos1).getBlock().isIn(MachineTags.Blocks.MEDIUMLOWHEATSOURCE))
        {
            fuelTemp = 650;
        }
        if(world.getBlockState(pos1).getBlock().isIn(MachineTags.Blocks.LOWHIGHHEATSOURCE))
        {
            fuelTemp = 1500;
        }
        if(world.getBlockState(pos1).getBlock().isIn(MachineTags.Blocks.HIGHHEATSOURCE))
        {
            fuelTemp = 2000;
        }
        if(world.getBlockState(pos1).getBlock().isIn(MachineTags.Blocks.EXTREMELYHIGHHEATSOURCE))
        {
            fuelTemp = 3000;
        }

        else{ fuelTemp = 0;}
    }


    public boolean hasFuel() {
        if(getFuelTemp() >= 150) {
            hasFuel = true;
        }
        else{ hasFuel = false;}

        return hasFuel;
    }


    @Override
    public void tick() {

        setFuelTemp();
        if(hasFuel()){
            MachineTest.LOGGER.info("This hasFuel");

        }


    }
}
