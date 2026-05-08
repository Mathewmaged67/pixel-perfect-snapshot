import { createFileRoute, Outlet, Link, useRouterState } from "@tanstack/react-router";
import { AppShell } from "@/components/layout/app-shell";
import { cn } from "@/lib/utils";

export const Route = createFileRoute("/nutrition")({
  head: () => ({
    meta: [
      { title: "Nutrition — FitQuest" },
      { name: "description", content: "Fuel your hero with macros, meals, and recovery plans." },
    ],
  }),
  component: NutritionLayout,
});

const TABS = [
  { to: "/nutrition/today", label: "Today" },
  { to: "/nutrition/post-workout", label: "Post-workout" },
  { to: "/nutrition/log", label: "Log" },
  { to: "/nutrition/goals", label: "Goals" },
] as const;

function NutritionLayout() {
  const path = useRouterState({ select: (s) => s.location.pathname });
  return (
    <AppShell>
      <header className="mb-4">
        <h1 className="text-xl">Nutrition</h1>
      </header>
      <nav className="mb-6 flex gap-2 overflow-x-auto">
        {TABS.map((t) => (
          <Link
            key={t.to}
            to={t.to}
            className={cn(
              "rounded-md border px-3 py-1.5 text-xs whitespace-nowrap transition",
              path === t.to ? "border-gold bg-elevated text-gold" : "border-border text-muted-foreground hover:border-gold-dim",
            )}
          >
            {t.label}
          </Link>
        ))}
      </nav>
      <Outlet />
    </AppShell>
  );
}
