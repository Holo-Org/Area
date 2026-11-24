# Spike: Svelte

- **Reactivity**: Svelte 5 "runes" (`$state`, `$derived`, `$effect`, `$props`). Values declared with `$state` behave like plain variables and objects, and the UI updates in real time when they change.
- **Compiler**: Svelte is a compiler, not a runtime library. Components become small, direct DOM-update code, which gives small bundles and good performance.
- **Syntax**: HTML-first templates with `{#if}`, `{#each}` and `{#await}` blocks. Script, markup and style live in one `.svelte` file, close to plain HTML/CSS/JS.
- **Routing**: SvelteKit uses file-based routing (`src/routes/...`); creating a page is just creating a folder, which makes routes very easy.
- **Learning curve**: gentle, little boilerplate. Watch out: many tutorials and answers online still use the Svelte 4 syntax (`let` + `$:`), which differs from runes.
- **Ecosystem**: good and growing, smaller than Vue's but bigger than Solid's. UI kits such as shadcn-svelte, Skeleton and Flowbite-Svelte.
- **Accessibility**: the compiler emits a11y warnings at build time (missing `alt`, click handlers on non-interactive elements, etc).
- **Tooling / debugging**: official VS Code extension, `svelte-check` for type checking, Vite dev server with fast HMR, browser DevTools extension.
- **Summary**: easy to learn, clean syntax, great routing and tooling; the main risk is the Svelte 4 / Svelte 5 split in learning resources.
