
# FitQuest — frontend build plan

A full-frontend implementation of FitQuest using mock data and Zustand. No backend yet. Lovable Cloud can be added in a later pass to persist data and add auth.

## Scope of this build

- All routes from the spec, fully navigable, with the design system fully expressed
- Onboarding, dashboard, workout logger (active session + post-workout), nutrition hub, quest board, inventory, profile
- Pixel-art character sprites generated for all class × gender variants and a starter set of gear icons
- Curated seed data: ~30 exercises and ~50 foods bundled as JSON
- All XP / stat / macro logic runs client-side from Zustand stores; persisted to `localStorage`

Out of scope for this pass: auth, multi-device sync, friends/party real data, sound effects, server-side stat recalculation.

## Design system

Implemented in `src/styles.css` as semantic tokens (oklch). Token names map 1:1 to Tailwind utilities.

- Background, surface, elevated (deep purple-black palette)
- Accent gold (XP, highlights), HP green, MP blue
- Macro colors (protein red, fat yellow, carbs blue)
- Stat colors (STR orange, STA cyan, VIT green, AGI purple)
- Rarity colors (common/rare/epic/legend) for loot borders
- Gradients: `--gradient-xp`, `--gradient-mesh-bg`
- Shadows: `--shadow-gold-glow`, `--shadow-rarity-legend` (animated pulse)
- SVG noise filter overlay for parchment grain

Fonts loaded via Google Fonts in `__root.tsx` head: Press Start 2P (display), Cinzel (body), Share Tech Mono (numbers). Mapped to Tailwind via theme tokens.

Theme: dark fantasy by default; light parchment toggle wired in profile settings.

## Routes (TanStack Start file-based)

```
src/routes/
  __root.tsx              shell, fonts, providers, noise overlay
  index.tsx               redirects to /onboarding or /dashboard based on store
  onboarding.tsx          4-step character creation wizard
  _app.tsx                authenticated-shell layout (sidebar + bottom nav + Outlet)
  _app/dashboard.tsx
  _app/workout.tsx        layout with sub-route Outlet
  _app/workout/index.tsx  start / launcher
  _app/workout/$id.tsx    active session
  _app/workout/history.tsx
  _app/workout/templates.tsx
  _app/workout/summary.$id.tsx   post-workout summary
  _app/nutrition.tsx      layout
  _app/nutrition/today.tsx
  _app/nutrition/post-workout.tsx
  _app/nutrition/log.tsx
  _app/nutrition/goals.tsx
  _app/quests.tsx
  _app/inventory.tsx
  _app/profile.tsx
```

Each route sets its own `head()` with title and description.

## Core components

`src/components/rpg/`: XPBar, StatBadge, QuestCard, ExerciseCard, MacroRing, RestTimer, LootCard, HeatMap (body SVG), CharacterSprite, NotificationToast, GoldCounter, StreakBadge.

`src/components/layout/`: AppShell, Sidebar (desktop, expand-on-hover), BottomNav (mobile), TopBar.

shadcn/ui used for primitives (Button, Card, Dialog, Sheet, Tabs, Progress, Toast/Sonner) and re-skinned via tokens.

## State (Zustand, persisted)

- `useCharacterStore` — name, class, gender, sprite, level, xp, gold, stats (STR/STA/VIT/AGI), HP, MP
- `useWorkoutStore` — active session, history list, templates; survives refresh
- `useNutritionStore` — daily log, macro goals, last-workout-derived targets
- `useQuestStore` — daily / weekly / epic quests, progress
- `useInventoryStore` — owned + equipped gear
- `useUiStore` — theme, units (kg/lbs), toast queue

A small `xpEngine.ts` derives stat XP from logged sets / meals and emits toast events.

## Seed data

`src/data/`:
- `exercises.ts` — ~30 entries: name, muscle groups, equipment, default sets/reps
- `foods.ts` — ~50 entries: name, kcal, protein, carbs, fats, serving size
- `quests.ts` — daily/weekly/epic quest definitions
- `gear.ts` — starter gear catalog with rarity and unlock conditions

## Generated pixel art

Using `imagegen` (premium tier, transparent background) into `src/assets/sprites/`:
- 4 classes × 3 gender variants = 12 character sprites (idle pose)
- Class-emblem badges (4)
- ~8 gear icons covering common/rare/epic/legend examples
- 1 wide hero/parchment background for onboarding

Sprites imported as ES6 modules; CharacterSprite component handles idle hover bob via CSS keyframes.

## Responsive strategy

- Mobile (<640): bottom tab nav (Dashboard / Workout / Nutrition / Quests / Profile), single column, full-screen modals
- Tablet (640-1024): two-column dashboard, drawer nav
- Desktop (>1024): persistent collapsible sidebar, three-column dashboard grid

## Micro-interactions

- Framer Motion for page transitions, level-up burst, loot card flip, quest-complete sequence
- XP bar shimmer via Tailwind animate + custom keyframes in styles.css
- Floating "+XP" numbers via a portal toast queue
- Rest timer SVG ring using stroke-dashoffset

## Build order

1. Tokens, fonts, noise overlay, app shell with sidebar + bottom nav
2. Generate sprites and gear assets
3. Onboarding wizard + character store
4. Dashboard HUD wired to stores
5. Workout logger (start → active session → summary) + xpEngine
6. Nutrition hub + post-workout meal advisor
7. Quest board + inventory + profile
8. Polish pass: animations, level-up flow, toasts, mobile QA

## Technical notes

- TanStack Start file-based routing under `src/routes/`; `_app.tsx` provides the authenticated shell layout via `<Outlet />`
- Strict TS, named exports, kebab-case files, camelCase vars, PascalCase components
- All colors via semantic tokens — no hex in components
- React Query installed but unused this pass (kept for the Cloud pass)
- DD/MM/YYYY dates; comma decimal separator in displayed numbers
- No `console.log`, no `any`
