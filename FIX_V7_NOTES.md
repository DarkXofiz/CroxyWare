# V7 - black screen after Mojang logo (mobile)
Cause: client registers its own pipelines via RenderPipelines.register(...) (static initialisers in sg.ec). The game precompiles
every registered pipeline during resource loading; one unresolved hex shader keeps the loading overlay on a black screen.
- MobilePipelineRegisterMixin: on mobile, pipelines registered directly by sg.ec classes are not added to the precompile list.
- Mobile: custom main menu is not used at all (vanilla TitleScreen, no animation). TitleScreenHooks.method11006() returns the vanilla screen.
- Mobile: custom blur/glow/ESP/scan/hand renderers return early (GuiBlur, WorldVisual, BlockEsp, JumpCircle, HandGlow, GuiGlow, TargetScan, EntityGlow).
Desktop unchanged. If still black: send logs/latestlog.txt and croxyware_render.log.
