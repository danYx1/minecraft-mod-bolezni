package ru.mindclinic;

import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class ModBlocks {
    public static Block CLINIC_WALL, CLINIC_STRIPE, CLINIC_LAMP, CLINIC_PANEL;

    private ModBlocks() {}

    private static Block register(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties props) {
        Identifier id = Identifier.fromNamespaceAndPath(MindClinic.MOD_ID, name);
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, id);
        Block block = Registry.register(BuiltInRegistries.BLOCK, blockKey, factory.apply(props.setId(blockKey)));
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);
        Registry.register(BuiltInRegistries.ITEM, itemKey,
            new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix()));
        return block;
    }

    public static void register() {
        BlockBehaviour.Properties base = BlockBehaviour.Properties.of().strength(1.5f, 6f).sound(SoundType.STONE);
        CLINIC_WALL = register("clinic_wall", Block::new, BlockBehaviour.Properties.of().strength(1.5f, 6f).sound(SoundType.STONE));
        CLINIC_STRIPE = register("clinic_stripe", Block::new, BlockBehaviour.Properties.of().strength(1.5f, 6f).sound(SoundType.STONE));
        CLINIC_LAMP = register("clinic_lamp", Block::new,
            BlockBehaviour.Properties.of().strength(0.5f).sound(SoundType.GLASS).lightLevel(s -> 15));
        CLINIC_PANEL = register("clinic_panel", DiagnosisPanelBlock::new, BlockBehaviour.Properties.of().strength(1.5f, 6f).sound(SoundType.STONE));
    }
}
