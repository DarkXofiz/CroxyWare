// Original binary: sg/mx/lZM6oCixbgPLaomr
package sg.mx;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import net.minecraft.client.renderer.feature.FeatureFrameContext;
import net.minecraft.client.renderer.feature.NameTagFeatureRenderer;
import net.minecraft.client.renderer.feature.RenderTypeFeatureRenderer;
import net.minecraft.client.renderer.feature.NameTagFeatureRenderer.Submit;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import sg.ec.ReorderableRenderState;
import sg.ec.WorldVertexConsumerAccess;
import sg.ec.GlyphWidthVisitor;

// $VF: Compiled from s
@Mixin(NameTagFeatureRenderer.class)
public abstract class NameTagFeatureRendererMixin extends RenderTypeFeatureRenderer<Submit> implements WorldVertexConsumerAccess {
   @WrapMethod(method = "buildGroup")
   private void hex$batchGlyphs(FeatureFrameContext var1, List<Submit> var2, Operation<Void> var3) {
      GlyphWidthVisitor.INSTANCE.build(var1.font(), var2, this, ((ReorderableRenderState)(Object)this).hexCanReorder());
   }

   @Override
   public VertexConsumer hexVertexBuilder(RenderType var1) {
      return this.getVertexBuilder(var1);
   }
}
