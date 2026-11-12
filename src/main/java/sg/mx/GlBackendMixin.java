package sg.mx;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.nio.ByteBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sg.ec.GlCompat;

/**
 * Mobile fix for "glGetError mismatch" in RenderPearl's OpenGL backend (see sg.ec.GlCompat).
 * Both SDL_GL_GetProcAddress overloads are wrapped because the exact one the game calls is not fixed;
 * injections that do not match are skipped (require = 0), so this can never break startup by itself.
 */
@Mixin(targets = "com.mojang.renderpearl.backend.opengl.GlBackend", remap = false)
public class GlBackendMixin {
   @WrapOperation(
      method = {"loadLibrary", "lambda$loadLibrary$0", "lambda$loadLibrary$1"},
      at = @At(value = "INVOKE", target = "Lorg/lwjgl/sdl/SDLVideo;SDL_GL_GetProcAddress(Ljava/lang/CharSequence;)J", remap = false),
      require = 0,
      remap = false
   )
   private static long croxy$alignGlGetErrorChars(CharSequence name, Operation<Long> original) {
      long address = original.call(name);
      return GlCompat.alignGlGetError(String.valueOf(name), address);
   }

   @WrapOperation(
      method = {"loadLibrary", "lambda$loadLibrary$0", "lambda$loadLibrary$1"},
      at = @At(value = "INVOKE", target = "Lorg/lwjgl/sdl/SDLVideo;SDL_GL_GetProcAddress(Ljava/nio/ByteBuffer;)J", remap = false),
      require = 0,
      remap = false
   )
   private static long croxy$alignGlGetErrorBuffer(ByteBuffer name, Operation<Long> original) {
      long address = original.call(name);
      return GlCompat.alignGlGetError(GlCompat.asciiName(name), address);
   }
}
