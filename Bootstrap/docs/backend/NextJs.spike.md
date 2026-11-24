# Next.js Spike
In 15 minutes every member of the group could be able to create a simple function server using Next.js.
This server should expose a simple GET route that returns a "status" : "ok".

Next.js is simple to setup: you only need Node.js and `npx create-next-app`. A route is a file `app/api/status/route.ts` that exports a `GET` function returning `Response.json({ status: "ok" })`, the framework takes care of the routing, no extra dependency is needed.

Learning cost is very low for a developper who already knows JS / TypeScript, it's just a matter of writing a few lines of code. The only tricky part is to understand the App Router conventions (server vs client components, caching).

Next.js is great for AREA because it allows to implement the frontend in the same codebase, with the same language, and the types can be shared between the client and the API.
Also it has a lot of security systems built in, updates are regularly released, and there is a huge ecosystem of libraries. For the auth part: Auth.js (NextAuth) or Better Auth handle OAuth 2.0 and OpenID Connect providers.

However Next.js is not made for long-running processes: route handlers are request / response oriented (and designed for serverless). For the hooks (pollers, workflow executions running in the background) we need a separate worker process or a job queue (ex: BullMQ + Redis), or a cron calling an API route.

Note : For the infrastructure we need to build our own docker image, with a multi-stage build on a node.js image (using `output: "standalone"` in the Next config to get a small image), plus a worker container if we need background jobs.
