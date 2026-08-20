import { auth0 } from "@/lib/auth0";

export default async function Home() {
  // Check if user is authenticated
  const session = await auth0.getSession();

  if (!session) {
    return (
      <main className="flex min-h-screen flex-col items-center justify-center gap-6 px-6 text-center">
        <h1 className="text-[56px] leading-[1.05] font-bold text-foreground">
          Lumen
        </h1>
        <p className="max-w-md text-[17px] leading-normal text-foreground-secondary">
          La galerie numérique qui connecte artistes, organisateurs et
          mécènes.
        </p>

        <div className="mt-4 flex flex-wrap items-center justify-center gap-4">
          {/* Redirects to Auth0 to sign up */}
          <a
            href="/auth/login?screen_hint=signup"
            className="inline-flex h-11 items-center justify-center rounded-full bg-accent px-6 text-[17px] font-semibold text-white transition-colors duration-200 ease-out hover:bg-accent-hover active:bg-accent-pressed"
          >
            Créer un compte
          </a>
          {/* Redirects to Auth0 to log in */}
          <a
            href="/auth/login"
            className="inline-flex h-11 items-center justify-center rounded-full border border-separator px-6 text-[17px] font-semibold text-foreground transition-colors duration-200 ease-out hover:border-accent hover:text-accent"
          >
            Se connecter
          </a>
        </div>
      </main>
    );
  }

  return (
    <main className="mx-auto flex min-h-screen w-full max-w-300 flex-col items-center justify-center gap-6 px-6 py-16">
      <div className="w-full max-w-md rounded-2xl border border-separator bg-background-secondary p-6 shadow-[0_1px_3px_rgba(0,0,0,0.08)]">
        <p className="text-[13px] leading-[1.4] tracking-wide text-foreground-secondary uppercase">
          Connecté en tant que
        </p>
        <p className="mt-1 text-[22px] leading-[1.2] font-semibold text-foreground">
          {session.user.email}
        </p>

        {/* Display user info (name, email, etc.) */}
        <h2 className="mt-6 text-[17px] leading-normal font-semibold text-foreground">
          Profil
        </h2>
        <pre className="mt-2 max-h-64 overflow-auto rounded-lg bg-background p-4 text-[13px] leading-[1.4] text-foreground-secondary">
          {JSON.stringify(session.user, null, 2)}
        </pre>

        {/* Ends the session and redirects to Auth0 to log out */}
        <a
          href="/auth/logout"
          className="mt-6 inline-flex h-11 items-center justify-center rounded-full border border-separator px-6 text-[17px] font-semibold text-foreground transition-colors duration-200 ease-out hover:border-error hover:text-error"
        >
          Se déconnecter
        </a>
      </div>
    </main>
  );
}
