# caching-demo

Standalone Spring Boot app demonstrating Redis-backed caching over a PostgreSQL-persisted
`Product` CRUD entity. It's a separate app from `blog-api` at the repo root — its own `pom.xml`,
own Maven build, run it independently.

## Running

Requires PostgreSQL and Redis. The included `docker-compose.yml` starts both:

```bash
docker compose up -d
./mvnw spring-boot:run          # bash
.\mvnw.cmd spring-boot:run      # PowerShell
```

The app listens on port 8080 (change `server.port` in `application.properties` if you're also
running `blog-api` at the same time).

## Endpoints

| Method | Path                  | Description          |
|--------|-----------------------|-----------------------|
| POST   | `/api/products`       | Create a product (`name`, `category`, `price` request params) |
| GET    | `/api/products`       | List all products    |
| GET    | `/api/products/{id}`  | Get a product by id   |
| PUT    | `/api/products/{id}`  | Update a product      |
| DELETE | `/api/products/{id}`  | Delete a product      |

## Seeing the cache work

`ProductService` logs a line every time it actually hits the database (`Fetching product ... from
DB`, `Fetching all products from DB`). Reads (`getProductById`, `getAllProducts`) are `@Cacheable`;
writes (`createProduct`, `updateProduct`, `deleteProduct`) use `@CachePut`/`@CacheEvict` to keep the
cache consistent instead of just letting it go stale.

1. `POST /api/products` to create one, note the returned `id`.
2. `GET /api/products/{id}` twice — the DB log line should print only once; the second call is
   served from Redis.
3. Inspect the cached entry directly: `redis-cli GET '"products"::<uuid>'` (Spring's default
   key format), or `redis-cli KEYS '*'` to see everything cached.
4. `PUT`/`DELETE` the product, then `GET` it again — the log line reappears because the write
   evicted/replaced the cache entry.

## Why Redis instead of the default cache

Spring Boot's zero-config default (`ConcurrentMapCacheManager`) is a plain unbounded, non-expiring
map living in this one JVM's heap — fine for a single instance, useless the moment you run more
than one, and it never forgets an entry on its own. `RedisCacheConfig` swaps in a `RedisCacheManager`
instead: cached values live in Redis (shared across instances, survives an app restart) and expire
after `product.cache.ttl` (`application.properties`, default 10 minutes) via `entryTtl`. Values are
serialized as JSON (`GenericJackson2JsonRedisSerializer`) rather than Java's binary serialization, so
entries are human-readable in Redis and don't break the moment `Product`'s bytecode changes between
deploys.
