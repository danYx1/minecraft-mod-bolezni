package ru.mindclinic;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public final class ModItems {
    public static Item PILL;

    private ModItems() {}

    public static void register() {
        Identifier id = Identifier.fromNamespaceAndPath(MindClinic.MOD_ID, "pill");
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
        PILL = Registry.register(BuiltInRegistries.ITEM, key, new PillItem(new Item.Properties().setId(key).stacksTo(16)));
    }
}
