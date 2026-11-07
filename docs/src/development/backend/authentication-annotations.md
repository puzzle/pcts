# Authentication Annotations

Authorization on controllers is declared with custom annotations instead of
inline `@PreAuthorize` expressions. Each annotation wraps a `@PreAuthorize`
expression that delegates to `SecurityService`
(`ch.puzzle.pcts.service.SecurityService`), which is exposed to SpEL as the
bean `securityService`.

- `@IsAdmin`: only members of the configured admin group may access the
  endpoint.
- `@IsAdminOrOwner`: access is allowed if the caller is an admin, or the
  owner of the resource.
- `@IsAuthenticated`: any authenticated user may access the endpoint, with no
  further restriction.

Both annotations can be placed on a controller class to set the default for every
endpoint and/or on individual methods to override that default.

## Usage pattern

Put `@IsAdmin` on the class to make admin-only access the
default, then relax individual endpoints with `@IsAdminOrOwner` or
`@IsAuthenticated` where appropriate:

```java
@IsAdmin
@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    @GetMapping
    public ResponseEntity<List<MemberDto>> getMember() {
        // admin-only, inherited from the class-level @IsAdmin
    }

    @IsAdminOrOwner
    @GetMapping("{memberId}")
    public ResponseEntity<MemberDto> getMemberById(
            @PathVariable @P("id") Long memberId) {
        // admins, or the member themselves
    }

    @IsAuthenticated
    @GetMapping("myself")
    public ResponseEntity<MemberDto> getMyself() {
        // any authenticated user
    }
}
```

### Binding the owner id

`@IsAdminOrOwner` calls `isOwner(#id)`, so the path variable
that identifies the resource must be exposed to SpEL under the name `id`
using Spring Security's `@P` annotation:

```java
@PathVariable @P("id") Long memberId
```

Without `@P("id")`, the SpEL expression has no `#id` to resolve and the
check fails.
