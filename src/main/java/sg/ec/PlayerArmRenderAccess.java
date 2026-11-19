// Original binary: sg/ec/JVDl6vJl34TN2D0Q
package sg.ec;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

// $VF: Compiled from s
public interface PlayerArmRenderAccess {
   void hmi$renderPlayerArm(PoseStack var1, SubmitNodeCollector var2, int var3, float var4, float var5, HumanoidArm var6);

   void hmi$renderMap(PoseStack var1, SubmitNodeCollector var2, int var3, ItemStack var4);
}
