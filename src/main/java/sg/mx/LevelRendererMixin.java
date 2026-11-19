// Original binary: sg/mx/MwmyboJtUstIjKmH
package sg.mx;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.resource.ResourceHandle;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher.PreparedFrame;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sg.ec.WorldColorEvent;
import sg.ec.LevelRendererAccess;
import sg.ec.TargetScanRenderer;
import sg.ec.LevelRendererHooks;

// $VF: Compiled from s
@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin implements LevelRendererAccess {
   @Unique
   private Frustum frustum;
   @Unique
   private Matrix4f hexScratchMatrix;
   @Shadow
   @Final
   private SubmitNodeStorage submitNodeStorage;
   @Shadow
   @Final
   private LevelTargetBundle targets;

   @Unique
   private Matrix4f hexScratchMatrix() {
      Matrix4f var1 = this.hexScratchMatrix;
      if (var1 == null) {
         var1 = new Matrix4f();
         this.hexScratchMatrix = var1;
      }

      return var1;
   }

   @Override
   public Frustum frustum() {
      return this.frustum;
   }

   @ModifyExpressionValue(
      method = "lambda$addSkyPass$0",
      at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/state/level/SkyRenderState;skyColor:I")
   )
   private static int lambda$addSkyPass$0(int var0) {
      return WorldColorEvent.field02679.method01252(WorldColorEvent.Nested37.SKY, var0);
   }

   @ModifyArg(
      method = "lambda$addCloudsPass$0",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/CloudRenderer;render(ILnet/minecraft/client/CloudStatus;FILnet/minecraft/world/phys/Vec3;JF)V"
      ),
      index = 0
   )
   private int lambda$addCloudsPass$0(int var1) {
      return WorldColorEvent.field02679.method01252(WorldColorEvent.Nested37.CLOUD, var1);
   }

   @Inject(
      method = "render",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/feature/FeatureRenderDispatcher;prepareFrame(Lnet/minecraft/client/renderer/SubmitNodeStorage;)Lnet/minecraft/client/renderer/feature/FeatureRenderDispatcher$PreparedFrame;"
      )
   )
   private void render(
      GraphicsResourceAllocator var1,
      DeltaTracker var2,
      boolean var3,
      CameraRenderState var4,
      Matrix4fc var5,
      GpuBufferSlice var6,
      Vector4f var7,
      boolean var8,
      CallbackInfo var9
   ) {
      this.frustum = var4.cullFrustum;
      TargetScanRenderer.method05986(var4.projectionMatrix);
      LevelRendererHooks.method08951(this.submitNodeStorage, var2, var5, this.hexScratchMatrix(), this.frustum);
   }

   @Redirect(
      method = "doEntityOutline",
      at = @At(
         value = "INVOKE",
         target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;blitAndBlendToTexture(Lcom/mojang/blaze3d/textures/GpuTextureView;Lcom/mojang/blaze3d/textures/GpuTextureView;)V"
      )
   )
   private void doEntityOutline(RenderTarget var1, GpuTextureView var2, GpuTextureView var3) {
      LevelRendererHooks.method08953(var1, var2, var3);
   }

   @Inject(method = "entityOutlineTarget", at = @At("HEAD"), cancellable = true)
   private void entityOutlineTarget(CallbackInfoReturnable<RenderTarget> var1) {
      LevelRendererHooks.method08952(var1);
   }

   @Override
   public RenderTarget hexRawOutlineTarget() {
      return this.targets != null && this.targets.entityOutline != null ? this.targets.entityOutline.get() : null;
   }

   @ModifyExpressionValue(
      method = "addAlwaysOnTopPass",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/FeatureRenderDispatcher$PreparedFrame;hasAnyAlwaysOnTop()Z")
   )
   private boolean hexForceAlwaysOnTopPass(boolean var1, @Local(argsOnly = true) PreparedFrame var2) {
      return LevelRendererHooks.method08955(var1, var2);
   }

   @Inject(
      method = "lambda$addAlwaysOnTopPass$0",
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/CommandEncoder;clearDepthTexture(Lcom/mojang/blaze3d/textures/GpuTexture;D)V")
   )
   private void hexRenderBeforeClear(GpuBufferSlice var1, ResourceHandle<?> var2, PreparedFrame var3, CallbackInfo var4) {
      LevelRendererHooks.method08954(var3);
   }
}
