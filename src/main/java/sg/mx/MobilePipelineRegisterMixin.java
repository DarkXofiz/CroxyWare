package sg.mx;

import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.minecraft.client.renderer.RenderPipelines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sg.ec.MobileCompat;

/**
 * Mobile only: pipelines registered by the client (package sg.ec) are not added to the global list that the
 * game precompiles during resource loading. A single unresolved hex shader there keeps the loading overlay
 * on a black screen forever. They are still usable lazily, and every client render hook is guarded.
 */
@Mixin(RenderPipelines.class)
public class MobilePipelineRegisterMixin {
   @Inject(
      method = "register(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;)Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;",
      at = @At("HEAD"),
      cancellable = true,
      require = 0
   )
   private static void croxy$skipClientPipelinePrecompile(RenderPipeline pipeline, CallbackInfoReturnable<RenderPipeline> cir) {
      if (!MobileCompat.isMobile()) {
         return;
      }

      boolean fromClient = StackWalker.getInstance().walk(frames -> frames
         .filter(f -> !f.getClassName().equals("net.minecraft.client.renderer.RenderPipelines"))
         .findFirst()
         .map(f -> f.getClassName().startsWith("sg.ec."))
         .orElse(false));
      if (fromClient) {
         cir.setReturnValue(pipeline);
      }
   }
}
