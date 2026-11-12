# V5 - fix: black screen after Mojang logo on Zalith/Pojav (26.3, OpenGL via MobileGlues)

Log: `[Worker-Main-5/ERROR]: Couldn't find source for FRAGMENT shader (hex:post/hand_copy)`

Cause: on the OpenGL backend three custom render paths build a pipeline that uses `hex:post/hand_copy`
(SceneSwapRenderer, HudBlitRenderer, HandGlowRenderer). They are only reached on OpenGL (desktop uses Vulkan),
and the async pipeline compile for them fails to resolve the encrypted hex shader -> loading overlay never finishes.

Fix (mobile only, desktop unchanged via MobileCompat gate):
- SceneSwapRenderer: on mobile use the plain copyTextureToTexture fallback (no hand_copy pipeline).
- HudBlitRenderer: HUD throttle blit disabled on mobile.
- HandGlowRenderer: hand glow copy pipeline disabled on mobile.

If a different hex:post/<name> shader shows up in the log next, send the new log line.
