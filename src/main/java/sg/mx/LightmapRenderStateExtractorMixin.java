// Original binary: sg/mx/k1b6R2gyaH1O7gpT
package sg.mx;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sg.ec.WorldBrightnessEvent;
import sg.ec.LightmapRenderStateExtractorHooks;

// $VF: Compiled from s
@Mixin(LightmapRenderStateExtractor.class)
public class LightmapRenderStateExtractorMixin {
   @ModifyExpressionValue(
      method = "extract",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getEffectBlendFactor(Lnet/minecraft/core/Holder;F)F")
   )
   private float hexNoDarkness(float var1) {
      return LightmapRenderStateExtractorHooks.method11600(var1);
   }

   @ModifyExpressionValue(
      method = "extract",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ARGB;vector3fFromRGB24(I)Lorg/joml/Vector3f;", ordinal = 2)
   )
   private Vector3f hexWorldBrightness(Vector3f var1) {
      return WorldBrightnessEvent.field03023.method01302(var1);
   }
}
