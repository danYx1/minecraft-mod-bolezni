package ru.mindclinic;

import net.minecraft.world.effect.MobEffectCategory;

/** Список расстройств. id используется в командах и в названиях ресурсов. */
public enum Disease {
    DEPRESSION("depression", 0x46507A, MobEffectCategory.HARMFUL, 20),
    ADHD("adhd", 0xDC6E3C, MobEffectCategory.HARMFUL, 20),
    SCHIZOPHRENIA("schizophrenia", 0x783270, MobEffectCategory.HARMFUL, 20),
    ANXIETY("anxiety", 0x468C96, MobEffectCategory.HARMFUL, 20),
    INSOMNIA("insomnia", 0x2D2D64, MobEffectCategory.HARMFUL, 20),
    OCD("ocd", 0x3C6E5A, MobEffectCategory.NEUTRAL, 20),
    BIPOLAR("bipolar", 0x965AA0, MobEffectCategory.HARMFUL, 20),
    /** РАС — не болезнь, поэтому эффект нейтральный: плюсы и минусы. */
    AUTISM("autism", 0x46465A, MobEffectCategory.NEUTRAL, 5);

    public final String id;
    public final int color;
    public final MobEffectCategory category;
    /** Как часто (в тиках) срабатывает логика эффекта. */
    public final int tickInterval;

    Disease(String id, int color, MobEffectCategory category, int tickInterval) {
        this.id = id;
        this.color = color;
        this.category = category;
        this.tickInterval = tickInterval;
    }

    public static Disease byId(String id) {
        for (Disease d : values()) if (d.id.equals(id)) return d;
        return null;
    }
}
