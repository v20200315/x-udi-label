# Branch: `auth-keycloak-phase-1`

## 1. Overview

This branch implements Phase 1 authentication for `x-udi-label` using Keycloak as the external Identity Provider.

The primary objective is to establish a working authentication foundation without implementing the company-wide IAM system yet.

Keycloak is temporarily responsible for user authentication, while `x-udi-label` maintains its own application user records and business data.

## 2. Technology Stack

- Java 21
- Spring Boot 4
- Spring Security 7
- Spring OAuth2 Client / OpenID Connect (OIDC)
- Keycloak
- MySQL
- MyBatis-Plus
- Thymeleaf
- Tabler UI
- Maven

## 3. Authentication Architecture

### 3.1 Responsibilities

**Keycloak**

- User registration
- User login and logout
- Password management
- Email verification
- OIDC authentication
- Client roles

**x-udi-label**

- Application user management
- Mapping Keycloak identities to internal users
- Application-specific user status
- Business authorization and access control

### 3.2 Authentication Flow

1. An unauthenticated user accesses the application.
2. Spring Security redirects the user to Keycloak.
3. The user authenticates through Keycloak.
4. Keycloak returns an OIDC authentication response.
5. `KeycloakOidcUserService` loads the OIDC user.
6. The application extracts the external identity information:
   - Issuer
   - Subject (`sub`)
   - Email
   - Display name
   - Email verification status
7. `UserApplicationService` finds or creates the corresponding application user.
8. `ApplicationOidcUser` wraps the original OIDC principal and exposes the internal application user ID.
9. Spring Security establishes an authenticated session.
10. The user is redirected to `/console`.

### 3.3 Identity Mapping

The application uses its own UUID as the internal user ID.

Keycloak's `sub` is treated as an external identity identifier, not as the application's internal user ID.

The identity mapping is based on:

```text
(issuer, subject) -> application_user_id
```

Email is not used as the sole identity-matching key.

## 4. Application User Data Model

Two main tables are used:

### `x_application_user`

Stores application-level user information.

- `id`
- `email`
- `display_name`
- `status`
- `created_at`
- `created_by`
- `updated_at`
- `updated_by`

### `x_user_identity`

Maps external identities to application users.

- `id`
- `user_id`
- `issuer`
- `subject`
- `created_at`
- `created_by`
- `updated_at`
- `updated_by`

Unique constraints:

```sql
UNIQUE (issuer, subject)
UNIQUE (user_id, issuer)
```

A dedicated internal `SystemUser` is used as the audit operator when application users are provisioned during their first login.

## 5. Authorization

Keycloak client roles are used as the initial role source.

Current role examples:

- `role_admin`
- `role_operator`
- `role_viewer`

Application permissions can be expressed as authorities, for example:

- `product:create`
- `product:view`
- `label:generate`
- `label:view`

Spring Security method-level authorization is enabled through `@EnableMethodSecurity`.

New users should receive the default `role_viewer` role.

## 6. Session Management

The application currently uses server-side HTTP sessions.

This is appropriate for the current architecture:

- Thymeleaf
- Tabler
- Server-rendered pages
- Spring Security

JWT-based stateless authentication is not part of this phase.

Logout uses Keycloak OIDC RP-initiated logout and redirects to `/logged-out`.

## 7. Security Components

The main security components include:

- `SecurityConfig`
- `KeycloakOidcUserService`
- `ApplicationOidcUser`
- `KeycloakAuthoritiesMapper`
- `SecurityCurrentUserContext`
- `CurrentUserContext`

`CurrentUserContext` provides access to the authenticated application's internal user ID and external Keycloak subject.

## 8. Future Direction: Company Management

The next business-side functionality is a simple company creation and account-binding model.

No company verification, membership approval, or Tenant provisioning workflow is included in the initial scope.

### Business Rules

1. One Account can bind multiple Companies.
2. Multiple Accounts can bind to the same Company.
3. A Company has a name and a unique code.
4. When a user submits a company code:
   - If the Company does not exist, create it.
   - If it already exists, reuse the existing Company.
   - Create the Account–Company binding if it does not already exist.
5. The same Account cannot bind to the same Company more than once.
6. Existing company information should not be overwritten during binding.

### Proposed Data Model

**`company`**

- `id`
- `name`
- `code`
- `created_at`
- `created_by`

**`account_company`**

- `id`
- `account_id`
- `company_id`
- `created_at`

Database constraints:

```sql
UNIQUE (code)
UNIQUE (account_id, company_id)
```

### Proposed Application Service

```java
public interface CompanyApplicationService {

    UUID createOrBindCompany(CreateCompanyRequest request);

    List<CompanySummary> getMyCompanies();

    void unbindCompany(UUID companyId);
}
```

The current authenticated Account ID should be obtained through `CurrentUserContext`, rather than accepted directly from the frontend.

## 9. Future Architecture Direction

Keycloak is an interim authentication solution for Phase 1.

The long-term direction is to introduce a centralized company IAM platform responsible for:

- Person and account identity
- Organization and company management
- Account–company relationships
- Tenant management
- Roles and permissions
- Authentication and authorization integration

The application should eventually integrate with the company IAM platform instead of independently managing identity mappings and authorization rules in every business system.

## 10. Scope Boundaries

**Included in Phase 1**

- Keycloak OIDC integration
- Login and logout
- Application user provisioning on first successful login
- External identity mapping
- Internal application user ID
- Session-based authentication
- Initial role and authority mapping

**Not included in Phase 1**

- Centralized company IAM
- Cross-application SSO integration through the future IAM
- Company verification
- Tenant provisioning
- Advanced RBAC / ABAC / ReBAC
- JWT resource-server architecture
- Distributed authorization services

## 11. Key Design Principles

- Keycloak owns authentication; the application owns its business data.
- Internal user IDs are independent of external identity-provider IDs.
- External identities are mapped using issuer and subject.
- User provisioning occurs on the first successful OIDC login.
- Authentication infrastructure should be reusable across business applications.
- Avoid introducing unnecessary microservices or complex IAM abstractions during Phase 1.

---

**Branch:** `auth-keycloak-phase-1`

**Purpose:** Establish the initial Keycloak-based authentication foundation for `x-udi-label`, providing a transition path toward the future centralized company IAM platform.

## Implementation Reference

[`KeycloakOidcUserService.java`](src/main/java/com/lsxuan/udilabel/infrastructure/security/KeycloakOidcUserService.java)
