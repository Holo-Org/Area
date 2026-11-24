# Spike: Solid.js

- **Reactivity**: fine-grained signals (`createSignal`, `createMemo`, `createEffect`). A component function runs only once to set up the view; afterwards only the exact DOM nodes that depend on a signal are updated.
- **Pitfall**: because components never re-run, React habits break things. Destructuring `props` or reading a signal outside a tracking scope silently loses reactivity, which can lead to subtle bugs.
- **Syntax**: JSX, the same syntax as React, so anyone who knows React reads it quickly, but it compiles to real DOM operations, not a virtual DOM.
- **Routing**: `@solidjs/router` or the SolidStart meta-framework (file-based routing); works but has less documentation and fewer examples than Vue Router or SvelteKit.
- **Performance**: the fastest and lightest of the three in benchmarks, with a very small runtime. For an app the size of AREA this gap is unlikely to be noticeable.
- **Ecosystem**: the smallest of the three. Fewer ready-made UI kits and libraries, so more things must be built by hand or adapted.
- **Tooling**: Vite-based, fast dev server; Solid DevTools exist but are less polished than Vue's.
- **Summary**: the best raw performance and a clean reactivity model, at the cost of a smaller ecosystem and a reactivity model that is easy to misuse when you are learning.
