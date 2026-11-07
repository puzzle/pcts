# Frontend Overview

The frontend is structured into the following levels:

- `Resolver`: pre-fetches the data a route needs before its component is activated
- `Component`: renders the UI, reacts to user input and injects the services it needs
- `Service`: communicates with the backend REST API via `HttpClient` and maps between DTOs and models
- `DTO`: the shape of data as sent to/received from the backend
- `Model`: the shape of data as used within the frontend application
- `Interceptor`: handles cross-cutting concerns for every HTTP request/response, e.g. `error-interceptor` and `success-interceptor`

A hypothetical request is plotted in the diagram below. Note that
some calls are optional.

```plantuml
@startuml
skinparam sequenceMessageAlign center

actor User

note left of Router: The user navigates to a route

User -> Router: GET /members

Router -> Resolver: Resolve route data

note over Resolver
    Resolvers pre-fetch the data a
    component needs before it is activated.
end note
Resolver -> Service: Load data
Service -> Backend: GET /api/v1/members
Backend -> Service: 200 OK
Service -> Resolver: Return model(s)
Resolver -> Router: Return model(s)

Router -> Component: Activate with resolved data
Component --> User: Render view

...

note over Component
    Subsequent user interactions,
    e.g. submitting a form, trigger
    further service calls.
end note
User -> Component: Submit form
Component -> Service: Save data
Service -> Backend: POST /api/v1/members

opt
    note over Interceptor
        Interceptors handle cross-cutting
        concerns, such as showing an error
        or success toast, for every HTTP call.
    end note
    Backend -> Interceptor: Error response
    Interceptor --> Component: Propagate error
end

Backend -> Service: 200 OK
Service -> Component: Return model
Component --> User: Update view

@enduml
```
