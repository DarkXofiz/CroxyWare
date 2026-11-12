package sg.ec;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public final class TitleScreenHooks implements MinecraftAccess {
   private static MainMenuScreen field24684;
   private static TitleScreen vanilla;

   /** Parent screen for other screens. Mobile always gets the plain vanilla title screen. */
   public static Screen method11006() {
      if (MobileCompat.isMobile()) {
         if (vanilla == null) {
            vanilla = new TitleScreen();
         }

         return vanilla;
      }

      if (field24684 == null) {
         field24684 = new MainMenuScreen();
      }

      return field24684;
   }

   private TitleScreenHooks() {
   }

   public static void method11007(CallbackInfo var0) {
      if (MobileCompat.isMobile()) {
         return;
      }

      if (!ClientFeaturePolicyEvent.field14592.method06426(ClientFeaturePolicyEvent.Nested186.MAIN_MENU)) {
         Screen var1 = method11006();
         if (mc.gui.screen() != var1) {
            mc.gui.setScreen(var1);
            var0.cancel();
         }
      }
   }
}
