"use client";

import { useAnimatedMessage } from "./use-animated-message";

export function FormError({ message }: { message: string | null }) {
  const { message: displayed, visible } = useAnimatedMessage(message);

  if (!displayed) return null;

  return (
    <div
      role="alert"
      className={`rounded-sm border border-error/30 bg-error/10 px-4 py-3 text-[13px] leading-[1.4] text-error transition-all duration-200 ease-out ${
        visible ? "translate-y-0 opacity-100" : "-translate-y-1 opacity-0"
      }`}
    >
      {displayed}
    </div>
  );
}
