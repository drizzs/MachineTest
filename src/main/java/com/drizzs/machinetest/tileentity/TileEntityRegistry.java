package com.drizzs.machinetest.tileentity;


import com.drizzs.machinetest.init.ModBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.drizzs.machinetest.MachineTest.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class TileEntityRegistry
{

    @SubscribeEvent
    public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event)
    {
        event.getRegistry().register(TileEntityType.Builder.create(CrucibleTile:: new, ModBlocks.crucible).build(null).setRegistryName("crucible"));

    }



}
