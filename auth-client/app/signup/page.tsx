"use client";

import Link from "next/link";
import { useState, type ChangeEvent, type FormEvent, type InputHTMLAttributes } from "react";
import { FormError } from "./form-error";

const PENDING_PROFILE_KEY = "lumen_pending_profile";

interface FormState {
  email: string;
  password: string;
  confirmPassword: string;
  username: string;
  firstname: string;
  lastname: string;
}

const initialForm: FormState = {
  email: "",
  password: "",
  confirmPassword: "",
  username: "",
  firstname: "",
  lastname: "",
};

export default function SignupPage() {
  const [form, setForm] = useState<FormState>(initialForm);
  const [error, setError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  function handleChange(field: keyof FormState) {
    return (event: ChangeEvent<HTMLInputElement>) => {
      setForm((prev) => ({ ...prev, [field]: event.target.value }));
    };
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setError(null);

    if (form.password !== form.confirmPassword) {
      setError("Les mots de passe ne correspondent pas.");
      return;
    }

    setIsSubmitting(true);

    try {
      const response = await fetch("/api/signup", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          email: form.email,
          password: form.password,
          username: form.username,
          firstname: form.firstname,
          lastname: form.lastname,
        }),
      });

      const data = await response.json();

      if (!response.ok) {
        setError(data.error ?? "La création du compte a échoué.");
        setIsSubmitting(false);
        return;
      }

      sessionStorage.setItem(
        PENDING_PROFILE_KEY,
        JSON.stringify({
          username: form.username,
          firstname: form.firstname,
          lastname: form.lastname,
        })
      );

      // Full navigation required: Auth0's login route must not be reached via the client router.
      // eslint-disable-next-line @next/next/no-location-assign-relative-destination
      window.location.href = `/auth/login?login_hint=${encodeURIComponent(form.email)}`;
    } catch {
      setError("Une erreur inattendue est survenue.");
      setIsSubmitting(false);
    }
  }

  return (
    <main className="mx-auto flex min-h-screen w-full max-w-md flex-col justify-center gap-6 px-6 py-16">
      <div className="text-center">
        <h1 className="text-[40px] leading-[1.1] font-bold text-foreground">Créer un compte</h1>
        <p className="mt-2 text-[17px] leading-normal text-foreground-secondary">
          Rejoignez Lumen en une seule étape.
        </p>
      </div>

      <form onSubmit={handleSubmit} className="flex flex-col gap-4">
        <Field
          label="Email"
          type="email"
          value={form.email}
          onChange={handleChange("email")}
          required
          autoComplete="email"
        />
        <Field
          label="Mot de passe"
          type="password"
          value={form.password}
          onChange={handleChange("password")}
          required
          autoComplete="new-password"
          minLength={8}
        />
        <Field
          label="Confirmer le mot de passe"
          type="password"
          value={form.confirmPassword}
          onChange={handleChange("confirmPassword")}
          required
          autoComplete="new-password"
          minLength={8}
        />
        <Field
          label="Pseudo"
          type="text"
          value={form.username}
          onChange={handleChange("username")}
          required
          autoComplete="username"
          maxLength={50}
        />
        <Field
          label="Prénom"
          type="text"
          value={form.firstname}
          onChange={handleChange("firstname")}
          required
          autoComplete="given-name"
          maxLength={100}
        />
        <Field
          label="Nom"
          type="text"
          value={form.lastname}
          onChange={handleChange("lastname")}
          required
          autoComplete="family-name"
          maxLength={100}
        />

        <FormError message={error} />

        <button
          type="submit"
          disabled={isSubmitting}
          className="mt-2 inline-flex h-11 items-center justify-center rounded-full bg-accent px-6 text-[17px] font-semibold text-white transition-colors duration-200 ease-out hover:bg-accent-hover active:bg-accent-pressed disabled:opacity-50"
        >
          {isSubmitting ? "Création..." : "Créer mon compte"}
        </button>
      </form>

      <Link
        href="/"
        className="text-center text-[13px] leading-[1.4] text-foreground-secondary transition-colors duration-200 ease-out hover:text-accent"
      >
        Retour à l&rsquo;accueil
      </Link>
    </main>
  );
}

function Field({ label, ...props }: { label: string } & InputHTMLAttributes<HTMLInputElement>) {
  return (
    <label className="flex flex-col gap-1.5">
      <span className="text-[13px] leading-[1.4] tracking-wide text-foreground-secondary">{label}</span>
      <input
        {...props}
        className="h-11 rounded-sm border border-separator bg-background px-4 text-[17px] leading-normal text-foreground outline-none transition-colors duration-200 ease-out focus:border-accent"
      />
    </label>
  );
}
