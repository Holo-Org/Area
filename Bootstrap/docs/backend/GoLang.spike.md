# GoLang Spike
In 15 minutes every member of the group could be able to create a simple function server using Go.
This server should expose a simple GET route that returns a "status" : "ok".

Go is simple to setup: you only need to install the Go toolchain, no extra dependency manager. Since Go 1.22 the standard library (`net/http`) can route by method and path (ex: `GET /status`), so a simple server needs no framework. Frameworks like Gin, Echo or Chi exist if we want more features (middlewares, validation).

Learning cost is medium: the syntax is small, but the concepts (explicit error handling, structs/interfaces, goroutines) are different from JS or PHP and can be a bit high for a developper who is not familiar with Go.

Go is great for AREA because of the hooks: goroutines and channels make it easy to run many pollers / workflow executions in the background of the same server, without a separate worker system.
Also Go compiles to a single static binary, it is fast and uses little memory.
Libraries are available for the auth part: `golang.org/x/oauth2` for OAuth 2.0 and `coreos/go-oidc` for OpenID Connect.

Go has no frontend in the same codebase, so the web client has to be a separate project.

Note : For the infrastructure we need to build our own docker image, with a multi-stage build (Go image to compile, then a minimal image like distroless or scratch to run the binary).
