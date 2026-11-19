package sg.ec;

import net.minecraft.world.phys.Vec3;

/**
 * SuperFirework (ported from Nursultan's movement module of the same name). The original overrode the
 * firework rocket entity's own boost-power constant via a mixin; to avoid touching the firework entity
 * (and risking vanilla elytra physics elsewhere), this version detects the velocity jump a firework burst
 * causes on its own and amplifies just that jump by the chosen power — any firework use while gliding
 * (this client's or a plain vanilla right-click) gets boosted, with no entity hook needed.
 */
public class SuperFirework extends ClientModule {
   private static final String POWER = "Power";

   private final NumberSetting<Float> power;
   private double lastSpeed = -1.0;

   public SuperFirework() {
      super(ModuleCategory.Extra, ModuleSubCategory.Main);
      this.power = new NumberSetting<Float>(POWER)
         .method10835(1.5F)
         .method10838(2.0F)
         .method10834(0.01F)
         .method03017(1.6F)
         .method03023(this);
      this.method03935("SuperFirework");
      this.method03931("Amplifies the speed boost from any firework used while gliding by the chosen power.");
   }

   @Override
   public void method00276() {
      super.method00276();
      this.lastSpeed = -1.0;
   }

   @Subscribe
   private void onTick(TickEvent event) {
      if (mc.player == null || !mc.player.isFallFlying()) {
         this.lastSpeed = -1.0;
         return;
      }

      Vec3 motion = mc.player.getDeltaMovement();
      double speed = motion.length();
      if (this.lastSpeed >= 0.0) {
         double jump = speed - this.lastSpeed;
         // A real firework burst gives a clearly visible one-tick speed jump; small drift from normal
         // gliding/turning stays untouched so this never fights the vanilla flight model.
         if (jump > 0.15 && speed > 1.0E-4) {
            double boosted = this.lastSpeed + jump * this.power.method03022();
            mc.player.setDeltaMovement(motion.scale(boosted / speed));
            speed = boosted;
         }
      }

      this.lastSpeed = speed;
   }
}
