# V8 - mobile SAFE MODE (black screen with mod, vanilla fine)
On Zalith/Pojav (MobileCompat.isMobile()) the mixin plugin (CroxyWare$Nested22) now skips every mixin that touches
rendering, resources, textures, fonts, GUI, screens, sound, packs, camera, level rendering (see MobileCompat.SAFE_MODE_BLOCKED).
Only gameplay/network/entity/player hooks stay. The game then loads like vanilla.
- Client init is wrapped in try/catch on mobile; the custom ClickGUI is not created in safe mode.
- Turn safe mode off for testing: JVM arg -Dcroxyware.safe=false
- Force mobile / desktop: -Dcroxyware.mobile=true / -Dcroxyware.desktop=true
Desktop is unchanged. Logs: croxyware_render.log
- v8b: also detects DroidBridge launcher (-Ddroidbridge.path.minecraft / glfwstub.* props, krypton).
- v8c: prints '[CroxyWare] mixin plugin onLoad ... mobile= safeMode=' and '[CroxyWare] game started ... skippedMixins=' to latest.log.
- v8d: STAGED LEVELS to find what breaks the menu (mobile default = 2):
  0 = inert (no mixins, no init, no script mixins)   1 = safe mixins only   2 = safe mixins + client init   3 = everything (desktop default)
  Set via jar name (croxyware-L0.jar ... -L3.jar), file croxyware_level.txt in game folder, or -Dcroxyware.level=N.
  First line of croxyware_render.log shows "[CroxyWare] mobile=.. level=..".
