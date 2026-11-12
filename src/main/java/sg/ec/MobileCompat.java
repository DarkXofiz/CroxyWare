package sg.ec;

import java.util.Locale;

/**
 * Detects Android launchers (ZalithLauncher / PojavLauncher / FCL etc.) and exposes
 * helpers so desktop-only features (Vulkan hooks, AWT tray, JNA/Win32, ONNX, media
 * transport) can be skipped instead of crashing the renderer or the game.
 */
public final class MobileCompat {
   private static final boolean MOBILE = detect();

   private MobileCompat() {
   }

   private static boolean detect() {
      try {
         if (Boolean.getBoolean("croxyware.mobile")) {
            return true;
         }
         if (Boolean.getBoolean("croxyware.desktop")) {
            return false;
         }
         for (String key : new String[]{"ANDROID_ROOT", "ANDROID_DATA", "POJAV_RENDERER", "POJAV_NATIVEDIR", "POJAV_ENVIRON", "ZALITH_RENDERER", "FCL_RENDERER"}) {
            String v = System.getenv(key);
            if (v != null && !v.isEmpty()) {
               return true;
            }
         }
         for (String key : new String[]{"os.name", "java.vendor", "java.vm.vendor", "java.vm.name", "java.home", "user.home"}) {
            String v = System.getProperty(key, "");
            String l = v.toLowerCase(Locale.ROOT);
            if (l.contains("android") || l.contains("pojav") || l.contains("zalith") || l.contains("com.movtery")) {
               return true;
            }
         }
         String renderer = System.getProperty("org.lwjgl.opengl.libname", "").toLowerCase(Locale.ROOT);
         return renderer.contains("gl4es") || renderer.contains("mobileglues") || renderer.contains("angle") || renderer.contains("holygl4es");
      } catch (Throwable ignored) {
         return false;
      }
   }

   public static boolean isMobile() {
      return MOBILE;
   }

   public static boolean isWindows() {
      return System.getProperty("os.name", "").toLowerCase(Locale.ROOT).contains("win") && !MOBILE;
   }

   /** True when AWT desktop features (tray icon, Toolkit) must not be touched. */
   public static boolean isDesktopSafe() {
      return !MOBILE && !java.awt.GraphicsEnvironment.isHeadless();
   }
}
