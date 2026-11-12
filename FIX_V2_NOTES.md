# CroxyWare Fix V2 (mobile)

Fixes: "No supported graphics backend was found - Tried OpenGL: glGetError mismatch" on Zalith/Pojav.

Added
- src/main/java/sg/ec/GlCompat.java
- src/main/java/sg/mx/GlBackendMixin.java (registered in croxyware.mixins.json "client")

Not changed: everything else from the previous build. Desktop behaviour is identical (MobileCompat gate).
Vulkan on this device is rejected by the game itself ("Device missing capabilities"), so OpenGL is the path.
