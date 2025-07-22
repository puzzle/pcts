# Backend Overview

The backend is structured into the following levels:
- `XYZController`: receives requests through the API for endpoint /xyz
- `Mapper`: maps from/to DTOs
- `XYZService`: the business service for XYZ
- `ValidationService`: validates business constraints
- `PersistenceService`: provides access to the database and any operations that need the database
- `Repository`: interface to the database


A hypothetical request is plotted in the diagram below. Note that some calls are optional.
```plantuml
@startuml

skinparam sequenceMessageAlign center

note left: A request comes through the REST API

-> Controller

opt
    note over Mapper: Sometimes a DTO/Model needs \n to be mapped to something else.
    Controller -> Mapper: Transform DTO to model
    Mapper -> Controller: Return model
end
Controller -> Service

Service -> ValidationService: Validate if the request is OK

opt
    note over ValidationService: Optionally the validation service can check \n certain conditions on the database.
    ValidationService -> PersistenceService
    PersistenceService -> ValidationService
end
ValidationService -> Service: Request is OK
Service -> PersistenceService
PersistenceService -> Repository: Save to DB
Repository -> PersistenceService
PersistenceService -> Service
Service -> Controller
Controller -> Mapper: Transform model to DTO
Mapper -> Controller: Return DTO
note left: The answer gets sent back through the REST API


@enduml
```