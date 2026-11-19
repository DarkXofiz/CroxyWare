# QuickMobile V5 - Minecraft 26.2 port
Base: QuickClient 26.2 sources (same code base), renamed to QuickMobile (mod id quickmobile, class sg.ec.QuickMobile).
Re-applied: Android fixes (MobileCompat, ONNX guarded with Throwable, Windows-only media/notification skipped on mobile,
Vulkan mixins skipped on mobile), Miran-style animated QuickMobile logo + ring on the main menu, vanilla button fallback
menu if fonts/pipeline fail (RenderGuard). 26.3-only parts (SDL input compat, renderpearl hooks) were NOT carried over.
Workflow uploads artifact QuickMobile-LOGS (always) and QuickMobile jar (on success).
Not compiled locally - build in GitHub Actions and send QuickMobile-LOGS if it fails.

# V6
Log from the phone (Krypton Wrapper, GLES 3.2) showed: hex:core/prediction_line uses `noperspective` +
GL_NV_shader_noperspective_interpolation, which the driver rejects -> 5 required pipelines fail ->
Minecraft aborts the resource reload ("Failed to load required shader programs"), resets packs and the later
hex pipelines (gui_blur, mainmenu_blit, ...) lose their sources -> black menu.
Fix: on mobile the decrypted shader source is rewritten (extension line + `noperspective` removed) in
EncryptedTextureUtils.patchGlslForMobile.

# V7
UI text translated from Russian to English (module/setting names, screens, command output, notifications, main menu).
Deliberately NOT translated (they match Russian server chat / item names / scoreboard text, translating would break them):
KillMessage, EnchantmentIds, PotionItemData, CustomEnchantmentAliases, ServerHelper, ServerJoiner, Searcher,
ScoreboardTextFormatter, AuctionChatParser/ItemText, AutoBuyItemMatcher, Cyrillic letter map in BetterChat, staff keywords,
Baritone command help texts, internal debug logs.

# V8
- Display name is now "Quick Mobile" (fabric.mod.json name, main menu, logo image, watermark, chat prefix). Mod id/package stay quickmobile.
- Accounts button: when the IAS mod is not installed it opens a built-in offline account screen (Hex/accounts.txt).
- Configs: saving works without the Globals server. Configs panel Create/Save/Activate and `.config save <name>` now
  write/read Hex/custom/<name>.file locally (the cloud list in the panel stays empty; use `.config list/load`).

# V9
Configs panel (ClickGUI > Configs) now lists, creates, saves, activates and deletes configs from Hex/custom/*.file when the
Globals server is not connected (before: empty list, nothing saved).

# V11 - everything renamed to Quick Mobile
Mod id quickmobile, display name "Quick Mobile", main class sg.ec.QuickMobile, mixin/accesswidener files quickmobile.*,
assets/quickmobile, group dev.quickmobile, jar quickmobile-1.0.0.jar, workflow artifacts QuickMobile / QuickMobile-LOGS.
Kept on purpose: the "Hex" config folder and the internal "hex" resource namespace (encrypted shaders/models depend on it).

# V12
Configs now live in a visible folder: /storage/emulated/0/QuickMobile/configs (Android shared storage; falls back to
<game dir>/QuickMobile/configs if it is not writable). Old Hex/custom configs are copied there once. Drop a <name>.file in
that folder and it shows up in ClickGUI > Configs immediately (list refreshes every frame); click to load.
Saving: always writes locally when Globals is offline, writes last_save_status.txt next to the configs (OK/FAILED + stack trace),
and `.config save <name>` prints the folder or the failure reason in chat.

# V13
Config files are now <name>.cfg (e.g. test.cfg) in /storage/emulated/0/QuickMobile/configs. Old *.file configs (also from Hex/custom)
are renamed/copied to .cfg automatically on start; the .file extension is still readable. Saving, listing, loading, deleting
(`.config save/load/list/remove` and the ClickGUI Configs panel) all use .cfg.

# V14 - FireworkSet (Combat)
New module FireworkSet (Combat > Combat). Conditions: elytra gliding + firework rocket in the main hand.
- Fires the rocket (Auto firework, Rocket delay) and hits the target with a hotbar sword via silent slot swap
  (rocket stays selected on the client; slot packets: sword -> attack -> back).
- Flight speed (0.5-4.0, default 1.6) with optional smooth acceleration (anticheat friendly).
- Rotation mode like the aura: Grim, Matrix, MatrixSnap, Legit, Smooth, Universal (approximation of the aura modes using RotationManager).
- Target: aura target if present, otherwise nearest player/mob within Hit range. Hit delay is configurable (sword cooldown).
Not compiled/tested locally.

# V15
- FireworkSet: condition changed from "rocket in hand" to "sword in hand". Now hits normally with the sword (no
  slot swap for the hit) and silently swaps sword->rocket->use->sword to keep firing fireworks. Removed the
  Hit delay setting (relies on vanilla attack-strength gate + AttackAura for hit timing, per request). Rocket
  delay is now 0-300 (0 = as fast as possible), default 20.
- New module ShowHitbox (Combat): while gliding, draws the hitbox of the nearest target ahead (within Search range)
  and, once within Pass distance (configurable), turns to face it and swings once (attack-strength gated) -
  a "fly past and hit behind" move. Settings: Pass distance, Search range, Players, Mobs, Hitbox color.
Not compiled/tested locally - this is a from-scratch module, review closely before using on a real server.

# V16 - new "Extra" ClickGUI category
Ported from NursultanElytraTarget1_21_11.zip (the 1.16.5 zip had no AttackAura class at all, so that part was dropped
per the user's confirmation):
- ElytraChase (renamed from "ElytraTarget" — this client already has its own, unrelated ElytraTarget module/archery-ESP
  with that name, so the port got a new name to avoid a class/name collision): Detection range, Firework mode
  (Off/Manual/Ticks/Auto), Ticks delay, Outrun, Overshoot, Manual key. Uses AttackAura's current target
  (AttackAura.field00785) read-only; AttackAura.java itself was NOT modified. Outrun/Overshoot do not redirect
  AttackAura's aim (that would require editing AttackAura); instead they trigger an extra firework burst whenever
  the player starts falling behind the target's overshoot-projected path. Firework use is a silent hotbar/offhand
  swap, same pattern as FireworkSet.
- SuperFirework: Power setting (1.5-2.0, default 1.6). Instead of a mixin on the firework rocket entity (the
  original's approach), it detects the one-tick velocity jump any firework burst causes while gliding and
  amplifies just that jump by Power — works for a firework fired by any module or by hand.
- ElytraTargetHelper was not ported as a module: it has no settings of its own in the original either, it's just
  AttackAura's internal rotation bridge; its only two real behaviors (effective range, overshoot-shifted aim) are
  folded into ElytraChase's own logic above instead of being wired into AttackAura.
- ElytraHelper from the zip was NOT ported: this client already has its own, more complete "Elytra Helper" module
  (Auto Takeoff w/ Legit/Rage modes, Swap key, Firework key, Legit swap/use, levitation takeoff) — porting the
  zip's simpler version would have meant a duplicate, conflicting module.
- New ModuleCategory.Extra ("E" icon code, Main/Other subcategories) added so these show in their own ClickGUI tab.
Not compiled/tested locally — from-scratch modules, review closely before relying on them.

# V17 - build fix
V16 failed to compile: ModuleArrayListHud.java has an exhaustive switch EXPRESSION over ModuleCategory that didn't
cover the new Extra case. Added Extra to its "hidden from the module list HUD" branch (same as Configs/Themes/
Friends/Bots/AutoBuy). Checked the rest of the project for other exhaustive switches on ModuleCategory - none found
(ClickGuiScreen's switch on it is a plain statement with no default requirement, left untouched).
