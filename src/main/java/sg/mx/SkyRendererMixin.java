// Original binary: sg/mx/TQ9WKHQDlHr5vbnw
package sg.mx;

import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.Supplier;
import net.minecraft.client.renderer.SkyRenderer;
import org.joml.Vector4fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.ec.SkyRendererHooks;

// $VF: Compiled from s
@Mixin(SkyRenderer.class)
public class SkyRendererMixin {
   @Inject(method = "renderSkyDisc", at = @At("HEAD"), require = 0)
   private void hex$skyMergeBegin(CallbackInfo var1) {
      SkyRendererHooks.method09492();
   }

   @Inject(method = "renderSunMoonAndStars", at = @At("RETURN"), require = 0)
   private void hex$skyMergeEnd(CallbackInfo var1) {
      SkyRendererHooks.method09494();
   }

   @Redirect(
      method = {"renderSkyDisc", "renderSunriseAndSunset", "renderSun", "renderMoon", "renderStars"},
      require = 0,
      at = @At(
         value = "INVOKE",
         target = "Lcom/mojang/blaze3d/systems/CommandEncoder;createRenderPass(Ljava/util/function/Supplier;Lcom/mojang/blaze3d/textures/GpuTextureView;Ljava/util/Optional;Lcom/mojang/blaze3d/textures/GpuTextureView;Ljava/util/OptionalDouble;)Lcom/mojang/blaze3d/systems/RenderPass;"
      )
   )
   private RenderPass hex$skyMergeAcquire(
      CommandEncoder var1, Supplier<String> var2, GpuTextureView var3, Optional<Vector4fc> var4, GpuTextureView var5, OptionalDouble var6
   ) {
      return SkyRendererHooks.method09491(var1, var2, var3, var4, var5, var6);
   }

   @Redirect(
      method = {"renderSkyDisc", "renderSunriseAndSunset", "renderSun", "renderMoon", "renderStars"},
      require = 0,
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;close()V")
   )
   private void hex$skyMergeClose(RenderPass var1) {
      SkyRendererHooks.method09493(var1);
   }
}
