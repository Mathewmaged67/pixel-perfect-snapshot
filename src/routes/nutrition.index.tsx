import { createFileRoute, Navigate } from "@tanstack/react-router";

export const Route = createFileRoute("/nutrition/")({
  component: () => <Navigate to="/nutrition/today" />,
});
