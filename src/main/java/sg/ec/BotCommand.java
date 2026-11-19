// Original binary: sg/ec/s6kxHOW8t8twAQFt
package sg.ec;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;

// $VF: Compiled from s
@CommandInfo(name = "bot", description = "Bot management")
public class BotCommand extends AbstractCommand {
   private static final String field23570 = "mirror";
   private static final String field23571 = "name";
   private static final String field23572 = "follow";
   private static final String field23573 = "name";
   private static final String field23574 = "chat";
   private static final String field23575 = "bot";
   private static final String field23576 = "message";
   private static final String field23577 = "stop";
   private static final String field23578 = "Bots stopped.";
   private static final String field23579 = "bot";
   private static final String field23580 = "message";
   private static final String field23581 = "Message is empty.";
   private static final String field23582 = "all";
   private static final String field23583 = "No bot is in game.";
   private static final String field23584 = "name";
   private static final String field23585 = "off";
   private static final String field23586 = "Bots no longer follow the player.";
   private static final String field23587 = "name";
   private static final String field23588 = "off";
   private static final String field23589 = "Bots no longer repeat actions.";

   private static String method10609(String var0) {
      String var1 = var0.trim();
      return var1.length() >= 2 && var1.charAt(0) == '"' && var1.charAt(var1.length() - 1) == '"' ? var1.substring(1, var1.length() - 1).trim() : var1;
   }

   @Override
   public void method00273(LiteralArgumentBuilder<ClientSuggestionProvider> var1) {
      BotFollowManager var2 = BotFollowManager.INSTANCE;
      var1.then(this.method01188(field23570).then(this.method01184(field23571, new BotFollowNameArgument()).executes(var2x -> {
         String var3 = (String)var2x.getArgument(field23587, String.class);
         if (var3.equalsIgnoreCase(field23588)) {
            var2.method02184();
            this.method01187(field23589);
            return 1;
         } else {
            var2.method02182(var3);
            this.method01187("Bots repeat the player's actions: " + var3);
            return 1;
         }
      })));
      var1.then(this.method01188(field23572).then(this.method01184(field23573, new BotFollowNameArgument()).executes(var2x -> {
         String var3 = (String)var2x.getArgument(field23584, String.class);
         if (var3.equalsIgnoreCase(field23585)) {
            var2.method02185();
            this.method01187(field23586);
            return 1;
         } else {
            var2.method02183(var3);
            this.method01187("Bots follow the player: " + var3);
            return 1;
         }
      })));
      var1.then(
         this.method01188(field23574)
            .then(
               this.method01184(field23575, new BotNameArgument()).then(this.method01184(field23576, StringArgumentType.greedyString()).executes(var1x -> {
                  String var2x = (String)var1x.getArgument(field23579, String.class);
                  String var3 = method10609(StringArgumentType.getString(var1x, field23580));
                  if (var3.isBlank()) {
                     this.method01187(field23581);
                     return 0;
                  }

                  if (var2x.equalsIgnoreCase(field23582)) {
                     int var5 = BotManager.INSTANCE.method03077(var3);
                     if (var5 == 0) {
                        this.method01187(field23583);
                        return 0;
                     } else {
                        this.method01187("Message sent from " + var5 + " bots.");
                        return 1;
                     }
                  } else {
                     BotClient var4 = BotManager.INSTANCE.method03073(var2x);
                     if (var4 == null) {
                        this.method01187("Bot with nickname: " + var2x + " is not in the list.");
                        return 0;
                     } else if (!var4.method00146().method05278() && !BotSessionManager.INSTANCE.method08733(var4)) {
                        this.method01187("Bot with nickname: " + var2x + " is not in game.");
                        return 0;
                     } else {
                        var4.method00144(var3);
                        this.method01187("Message sent from bot: " + var2x);
                        return 1;
                     }
                  }
               }))
            )
      );
      var1.then(this.method01188(field23577).executes(var2x -> {
         var2.method02186();
         this.method01187(field23578);
         return 1;
      }));
   }
}
