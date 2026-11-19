package sg.ec;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import java.awt.Color;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;

/**
 * ShowHitbox (Combat): while flying with the elytra, draws the hitbox of the nearest target ahead and, once the
 * player is about to pass it (distance to it drops to the configured value), turns to face it and swings — a
 * "fly past and backstab" hit while still airborne.
 */
public class ShowHitbox extends ClientModule {
   private static final String PASS_DISTANCE = "Pass distance";
   private static final String RANGE = "Search range";
   private static final String PLAYERS = "Players";
   private static final String MOBS = "Mobs";
   private static final String BOX_COLOR = "Hitbox color";

   private final NumberSetting<Float> passDistance;
   private final NumberSetting<Float> range;
   private final ToggleSetting players;
   private final ToggleSetting mobs;
   private final ColorSetting boxColor;

   private LivingEntity tracked;
   private boolean handledThisPass;

   public ShowHitbox() {
      super(ModuleCategory.Combat, ModuleSubCategory.Combat);
      this.passDistance = new NumberSetting<Float>(PASS_DISTANCE)
         .method10835(0.5F)
         .method10838(10.0F)
         .method10834(0.1F)
         .method03017(2.5F)
         .method03023(this);
      this.range = new NumberSetting<Float>(RANGE)
         .method10835(3.0F)
         .method10838(30.0F)
         .method10834(0.5F)
         .method03017(12.0F)
         .method03023(this);
      this.players = new ToggleSetting(PLAYERS).method03017(true).method03023(this);
      this.mobs = new ToggleSetting(MOBS).method03017(false).method03023(this);
      this.boxColor = new ColorSetting(BOX_COLOR).method03017(new Color(255, 70, 70)).method03023(this);
      this.method03935("ShowHitbox");
      this.method03931("While gliding, shows the nearest target's hitbox ahead. Passing within the set distance turns the player to face it and hits.");
   }

   @Override
   public void method00276() {
      super.method00276();
      this.tracked = null;
      this.handledThisPass = false;
   }

   @Subscribe
   private void onTick(TickEvent event) {
      if (mc.player == null || mc.level == null || !mc.player.isFallFlying()) {
         this.tracked = null;
         return;
      }

      LivingEntity target = this.findAheadTarget();
      if (target != this.tracked) {
         this.handledThisPass = false;
      }

      this.tracked = target;
      if (target == null) {
         return;
      }

      double distance = mc.player.distanceTo(target);
      if (distance <= this.passDistance.method03022() && !this.handledThisPass) {
         this.handledThisPass = true;
         this.turnAndHit(target);
      }
   }

   @Subscribe
   private void onRender3D(Render3DEvent event) {
      if (this.tracked == null || !this.tracked.isAlive()) {
         return;
      }

      AABB box = this.tracked.getBoundingBox();
      int color = ColorUtils.method04616(this.boxColor.method03022().getRGB(), 180);
      WorldBoxRenderer.method06958(box, event.method00996(), event.method00994(), event.method00986(), event.method00985(), color);
   }

   private LivingEntity findAheadTarget() {
      Vec3 from = mc.player.getEyePosition();
      Vec3 dir = mc.player.getDeltaMovement();
      if (dir.lengthSqr() < 1.0E-4) {
         dir = mc.player.getLookAngle();
      } else {
         dir = dir.normalize();
      }

      float maxRange = this.range.method03022();
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

         Vec3 to = living.position().subtract(from);
         double dist = to.length();
         if (dist > maxRange) {
            continue;
         }

         if (to.normalize().dot(dir) <= 0.3) {
            continue;
         }

         if (dist < bestDist) {
            best = living;
            bestDist = dist;
         }
      }

      return best;
   }

   private void turnAndHit(LivingEntity target) {
      Vector2f rot = PlayerUtils.method11816(mc.player.getEyePosition(), target.getEyePosition());
      RotationManager.method06547(new Rotation(rot.x, rot.y), 9999.0F, 100, false);
      if (mc.player.getAttackStrengthScale(0.5F) >= 0.9F) {
         mc.gameMode.attack(mc.player, target);
         mc.player.swing(net.minecraft.world.InteractionHand.MAIN_HAND);
      }
   }
}
