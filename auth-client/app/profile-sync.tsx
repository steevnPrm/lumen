"use client";

import { useEffect } from "react";

const PENDING_PROFILE_KEY = "lumen_pending_profile";

// Reads the profile fields collected on the signup form (stashed in
// sessionStorage right before the redirect to Auth0's login page) and pushes
// them to our backend now that a session exists. Best-effort: failures are
// swallowed so a broken sync never blocks the user from using the app.
export function ProfileSync() {
  useEffect(() => {
    const pending = sessionStorage.getItem(PENDING_PROFILE_KEY);
    if (!pending) return;
    sessionStorage.removeItem(PENDING_PROFILE_KEY);

    fetch("/api/profile-sync", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: pending,
    }).catch(() => {});
  }, []);

  return null;
}
