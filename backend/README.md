# OpsPanel Backend Architecture Overview

## 1. Overview

The **OpsPanel Backend** is a modular Spring Boot system designed for enterprise-grade management and service orchestration.  
It follows a **multi-module architecture** inspired by RuoYi and domain-driven design (DDD), with clear separation between:

- **ops-admin** → Application entrypoint, authentication, Swagger aggregation
- **ops-system** → Core system domain and internal REST APIs
- **ops-api** → Public-facing API layer for third-party or external integrations
- **ops-framework** → Shared security, utility, and cross-cutting infrastructure
- **ops-common** → Common DTOs, enums, exceptions, and response wrappers
- **ops-insight / ops-dashboard / ops-task** → Optional domain extensions 


---

## 2. Module Responsibilities

| Module | Purpose | Key Components | Used By |
|--------|----------|----------------|----------|
| **ops-admin** | Application entrypoint for admin portal. Provides login, authentication, and Swagger aggregation. | `AuthController`, `IndexController`, `SwaggerConfig`, `OpsPanelApplication` | Frontend (Vue admin panel) |
| **ops-system** | Core system service containing all management domain logic. Directly provides APIs for user, role, menu, dept, and log operations. | `SysUserController`, `SysRoleController`, `SysMenuController`, `SysDeptController`, etc. | `ops-admin` (via dependency injection) |
| **ops-api** | Exposes selected business capabilities as public REST APIs to external systems. | `UserApiController`, `RoleApiController`, `MenuApiController` | External clients |
| **ops-framework** | Security, event, and cross-cutting framework layer. | `JwtTokenProvider`, `AuthenticationEntryPointImpl`, `UserDetailsServiceImpl` | All modules |
| **ops-common** | Defines reusable models, utilities, and response wrappers (`ApiResponse`). | `ApiResponse`, `BusinessException`, `ResultCode` | All modules |
| **ops-insight** | (Optional) Reporting or analytics module. | `ReportService`, `InsightController` | `ops-admin` |
| **ops-dashboard** | (Optional) Visual dashboard aggregation module. | `DashboardController` | `ops-admin` |
| **ops-task** | (Optional) Scheduled job management module. | `TaskScheduler`, `JobService` | System cron jobs |

---

## 3. Inter-Module Dependencies

| From | Depends On | Purpose |
|------|-------------|----------|
| `ops-admin` | `ops-system`, `ops-framework`, `ops-common` | Calls system APIs and uses shared security and DTOs |
| `ops-system` | `ops-framework`, `ops-common` | Implements domain logic with security and common response |
| `ops-api` | `ops-system`, `ops-common` | Exposes selected APIs to public users |
| `ops-framework` | `ops-common` | Uses global models and exceptions |
| `ops-common` | *(no dependencies)* | Foundation module |

---

## 4. Controller Exposure Strategy

| Module | Controller | Path | Response Type | Target Consumer |
|---------|-------------|-------|----------------|------------------|
| **ops-admin** | `AuthController` | `/auth/**` | `ResponseEntity` | Vue frontend |
| **ops-system** | `SysUserController` | `/system/users/**` | `ApiResponse` | Vue frontend & internal modules |
| **ops-system** | `SysRoleController` | `/system/roles/**` | `ApiResponse` | Vue frontend |
| **ops-system** | `SysMenuController` | `/system/menus/**` | `ApiResponse` | Vue frontend |
| **ops-api** | `UserApiController` | `/api/v1/users/**` | `ApiResponse` | Third-party systems |
