# Backend Overview

The backend is structured into the following levels:

- `XYZController`: receives requests through the API for endpoint /xyz
- `Mapper`: maps from/to DTOs
- `XYZService`: the business service for XYZ
- `ValidationService`: validates business constraints
- `PersistenceService`: provides access to the database and any
  operations that need the database
- `Repository`: interface to the database

A hypothetical request is plotted in the diagram below. Note that
some calls are optional.

```plantuml
@startuml
skinparam sequenceMessageAlign center

actor User

note left of Controller: A request comes through the REST API

User -> Controller: GET /resource


note over Mapper
    Usually a DTO/Model needs
    to be mapped to something else.
end note
Controller -> Mapper: Transform DTO to model
Mapper -> Controller: Return model


Controller -> BusinessService

BusinessService -> ValidationService: Validate if the request is OK

opt
    note over ValidationService
        Optionally the validation service can check
        certain conditions on the database.
    end note
    ValidationService -> PersistenceService
    PersistenceService -> ValidationService
end

ValidationService -> BusinessService: Request is OK
BusinessService -> PersistenceService
PersistenceService -> Repository: Save to DB
Repository -> PersistenceService
PersistenceService -> BusinessService
BusinessService -> Controller
Controller -> Mapper: Transform model to DTO
Mapper -> Controller: Return DTO



Controller --> User: 200 OK
note right of User: The answer gets sent back through the REST API

@enduml
```
