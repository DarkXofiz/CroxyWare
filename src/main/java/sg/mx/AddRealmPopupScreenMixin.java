// Original binary: sg/mx/7jeUV35CNeCL5v3J
package sg.mx;

import com.mojang.realmsclient.gui.screens.AddRealmPopupScreen;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// $VF: Compiled from s
@Mixin(AddRealmPopupScreen.class)
public class AddRealmPopupScreenMixin {
   @Inject(method = "updateCarouselImages", at = @At("HEAD"), cancellable = true)
   private static void hex$noCarouselScan(ResourceManager var0, CallbackInfo var1) {
      var1.cancel();
   }
}
