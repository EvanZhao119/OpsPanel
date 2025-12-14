# ops-framework

The `ops-framework` module provides core infrastructure for the OpsPanel backend system. It encapsulates reusable components for authentication, authorization, logging, and utility access, designed to be consumed by higher-level modules such as `ops-api`, `ops-admin`, and `ops-system`.

## Features

### 🔐 Security (Spring Security + JWT)
- Stateless authentication using JWT tokens
- Custom authentication filter and token provider
- User context access via `AuthContextUtils`
- Custom handlers for unauthorized and forbidden access

### 📋 AOP Logging
- Method-level logging via `@Log` annotation
- Execution time tracking with `LogAspect`

### 🧰 Utilities
- `ServletUtils`: Access current HTTP request context

### ❗ Exception Handling
Exception classes and global handlers are implemented in the `ops-common` module to ensure cross-module availability.

## Usage

### 1. Add dependency in other modules

```xml
<dependency>
  <groupId>com.opspanel</groupId>
  <artifactId>ops-framework</artifactId>
  <version>${project.version}</version>
</dependency>
```

### 2. Access current user information
```java
Long userId = AuthContextUtils.getUserId();
String username = AuthContextUtils.getUsername();
```
### 3. Annotate methods for logging
```java
@Log("Create new task")
public void createTask(TaskDto dto) {
    // business logic
}
```

### 4. Generate JWT token
```java
AuthenticatedUser user = new AuthenticatedUser(...);
String token = jwtTokenProvider.generateToken(user);
```

### 5. Configure JWT properties in `application.yml`
```yaml
jwt:
  secret: your-secure-secret-key
  expiration: 3600000 # 1 hour
```

### Module Structure
```xml
ops-framework
├── config
│   └── SecurityConfiguration.java
├── security
│   ├── JwtTokenProvider.java
│   ├── JwtAuthenticationFilter.java
│   ├── AuthenticationEntryPointImpl.java
│   ├── AccessDeniedHandlerImpl.java
│   ├── AuthContextUtils.java
│   ├── UserDetailsServiceImpl.java
│   └── model
│       └── AuthenticatedUser.java
├── aop
│   └── LogAspect.java
├── annotation
│   └── Log.java
├── util
│   └── ServletUtils.java
```

### License
This module is part of the OpsPanel open-source platform. You are free to use, modify, and extend it under the terms of your chosen license.
