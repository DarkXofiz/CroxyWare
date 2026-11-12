package sg.ec;

import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL;

/**
 * Minecraft 26.3 (RenderPearl) opens OpenGL through SDL and then requires that the glGetError address
 * returned by SDL_GL_GetProcAddress equals the one LWJGL resolved. On Android launchers (Zalith/Pojav
 * with MobileGlues, Zink, GL4ES, ...) the two come from different libraries, so the game aborts with
 * "BackendCreationException: glGetError mismatch" and "No supported graphics backend was found".
 * On mobile we hand back the LWJGL address so the check passes; desktop behaviour is untouched.
 */
public final class GlCompat {
   private static boolean reported;

   private GlCompat() {
   }

   public static long alignGlGetError(String name, long sdlAddress) {
      if (!"glGetError".equals(name) || !MobileCompat.isMobile()) {
         return sdlAddress;
      }

      try {
         long lwjglAddress = GL.getFunctionProvider().getFunctionAddress("glGetError");
         if (lwjglAddress != 0L && lwjglAddress != sdlAddress) {
            if (!reported) {
               reported = true;
               System.err.println("[CroxyWare] mobile: aligned SDL glGetError address with LWJGL (RenderPearl check bypassed)");
            }

            return lwjglAddress;
         }
      } catch (Throwable ignored) {
      }

      return sdlAddress;
   }

   public static String asciiName(ByteBuffer buffer) {
      StringBuilder sb = new StringBuilder();
      ByteBuffer view = buffer.duplicate();
      while (view.hasRemaining()) {
         byte c = view.get();
         if (c == 0) {
            break;
         }

         sb.append((char)(c & 0xFF));
      }

      return sb.toString();
   }
}
