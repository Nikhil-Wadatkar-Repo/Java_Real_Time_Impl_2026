# Architecture Decision Log

Every significant technical decision made while building this system, in
chronological order. Newest at the bottom.

---

### Decision: Java 21, Spring Boot 3.5.16, Spring Cloud 2025.0.3

**Reason:** Java 21 is the current LTS. Spring Boot 3.5.x is the latest
stable 3.x line (Spring Boot 4 exists but pairs with Spring Framework 7 and
a still-maturing Spring Cloud train - too new for an interview-focused
project that needs stable, well-documented behavior). Spring Cloud
**2025.0.x** is the release train that is actually binary-compatible with
Boot 3.5.x.

**Alternative considered:** Spring Cloud 2025.1.x (the "latest" train on
Maven Central at the time of writing).

**Rejected because:** 2025.1.x's `spring-cloud-netflix-eureka-server:5.0.2`
is compiled against relocated Spring Boot 4 autoconfigure packages
(`org.springframework.boot.web.server.autoconfigure.ServerProperties`).
Running it against Boot 3.5.16 fails at startup with
`NoClassDefFoundError`. This was caught by actually booting the discovery
server, not by reading a compatibility matrix - the matrix lags reality.

**Trade-off:** None material; 2025.0.3 is still current and fully supported.

---

### Decision: Parent POM extends `spring-boot-starter-parent` directly

**Reason:** Only importing `spring-boot-dependencies` as a BOM (without
also inheriting the parent) loses Spring Boot's managed plugin versions
(`maven-compiler-plugin`, `maven-surefire-plugin`, resources plugin). The
Maven super-POM defaults are ancient (surefire 2.12.4) and silently produced
`Tests run: 0` - the old provider didn't pick up JUnit 5 test classes.

**Alternative considered:** BOM-only import + hand-pinning every plugin
version in `<pluginManagement>`.

**Rejected because:** more to maintain for no benefit over the standard
multi-module idiom (parent = starter-parent, plus one extra BOM import for
`spring-cloud-dependencies`).

**Trade-off:** Couples every module's plugin versions to the Spring Boot
parent release cadence - acceptable, that's the whole point of using it.

---

### Decision: Each service owns its schema; cross-service references are id-only

**Reason:** Employee holds `departmentId` as a plain `Long`, not a JPA
`@ManyToOne` to a `Department` entity, because Department lives in a
separate database (`department_db`) owned by department-service. A real
foreign key across service boundaries would violate the "each service owns
its own data" rule and make schema migrations a distributed transaction.

**Alternative considered:** Shared database with FK constraints.

**Rejected because:** it's the classic microservices anti-pattern - any
service could break another's schema, and independent deployability is
lost.

**Trade-off:** No DB-level referential integrity between employee and
department; consistency is enforced at the application layer (Feign calls
in Phase 4, this project's `[[phase-plan]]`). Orphaned `departmentId`
values are possible if department-service data is deleted incorrectly -
acceptable for this project's scope, called out in
`10-database-design.md` when written.

---

### Decision: Hand-written mapper classes instead of MapStruct

**Reason:** Employee has one shape to map (request/entity/response); a
mapping framework's build-time codegen isn't worth the added dependency
and IDE friction for something this small.

**Alternative considered:** MapStruct.

**Rejected because:** over-engineering for a 3-field-mapping class. Will
reconsider if a later service needs many nested/conditional mappings.

---

### Decision: Optimistic locking via `@Version` + explicit `ifMatchVersion` query param

**Reason:** Hibernate's `@Version` column already prevents silent lost
updates (throws `OptimisticLockException` on a stale write). The
controller additionally accepts an optional `ifMatchVersion` query param so
a client that already fetched the entity can fail fast with a clear
`OPTIMISTIC_LOCK_CONFLICT` message *before* Hibernate would have caught it
at flush time.

**Trade-off:** Two code paths doing a similar job (explicit check + `@Version`
safety net). Kept both because the explicit check gives a much clearer
error message; the `@Version` check is the real guarantee if a caller
skips the query param.

---

### Decision: `spring-cloud-starter-gateway-server-webflux` (not the older `spring-cloud-starter-gateway`)

**Reason:** Spring Cloud Gateway 4.2+ split into `gateway-server-webflux`
and `gateway-server-webmvc` starters; the combined starter is deprecated.
Confirmed via Context7 docs that route/discovery-locator properties moved
under `spring.cloud.gateway.server.webflux.*` (routes) while
`spring.cloud.gateway.discovery.locator.*` stayed at the top level -
verified against the current reference docs rather than assumed, since
this is exactly the kind of thing that silently breaks between minor
versions.

---

### Decision: H2 (in `test` profile) for repository/controller tests, real PostgreSQL only via Flyway in `main`

**Reason:** Fast, dependency-free unit/integration tests that don't need
Docker running. Flyway is disabled in the `test` profile (`ddl-auto:
create-drop` instead) since Flyway migrations are written in
PostgreSQL-specific SQL.

**Trade-off:** H2 in `PostgreSQL` compatibility mode is not a perfect
stand-in for real Postgres (e.g. no `NUMERIC` precision edge cases, no
partial indexes). A Testcontainers-based Postgres integration test is the
correct addition for Phase 22 (Testing) once Docker Desktop is confirmed
running in this environment - not yet verified in this session.

---

### Decision: `spring.config.import: optional:configserver:...` in every service's `application.yml` from day one

**Reason:** Once `spring-cloud-starter-config` is on the classpath, Spring
Cloud Config Client's `ConfigDataMissingEnvironmentPostProcessor` throws
`ImportException: No spring.config.import set` and refuses to start the
Spring context at all unless `spring.config.import` is explicitly set -
this is enforced at environment-preparation time, before any profile
selection logic even runs. Discovered by the full `mvn test` reactor build
failing on `EmployeeControllerTest` (a `@WebMvcTest` with no active
profile) even though the equivalent `-pl employee-service test` run had
passed - the difference was WebMvcTest not activating the `test` profile
that separately disabled config import, exposing that the real fix
belongs in `main`, not in test config.

**Alternative considered:** Setting `spring.cloud.config.enabled=false` only
in the `test` profile.

**Rejected because:** it only masked the problem for test runs that happen
to activate that profile; a `@WebMvcTest` with no `@ActiveProfiles` (a
completely normal, valid test) would still crash. The `optional:` prefix
fixes it everywhere - standalone runs, tests, and real config-server-backed
runs - by making the import a no-op instead of a hard failure when the
config server isn't reachable.

---

### Decision: Department Service structurally mirrors Employee Service exactly

**Reason:** Same package layout (`entity/dto/exception/repository/mapper/
service/controller`), same optimistic-locking + `ifMatchVersion` pattern,
same `Specifications`-based search, same `ErrorResponse` shape. Department
has no salary/status/date-range filters (it's a much smaller domain: name,
code, description, location) but the *shape* of the solution is identical
on purpose - consistency across services matters more here than any
per-service cleverness, and it's what an interviewer expects when asking
"how did you keep 7 services consistent".

**Trade-off:** `ErrorResponse`/`GlobalExceptionHandler`/exception classes
are duplicated per-service rather than pulled into a shared library module.
Accepted deliberately: a shared library would create a compile-time
coupling between otherwise-independent services (one team's exception
class change forces every other service to rebuild) - the classic
microservices trade-off of "duplicate a little to decouple a lot". If this
duplication becomes painful (e.g. once there are 7 near-identical
`GlobalExceptionHandler`s), consider extracting a `common-web` module -
but only for the truly generic pieces (`ErrorResponse` shape, base
exception types), never for anything domain-specific.

**Verification note:** Department has two independent uniqueness
constraints (`name` and `code`, unlike Employee's single `email`), so
`create`/`update` check both and `DepartmentServiceImplTest` has a
dedicated test per constraint - this is the one real behavioral
difference worth calling out versus Employee's mapper/service/controller.

---

### Decision: Employee → Department validation via OpenFeign, ahead of the "official" Phase 8 slot

**Reason:** The phase plan lists Feign as Phase 8 and Resilience4j as
Phase 9, but Phase 4 ("Employee ↔ Department communication") cannot be
done without *some* synchronous call mechanism - Feign is what the rest
of this project standardizes on, so introducing it here (with only basic
bounded timeouts, no retry/circuit breaker yet) is more consistent than
using `RestTemplate`/`RestClient` now and rewriting to Feign in Phase 8.

**What's in now vs. deferred to Phase 9:** `DepartmentClient` (a plain
`@FeignClient(name = "department-service")`, resolved via Eureka + Spring
Cloud LoadBalancer) with `connect-timeout: 2000` / `read-timeout: 3000` so
an unreachable dependency fails in ~2s instead of hanging. Retry, circuit
breaker (sliding window, failure threshold, half-open state) and a
fallback response are explicitly Phase 9 work - adding them now would mean
re-explaining the same concepts twice across two "phases" for no benefit.

**Verified live** (not just unit-tested): booted `discovery-server` +
`department-service` + `employee-service` together against a real
Dockerized PostgreSQL and confirmed all three cases over HTTP:
1. `POST /employees` with a valid `departmentId` → `201 Created`.
2. `POST /employees` with a non-existent `departmentId` → `400
   INVALID_REQUEST` ("Department not found with id: 999") - proves the
   `FeignException.NotFound` → `InvalidRequestException` mapping works
   against a real 404, not just a mocked one.
3. `POST /employees` with department-service killed mid-test → `503
   EXTERNAL_SERVICE_UNAVAILABLE` in ~2.2s, confirming the 2000ms
   connect-timeout actually bounds the failure instead of hanging.

**Error classification:** a 404 from department-service is the caller's
fault (bad `departmentId` in the request) → HTTP 400. Anything else
(timeout, connection refused, 5xx) is the platform's fault → HTTP 503.
Conflating these into one "department problem" error would force every
client to guess whether retrying makes sense; splitting them means 400 =
fix your request, 503 = retry later.

**Transaction-boundary note:** `validateDepartmentExists()` is called as
the *first* statement in both `create()` and `update()`, before any
repository access, even though the method carries a class-level
`@Transactional`. Hibernate/HikariCP only acquire a physical DB connection
on the first actual query, so calling out to Feign before any repository
call means the (potentially slow) network round-trip never holds a pooled
connection open.

**Local verification gotcha (worth documenting for Phase 21):** the host
machine already had a native Windows PostgreSQL service bound to
`0.0.0.0:5432`. Docker's `-p 5432:5432` port mapping did not error, but
connections silently reached the *native* Postgres instead of the
container, producing a `password authentication failed` error that looked
like a credentials bug but wasn't. Fixed for this session by mapping the
container to host port 5433. Docker Compose in Phase 21 sidesteps this
entirely (compose network, no host port collision), but it's a real trap
for anyone reproducing this manually on a machine with a local Postgres
already installed.
