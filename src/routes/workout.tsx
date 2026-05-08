import { createFileRoute, Outlet } from "@tanstack/react-router";

export const Route = createFileRoute("/workout")({
  head: () => ({
    meta: [
      { title: "Workout — FitQuest" },
      { name: "description", content: "Log sessions, track sets, and earn XP." },
    ],
  }),
  component: WorkoutLayout,
});

function WorkoutLayout() {
  return <Outlet />;
}
