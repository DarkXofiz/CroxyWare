// Original binary: sg/mx/78AlU39yDYYSGZIR
package sg.mx;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
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
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.ec.HeldItemRenderEvent;
import sg.ec.SwingTransformEvent;
import sg.ec.HandGlowRenderer;
import sg.ec.ViewModelOffsetEvent;
import sg.ec.ItemInHandRendererHooks;
import sg.ec.HeldItemRenderHooks;

// $VF: Compiled from s
@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
   @Shadow
   @Final
   private Minecraft minecraft;
   @Shadow
   private ItemStack mainHandItem;
   @Shadow
   private ItemStack offHandItem;

   @Shadow
   protected abstract void applyItemArmAttackTransform(PoseStack var1, HumanoidArm var2, float var3);

   @ModifyExpressionValue(method = "submitHandsWithItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getViewXRot(F)F"))
   private float renderItemPitch(float var1) {
      return HeldItemRenderHooks.method05664(this.minecraft, var1);
   }

   @ModifyExpressionValue(method = "submitHandsWithItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getViewYRot(F)F"))
   private float renderItemYaw(float var1) {
      return HeldItemRenderHooks.method05661(this.minecraft, var1);
   }

   @Inject(
      method = "submitArmWithItem",
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", shift = Shift.AFTER, ordinal = 0)
   )
   private void renderFirst(
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
      ViewModelOffsetEvent.field09348.method04141(var1, var4, var8);
   }

   @ModifyVariable(method = "submitArmWithItem", at = @At("HEAD"), argsOnly = true, ordinal = 0)
   private ItemStack renderFirstPersonItem(ItemStack var1, AbstractClientPlayer var2, float var3, float var4, InteractionHand var5) {
      return ItemInHandRendererHooks.field11293 ? var1 : HeldItemRenderHooks.method05665(var5, this.minecraft, var1);
   }

   @ModifyExpressionValue(
      method = "submitArmWithItem",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;isInvisible()Z")
   )
   private boolean renderInvisibleArm(boolean var1) {
      return var1 && !HeldItemRenderHooks.method05662();
   }

   @ModifyExpressionValue(
      method = {"renderOneHandedMap", "renderTwoHandedMap"},
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isInvisible()Z")
   )
   private boolean renderInvisibleMapArms(boolean var1) {
      return var1 && !HeldItemRenderHooks.method05662();
   }

   @ModifyExpressionValue(
      method = "tick",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getMainHandItem()Lnet/minecraft/world/item/ItemStack;")
   )
   private ItemStack updateHeldItemsMainHand(ItemStack var1) {
      return HeldItemRenderEvent.field00682.method00316(this.minecraft.player, var1);
   }

   @Redirect(method = "swingArm", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"))
   private void swingArmTranslate(PoseStack var1, float var2, float var3, float var4) {
      SwingTransformEvent.field01060.method00536(var1, var2, var3, var4);
   }

   @Redirect(
      method = "swingArm",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;applyItemArmAttackTransform(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/HumanoidArm;F)V"
      )
   )
   private void swingArm(ItemInHandRenderer var1, PoseStack var2, HumanoidArm var3, float var4) {
      SwingTransformEvent.field01060.method00534(var2, var3, var4, () -> this.applyItemArmAttackTransform(var2, var3, var4));
   }

   @Redirect(method = "applyItemArmTransform", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"))
   private void applyEquipOffset(PoseStack var1, float var2, float var3, float var4, @Local(ordinal = 0) int var5, @Local(argsOnly = true) float var6) {
      SwingTransformEvent.field01060.method00535(var1, var2, var3, var4, var5, var6);
   }

   @ModifyArg(
      method = "renderItem",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V"
      ),
      index = 4
   )
   private int hexHandGlowColor(int var1) {
      return HandGlowRenderer.method01239(var1);
   }

   @Inject(method = "submitHandsWithItems", at = @At("HEAD"))
   private void hexHandGlowSnapshot(float var1, PoseStack var2, SubmitNodeCollector var3, LocalPlayer var4, int var5, CallbackInfo var6) {
      HeldItemRenderHooks.method05667(this.mainHandItem, this.offHandItem);
   }
}
