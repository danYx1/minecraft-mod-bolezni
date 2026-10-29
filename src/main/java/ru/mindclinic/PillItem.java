package ru.mindclinic;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** ПКМ — снимает все расстройства. */
public class PillItem extends Item {
    public PillItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            boolean cured = false;
            for (var holder : ModEffects.HOLDERS.values()) {
                cured |= player.removeEffect(holder);
            }
            level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_EAT.value(), SoundSource.PLAYERS, 1f, 1.3f);
            if (cured) stack.consume(1, player);
        }
        return InteractionResult.SUCCESS;
    }
}
