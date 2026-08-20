import { useEffect, useState } from "react";
import { Subject, concat, of, timer } from "rxjs";
import { distinctUntilChanged, map, scan, switchMap } from "rxjs/operators";

// Matches the "Hover (boutons, cartes)" timing token from the design system
// (docs/design-system/README.md, section 7 — Mouvement), reused here since
// there's no dedicated token for inline form feedback.
const TRANSITION_MS = 200;

export interface AnimatedMessageState {
  message: string | null;
  visible: boolean;
}

interface Transition {
  previous: string | null;
  current: string | null;
}

const HIDDEN: AnimatedMessageState = { message: null, visible: false };

// Drives a message's mount/fade lifecycle through an RxJS pipeline: a new
// message renders hidden for one tick then flips visible (letting the CSS
// transition animate in), and a cleared message stays rendered — fading out
// — for TRANSITION_MS before actually unmounting.
export function useAnimatedMessage(message: string | null): AnimatedMessageState {
  const [subject] = useState(() => new Subject<string | null>());
  const [state, setState] = useState<AnimatedMessageState>(HIDDEN);

  useEffect(() => {
    subject.next(message);
  }, [subject, message]);

  useEffect(() => {
    const subscription = subject
      .pipe(
        distinctUntilChanged(),
        scan<string | null, Transition>((acc, value) => ({ previous: acc.current, current: value }), {
          previous: null,
          current: null,
        }),
        switchMap(({ previous, current }) => {
          if (current) {
            return concat(
              of<AnimatedMessageState>({ message: current, visible: false }),
              timer(0).pipe(map(() => ({ message: current, visible: true })))
            );
          }
          if (previous) {
            return concat(
              of<AnimatedMessageState>({ message: previous, visible: false }),
              timer(TRANSITION_MS).pipe(map(() => HIDDEN))
            );
          }
          return of(HIDDEN);
        })
      )
      .subscribe(setState);

    return () => subscription.unsubscribe();
  }, [subject]);

  return state;
}
