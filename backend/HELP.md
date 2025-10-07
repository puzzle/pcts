TODO: Delete this file and document whats necessary at the correct place!!

# Bean Validation
This branch represents a PoC, this should not be the final implementation, first and foremost the testing needs to be done still!
All other TODOS are marked in the Project with the following convention: `TODO: PoC: XY`


Springboot leverages the concept of Bean Validation to ensure that entities contain only expected values.
The Spring native way is to use `someControler(@Valid SomeEntity entity)` to validate the validity of the entity,
received from the API. Since we leverage the benefits of DTOs in this Project we can't directly use the 
annotation on the Controller input. However we can pretty easy invoke the validator in the `ValidationService`.
Namely we want to do it in the abstract `BaseValidationService`, to ensure validation happens on every entity.

On this branch, @Miguel7373 and @peggimann, implemented a PoC of such a workflow, which is explained in detail below.

## Invocation of Validator
In the `src/main/java/ch/puzzle/pcts/service/validation/MemberValidationService.java` we first inject the `BeanValidator`
into the class. 

NOTE: Our implementation differs from the standard shown in the code snippet below, this is explained later. 
```java
    ValidationBase(P persistenceService) {
        this.persistenceService = persistenceService;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }
```

### Validation of Entities
The actual validation of an entity passes in `src/main/java/ch/puzzle/pcts/service/validation/MemberValidationService.java`
in the method: `validate(Member member)` where we tell the validator to validate the member object, and then pass the 
result to a method called `processViolations(Set<ConstraintViolation<Member>> violations) `, which is responsible to show the error
in the format we expect, in order to handle the error properly in the frontend (This is done in ticket #145).

## Defining Validation Constraints
We define on the validation constraints on the model itself. An example of such is done in `src/main/java/ch/puzzle/pcts/model/member/Member.java`.
As you see there are different constraints defined a list of all available constraints of the jakarta.validation-api can be found 
[here](https://jakarta.ee/learn/docs/jakartaee-tutorial/current/beanvalidation/bean-validation/bean-validation.html).

In addition the constraints accept a message parameter. Each validation has a default constraint the problem is 
they neither specify which field is validated nor whats the actual value. There exists only two standard claims in the 
validation message we can leverage by default the `{min},{max}` claims and ONLY on the size constraint. More on that later.

However by default we can pass our own validation Messages if we want to. By default Spring scans the project for a file
at the root dir named `ValidationMessages.properties`. We implemented such a message template here: `src/main/resources/ValidationMessages.properties`.
This essentially allows us to define the error message via keys as shown in `src/main/java/ch/puzzle/pcts/model/member/Member.java`.
```java
    @NotBlank(message = "{attribute.not.blank}")
    @NotNull(message = "{attribute.notnull}")
    @Size(min = 2, max = 250, message = "{attribute.size.between}")
    private String lastName;
```
As you see we do no longer need to define on the field that is validated, its name nor its values or class. In the
template we have strings with placeholders as following:
```toml
attribute.size.between={class}.{field} size must be between {min} and {max}, given {value}
```
This is classic `toml` syntax as it is usual for `.properties` files.


## Substitution of Placeholders
Since we not only are interested in the boolean value whether a validation error occurred or not, but also want to know: 
* On which class did the error happen?
* On which field did the error happen?
* What was the given Value? 
* Potentially more Information....

We want this information to give the user a useful feedback. The naive approach would be to simply make translation messages
for every class and field. But not only would that be tedious but also we still wouldn't have a way to specify the value that 
violated the constraint.

Spring allows us to build a custom message interpolator, essentially allowing us to tailor the message to our needs.
This is documented [here](https://jakarta.ee/specifications/bean-validation/3.1/jakarta-validation-spec-3.1.html#validationapi-message-defaultmessageinterpolation-locale)

We implemented a PoC of that here: `src/main/java/ch/puzzle/pcts/service/validation/FieldAwareMessageInterpolator.java`
The method `public String interpolate(String messageTemplate, Context context, Locale locale)` searches the properties
defined in the message template and replaces them with the actual value. This allows us to add any information to the error message
we want. In our PoC we added `{class},{field},{value}` as properties. This can be tailored to our needs.

NOTE: Although we use the jakarta.validation-api this is only the api the actual implementation of the validation is provided by hibernate.

Now we only need a way to tell Spring to use our custom message interpolator this is done in the constructor injection 
of the `MemberValidationService.java` and will in future be done in `BaseValidationService.java`, in the following code
snippet you see how to configure the Validator to use our wrapped message interpolator:

```java
    public MemberValidationService(MemberPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
        try (ValidatorFactory factory = Validation
                .byDefaultProvider()
                .configure()
                .messageInterpolator(new FieldAwareMessageInterpolator(Validation
                        .byDefaultProvider()
                        .configure()
                        .getDefaultMessageInterpolator()))
                .buildValidatorFactory()) {
            validator = factory.getValidator();
        }
    }
```

Now we can dynamically set our attributes in the placeholders of the message template, allowing us to keep them concise and slim through the project.

# Conclusion
This PoC shows how we can achieve a simple, yet highly efficient validation of the entities, allowing us to safely interact with the
database and ensure entities actually contain the values we expect them to have. We prevent unexpected errors and provide valuable feedback to the client
in a form the client can handle as pleased. Even abstraction and i18n is straight forward.