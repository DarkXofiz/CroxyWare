// Original binary: sg/mx/q1Myiav4zrmgnih2
package sg.mx;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.ec.ItemAnimationState;
import sg.ec.PlayerArmRenderAccess;
import sg.ec.ItemInHandRendererHooks;
import sg.ec.HeldItemRenderHooks;

// $VF: Compiled from s
@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin_1296 implements PlayerArmRenderAccess {
   @Shadow
   @Final
   private Minecraft minecraft;

   @Shadow
   protected abstract void renderPlayerArm(PoseStack var1, SubmitNodeCollector var2, int var3, float var4, float var5, HumanoidArm var6);

   @Shadow
   protected abstract void renderMap(PoseStack var1, SubmitNodeCollector var2, int var3, ItemStack var4);

   @Override
   public void hmi$renderPlayerArm(PoseStack var1, SubmitNodeCollector var2, int var3, float var4, float var5, HumanoidArm var6) {
      this.renderPlayerArm(var1, var2, var3, var4, var5, var6);
   }

   @Override
   public void hmi$renderMap(PoseStack var1, SubmitNodeCollector var2, int var3, ItemStack var4) {
      this.renderMap(var1, var2, var3, var4);
   }

   @Inject(method = "submitHandsWithItems", at = @At("HEAD"))
   private void hmi$beginFrame(float var1, PoseStack var2, SubmitNodeCollector var3, LocalPlayer var4, int var5, CallbackInfo var6) {
      if (ItemInHandRendererHooks.field11293) {
         ItemAnimationState.INSTANCE.method03498();
      }
   }

   @Inject(method = "submitArmWithItem", at = @At("HEAD"), cancellable = true)
   private void hmi$overhaul(
      AbstractClientPlayer var1,
      float var2,
      float var3,
      InteractionHand var4,
      float var5,
      ItemStack var6,
      float var7,
      PoseStack var8,
      SubmitNodeCollector var9,
      int var10,
      CallbackInfo var11
   ) {
      if (ItemInHandRendererHooks.field11293) {
         ItemStack var12 = HeldItemRenderHooks.method05665(var4, this.minecraft, var6);
         ItemInHandRendererHooks.method05209((ItemInHandRenderer)(Object)this, var1, var2, var3, var4, var5, var12, var7, var8, var9, var10);
         var11.cancel();
      }
   }
}
