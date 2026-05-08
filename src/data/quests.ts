import type { Quest } from "@/lib/types";

export const seedQuests: Quest[] = [
  { id: "q-d1", title: "Log a meal", description: "Track at least one meal today", scope: "daily", rewardXp: 30, rewardGold: 10, goal: 1, progress: 0 },
  { id: "q-d2", title: "Hit your water goal", description: "Drink 2 L of water", scope: "daily", rewardXp: 25, rewardGold: 5, goal: 2, progress: 0 },
  { id: "q-d3", title: "Complete a workout", description: "Log a session of any kind", scope: "daily", rewardXp: 80, rewardGold: 20, goal: 1, progress: 0 },
  { id: "q-w1", title: "Train four times", description: "Four workouts this week", scope: "weekly", rewardXp: 220, rewardGold: 60, goal: 4, progress: 1 },
  { id: "q-w2", title: "Protein streak", description: "Hit protein goal five days", scope: "weekly", rewardXp: 180, rewardGold: 40, goal: 5, progress: 2 },
  { id: "q-e1", title: "Bench 100 kg", description: "Lift 100 kg on bench press", scope: "epic", rewardXp: 1500, rewardGold: 400, goal: 100, progress: 72 },
  { id: "q-e2", title: "30 day streak", description: "Log activity for 30 days straight", scope: "epic", rewardXp: 2000, rewardGold: 500, goal: 30, progress: 7 },
  { id: "q-e3", title: "Run a 5K", description: "Complete a 5 km cardio session", scope: "epic", rewardXp: 1200, rewardGold: 300, goal: 5, progress: 2 },
];
