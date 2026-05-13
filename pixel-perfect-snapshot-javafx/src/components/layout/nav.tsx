import { Link, useRouterState } from "@tanstack/react-router";
import { LayoutDashboard, Dumbbell, Apple, Scroll, Backpack, User, Swords } from "lucide-react";
import { cn } from "@/lib/utils";

const NAV = [
  { to: "/dashboard", label: "Dashboard", icon: LayoutDashboard },
  { to: "/workout", label: "Workout", icon: Dumbbell },
  { to: "/nutrition", label: "Nutrition", icon: Apple },
  { to: "/quests", label: "Quests", icon: Scroll },
  { to: "/inventory", label: "Inventory", icon: Backpack },
  { to: "/profile", label: "Profile", icon: User },
] as const;

export function DesktopSidebar() {
  const path = useRouterState({ select: (s) => s.location.pathname });
  return (
    <aside className="relative hidden h-screen w-16 shrink-0 overflow-visible lg:block">
      <div className="group absolute left-0 top-0 z-20 flex h-screen w-16 flex-col border-r border-border bg-sidebar transition-[width] duration-200 hover:w-56">
        <div className="flex h-16 items-center justify-center gap-2 border-b border-border px-3 group-hover:justify-start">
          <Swords className="h-6 w-6 text-gold" />
          <span className="hidden font-display text-xs uppercase tracking-wider text-gold group-hover:inline">FitQuest</span>
        </div>
        <nav className="flex-1 px-2 py-4">
          <ul className="space-y-1">
            {NAV.map((item) => {
              const Icon = item.icon;
              const active = path.startsWith(item.to);
              const itemClasses = cn(
                "flex items-center gap-3 rounded-md px-3 py-2 text-sm transition",
                active ? "bg-elevated text-gold" : "text-muted-foreground hover:bg-elevated hover:text-foreground",
              );
              return (
                <li key={item.to}>
                  {active ? (
                    <div className={itemClasses} aria-current="page">
                      <Icon className="h-5 w-5 shrink-0" />
                      <span className="hidden truncate group-hover:inline">{item.label}</span>
                    </div>
                  ) : (
                    <Link to={item.to} className={itemClasses}>
                      <Icon className="h-5 w-5 shrink-0" />
                      <span className="hidden truncate group-hover:inline">{item.label}</span>
                    </Link>
                  )}
                </li>
              );
            })}
          </ul>
        </nav>
      </div>
    </aside>
  );
}

const MOBILE_NAV = NAV.slice(0, 5);

export function MobileBottomNav() {
  const path = useRouterState({ select: (s) => s.location.pathname });
  return (
    <nav className="fixed bottom-0 left-0 right-0 z-30 grid grid-cols-5 border-t border-border bg-sidebar/95 backdrop-blur lg:hidden">
      {MOBILE_NAV.map((item) => {
        const Icon = item.icon;
        const active = path.startsWith(item.to);
        const itemClasses = cn(
          "flex flex-col items-center justify-center gap-1 py-2 text-[10px] transition",
          active ? "text-gold" : "text-muted-foreground",
        );
        return (
          <li key={item.to}>
            {active ? (
              <div className={itemClasses} aria-current="page">
                <Icon className="h-5 w-5" />
                <span className="font-display uppercase">{item.label}</span>
              </div>
            ) : (
              <Link to={item.to} className={itemClasses}>
                <Icon className="h-5 w-5" />
                <span className="font-display uppercase">{item.label}</span>
              </Link>
            )}
          </li>
        );
      })}
    </nav>
  );
}
