# Employee & Payroll Management System

Enterprise-style distributed Employee & Payroll platform built to demonstrate
production Spring Boot / Spring Cloud microservices patterns for a
6-years-experience Java interview: service discovery, centralized config,
API gateway, resilient service-to-service calls, event-driven workflows,
caching, batch processing, security, observability, and testing.

Full phase plan, decisions and rationale live in [`docs/DECISIONS.md`](docs/DECISIONS.md).
This README is kept current as each phase completes - see the Status table.

## Status

| Phase | Scope | Status |
|---|---|---|
| 1 | Project skeleton (parent POM, all 10 modules bootable) | ✅ Done |
| 2 | Employee Service (full CRUD, search, pagination, optimistic locking, Flyway, tests) | ✅ Done |
| 3 | Department Service (full CRUD, search, pagination, optimistic locking, Flyway, tests) | ✅ Done |
| 4 | Employee ↔ Department communication (OpenFeign, validated departmentId, 400 vs 503 split, timeouts) | ✅ Done |
| 5+ | See `docs/DECISIONS.md` phase plan | ⏳ Not started |

## Tech Stack (pinned versions - see `docs/DECISIONS.md` for why)

- Java 21
- Spring Boot 3.5.16
- Spring Cloud 2025.0.3
- PostgreSQL + Flyway
- Netflix Eureka (service discovery)
- Spring Cloud Gateway (WebFlux)
- Maven multi-module build

## Modules

```
employee-payroll-system/
├── discovery-server/     Eureka registry - port 8761
├── config-server/        Centralized config (native/git-backed) - port 8888
├── api-gateway/          Single entry point, routing + correlation id - port 8080
├── employee-service/     Employee CRUD/search/salary/status - port 8081 (fully implemented)
├── department-service/   Department CRUD/search - port 8082 (fully implemented)
├── project-service/      Projects + employee assignment - port 8083 (skeleton)
├── attendance-service/   Check-in/out, working hours - port 8084 (skeleton)
├── payroll-service/      Salary processing, payslips - port 8085 (skeleton)
├── notification-service/ Kafka-driven emails - port 8086 (skeleton)
├── reporting-service/    Async report generation - port 8087 (skeleton)
├── config-repo/          Config Server's native config source
└── docs/                 Architecture, decisions, per-technology deep dives
```

## Running Locally (current state)

`discovery-server`, `api-gateway`, `employee-service` and `department-service`
are runnable end to end today (the latter two need PostgreSQL - see below).

```bash
# From employee-payroll-system/
mvn -pl discovery-server -am package -DskipTests
java -jar discovery-server/target/discovery-server-1.0.0-SNAPSHOT.jar
# -> http://localhost:8761 (Eureka dashboard)
```

`employee-service` requires a running PostgreSQL instance:

```sql
CREATE DATABASE employee_db;
CREATE USER employee_user WITH PASSWORD 'employee_pass';
GRANT ALL PRIVILEGES ON DATABASE employee_db TO employee_user;
```

```bash
mvn -pl employee-service -am package -DskipTests
java -jar employee-service/target/employee-service-1.0.0-SNAPSHOT.jar
# -> http://localhost:8081/employees
```

`department-service` needs its own database the same way:

```sql
CREATE DATABASE department_db;
CREATE USER department_user WITH PASSWORD 'department_pass';
GRANT ALL PRIVILEGES ON DATABASE department_db TO department_user;
```

```bash
mvn -pl department-service -am package -DskipTests
java -jar department-service/target/department-service-1.0.0-SNAPSHOT.jar
# -> http://localhost:8082/departments
```

Docker Compose (Postgres/Kafka/Redis + all services) lands in Phase 21.

## Building & Testing

```bash
mvn compile   # builds every module
mvn test      # 35/35 tests passing as of Phase 3 (17 employee-service + 18 department-service)
```

## Verified in this session

- `mvn clean test` succeeds across all 10 modules (`BUILD SUCCESS`, 38/38
  tests passing: 20 employee-service + 18 department-service).
- `discovery-server` actually boots and reports `{"status":"UP"}` on
  `/actuator/health` and self-registers in Eureka.
- `api-gateway` and `department-service` (before its domain logic existed)
  both boot to `{"status":"UP"}` - this caught a real bug:
  `spring-cloud-starter-config` on the classpath makes `spring.config.import`
  mandatory at startup, which crashed every such service until
  `optional:configserver:...` was added to each `application.yml` (see
  `docs/DECISIONS.md`).
- **Full live end-to-end verification** (Phase 4): booted
  `discovery-server` + `department-service` + `employee-service` together
  against a real Dockerized PostgreSQL and exercised the actual Feign call
  over HTTP - valid `departmentId` → `201`, non-existent `departmentId` →
  `400 INVALID_REQUEST`, department-service killed mid-request → `503
  EXTERNAL_SERVICE_UNAVAILABLE` in ~2.2s (bounded by the 2000ms Feign
  connect-timeout). Full detail and a real gotcha (a native Postgres
  service already on port 5432) in `docs/DECISIONS.md`.
