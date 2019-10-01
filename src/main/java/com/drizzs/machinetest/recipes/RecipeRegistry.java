package com.drizzs.machinetest.recipes;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import static com.drizzs.machinetest.MachineTest.MOD_ID;
import static net.minecraft.world.biome.Biome.LOGGER;

public class RecipeRegistry {

    public static IRecipeType<CrucibleRecipes> CRUCIBLETYPE;
    public static CrucibleRecipeSerializer<CrucibleRecipes> CRUCIBLESERIALIZER;

    @SubscribeEvent
    public static void registerAll(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        if (!event.getName().equals(ForgeRegistries.RECIPE_SERIALIZERS.getRegistryName())) return;

        CRUCIBLETYPE = register("crucibletype");

        LOGGER.info("Recipe types registered");

        CRUCIBLESERIALIZER = register("crucibletype", new CrucibleRecipeSerializer<CrucibleRecipes>(CrucibleRecipes::new, 1));

        LOGGER.info("Recipe serializers registered");
    }

    private static <S extends IRecipeSerializer<R>, R extends IRecipe<?>> S register(String name, S serializer) {
        serializer.setRegistryName(getLocation(name));
        ForgeRegistries.RECIPE_SERIALIZERS.register(serializer);
        return serializer;
    }

    private static <R extends IRecipe<?>> IRecipeType<R> register(String name) {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(name), new IRecipeType<R>() {
            public String toString() {
                return name;
            }
        });
    }

    public static ResourceLocation getLocation(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
