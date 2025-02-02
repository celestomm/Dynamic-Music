package net.celeri.dynmus.mixin;

import net.celeri.dynmus.DynamicMusic;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.BiomeAmbientSoundsHandler;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Environment(EnvType.CLIENT)
@Mixin(BiomeAmbientSoundsHandler.class)
public class BiomeAmbientSoundsHandlerMixin {
    @Shadow
    private float moodiness;

    @Shadow
    private Optional<AmbientMoodSettings> moodSettings = Optional.empty();

    @Inject(method = "tick", at = @At("HEAD"))
    private void dynmus$tick(CallbackInfo ci) {
        this.moodSettings.ifPresent(
            ambientMoodSettings -> {
                if (DynamicMusic.isInPseudoMinecraft()) {
                    this.moodiness += (float) ((15 - DynamicMusic.getAverageDarkness()) / (float) ambientMoodSettings.getTickDelay());
                }
            }
        );
    }
}
