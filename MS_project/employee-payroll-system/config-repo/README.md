# Config Repo

Native filesystem-backed configuration source for `config-server`
(`spring.cloud.config.server.native.search-locations`). Each business
service will get its own `{service-name}.yml` and, where profile-specific
values are needed, `{service-name}-{profile}.yml` (dev/test/prod).

Not wired into business services yet - each service currently runs fully
standalone from its own `application.yml` (Phase 1-2 skeleton). Config
Server externalization happens in Phase 6 per `docs/DECISIONS.md`'s phase
plan, once there is more than one service's worth of config worth
centralizing.
