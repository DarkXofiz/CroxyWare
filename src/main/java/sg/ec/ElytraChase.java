package sg.ec;

import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

/**
 * ElytraChase (ported from the Nursultan "ElytraTarget" module). Renamed to avoid clashing with this
 * client's own ElytraTarget (its auto-shoot-while-gliding module) — same package can't have two classes
 * with that name, and the two features are unrelated.
 *
 * Uses the aura's current target (AttackAura.field00785) and, while gliding, fires fireworks on the chosen
 * schedule. AttackAura's own aiming code was left untouched: "Outrun"/"Overshoot" do not redirect the
 * aura's aim (that would mean editing AttackAura, which risks breaking it); instead they control an
 * "outrun assist" — an extra firework burst whenever the player is falling behind the target's predicted
 * (overshoot-projected) position.
 */
public class ElytraChase extends ClientModule {
   private static final String RANGE = "Detection range";
   private static final String MODE = "Firework mode";
   private static final String TICKS_DELAY = "Ticks delay";
   private static final String OUTRUN = "Outrun";
   private static final String OVERSHOOT = "Overshoot";
   private static final String MANUAL_KEY = "Manual key";

   private static final String OFF = "Off";
   private static final String MANUAL = "Manual";
   private static final String TICKS = "Ticks";
   private static final String AUTO = "Auto";

   private final NumberSetting<Float> range;
   private final ModeSetting mode;
   private final NumberSetting<Integer> ticksDelay;
   private final ToggleSetting outrun;
   private final NumberSetting<Float> overshoot;
   private final KeyBindSetting manualKey;

   private int lastFireTick = -1000;
   private double lastPredictedDistance = -1.0;

   public ElytraChase() {
      super(ModuleCategory.Extra, ModuleSubCategory.Main);
      this.range = new NumberSetting<Float>(RANGE)
         .method10835(10.0F)
         .method10838(100.0F)
         .method10834(5.0F)
         .method03017(50.0F)
         .method03023(this);
      this.mode = new ModeSetting(MODE).method05597(OFF, MANUAL, TICKS, AUTO).method03017(OFF).method03023(this);
      this.ticksDelay = new NumberSetting<Integer>(TICKS_DELAY)
         .method10835(2)
         .method10838(60)
         .method10834(1)
         .method03017(20)
         .method03023(this)
         .method03025(() -> this.mode.method05600(TICKS));
      this.outrun = new ToggleSetting(OUTRUN).method03017(true).method03023(this);
      this.overshoot = new NumberSetting<Float>(OVERSHOOT)
         .method10835(0.0F)
         .method10838(6.0F)
         .method10834(0.5F)
         .method03017(5.0F)
         .method03023(this)
         .method03025(this.outrun::method05999);
      this.manualKey = new KeyBindSetting(MANUAL_KEY)
         .method03017(new KeyBinding())
         .method03023(this)
         .method03025(() -> this.mode.method05600(MANUAL));
      this.method03935("ElytraChase");
      this.method03931("While gliding at the aura's target: fires fireworks on a schedule (Manual/Ticks/Auto) and, with Outrun, bursts an extra one whenever you start falling behind the target's predicted path.");
   }

   @Override
   public void method00276() {
      super.method00276();
      this.lastFireTick = -1000;
      this.lastPredictedDistance = -1.0;
   }

   @Subscribe
   private void onKey(KeyInputEvent event) {
      if (this.mode.method05600(MANUAL)
         && event.method05884() == InputAction.PRESS
         && event.method05887() == this.manualKey.method03022().method05871()) {
         this.tryFire(mc.player.tickCount, true);
      }
   }

   @Subscribe
   private void onTick(TickEvent event) {
      if (mc.player == null || mc.level == null || mc.gameMode == null || mc.player.connection == null) {
         return;
      }

      if (!mc.player.isFallFlying()) {
         this.lastPredictedDistance = -1.0;
         return;
      }

      LivingEntity target = AttackAura.field00785;
      if (target == null || !target.isAlive()) {
         this.lastPredictedDistance = -1.0;
         return;
      }

      int now = mc.player.tickCount;

      if (this.outrun.method05999() && target.isFallFlying()) {
         Vec3 motion = target.getDeltaMovement();
         Vec3 predicted = motion.lengthSqr() > 1.0E-4
            ? target.position().add(motion.normalize().scale(this.overshoot.method03022()))
            : target.position();
         double distance = mc.player.position().distanceTo(predicted);
         if (this.lastPredictedDistance >= 0.0 && distance > this.lastPredictedDistance + 0.02 && now % 5 == 0) {
            this.tryFire(now, false);
         }

         this.lastPredictedDistance = distance;
      }

      switch (this.mode.method03022()) {
         case AUTO -> {
            Vec3 velocity = mc.player.getDeltaMovement();
            float speed = (float)velocity.length();
            float velocityYaw = (float)(Math.toDegrees(Math.atan2(-velocity.x, velocity.z)));
            float yawDelta = net.minecraft.util.Mth.wrapDegrees(velocityYaw - mc.player.getYRot());
            if ((speed < 1.5F || Math.abs(yawDelta) > 150.0F) && now % 5 == 0) {
               this.tryFire(now, false);
            }
         }
         case TICKS -> {
            int delay = Math.max(1, this.ticksDelay.method03022());
            if (now % delay == 0) {
               this.tryFire(now, false);
            }
         }
         default -> {
         }
      }
   }

   private void tryFire(int now, boolean force) {
      if (!force && now - this.lastFireTick < 5) {
         return;
      }

      if (this.useFirework()) {
         this.lastFireTick = now;
      }
   }

   /** Silent firework use: offhand rocket is used directly; a hotbar rocket is swapped in, used, then swapped back. */
   private boolean useFirework() {
      if (mc.player.getOffhandItem().getItem() == Items.FIREWORK_ROCKET) {
         mc.gameMode.useItem(mc.player, InteractionHand.OFF_HAND);
         return true;
      }

      int slot = -1;
      for (int i = 0; i < 9; i++) {
         ItemStack stack = mc.player.getInventory().getItem(i);
         if (!stack.isEmpty() && stack.getItem() == Items.FIREWORK_ROCKET) {
            slot = i;
            break;
         }
      }

      if (slot < 0) {
         return false;
      }

      int original = mc.player.getInventory().getSelectedSlot();
      boolean swap = slot != original;
      if (swap) {
         mc.player.connection.send(new ServerboundSetCarriedItemPacket(slot));
      }

      mc.gameMode.useItem(mc.player, InteractionHand.MAIN_HAND);
      if (swap) {
         mc.player.connection.send(new ServerboundSetCarriedItemPacket(original));
      }

      return true;
   }
}
