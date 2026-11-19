// Original binary: sg/ec/jX65aleREK3DJqf5
package sg.ec;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PlayerHeadItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;

// $VF: Compiled from s
public class RareGrabber extends ClientModule {
   private final MultiToggleSetting field20440;
   private final NumberSetting<Integer> field20441;
   private final ToggleSetting field20442;
   private final ModeSetting field20443;
   private final MultiToggleSetting field20444;
   private final ToggleSetting field20445;
   private Entity field20446;
   private static final String field20447 = "Loot";
   private static final String field20448 = "Heads";
   private static final String field20449 = "Ench. totems";
   private static final String field20450 = "Elytra";
   private static final String field20451 = "Ghast tear";
   private static final String field20452 = "Sword sharpness 6-7";
   private static final String field20453 = "Protection 5 armor";
   private static final String field20454 = "Target after tick";
   private static final String field20455 = "Drop items";
   private static final String field20456 = "Drop from";
   private static final String field20457 = "Inventory";
   private static final String field20458 = "Hotbar";
   private static final String field20459 = "Inventory";
   private static final String field20460 = "What to drop";
   private static final String field20461 = "Blocks";
   private static final String field20462 = "Buffs";
   private static final String field20463 = "Client bow";
   private static final String field20464 = "Rare Grabber";
   private static final String field20465 = "Heads";
   private static final String field20466 = "Ench. totems";
   private static final String field20467 = "Elytra";
   private static final String field20468 = "Ghast tear";
   private static final String field20469 = "Sword sharpness 6-7";
   private static final String field20470 = "Protection 5 armor";
   private static final double field20471 = 30.0;
   private static final double field20472 = 2.0;
   private static final float field20473 = 1111.0F;

   @Subscribe
   private void method09012(PlayerUpdateEvent var1) {
      if (mc.player != null && mc.level != null) {
         this.field20446 = null;
         boolean[] var2 = new boolean[]{
            this.field20440.method04520(field20465),
            this.field20440.method04520(field20466),
            this.field20440.method04520(field20467),
            this.field20440.method04520(field20468),
            this.field20440.method04520(field20469),
            this.field20440.method04520(field20470)
         };
         float sx = method09013(this.field20441.method03022());

         for (Entity sxx : mc.level.entitiesForRendering()) {
            if (sxx instanceof ItemEntity sxxx && !(mc.player.getEyePosition().distanceTo(sxxx.position()) > field20471) && !(sxxx.getAge() < sx)) {
               this.field20446 = this.method09014(var2, sxxx);
            }
         }
      }
   }

   private static int method09013(int var0) {
      return Math.max(1, Math.min(20, var0));
   }

   private Entity method09014(boolean[] var1, ItemEntity var2) {
      if (var2 == null) {
         return null;
      } else {
         ItemStack var3 = var2.getItem();
         Item var4 = var3.getItem();
         if (var1[0] && var4 instanceof PlayerHeadItem) {
            return var2;
         } else if (var1[1] && var3.isEnchanted() && var4.equals(Items.TOTEM_OF_UNDYING)) {
            return var2;
         } else if (var1[2] && var4.equals(Items.ELYTRA)) {
            return var2;
         } else if (var1[3] && var4.equals(Items.GHAST_TEAR)) {
            return var2;
         } else if (var1[4] && EnchantmentUtils.method06757(var3, Enchantments.SHARPNESS) >= 6) {
            return var2;
         } else {
            return var1[5] && EnchantmentUtils.method06757(var3, Enchantments.PROTECTION) >= 5 ? var2 : null;
         }
      }
   }

   @Subscribe
   private void method09015(FrameUpdateEvent var1) {
      if (this.field20446 != null) {
         if (mc.player != null) {
            float var2 = var1.method03782().getGameTimeDeltaPartialTick(true);
            Vec3 var3 = this.field20446.getPosition(var2);
            Vec3 var4 = mc.player.getEyePosition(var2);
            Vec3 var5 = MovementUtils.method05718(this.field20446).scale(field20472);
            Vec3 var6 = var3.add(var5).subtract(var4);
            Rotation var7 = Rotation.method09438(var6);
            RotationManager.method06547(var7, field20473, 1000, this.field20445.method05999());
         }
      }
   }

   public RareGrabber() {
      super(ModuleCategory.Misc, ModuleSubCategory.Utility);
      this.field20440 = new MultiToggleSetting(field20447)
         .method04517(NamedOption.method08077(field20448, true))
         .method04517(NamedOption.method08077(field20449, false))
         .method04517(NamedOption.method08077(field20450, false))
         .method04517(NamedOption.method08077(field20451, false))
         .method04517(NamedOption.method08077(field20452, false))
         .method04517(NamedOption.method08077(field20453, false))
         .method03023(this);
      this.field20441 = new NumberSetting<Integer>(field20454).method10835(1).method10838(20).method10834(1).method03017(1).method03023(this);
      this.field20442 = new ToggleSetting(field20455).method03017(false).method03023(this);
      this.field20443 = new ModeSetting(field20456).method05597(field20457, field20458).method03017(field20459).method03023(this);
      this.field20444 = new MultiToggleSetting(field20460)
         .method04517(NamedOption.method08077(field20461, false))
         .method04517(NamedOption.method08077(field20462, false))
         .method03025(this.field20442::method03022)
         .method03023(this);
      this.field20445 = new ToggleSetting(field20463).method03017(false).method03023(this);
      this.field20446 = null;
      this.method03935(field20464);
   }
}
