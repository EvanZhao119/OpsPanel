# OpsPanel Backend Architecture Overview

## 1. Overview

The **OpsPanel Backend** is a modular Spring Boot system designed for enterprise-grade management and service orchestration.  
It follows a **multi-module architecture** inspired by RuoYi and domain-driven design (DDD), with clear separation between:

- **opps-admin** → Application entrypoint, authentication, Swagger aggregation
- **opps-system** → Core system domain and internal REST APIs
- **opps-api** → Public-facing API layer for third-party or external integrations
- **opps-framework** → Shared security, utility, and cross-cutting infrastructure
- **opps-common** → Common DTOs, enums, exceptions, and response wrappers
- **opps-insight / opps-dashboard / opps-task** → Optional domain extensions 


---

## 2. Module Responsibilities

| Module | Purpose | Key Components | Used By |
|--------|----------|----------------|----------|
| **opps-admin** | Application entrypoint for admin portal. Provides login, authentication, and Swagger aggregation. | `AuthController`, `IndexController`, `SwaggerConfig`, `OpsPanelApplication` | Frontend (Vue admin panel) |
| **opps-system** | Core system service containing all management domain logic. Directly provides APIs for user, role, menu, dept, and log operations. | `SysUserController`, `SysRoleController`, `SysMenuController`, `SysDeptController`, etc. | `opps-admin` (via dependency injection) |
| **opps-api** | Exposes selected business capabilities as public REST APIs to external systems. | `UserApiController`, `RoleApiController`, `MenuApiController` | External clients |
| **opps-framework** | Security, event, and cross-cutting framework layer. | `JwtTokenProvider`, `AuthenticationEntryPointImpl`, `UserDetailsServiceImpl` | All modules |
| **opps-common** | Defines reusable models, utilities, and response wrappers (`ApiResponse`). | `ApiResponse`, `BusinessException`, `ResultCode` | All modules |
| **opps-insight** | (Optional) Reporting or analytics module. | `ReportService`, `InsightController` | `opps-admin` |
| **opps-dashboard** | (Optional) Visual dashboard aggregation module. | `DashboardController` | `opps-admin` |
| **opps-task** | (Optional) Scheduled job management module. | `TaskScheduler`, `JobService` | System cron jobs |

---

## 3. Inter-Module Dependencies

| From | Depends On | Purpose |
|------|-------------|----------|
| `opps-admin` | `opps-system`, `opps-framework`, `opps-common` | Calls system APIs and uses shared security and DTOs |
| `opps-system` | `opps-framework`, `opps-common` | Implements domain logic with security and common response |
| `opps-api` | `opps-system`, `opps-common` | Exposes selected APIs to public users |
| `opps-framework` | `opps-common` | Uses global models and exceptions |
| `opps-common` | *(no dependencies)* | Foundation module |

---

## 4. Controller Exposure Strategy

| Module | Controller | Path | Response Type | Target Consumer |
|---------|-------------|-------|----------------|------------------|
| **opps-admin** | `AuthController` | `/auth/**` | `ResponseEntity` | Vue frontend |
| **opps-system** | `SysUserController` | `/system/users/**` | `ApiResponse` | Vue frontend & internal modules |
| **opps-system** | `SysRoleController` | `/system/roles/**` | `ApiResponse` | Vue frontend |
| **opps-system** | `SysMenuController` | `/system/menus/**` | `ApiResponse` | Vue frontend |
| **opps-api** | `UserApiController` | `/api/v1/users/**` | `ApiResponse` | Third-party systems |
