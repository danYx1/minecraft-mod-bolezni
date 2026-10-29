package ru.mindclinic;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/** ПКМ — «врач» ставит случайный диагноз на 2 минуты. */
public class DiagnosisPanelBlock extends Block {
    public static final int DURATION_TICKS = 20 * 120;

    public DiagnosisPanelBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide() && player instanceof ServerPlayer sp) {
            Disease[] all = Disease.values();
            Disease d = all[level.getRandom().nextInt(all.length)];
            sp.addEffect(new MobEffectInstance(ModEffects.HOLDERS.get(d), DURATION_TICKS, 0, false, true, true));
            level.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.BLOCKS, 1f, 0.8f);
            sp.sendSystemMessage(Component.translatable("message.mindclinic.diagnosis",
                Component.translatable("effect.mindclinic." + d.id)));
        }
        return InteractionResult.SUCCESS;
    }
}
