import { Auth0Client } from "@auth0/nextjs-auth0/server";

// AUTH0_AUDIENCE identifies the Spring API as an Auth0 "API" (Resource Server).
// Without it, Auth0 issues an opaque access token instead of a JWT, which the
// backend's resource server can't validate — see docs/adr/0006-custom-signup-form.md.
export const auth0 = new Auth0Client({
  authorizationParameters: process.env.AUTH0_AUDIENCE
    ? { audience: process.env.AUTH0_AUDIENCE }
    : undefined,
});
