# Spike: Vue 3

- **Syntax**: HTML-based templates with directives (`v-if`, `v-for`, `v-model`), easy to read for anyone who knows HTML.
- **Single File Components**: a `.vue` file holds the template, the logic (`<script setup>`) and the scoped style of a component, so everything about it is in one place.
- **Reactivity**: proxy-based (`ref`, `reactive`, `computed`, `watch`) through the Composition API. Fully reactive, but it relies on a virtual DOM, so it is slightly slower than Solid. The difference is not noticeable at AREA's scale.
- **Routing**: Vue Router is official, well documented and easy to set up; Nuxt adds file-based routing if needed.
- **Ecosystem**: the largest and most mature of the three: Vuetify, PrimeVue, Element Plus, Quasar, plus VueUse for utility composables.
- **Tooling**: Vite (created by Vue's author), Vue DevTools (component tree, Pinia state, router), Volar / Vue Official extension for VS Code with TypeScript support.
- **Documentation**: very clear official docs with Options/Composition API toggles and an interactive tutorial.
- **Summary**: not the fastest, but the safest choice: mature ecosystem, great tools and libraries, and the most learning resources.
