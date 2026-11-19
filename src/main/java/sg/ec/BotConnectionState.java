// Original binary: sg/ec/RZi9OBzBiYu0TPl9
package sg.ec;

// $VF: Compiled from s
public enum BotConnectionState {
   IDLE("Offline", false),
   QUEUED("In queue", true),
   CONNECTING("Connecting", true),
   LOGGING_IN("Authorization", true),
   CONFIGURING("Setup", true),
   PLAYING("In game", true),
   CONTROLLED("You are playing", false),
   FAILED("Error", false);

   private final String field11436;
   private final boolean field11437;

   BotConnectionState(String var3, boolean var4) {
      this.field11436 = var3;
      this.field11437 = var4;
   }

   public boolean method05278() {
      return this == PLAYING;
   }

   public String method05279() {
      return this.field11436;
   }

   public boolean method05280() {
      return this.field11437;
   }
}
