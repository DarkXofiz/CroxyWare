// Original binary: sg/mx/NqWLysrVhO2QCZki
package sg.mx;

import net.minecraft.client.renderer.ItemInHandRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// $VF: Compiled from s
@Mixin(ItemInHandRenderer.class)
public interface ItemInHandRendererMixin_1191 {
   @Accessor("mainHandHeight")
   float getMainHandHeight();
}
