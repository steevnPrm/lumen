import { NextRequest, NextResponse } from "next/server";

const CONNECTION = process.env.AUTH0_CONNECTION ?? "Username-Password-Authentication";

interface SignupRequestBody {
  email: string;
  password: string;
  username: string;
  firstname: string;
  lastname: string;
}

function isBlank(value: unknown): boolean {
  return typeof value !== "string" || value.trim().length === 0;
}

export async function POST(request: NextRequest) {
  const body = (await request.json()) as Partial<SignupRequestBody>;
  const { email, password, username, firstname, lastname } = body;

  if (isBlank(email) || isBlank(password) || isBlank(username) || isBlank(firstname) || isBlank(lastname)) {
    return NextResponse.json({ error: "Tous les champs sont obligatoires." }, { status: 400 });
  }

  const domain = process.env.AUTH0_DOMAIN;
  const clientId = process.env.AUTH0_CLIENT_ID;

  const auth0Response = await fetch(`https://${domain}/dbconnections/signup`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      client_id: clientId,
      connection: CONNECTION,
      email,
      password,
      // Stored on the Auth0 user as a durable backup; the profile also gets
      // synced to our own backend right after the user logs in (see
      // app/api/profile-sync/route.ts), but this survives even if that sync
      // never runs (e.g. AUTH0_AUDIENCE not configured yet).
      user_metadata: { username, firstname, lastname },
    }),
  });

  if (!auth0Response.ok) {
    const data = await auth0Response.json().catch(() => ({}));
    const message =
      typeof data.description === "string"
        ? data.description
        : "La création du compte a échoué.";
    return NextResponse.json({ error: message }, { status: auth0Response.status });
  }

  return NextResponse.json({ ok: true });
}
