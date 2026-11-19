// Original binary: sg/ec/aNQd4Ira6LMiWlU0
package sg.ec;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;

// $VF: Compiled from s
@CommandInfo(name = "way", description = "")
public class WayCommand extends AbstractCommand {
   private static final String field15590 = "add";
   private static final String field15591 = "name";
   private static final String field15592 = "x";
   private static final String field15593 = "y";
   private static final String field15594 = "z";
   private static final String field15595 = "remove";
   private static final String field15596 = "name";
   private static final String field15597 = "list";
   private static final String field15598 = "clear";
   private static final String field15599 = "[\\s_\\-]+";
   private static final String field15600 = "Waypoint list was cleared.";
   private static final String field15601 = "Waypoint list is already empty.";
   private static final String field15602 = "Waypoint list is empty.";
   private static final String field15603 = "Waypoint list:";
   private static final String field15604 = "name";
   private static final String field15605 = "x";
   private static final String field15606 = "y";
   private static final String field15607 = "z";
   private static final String field15608 = "name";
   private static final String field15609 = "Waypoint name cannot be empty.";
   private static final String field15610 = "A waypoint with a similar name already exists.";

   @Override
   public void method00273(LiteralArgumentBuilder<ClientSuggestionProvider> var1) {
      var1.then(
         this.method01188(field15590)
            .then(
               this.method01184(field15591, StringArgumentType.string())
                  .then(
                     this.method01184(field15592, IntegerArgumentType.integer())
                        .then(
                           this.method01184(field15593, IntegerArgumentType.integer())
                              .then(this.method01184(field15594, IntegerArgumentType.integer()).executes(var1x -> {
                                 int var2 = IntegerArgumentType.getInteger(var1x, field15605);
                                 int var3 = IntegerArgumentType.getInteger(var1x, field15606);
                                 int var4 = IntegerArgumentType.getInteger(var1x, field15607);
                                 String var5 = StringArgumentType.getString(var1x, field15608);
                                 if (var5 == null || var5.isBlank()) {
                                    this.method01187(field15609);
                                    return 0;
                                 } else if (this.method06921(var5)) {
                                    this.method01187(field15610);
                                    return 0;
                                 } else {
                                    WaypointManager.INSTANCE.method09832(var2, var3, var4, var5);
                                    this.method01187("Waypoint named: " + var5 + " was added.");
                                    return 1;
                                 }
                              }))
                        )
                  )
            )
      );
      var1.then(this.method01188(field15595).then(this.method01184(field15596, new WaypointNameArgument()).executes(var1x -> {
         String var2 = (String)var1x.getArgument(field15604, String.class);
         int var3 = WaypointManager.INSTANCE.method09834(var2);
         if (var3 > 0) {
            this.method01187("Waypoint named: " + var2 + " was removed.");
         } else {
            this.method01187("Waypoint named: " + var2 + " not found.");
         }

         return 1;
      })));
      var1.then(
         this.method01188(field15597)
            .executes(
               var1x -> {
                  var var2 = WaypointManager.INSTANCE.method09837();
                  if (var2.isEmpty()) {
                     this.method01187(field15602);
                     return 1;
                  }

                  this.method01187(field15603);

                  for (Waypoint var4 : var2) {
                     this.method01187(
                        "Name: "
                           + var4.method11262()
                           + ", x: "
                           + (int)var4.method11264()
                           + ", y: "
                           + (int)var4.method11260()
                           + ", z: "
                           + (int)var4.method11269()
                     );
                  }

                  return 1;
               }
            )
      );
      var1.then(this.method01188(field15598).executes(var1x -> {
         int var2 = WaypointManager.INSTANCE.method09835();
         if (var2 > 0) {
            this.method01187(field15600);
         } else {
            this.method01187(field15601);
         }

         return 1;
      }));
   }

   private boolean method06921(String var1) {
      String var2 = this.method06925(var1);
      if (var2.isBlank()) {
         return true;
      }

      for (String var4 : WaypointManager.INSTANCE.method09839()) {
         if (this.method06925(var4).equals(var2)) {
            return true;
         }
      }

      return false;
   }

   private String method06925(String var1) {
      return var1 == null ? "" : var1.toLowerCase(Locale.ROOT).replaceAll(field15599, "");
   }
}
