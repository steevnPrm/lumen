import { NextRequest, NextResponse } from "next/server";
import { auth0 } from "@/lib/auth0";

const API_BASE_URL = process.env.API_BASE_URL ?? "http://localhost:8080";

export async function POST(request: NextRequest) {
  const session = await auth0.getSession(request);
  if (!session) {
    return NextResponse.json({ error: "Non authentifié." }, { status: 401 });
  }

  const { username, firstname, lastname } = await request.json();
  if (!username || !firstname || !lastname) {
    return NextResponse.json({ error: "Champs manquants." }, { status: 400 });
  }

  // Route Handlers (unlike Server Components) can read/write cookies via the
  // ambient next/headers API, so no explicit (req, res) pair is needed here.
  const { token } = await auth0.getAccessToken();

  const backendResponse = await fetch(`${API_BASE_URL}/api/users/me`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({ username, firstname, lastname }),
  });

  return NextResponse.json(
    backendResponse.ok
      ? { ok: true }
      : { error: "La synchronisation du profil a échoué." },
    { status: backendResponse.ok ? 200 : backendResponse.status }
  );
}
