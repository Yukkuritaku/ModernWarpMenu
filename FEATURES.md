# Features Added

## Visual/Layout Changes
- Added a new **Rift NPC portal** entry to the overworld fast-travel layout.
- Added the same **Rift NPC portal** entry to the ultrawide resource-pack layout.
- Added and integrated texture asset:
  - `src/main/resources/assets/modernwarpmenu/textures/gui/islands/rift_npc.png`

## Layout Editor Tool
- Added a standalone visual editor:
  - `tools/layout-editor.html`
- Supports:
  - Dragging islands on the 64x36 screen grid.
  - Dragging warp portals on island-local grid.
  - Undo/Redo.
  - Editor mode vs Preview mode.
  - Runtime-like hover simulation in Preview.
  - Desktop resolution presets and manual resize.
  - Preset-aware layout source switching (default vs ultrawide) with confirmation prompts.
  - Unsaved-change discard/cancel prompt when switching between default and ultrawide layout sources.
  - Zoom controls (including Ctrl+wheel pointer-focused zoom).
  - Canvas-based grid rendering for more stable behavior while resizing.
  - Aspect-ratio guardrails during resize:
    - Ultrawide mode is clamped around 21:9 with 10% tolerance.
    - Non-ultrawide mode is prevented from stretching into ultrawide ratios.
  - Reserved settings-button area to prevent accidental overlap.
  - Load from repo `layout.json`.
  - Save to repo `layout.json` with destructive overwrite confirmation and active-layout target routing.
  - JSON download/export as a separate action.

## Local Save Server
- Added local editor server:
  - `tools/layout-editor-server.ps1`
  - `tools/layout-editor-server.py`
  - `tools/start-layout-editor.sh`
- Provides:
  - Static hosting for the editor (default: `http://localhost:8756/tools/layout-editor.html`).
  - Direct overwrite endpoint `POST /api/save-layout` for:
    - `src/main/generated/assets/modernwarpmenu/layouts/layout.json`
    - `src/main/resources/resourcepacks/ultra_wide_layout/assets/modernwarpmenu/layouts/layout.json`

## Quick Setup (User)
- Linux (Arch):
  - `chmod +x ./tools/start-layout-editor.sh`
  - `./tools/start-layout-editor.sh`
- Windows (PowerShell):
  - `.\tools\layout-editor-server.ps1`
- Open in browser:
  - Use the exact URL printed by the server in terminal output.
  - If no custom port was provided, use `http://localhost:8756/tools/layout-editor.html`.
- Save actions:
  - `Save To Repo layout.json`: overwrites the active layout source file directly (default or ultrawide, with confirmation).
  - `Download JSON`: downloads an exported copy.
- If PowerShell scripts are blocked on Windows, run once:
  - `Set-ExecutionPolicy -Scope CurrentUser RemoteSigned`

## Cleanup
- Removed old preview guide (`PREVIEW_GUIDE.md`).
