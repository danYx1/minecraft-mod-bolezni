package ru.mindclinic;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;

/** Один класс на все расстройства; поведение выбирается по enum. Постоянные бонусы/штрафы — атрибуты (см. ModEffects). */
public class DiseaseEffect extends MobEffect {
    private final Disease disease;

    public DiseaseEffect(Disease disease) {
        super(disease.category, disease.color);
        this.disease = disease;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % disease.tickInterval == 0;
    }

    private static void sound(ServerLevel level, LivingEntity entity, SoundEvent event, SoundSource source,
                              float volume, float pitch, int spread) {
        var rnd = entity.getRandom();
        BlockPos pos = entity.blockPosition().offset(
            spread == 0 ? 0 : rnd.nextInt(spread * 2 + 1) - spread, 0,
            spread == 0 ? 0 : rnd.nextInt(spread * 2 + 1) - spread);
        level.playSound(null, pos, event, source, volume, pitch);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        var rnd = entity.getRandom();
        ServerPlayer player = entity instanceof ServerPlayer p ? p : null;

        switch (disease) {
            case SCHIZOPHRENIA -> {
                // Слуховые «галлюцинации»: звуки из ниоткуда рядом с игроком.
                if (rnd.nextInt(12) == 0) {
                    SoundEvent[] voices = {
                        SoundEvents.ENDERMAN_STARE, SoundEvents.CREEPER_PRIMED, SoundEvents.ZOMBIE_AMBIENT,
                        SoundEvents.SKELETON_AMBIENT, SoundEvents.WITCH_AMBIENT
                    };
                    sound(level, entity, voices[rnd.nextInt(voices.length)], SoundSource.HOSTILE,
                        0.6f + rnd.nextFloat() * 0.4f, 0.7f + rnd.nextFloat() * 0.6f, 6);
                }
            }
            case ANXIETY -> {
                // Сердцебиение + дёрганость, если рядом враждебные мобы.
                if (rnd.nextInt(4) == 0) {
                    sound(level, entity, SoundEvents.WARDEN_HEARTBEAT, SoundSource.PLAYERS, 0.8f, 1.0f, 0);
                }
                if (!level.getEntitiesOfClass(Monster.class, entity.getBoundingBox().inflate(8)).isEmpty()) {
                    entity.addEffect(new MobEffectInstance(MobEffects.SPEED, 40, 0, true, false));
                }
            }
            case INSOMNIA -> {
                // Заснуть не получится.
                if (player != null && player.isSleeping()) {
                    player.stopSleepInBed(true, true);
                }
            }
            case BIPOLAR -> {
                // Фазы по 60 секунд: мания (скорость, прыжок) <-> депрессия (замедление, усталость).
                boolean mania = (level.getGameTime() / 1200L) % 2L == 0L;
                if (mania) {
                    entity.addEffect(new MobEffectInstance(MobEffects.SPEED, 60, 1, true, false));
                    entity.addEffect(new MobEffectInstance(MobEffects.JUMP_BOOST, 60, 0, true, false));
                } else {
                    entity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 60, 0, true, false));
                    entity.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, 60, 0, true, false));
                }
            }
            case AUTISM -> {
                // Сенсорная перегрузка: после получения урона на секунду замедляет.
                if (entity.hurtTime > 0) {
                    entity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 30, 1, true, false));
                }
            }
            case ADHD -> {
                // Отвлечения: изредка слышен посторонний звук.
                if (rnd.nextInt(30) == 0) {
                    sound(level, entity, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.7f, 0.5f + rnd.nextFloat(), 3);
                }
            }
            default -> { }
        }
        return true;
    }
}
