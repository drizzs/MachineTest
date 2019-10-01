package com.drizzs.machinetest.util;

import com.drizzs.machinetest.init.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class MachineIcon extends ItemGroup
{

    public static final MachineIcon instance = new MachineIcon(ItemGroup.GROUPS.length, "machineicon");

    private MachineIcon(int index, String label)
    {
        super(index, label);
    }

    @Override
    public ItemStack createIcon()
    {
        return new ItemStack(ModItems.machineicon);
    }


}
