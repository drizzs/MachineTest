package com.drizzs.machinetest.tileentity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

import static com.drizzs.machinetest.MachineTest.MOD_ID;


@ObjectHolder(MOD_ID)
public class TileEntityHandler {
    @ObjectHolder("machinetest:crucible")
    public static CrucibleBlock CRUCIBLE;

    @ObjectHolder("machinetest:crucible")
    public static TileEntityType<CrucibleTile> CRUCIBLETILE;

}
