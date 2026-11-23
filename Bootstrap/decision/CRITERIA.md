# Weighted decision matrix: criteria

Bootstrap exercise 2. This file defines the gate, the criteria, the weights, the
scoring anchors and the decision rule. It is locked and committed **before anyone
scores**. Member scores live in a separate file.

Status: LOCKED. Criteria, weights, anchors and decision rule are agreed and must not
change once scoring starts.

## 1. Scope and candidates

Three independent decisions, each with its own matrix:

| Part    | Candidates                                  |
| ------- | ------------------------------------------- |
| Backend | Go, Next.js, PHP Laravel                    |
| Web     | Solid.js, Vue, Svelte                       |
| Mobile  | Flutter, React Native (Expo), Kotlin Multiplatform |

The spikes for these candidates are still in progress. Scores are filled in only after
the spikes, as the subject requires.

## 2. Gate (pass/fail, applied before scoring)

A candidate that fails any item is eliminated and is not scored.

1. Reproducible build and run with Docker Compose (every part that runs in the stack).
2. Backend can produce an OpenAPI description with a browsable interface, directly or
   through a standard library.
3. Mobile produces an installable Android application.
4. Backend serves a pure HTTP API with no HTML rendering. Next.js and Laravel stay
   eligible only as API-only backends. The extra pitfalls of a full-stack framework
   are scored under *Maintainability*, not gated, because the brief says
   "strongly discouraged", not "forbidden".

## 3. Weight scale

| Weight | Meaning                                                                      |
| ------ | ---------------------------------------------------------------------------- |
| 1      | Nice to have: changes my preference but would not stop me choosing.          |
| 2      | Important: a poor score needs a plan to compensate.                          |
| 3      | Critical: a poor score puts delivery of the common base or chosen features at risk. |

## 4. Criteria and weights

Each part has exactly 8 criteria. `n/a` means the criterion does not apply to that part.

| Criterion                          | Backend | Web | Mobile |
| ---------------------------------- | :-----: | :-: | :----: |
| Ease of learning                   | 3 | 3 | 3 |
| Documentation clarity              | 2 | 2 | 2 |
| Ecosystem                          | 2 | 2 | 2 |
| Maintainability                    | 2 | 2 | 2 |
| Infra setup cost                   | 2 | n/a | n/a |
| Performance (naive use)            | 1 | 1 | n/a |
| OpenAPI and contract-test tooling  | 3 | n/a | n/a |
| Hook scheduling                    | 3 | n/a | n/a |
| Reactivity pattern                 | n/a | 2 | n/a |
| Builder-UI support                 | n/a | 3 | n/a |
| Accessibility and i18n tooling     | n/a | 2 | 2 |
| Cross-device and cross-platform    | n/a | n/a | 2 |
| Responsive layout and styling      | n/a | n/a | 2 |
| Reproducible APK build in Docker   | n/a | n/a | 3 |
| **Sum of weights**                 | **18** | **17** | **18** |
| **Maximum total (weights x 5)**    | **90** | **85** | **90** |

Weighted total = sum over criteria of (weight x score), score from 1 to 5.

## 5. Scoring anchors

Every member scores every candidate of every part, independently and without
discussion, from 1 to 5.

### 5.1 Measured criteria: relative ranking

Applies to *Performance*, *Infra setup cost* and *Reproducible APK build in Docker*.
Among the three candidates of a part: best = 5, middle = 3, worst = 1. Ties share the
score of the better rank. The raw numbers are recorded next to each score so the size
of the gap stays visible.

Protocol, identical for every candidate of a part:

- **Backend performance**: the `GET /ping` server from exercise 1, built as a
  production image. Record Docker image size, idle memory (container idle for 30 s)
  and time to first response after container start.
- **Web performance**: a minimal app, production build served as static files, in a
  production image. Same three measures.
- **Infra setup cost** (backend): time and number of steps to get the container
  running from a clean checkout, as recorded during the spike.
- **APK build in Docker** (mobile): time and number of manual steps to get an
  installable APK from a clean checkout using only the container.
- *Performance* is a single criterion. Rank on each of its three measures, then
  average the three ranks' scores and round to the nearest integer.

### 5.2 Qualitative criteria: written anchors

**Ease of learning**
- 5: Productive on the spike target within the first hour; the mental model matched what I already knew.
- 3: Productive after a few hours; some concepts needed a second pass.
- 1: Still stuck on basic concepts when the timebox ended.

**Documentation clarity**
- 5: Official docs answered nearly every question within 5 minutes, with working examples.
- 3: Docs answered most questions, but some needed issues, blog posts or source reading.
- 1: Docs were missing, outdated or contradictory on things I needed.

**Ecosystem**
- 5: Maintained, well-known libraries exist for every need I can foresee (auth, OIDC/OAuth, validation, testing, HTTP clients).
- 3: Libraries exist for most needs; one or two gaps I would have to fill myself.
- 1: Several core needs have no maintained library.

**Maintainability**
- 5: Conventions are strong, few traps; I expect the code to stay readable at the end of the project.
- 3: Some traps and anti-patterns; manageable with team discipline.
- 1: Many traps or anti-patterns; I expect the code to degrade quickly. For Next.js and Laravel, the cost of keeping them API-only counts here.

**OpenAPI and contract-test tooling** (backend)
- 5: OpenAPI generated or validated from the code, browsable UI built in, and a mismatch test is straightforward.
- 3: Possible with a maintained library plus some glue.
- 1: Mostly manual; keeping description and code in sync would be fragile.

**Hook scheduling** (backend)
- 5: Background jobs, scheduling and persisted state are first-class or covered by a standard library.
- 3: Doable with a maintained library and moderate setup.
- 1: Would need a self-built scheduler and persistence.

**Reactivity pattern** (web)
- Scored on each member's own taste. 5 = I would gladly work with this model daily; 1 = I would avoid it. Divergences of more than 2 points are findings, not noise.

**Builder-UI support** (web)
- 5: A mature canvas or node-graph library exists for this framework and integrates cleanly.
- 3: Libraries exist but are less mature, or need wrapping.
- 1: No credible option; the builder would be built from scratch.

**Accessibility and i18n tooling** (web, mobile)
- 5: Documented, built-in or well-maintained support for WCAG AA checks and for two languages.
- 3: Available through libraries with moderate setup.
- 1: Little tooling; mostly manual effort.

**Cross-device and cross-platform compatibility** (mobile)
- 5: Behaves consistently across the Android devices tested; iOS support is available if wanted (bonus).
- 3: Mostly consistent, with some device-specific fixes needed.
- 1: Frequent device-specific problems. iOS is a bonus here and counts only toward a higher score, never as a reason to lower one.

**Responsive layout and styling** (mobile)
- 5: Adapting to screen sizes is simple and idiomatic.
- 3: Workable, with some manual handling.
- 1: Layout adaptation is a recurring struggle.

## 6. Divergence rule

After independent scoring, compare matrices. For every criterion, flag any pair of
members whose scores differ by more than 2 points. Each flag is a finding: the two
members are not evaluating the same thing. Record the reason before settling it.

## 7. Decision rule

Fixed before scoring.

1. Eliminate any candidate that fails the gate.
2. Rank the remaining candidates by weighted total.
3. The highest weighted total wins.
4. If the top two totals are within 5% of the maximum possible total (4.5 points for
   backend, 4.25 for web, 4.5 for mobile), apply the tiebreak: first the sum of
   (weight x score) over the weight-3 criteria, then the lower infra and learning
   cost.
