# Controller API Reference

This document lists all controller methods under `com.mco.controller` in the employee management service.

## Base Paths

- `EmployeeController` -> `/employeeAPI`
- `DataLoadController` -> no class-level base path
- `ProductionIssuesController` -> `/api/prodIssues`
- `TransactionController` -> `/api`

## EmployeeController

| Method | HTTP Verb | Endpoint | Purpose | Request | Response |
|---|---|---|---|---|---|
| `create` | `POST` | `/employeeAPI` | Creates a new employee record. | `Employee` JSON body, validated with `@Valid` | `201 Created` with created `Employee` |
| `getAll` | `GET` | `/employeeAPI` | Returns all employees. | None | `200 OK` with `List<Employee>` |
| `update` | `PUT` | `/employeeAPI/{employeeId}` | Updates an employee by id. | `employeeId` path variable, `Employee` JSON body, validated with `@Valid` | `200 OK` with updated `Employee` |
| `delete` | `DELETE` | `/employeeAPI/{employeeId}` | Deletes an employee by id. | `employeeId` path variable | `204 No Content` |
| `getById` | `GET` | `/employeeAPI/getById/{id}` | Fetches a single employee by id. | `id` path variable | `200 OK` with `Employee` |

## DataLoadController

| Method | HTTP Verb | Endpoint | Purpose | Request | Response |
|---|---|---|---|---|---|
| `createBulk` | `POST` | `/bulk` | Saves a list of employees in bulk. | JSON array of `Employee` objects, validated with `@Valid` | `201 Created` with `BulkEmployeeSaveResponse` |
| `seedRealTimeEmployees` | `POST` | `/seed-real-time` | Seeds employee data using the supplied count. | Query param `count` with default `20000` and minimum `1` | `201 Created` with `BulkSeedResponse` |
| `seedAnother30000Employees` | `POST` | `/seed-another-30000` | Seeds another fixed employee batch. | None | `201 Created` with `BulkSeedResponse` |

## ProductionIssuesController

| Method | HTTP Verb | Endpoint | Purpose | Request | Response |
|---|---|---|---|---|---|
| `nPlusOneDemo` | `GET` | `/api/prodIssues/n-plus-one-demo` | Demonstrates the N+1 query issue. | None | `200 OK` with `List<EmployeeDepartmentView>` |
| `joinFetchDemo` | `GET` | `/api/prodIssues/join-fetch-demo` | Demonstrates the join fetch fix for N+1. | None | `200 OK` with `List<EmployeeDepartmentView>` |
| `getPagedEmployees` | `GET` | `/api/prodIssues/paged` or `/api/prodIssues/paged/{page}/{size}` | Returns paged employees with a max page size of `50`. | Query params `page` and `size`, or path params `page` and `size` | `200 OK` with `Page<Employee>` |
| `asyncUpdate` | `GET` | `/api/prodIssues/asyncUpdate` | Triggers asynchronous processing for all employees. | None | `200 OK` with `String` result |

### Paging notes

- `page` defaults to `0`
- `size` defaults to `10`
- `size` is capped at `50`
- Both query parameter and path variable forms are supported for the paged endpoint

## TransactionController

| Method | HTTP Verb | Endpoint | Purpose | Request | Response |
|---|---|---|---|---|---|
| `transfer` | `POST` | `/api/transfer` | Transfers money using the provided request payload. | `TransferRequest` JSON body | `SUCCESS` string on completion |

## Validation and Behavior Notes

- Controllers use `@CrossOrigin(origins = "*", allowedHeaders = "*")`.
- `EmployeeController` and `DataLoadController` use bean validation on request bodies and request parameters.
- `ProductionIssuesController#getPagedEmployees` resolves page and size from either query params or path variables, then enforces a maximum size of `50`.
- `EmployeeController#delete` returns `204 No Content` after deletion.
