package sg.ec;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Fault isolation for the client's custom render hooks. If one of them throws (bad shader on a
 * GL translation layer, unsupported texture format, ...) the feature is switched off for the rest
 * of the session instead of crashing the whole renderer. Details go to croxyware_render.log.
 */
public final class RenderGuard {
   private static final Set<String> DISABLED = ConcurrentHashMap.newKeySet();

   private RenderGuard() {
   }

   public static boolean isDisabled(String feature) {
      return DISABLED.contains(feature);
   }

   public static void run(String feature, Runnable body) {
      if (DISABLED.contains(feature)) {
         return;
      }

      try {
         body.run();
      } catch (Throwable t) {
         fail(feature, t);
      }
   }

   public static void fail(String feature, Throwable t) {
      if (!DISABLED.add(feature)) {
         return;
      }

      try {
         StringWriter sw = new StringWriter();
         t.printStackTrace(new PrintWriter(sw));
         String line = "[CroxyWare] render feature '" + feature + "' disabled after error (mobile=" + MobileCompat.isMobile() + ")\n" + sw + "\n";
         System.err.print(line);
         Path file = Path.of("croxyware_render.log");
         Files.writeString(file, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
      } catch (IOException | RuntimeException ignored) {
      }
   }
}
