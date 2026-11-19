package sg.ec;

import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;

/**
 * FireworkSet: while gliding with an elytra and holding a sword, the module hits normally with the sword
 * (no slot swap needed for the hit) and, on its own, silently swaps to a firework rocket in the hotbar,
 * fires it, and swaps straight back to the sword to keep boosting flight speed.
 * Hit timing/bypass is left to AttackAura (no separate hit delay here); only the rotation mode used while
 * aiming at the target, the flight speed and the rocket firing rate are configurable.
 */
public class FireworkSet extends ClientModule {
   private static final String NAME = "FireworkSet";
   private static final String MODE = "Rotation mode";
   private static final String SPEED = "Flight speed";
   private static final String SMOOTH_SPEED = "Smooth acceleration";
   private static final String RANGE = "Hit range";
   private static final String ROCKET_DELAY = "Rocket delay";
   private static final String AUTO_ROCKET = "Auto firework";
   private static final String PLAYERS = "Players";
   private static final String MOBS = "Mobs";

   private static final String GRIM = "Grim";
   private static final String MATRIX = "Matrix";
   private static final String MATRIX_SNAP = "MatrixSnap";
   private static final String LEGIT = "Legit";
   private static final String SMOOTH = "Smooth";
   private static final String UNIVERSAL = "Universal";

   private final ModeSetting mode;
   private final NumberSetting<Float> speed;
   private final ToggleSetting smoothSpeed;
   private final NumberSetting<Float> range;
   private final NumberSetting<Integer> rocketDelay;
   private final ToggleSetting autoRocket;
   private final ToggleSetting players;
   private final ToggleSetting mobs;

   private int lastRocketTick = -1000;

   public FireworkSet() {
      super(ModuleCategory.Combat, ModuleSubCategory.Combat);
      this.mode = new ModeSetting(MODE)
         .method05597(GRIM, MATRIX, MATRIX_SNAP, LEGIT, SMOOTH, UNIVERSAL)
         .method03017(MATRIX)
         .method03023(this);
      this.speed = new NumberSetting<Float>(SPEED)
         .method10835(0.5F)
         .method10838(4.0F)
         .method10834(0.1F)
         .method03017(1.6F)
         .method03023(this);
      this.smoothSpeed = new ToggleSetting(SMOOTH_SPEED).method03017(true).method03023(this);
      this.range = new NumberSetting<Float>(RANGE)
         .method10835(2.5F)
         .method10838(6.0F)
         .method10834(0.1F)
         .method03017(3.4F)
         .method03023(this);
      // How fast fireworks get pressed: 0 = as fast as possible (every tick), 300 = roughly one every 15s.
      this.rocketDelay = new NumberSetting<Integer>(ROCKET_DELAY)
         .method10835(0)
         .method10838(300)
         .method10834(1)
         .method03017(20)
         .method03023(this);
      this.autoRocket = new ToggleSetting(AUTO_ROCKET).method03017(true).method03023(this);
      this.players = new ToggleSetting(PLAYERS).method03017(true).method03023(this);
      this.mobs = new ToggleSetting(MOBS).method03017(false).method03023(this);
      this.method03935(NAME);
      this.method03931("Elytra + sword in hand: hits normally with the sword and silently fires a firework rocket from the hotbar without switching away from the sword.");
   }

   @Override
   public void method00276() {
      super.method00276();
      this.lastRocketTick = -1000;
   }

   @Subscribe
   private void onTick(TickEvent event) {
      if (mc.player == null || mc.level == null || mc.gameMode == null || mc.player.connection == null) {
         return;
      }

      if (!mc.player.isFallFlying()) {
         return;
      }

      ItemStack held = mc.player.getMainHandItem();
      if (!held.is(ItemTags.SWORDS)) {
         return;
      }

      int now = mc.player.tickCount;
      LivingEntity target = this.findTarget();

      if (target != null && this.aim(target) && mc.player.getAttackStrengthScale(0.5F) >= 0.9F) {
         mc.gameMode.attack(mc.player, target);
         mc.player.swing(InteractionHand.MAIN_HAND);
      }

      this.useRocketIfNeeded(now);
      this.applySpeed(now);
   }

   /** Silently swaps sword -> rocket -> use item -> back to sword, without ever showing the rocket in hand. */
   private void useRocketIfNeeded(int now) {
      if (!this.autoRocket.method05999()) {
         return;
      }

      if (now - this.lastRocketTick < this.rocketDelay.method03022()) {
         return;
      }

      int rocket = this.findRocketSlot();
      if (rocket < 0) {
         return;
      }

      int original = mc.player.getInventory().getSelectedSlot();
      mc.player.connection.send(new ServerboundSetCarriedItemPacket(rocket));
      mc.gameMode.useItem(mc.player, InteractionHand.MAIN_HAND);
      mc.player.connection.send(new ServerboundSetCarriedItemPacket(original));
      this.lastRocketTick = now;
   }

   private int findRocketSlot() {
      for (int i = 0; i < 9; i++) {
         ItemStack stack = mc.player.getInventory().getItem(i);
         if (!stack.isEmpty() && stack.getItem() == Items.FIREWORK_ROCKET) {
            return i;
         }
      }

      return -1;
   }

   private LivingEntity findTarget() {
      LivingEntity aura = AttackAura.field00785;
      float maxRange = this.range.method03022();
      if (aura != null && aura.isAlive() && mc.player.distanceTo(aura) <= maxRange) {
         return aura;
      }

      LivingEntity best = null;
      double bestDist = Double.MAX_VALUE;
      for (Entity entity : mc.level.entitiesForRendering()) {
         if (!(entity instanceof LivingEntity living) || entity == mc.player || !living.isAlive()) {
            continue;
         }

         boolean isPlayer = living instanceof Player;
         if (isPlayer ? !this.players.method05999() : !this.mobs.method05999()) {
            continue;
         }

         double dist = mc.player.distanceTo(living);
         if (dist <= maxRange && dist < bestDist) {
            best = living;
            bestDist = dist;
         }
      }

      return best;
   }

   /** Rotates like the aura does (per-mode speed / jitter). Returns true when the aim is good enough to hit. */
   private boolean aim(LivingEntity target) {
      Vector2f rot = PlayerUtils.method11816(mc.player.getEyePosition(), target.getEyePosition());
      float yaw = rot.x;
      float pitch = rot.y;
      float step;
      float jitter = 0.0F;
      boolean legit = false;

      if (this.mode.method05600(MATRIX_SNAP) || this.mode.method05600(UNIVERSAL)) {
         step = 9999.0F;
      } else if (this.mode.method05600(LEGIT)) {
         step = 28.0F;
         jitter = 1.1F;
         legit = true;
      } else if (this.mode.method05600(SMOOTH)) {
         step = 45.0F;
         jitter = 0.4F;
      } else if (this.mode.method05600(GRIM)) {
         step = 60.0F;
      } else {
         step = 35.0F;
         jitter = 0.8F;
      }

      if (jitter > 0.0F) {
         yaw += (float)((Math.random() - 0.5) * 2.0 * jitter);
         pitch += (float)((Math.random() - 0.5) * 2.0 * jitter * 0.6);
      }

      return RotationManager.method06547(new Rotation(yaw, pitch), step, 100, legit);
   }

   /** Raises the speed up to the chosen value, along the look direction (never slows the player down). */
   private void applySpeed(int now) {
      if (now - this.lastRocketTick > 40) {
         return;
      }

      Vec3 motion = mc.player.getDeltaMovement();
      double current = motion.length();
      double goal = this.speed.method03022();
      double next = this.smoothSpeed.method05999() ? Math.min(goal, current + 0.06) : goal;
      if (next > current) {
         mc.player.setDeltaMovement(mc.player.getLookAngle().scale(next));
      }
   }
}
