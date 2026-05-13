import { createFileRoute, Navigate } from "@tanstack/react-router";
import { useEffect, useState } from "react";
import { useCharacterStore } from "@/stores/character-store";

export const Route = createFileRoute("/")({
  component: Index,
});

function Index() {
  const initialized = useCharacterStore((s) => s.initialized);
  // Avoid SSR hydration mismatch with persisted store
  const [hydrated, setHydrated] = useState(false);
  useEffect(() => setHydrated(true), []);
  if (!hydrated) return null;
  return <Navigate to={initialized ? "/dashboard" : "/onboarding"} />;
}
