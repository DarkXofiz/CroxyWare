// Original binary: sg/ec/A4XtXxhrCRoJZBtQ
package sg.ec;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// $VF: Compiled from s
public enum CustomResourceManager implements MinecraftAccess {
   INSTANCE;

   public final File field05980 = resolveConfigDir();
   public volatile String lastSaveError = "";

   // Visible config folder. Android: /storage/emulated/0/QuickMobile/configs (any file manager);
   // fallback / desktop: <game dir>/QuickMobile/configs. Old Hex/custom configs are copied over once.
   private static File resolveConfigDir() {
      File dir = null;
      try {
         if (MobileCompat.isMobile()) {
            File shared = new File("/storage/emulated/0/QuickMobile/configs");
            if ((shared.isDirectory() || shared.mkdirs())) {
               File probe = new File(shared, ".write_test");
               if (probe.createNewFile() || probe.exists()) {
                  probe.delete();
                  dir = shared;
               }
            }
         }
      } catch (Throwable ignored) {
         dir = null;
      }

      if (dir == null) {
         dir = new File(mc.gameDirectory, "QuickMobile/configs");
         dir.mkdirs();
      }

      try {
         File old = new File(ClientEnvironment.field07577, "custom/");
         File[] oldFiles = old.listFiles();
         if (oldFiles != null) {
            for (File f : oldFiles) {
               String fn = f.getName();
               String base = fn.toLowerCase().endsWith(".file") ? fn.substring(0, fn.length() - 5) : fn.toLowerCase().endsWith(".cfg") ? fn.substring(0, fn.length() - 4) : null;
               if (f.isFile() && base != null) {
                  File target = new File(dir, base + ".cfg");
                  if (!target.exists()) {
                     java.nio.file.Files.copy(f.toPath(), target.toPath());
                  }
               }
            }
         }
      } catch (Throwable ignored) {
      }

      // rename leftover Test.file -> Test.cfg
      try {
         File[] existing = dir.listFiles();
         if (existing != null) {
            for (File f : existing) {
               String fn = f.getName();
               if (f.isFile() && fn.toLowerCase().endsWith(".file")) {
                  File target = new File(dir, fn.substring(0, fn.length() - 5) + ".cfg");
                  if (!target.exists()) {
                     f.renameTo(target);
                  } else {
                     f.delete();
                  }
               }
            }
         }
      } catch (Throwable ignored) {
      }

      return dir;
   }
   public final File field05981;
   public final File field05982;
   private final String field05983;

   CustomResourceManager() {
      Object var10001 = null;
      this.field05981 = new File(ClientEnvironment.field07577, "mics/");
      this.field05982 = new File(ClientEnvironment.field07577, "theme/");
      this.field05983 = ".file";
   }

   public void method02458() {
   }

   public void method02459() {
      try {
         this.method02464("Friend");
         this.method02464("Macro");
         this.method02464("Staff");
         this.method02464("WayPoint");
         this.method02464("Theme");
         this.method02464("NoTrace");
         this.method02464("Proxy");
         this.method02464("Bots");
         this.method02464("Default");
      } catch (Throwable var2) {
         throw var2;
      }
   }

   public boolean method02460() {
      return this.method02463(StorageCategory.BOTS, "Bots", BotSettingsStorage.method00299());
   }

   public boolean method02461() {
      return true;
   }

   public boolean method02462() {
      return this.method02463(StorageCategory.AUTO_BUY, "AutoBuy", AutoBuyStorage.method01323());
   }

   private boolean method02463(StorageCategory var1, String var2, byte[] var3) {
      return var3 != null && var3.length != 0 ? GlobalRequestSender.method09378(var3x -> {
         if (StoredContentCache.method05221(var1, var2, var3)) {
            var3x.method01839(var1, var2, var3);
            StoredContentCache.method05219(var1, var2, var3);
         }
      }) : false;
   }

   public boolean method02464(String var1) {
      AbstractStorage var2 = this.method02466(var1);
      return var2 != null && var2.method00298();
   }

   // Local config save (works without the Globals server): <config dir>/<name>.file
   public boolean saveLocalConfig(String name) {
      this.lastSaveError = "";
      try {
         if (name == null || name.isBlank() || name.contains("/") || name.contains("\\") || name.contains("..")) {
            this.lastSaveError = "invalid config name";
            return false;
         }

         this.field05980.mkdirs();
         name = stripConfigExt(name.trim());
         File file = new File(this.field05980, name + ".cfg");
         byte[] raw = ModuleConfigStorage.rawJson();
         byte[] data;
         try {
            data = method02469(raw);
            if (data == null || data.length == 0) {
               data = raw;
            }
         } catch (Throwable encodeError) {
            data = raw;
         }

         java.nio.file.Files.write(file.toPath(), data);
         boolean ok = file.isFile() && file.length() > 0L;
         if (ok) {
            new File(this.field05980, name + ".file").delete();
         }
         if (!ok) {
            this.lastSaveError = "file was not written: " + file.getAbsolutePath();
         }

         this.writeStatus(ok ? "OK: " + file.getAbsolutePath() : this.lastSaveError, null);
         return ok;
      } catch (Throwable t) {
         this.lastSaveError = t.getClass().getSimpleName() + ": " + t.getMessage();
         this.writeStatus("FAILED: " + this.lastSaveError, t);
         return false;
      }
   }

   public static String stripConfigExt(String n) {
      if (n == null) {
         return "";
      }

      n = n.trim();
      if (n.toLowerCase().endsWith(".cfg")) {
         return n.substring(0, n.length() - 4);
      }

      if (n.toLowerCase().endsWith(".file")) {
         return n.substring(0, n.length() - 5);
      }

      return n;
   }

   public boolean hasLocalConfig(String name) {
      String base = stripConfigExt(name);
      return new File(this.field05980, base + ".cfg").isFile() || new File(this.field05980, base + ".file").isFile();
   }

   public List<String> localConfigNames() {
      java.util.TreeSet<String> names = new java.util.TreeSet<>(String.CASE_INSENSITIVE_ORDER);
      File[] files = this.field05980.listFiles();
      if (files != null) {
         for (File f : files) {
            String fn = f.getName().toLowerCase();
            if (f.isFile() && (fn.endsWith(".cfg") || fn.endsWith(".file"))) {
               names.add(stripConfigExt(f.getName()));
            }
         }
      }

      return new ArrayList<>(names);
   }

   private void writeStatus(String line, Throwable t) {
      try {
         java.io.StringWriter sw = new java.io.StringWriter();
         sw.append(line).append("\n");
         if (t != null) {
            t.printStackTrace(new java.io.PrintWriter(sw));
         }

         java.nio.file.Files.writeString(new File(this.field05980, "last_save_status.txt").toPath(), sw.toString());
      } catch (Throwable ignored) {
      }
   }

   public boolean method02465(String var1) {
      AbstractStorage var2 = this.method02466(var1);
      if (var2 == null) {
         return false;
      }

      File var3 = var2.method03303().getParentFile();
      return var3 != null && !var3.exists() && !var3.mkdirs() ? false : var2.method00296();
   }

   public AbstractStorage method02466(String var1) {
      if (var1.equals("default")) {
         return new ModuleConfigStorage(new File(ClientEnvironment.field07577, "default.file"));
      }

      File[] var2 = this.field05980.listFiles();
      if (var2 != null) {
         for (File var6 : var2) {
            if (var6.isFile() && stripConfigExt(var6.getName()).equals(stripConfigExt(var1))
               && (var6.getName().toLowerCase().endsWith(".cfg") || var6.getName().toLowerCase().endsWith(".file"))) {
               return new ModuleConfigStorage(var6);
            }
         }
      }

      File[] var9 = this.field05981.listFiles();
      if (var9 != null) {
         for (File var7 : var9) {
            String var8 = var7.getName().replace(".file", "");
            if (var8.equalsIgnoreCase(var1)) {
               return this.method02467(var8, var7);
            }
         }
      }

      return this.method02467(var1, new File(this.field05981, var1 + ".file"));
   }

   private AbstractStorage method02467(String var1, File var2) {
      return switch (var1) {
         case "Friend" -> new NoTraceStorage(var2);
         case "LastAccount" -> new AccountStorage(var2);
         case "Macro" -> new MacroStorage(var2);
         case "Staff" -> new StaffStorage(var2);
         case "WayPoint" -> new WaypointStorage(var2);
         case "Theme" -> new ThemeStorage(var2);
         case "NoTrace" -> new FriendStorage(var2);
         case "Bots" -> new BotSettingsStorage(var2);
         case "Proxy" -> new ProxyStorage(var2);
         case "AutoBuy" -> new AutoBuyStorage(var2);
         default -> null;
      };
   }

   public List<ModuleConfigStorage> method02468() {
      ArrayList var1 = new ArrayList();
      File[] var2 = this.field05980.listFiles();
      if (var2 != null) {
         for (File var6 : var2) {
            String fn = var6.getName().toLowerCase();
            if (var6.isFile() && (fn.endsWith(".cfg") || fn.endsWith(".file"))) {
               var1.add(new ModuleConfigStorage(var6));
            }
         }
      }

      return var1;
   }

   public static byte[] method02469(byte[] var0) {
      return ResourceByteCodec.method06878(var0);
   }

   public static byte[] method02470(byte[] var0) {
      return ResourceByteCodec.method06874(var0);
   }
}
