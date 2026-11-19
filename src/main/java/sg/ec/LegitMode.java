// Original binary: sg/ec/RHVb1vKRqWdWZlGt
package sg.ec;

// $VF: Compiled from s
public class LegitMode extends ClientModule {
   private final MultiToggleSetting field11402;
   private static final String field11403 = "What to remove?";
   private static final String field11404 = "Client messages";
   private static final String field11405 = "Chat commands";
   private static final String field11406 = "Drop everything";
   private static final String field11407 = "Main menu";
   private static final String field11408 = "Client Cape/Elytra";
   private static final String field11409 = "Search in chests";
   private static final String field11410 = "Legit Mode";
   private static final String field11411 = "Client messages";
   private static final String field11412 = "Drop everything";
   private static final String field11413 = "Chat commands";
   private static final String field11414 = "Main menu";
   private static final String field11415 = "Client Cape/Elytra";
   private static final String field11416 = "Search in chests";

   @Subscribe
   private void method05268(ClientFeaturePolicyEvent var1) {
      boolean var2 = switch (var1.method06425()) {
         case CLIENT_MESSAGE -> this.field11402.method04520(field11411);
         case INVENTORY_BUTTON -> this.field11402.method04520(field11412);
         case CHAT_COMMAND -> this.field11402.method04520(field11413);
         case MAIN_MENU -> this.field11402.method04520(field11414);
         case CLIENT_CAPE -> this.field11402.method04520(field11415);
         case CHEST_SEARCH -> this.field11402.method04520(field11416);
      };
      if (var2) {
         var1.method05696();
      }
   }

   public LegitMode() {
      super(ModuleCategory.Misc, ModuleSubCategory.Other);
      this.field11402 = new MultiToggleSetting(field11403)
         .method04517(NamedOption.method08077(field11404, false))
         .method04517(NamedOption.method08077(field11405, true))
         .method04517(NamedOption.method08077(field11406, true))
         .method04517(NamedOption.method08077(field11407, false))
         .method04517(NamedOption.method08077(field11408, false))
         .method04517(NamedOption.method08077(field11409, true))
         .method03023(this);
      this.method03935(field11410);
   }
}
