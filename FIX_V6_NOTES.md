# V6
Log: `Couldn't find source for FRAGMENT shader (hex:post/mainmenu_blit)` (after hand_copy was fixed in V5).
All hex shaders failed to resolve on mobile, so:
- 57 hex shaders/configs are now also shipped as plain files in assets/hex/shaders/** (decrypted copies of the encrypted ones).
- MainMenuBlitRenderer is skipped on mobile (vanilla menu is used there anyway).
- CroxyWare writes `mobile=... encryptedIndexEntries=N` to croxyware_render.log at startup (N=0 means the encrypted asset index failed to load).
