package com.drizzs.machinetest.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

import static com.drizzs.machinetest.MachineTest.MOD_ID;

public class MachineTags
{

    public static class Blocks {

        //Heat Sources
        public static final Tag<Block> LOWHEATSOURCE = tag("lowheatsource");
        public static final Tag<Block> MEDIUMLOWHEATSOURCE = tag("mediumlowheatsource");
        public static final Tag<Block> MEDIUMHEATSOURCE = tag("mediumheatsource");
        public static final Tag<Block> LOWHIGHHEATSOURCE = tag("lowhighheatsource");
        public static final Tag<Block> HIGHHEATSOURCE = tag("highheatsource");
        public static final Tag<Block> EXTREMELYHIGHHEATSOURCE = tag("extremelyhighheatsource");



        private static Tag<Block> tag(String name) {
            return new BlockTags.Wrapper(new ResourceLocation(MOD_ID, name));
        }
    }


    public static class Items {
        public static final Tag<Item> CRUCIBLE = tag("crucible");

        private static Tag<Item> tag(String name) {
            return new ItemTags.Wrapper(new ResourceLocation(MOD_ID, name));
        }
    }




}
