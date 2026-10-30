package ru.mindclinic;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.world.item.CreativeModeTabs;

public class MindClinic implements ModInitializer {
    public static final String MOD_ID = "mindclinic";

    @Override
    public void onInitialize() {
        ModEffects.register();
        ModBlocks.register();
        ModItems.register();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> DiseaseCommand.register(dispatcher));

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(output -> {
            output.accept(ModBlocks.CLINIC_WALL);
            output.accept(ModBlocks.CLINIC_STRIPE);
            output.accept(ModBlocks.CLINIC_LAMP);
            output.accept(ModBlocks.CLINIC_PANEL);
        });
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(output -> output.accept(ModItems.PILL));
    }
}
