// Original binary: sg/mx/cLyao7zqLFSJlqQK
package sg.mx;

import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.Supplier;
import net.minecraft.client.renderer.rendertype.PreparedRenderType;
import org.joml.Vector4fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import sg.ec.EntityMaskRenderHooks;

// $VF: Compiled from s
@Mixin(PreparedRenderType.class)
public class PreparedRenderTypeMixin {
   @Redirect(
      method = "drawFromBuffer(Lcom/mojang/blaze3d/buffers/GpuBuffer;Lcom/mojang/blaze3d/buffers/GpuBuffer;Lcom/mojang/blaze3d/IndexType;III)V",
      require = 0,
      at = @At(
         value = "INVOKE",
         target = "Lcom/mojang/blaze3d/systems/CommandEncoder;createRenderPass(Ljava/util/function/Supplier;Lcom/mojang/blaze3d/textures/GpuTextureView;Ljava/util/Optional;Lcom/mojang/blaze3d/textures/GpuTextureView;Ljava/util/OptionalDouble;)Lcom/mojang/blaze3d/systems/RenderPass;"
      )
   )
   private RenderPass hex$worldAcquire(
      CommandEncoder var1, Supplier<String> var2, GpuTextureView var3, Optional<Vector4fc> var4, GpuTextureView var5, OptionalDouble var6
   ) {
      return EntityMaskRenderHooks.method04012(var1, var2, var3, var4, var5, var6);
   }

   @Redirect(
      method = "drawFromBuffer(Lcom/mojang/blaze3d/buffers/GpuBuffer;Lcom/mojang/blaze3d/buffers/GpuBuffer;Lcom/mojang/blaze3d/IndexType;III)V",
      require = 0,
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;close()V")
   )
   private void hex$worldClose(RenderPass var1) {
      EntityMaskRenderHooks.method04013(var1);
   }
}
