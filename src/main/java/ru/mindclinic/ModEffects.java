package ru.mindclinic;

import java.util.EnumMap;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;

public final class ModEffects {
    public static final Map<Disease, Holder<MobEffect>> HOLDERS = new EnumMap<>(Disease.class);

    private ModEffects() {}

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MindClinic.MOD_ID, path);
    }

    public static void register() {
        for (Disease d : Disease.values()) {
            DiseaseEffect effect = new DiseaseEffect(d);
            Identifier modId = id("modifier." + d.id);
            switch (d) {
                case DEPRESSION -> effect
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, modId, -0.15, Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE, id("modifier.depression_dmg"), -0.25, Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.BLOCK_BREAK_SPEED, id("modifier.depression_mine"), -0.3, Operation.ADD_MULTIPLIED_TOTAL);
                case ADHD -> effect
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, modId, 0.2, Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.ATTACK_SPEED, id("modifier.adhd_as"), 0.2, Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.BLOCK_BREAK_SPEED, id("modifier.adhd_mine"), -0.25, Operation.ADD_MULTIPLIED_TOTAL);
                case SCHIZOPHRENIA -> effect
                    .addAttributeModifier(Attributes.ARMOR, modId, -2.0, Operation.ADD_VALUE);
                case ANXIETY -> effect
                    .addAttributeModifier(Attributes.MAX_HEALTH, modId, -2.0, Operation.ADD_VALUE);
                case INSOMNIA -> effect
                    .addAttributeModifier(Attributes.ATTACK_SPEED, modId, -0.15, Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, id("modifier.insomnia_ms"), -0.05, Operation.ADD_MULTIPLIED_TOTAL);
                case OCD -> effect
                    .addAttributeModifier(Attributes.BLOCK_BREAK_SPEED, modId, 0.2, Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, id("modifier.ocd_ms"), -0.05, Operation.ADD_MULTIPLIED_TOTAL);
                case AUTISM -> effect
                    .addAttributeModifier(Attributes.BLOCK_BREAK_SPEED, modId, 0.35, Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE, id("modifier.autism_dmg"), 0.1, Operation.ADD_MULTIPLIED_TOTAL);
                default -> { }
            }
            HOLDERS.put(d, Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, id(d.id), effect));
        }
    }
}
