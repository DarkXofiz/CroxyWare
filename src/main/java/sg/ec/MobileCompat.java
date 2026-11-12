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
         if (Boolean.getBoolean("croxyware.mobile") || System.getProperty("droidbridge.path.minecraft") != null || System.getProperty("glfwstub.windowWidth") != null) {
            return true;
         }
         if (Boolean.getBoolean("croxyware.desktop")) {
            return false;
         }
         for (String key : new String[]{"ANDROID_ROOT", "ANDROID_DATA", "POJAV_RENDERER", "POJAV_NATIVEDIR", "POJAV_ENVIRON", "ZALITH_RENDERER", "FCL_RENDERER", "DROIDBRIDGE_RENDERER"}) {
            String v = System.getenv(key);
            if (v != null && !v.isEmpty()) {
               return true;
            }
         }
         for (String key : new String[]{"os.name", "java.vendor", "java.vm.vendor", "java.vm.name", "java.home", "user.home"}) {
            String v = System.getProperty(key, "");
            String l = v.toLowerCase(Locale.ROOT);
            if (l.contains("android") || l.contains("pojav") || l.contains("zalith") || l.contains("com.movtery") || l.contains("droidbridge") || l.contains("cheesakeyk") || l.contains("fcl") && l.contains("plugin")) {
               return true;
            }
         }
         String renderer = System.getProperty("org.lwjgl.opengl.libname", "").toLowerCase(Locale.ROOT);
         return renderer.contains("gl4es") || renderer.contains("mobileglues") || renderer.contains("angle") || renderer.contains("holygl4es") || renderer.contains("krypton") || renderer.contains("ng_gl4es");
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

   private static final String[] SAFE_MODE_BLOCKED = {
      "Render", "Shader", "Texture", "Gui", "Hud", "Pack", "Resource", "Sprite", "Atlas", "Font", "Glyph", "Screen", "Title", "CubeMap",
      "DynamicTexture", "Window", "Frustum", "Sky", "Fog", "Particle", "ModelManager", "ModelPart", "Vertex", "Buffer", "SubmitNode",
      "PoseStack", "Skull", "Chest", "Beacon", "FishingHook", "Lightmap", "MapRenderer", "Sound", "Options", "Framerate", "DeltaTracker",
      "MouseHandler", "Vulkan", "GlBackend", "CommandEncoder", "FirstPerson", "ItemInHand", "ItemStackRenderState", "Camera", "Overlay",
      "StringSplitter", "StringDecomposer", "Style", "MutableComponent", "ChatComponent", "Realms", "ServerSelection", "JoinMultiplayer",
      "PlayerTabOverlay", "LevelExtractor", "LevelEventHandler", "ScreenRectangle", "BossHealth", "DebugEntry", "Profiler", "Waypoint", "ArmedEntityRenderState"
   };

   /**
    * Mobile safe mode: on Zalith/Pojav every mixin that touches rendering, resource loading or GUI is skipped, so the
    * game starts exactly like vanilla (the mod only keeps gameplay/network hooks). Disable with -Dcroxyware.safe=false.
    */
   public static boolean isSafeMode() {
      return MOBILE && !"false".equalsIgnoreCase(System.getProperty("croxyware.safe", "true"));
   }

   public static boolean isBlockedInSafeMode(String mixinClassName) {
      if (mixinClassName == null) {
         return false;
      }

      String simple = mixinClassName.substring(mixinClassName.lastIndexOf('.') + 1);
      if (simple.equals("MobilePipelineRegisterMixin")) {
         return false;
      }

      for (String key : SAFE_MODE_BLOCKED) {
         if (simple.contains(key)) {
            return true;
         }
      }

      return false;
   }
}
