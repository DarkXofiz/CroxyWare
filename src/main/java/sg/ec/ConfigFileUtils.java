// Original binary: sg/ec/e1yqL1F9sp1ruhCg
package sg.ec;

import java.io.File;
import java.util.List;

// $VF: Compiled from s
public final class ConfigFileUtils {
   private static final String field18452 = "default";

   public static List<ModuleConfigStorage> method07557() {
      return CustomResourceManager.INSTANCE.method02468();
   }

   public static boolean method07558(String var0) {
      if (var0 != null && !var0.equalsIgnoreCase(field18452)) {
         String base = CustomResourceManager.stripConfigExt(var0);
         File cfg = new File(CustomResourceManager.INSTANCE.field05980, base + ".cfg");
         File old = new File(CustomResourceManager.INSTANCE.field05980, base + ".file");
         boolean a = cfg.exists() && cfg.delete();
         boolean b = old.exists() && old.delete();
         return a || b;
      } else {
         return false;
      }
   }
}
