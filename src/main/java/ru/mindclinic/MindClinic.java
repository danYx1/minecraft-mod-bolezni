package ru.mindclinic;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;

public class MindClinic implements ModInitializer {
    public static final String MOD_ID = "mindclinic";

    @Override
    public void onInitialize() {
        ModEffects.register();
        ModBlocks.register();
        ModItems.register();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> DiseaseCommand.register(dispatcher));

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> {
            entries.accept(ModBlocks.CLINIC_WALL);
            entries.accept(ModBlocks.CLINIC_STRIPE);
            entries.accept(ModBlocks.CLINIC_LAMP);
            entries.accept(ModBlocks.CLINIC_PANEL);
        });
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(entries -> entries.accept(ModItems.PILL));
    }
}
