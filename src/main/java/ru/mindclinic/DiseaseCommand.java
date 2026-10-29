package ru.mindclinic;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import java.util.Arrays;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;

/**
 * /disease give <игрок> <болезнь> [секунд]
 * /disease cure <игрок> [болезнь|all]
 * /disease list
 */
public final class DiseaseCommand {
    private DiseaseCommand() {}

    private static final String[] NAMES = Arrays.stream(Disease.values()).map(d -> d.id).toArray(String[]::new);

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("disease")
            .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
            .then(Commands.literal("list").executes(ctx -> {
                ctx.getSource().sendSuccess(() -> Component.literal("Расстройства: " + String.join(", ", NAMES)), false);
                return NAMES.length;
            }))
            .then(Commands.literal("give")
                .then(Commands.argument("target", EntityArgument.player())
                    .then(Commands.argument("disease", StringArgumentType.word())
                        .suggests((c, b) -> SharedSuggestionProvider.suggest(NAMES, b))
                        .executes(ctx -> give(ctx.getSource(), EntityArgument.getPlayer(ctx, "target"),
                            StringArgumentType.getString(ctx, "disease"), 300))
                        .then(Commands.argument("seconds", IntegerArgumentType.integer(1, 100000))
                            .executes(ctx -> give(ctx.getSource(), EntityArgument.getPlayer(ctx, "target"),
                                StringArgumentType.getString(ctx, "disease"), IntegerArgumentType.getInteger(ctx, "seconds")))))))
            .then(Commands.literal("cure")
                .then(Commands.argument("target", EntityArgument.player())
                    .executes(ctx -> cure(ctx.getSource(), EntityArgument.getPlayer(ctx, "target"), "all"))
                    .then(Commands.argument("disease", StringArgumentType.word())
                        .suggests((c, b) -> SharedSuggestionProvider.suggest(
                            java.util.stream.Stream.concat(java.util.stream.Stream.of("all"), Arrays.stream(NAMES)), b))
                        .executes(ctx -> cure(ctx.getSource(), EntityArgument.getPlayer(ctx, "target"),
                            StringArgumentType.getString(ctx, "disease")))))));
    }

    private static int give(CommandSourceStack src, ServerPlayer target, String name, int seconds) {
        Disease d = Disease.byId(name);
        if (d == null) {
            src.sendFailure(Component.literal("Неизвестное расстройство: " + name));
            return 0;
        }
        target.addEffect(new MobEffectInstance(ModEffects.HOLDERS.get(d), seconds * 20, 0, false, true, true));
        src.sendSuccess(() -> Component.translatable("message.mindclinic.given",
            Component.translatable("effect.mindclinic." + d.id), target.getDisplayName()), true);
        return 1;
    }

    private static int cure(CommandSourceStack src, ServerPlayer target, String name) {
        int n = 0;
        if (name.equals("all")) {
            for (var h : ModEffects.HOLDERS.values()) if (target.removeEffect(h)) n++;
        } else {
            Disease d = Disease.byId(name);
            if (d == null) {
                src.sendFailure(Component.literal("Неизвестное расстройство: " + name));
                return 0;
            }
            if (target.removeEffect(ModEffects.HOLDERS.get(d))) n++;
        }
        final int cured = n;
        src.sendSuccess(() -> Component.translatable("message.mindclinic.cured", cured, target.getDisplayName()), true);
        return n;
    }
}
