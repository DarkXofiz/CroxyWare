// Original binary: sg/ec/4gcNapV3MQRi4zNL
package sg.ec;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.Identifier;

// $VF: Compiled from s
public final class MainMenuBlitRenderer {
   private static final Supplier<String> field02050 = () -> MainMenuBlitRenderer.field02060;
   private static RenderPipeline field02051;
   private static boolean field02052;
   private static final String field02053 = "InSampler";
   private static final String field02054 = "hex";
   private static final String field02055 = "pipeline/mainmenu_blit";
   private static final String field02056 = "core/screenquad";
   private static final String field02057 = "hex";
   private static final String field02058 = "post/mainmenu_blit";
   private static final String field02059 = "InSampler";
   private static final String field02060 = "hex main menu blit";

   private static RenderPipeline method00876() {
      if (field02051 == null) {
         field02051 = RenderPipeline.builder()
            .withLocation(Identifier.fromNamespaceAndPath(field02054, field02055))
            .withVertexShader(field02056)
            .withFragmentShader(Identifier.fromNamespaceAndPath(field02057, field02058))
            .withBindGroupLayout(BindGroupLayout.builder().withSampler(field02059).build())
            .withColorTargetState(new ColorTargetState(Optional.empty(), GpuFormat.RGBA8_UNORM, 15))
            .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
            .build();
      }

      return field02051;
   }

   public static void method00878(Identifier var0) {
      if (var0 != null) {
         Minecraft var1 = Minecraft.getInstance();
         RenderTarget sx = var1.gameRenderer.mainRenderTarget();
         if (sx != null && sx.getColorTexture() != null) {
            AbstractTexture sxx = var1.getTextureManager().getTexture(var0);
            if (sxx != null) {
               GpuTextureView sxxx = sxx.getTextureView();
               if (sxxx != null) {
                  try {
                     CommandEncoder sxxxx = RenderSystem.getDevice().createCommandEncoder();
                     RenderPass sxxxxx = sxxxx.createRenderPass(field02050, sx.getColorTextureView(), Optional.empty());

                     try {
                        sxxxxx.setPipeline(method00876());
                        RenderSystem.bindDefaultUniforms(sxxxxx);
                        sxxxxx.bindTexture(field02053, sxxx, sxx.getSampler());
                        sxxxxx.draw(3, 1, 0, 0);
                     } finally {
                        sxxxxx.close();
                     }
                  } catch (Throwable sxxxx) {
                     if (!field02052) {
                        field02052 = true;
                     }
                  }
               }
            }
         }
      }
   }

   private MainMenuBlitRenderer() {
   }
}
