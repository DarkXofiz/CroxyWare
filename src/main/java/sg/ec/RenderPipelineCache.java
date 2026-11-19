// Original binary: sg/ec/tAzARPllmbk0Pi4d
package sg.ec;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.IdentityHashMap;
import java.util.Map;

// $VF: Compiled from s
public final class RenderPipelineCache {
   private static final Map<RenderPipeline, Boolean> field23832 = new IdentityHashMap<>();

   public static boolean method10799(RenderPipeline var0) {
      if (var0 == null) {
         return false;
      }

      Boolean var1 = field23832.get(var0);
      if (var1 != null) {
         return var1;
      }

      boolean sx;
      try {
         sx = RenderSystem.getDevice().precompilePipeline(var0).isValid();
      } catch (Throwable sxx) {
         sx = false;
      }

      field23832.put(var0, sx);
      return sx;
   }

   public static void method10800() {
      field23832.clear();
   }

   private RenderPipelineCache() {
   }
}
