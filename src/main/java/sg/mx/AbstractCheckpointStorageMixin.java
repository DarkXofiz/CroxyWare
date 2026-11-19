// Original binary: sg/mx/Mp4CWVqstjtywmQ5
package sg.mx;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// $VF: Compiled from s
@Mixin(targets = "com.mojang.blaze3d.vulkan.checkpoints.AbstractCheckpointStorage")
public class AbstractCheckpointStorageMixin {
   @Inject(
      method = "recordCheckpoint(Lorg/lwjgl/vulkan/VkCommandBuffer;Lcom/mojang/blaze3d/vulkan/checkpoints/CheckpointExtension$CheckpointType;Ljava/util/function/Supplier;)V",
      at = @At("HEAD"),
      cancellable = true,
      require = 0
   )
   private void hex$skipDiagnosticCheckpoint(CallbackInfo var1) {
      var1.cancel();
   }
}
