import type { ReactNode } from "react";
import { DesktopSidebar, MobileBottomNav } from "./nav";

export function AppShell({ children }: { children: ReactNode }) {
  return (
    <div className="noise-overlay flex min-h-screen w-full">
      <DesktopSidebar />
      <main className="flex min-h-screen flex-1 flex-col pb-20 lg:pb-0">
        <div className="mx-auto w-full max-w-7xl flex-1 px-4 py-6 lg:px-8">{children}</div>
      </main>
      <MobileBottomNav />
    </div>
  );
}
