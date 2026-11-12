# CroxyWare V2 (this zip)

1. Crash fix (Mojang screen -> crash after ~5s on Zalith):
   ONNX Runtime native lib cannot load on Android (libdl.so.2 missing). RotationModelRepository only caught
   Exception, so UnsatisfiedLinkError killed the game from AttackAura.<init>. Now: skipped on mobile and
   all ONNX init is wrapped in catch (Throwable) with an "unavailable" flag.
2. Main menu: new Miran-style glowing blue->cyan "CroxyWare" logo (croxy_logo.png) + animated faint ring
   (croxy_ring.png) behind the panel; logo fades in, breathes and bobs.

# V2
Black main menu on Android: the client's custom GUI pipelines (RoundedRect/GuiRenderBatch/fonts) draw nothing on
MobileGlues. MainMenuScreen now uses a vanilla-widget + vanilla-blit menu (same Miran-style logo and animated ring)
when MobileCompat.isMobile(), fonts are not ready, or the custom path failed. Desktop keeps the custom menu.
Errors are written to croxyware_render.log in the game folder.

# V3 - target Minecraft 26.2
gradle.properties: minecraft_version=26.2, fabric_api_version=0.161.0+26.2 (loom/gradle/loader unchanged).
fabric.mod.json: depends minecraft ~26.2-.
Reason: 26.3 render stack does not work with MobileGlues on Mali-G57; 26.2 starts on the device.
If the build fails with "could not find fabric-api:0.161.0+26.2", pick an existing 26.2 version from
https://fabricmc.net/develop and put it in gradle.properties. Code was NOT compiled against 26.2 yet.

# V4 - back to Minecraft 26.3 (26.2 port is not feasible: renderpearl API does not exist there)
Workflow now always uploads artifact "CroxyWare-LOGS" (full build log + first 500 errors + error count),
and "CroxyWare" (jar) only on success.
