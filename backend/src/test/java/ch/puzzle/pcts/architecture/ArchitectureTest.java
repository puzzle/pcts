package ch.puzzle.pcts.architecture;

import static ch.puzzle.pcts.architecture.CustomTransformers.annotations;
import static ch.puzzle.pcts.architecture.CustomTransformers.packages;
import static ch.puzzle.pcts.architecture.condition.AnnotationConditions.haveSuffix;
import static ch.puzzle.pcts.architecture.condition.AnnotationConditions.haveValuePrefix;
import static ch.puzzle.pcts.architecture.condition.AnnotationConditions.shouldBeValidDescription;
import static ch.puzzle.pcts.architecture.condition.ClassConditions.followPattern;
import static ch.puzzle.pcts.architecture.condition.ClassConditions.overrideEqualsMethod;
import static ch.puzzle.pcts.architecture.condition.ClassConditions.overrideHashCodeMethod;
import static ch.puzzle.pcts.architecture.condition.ClassConditions.overrideToStringMethod;
import static ch.puzzle.pcts.architecture.condition.CodeUnitConditions.trimAssignedStringFields;
import static com.tngtech.archunit.base.DescribedPredicate.not;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.*;
import static com.tngtech.archunit.lang.conditions.ArchConditions.and;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import ch.puzzle.pcts.model.Model;
import com.tngtech.archunit.core.domain.*;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.library.Architectures;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

class ArchitectureTest {

    @DisplayName("Repositories should only be accessed by persistence services")
    @Test
    void repositoriesShouldOnlyBeAccessedByPersistenceServices() {
        JavaClasses importedClasses = getMainSourceClasses();
        ArchRule rule = classes()
                .that()
                .resideInAPackage("..repository..")
                .should()
                .onlyBeAccessed()
                .byAnyPackage("..service.persistence..", "..repository")
                .andShould()
                .beInterfaces();

        rule.check(importedClasses);
    }

    @DisplayName("Repositories should not access other repositories")
    @Test
    void repositoriesShouldNotAccessEachOther() {
        JavaClasses importedClasses = getMainSourceClasses();
        ArchRule rule = noClasses()
                .that()
                .resideInAnyPackage("ch.puzzle.pcts.repository..")
                .should()
                .onlyDependOnClassesThat()
                .resideInAPackage("ch.puzzle.pcts.repository..")
                .andShould()
                .onlyDependOnClassesThat()
                .areAnnotatedWith(NoRepositoryBean.class);

        rule.check(importedClasses);
    }

    @DisplayName("Mappers should only be accessed by themselves and controllers")
    @Test
    void mappersShouldOnlyBeAccessedByThemselvesAndControllersAndAuthorization() {
        JavaClasses importedClasses = getMainSourceClasses();
        ArchRule rule = classes()
                .that()
                .resideInAPackage("..mapper..")
                .should()
                .onlyBeAccessed()
                .byClassesThat(resideInAPackage("..controller..")
                        .or(resideInAPackage("..mapper.."))
                        .or(type(ch.puzzle.pcts.GlobalExceptionHandler.class)));

        rule.check(importedClasses);
    }

    @DisplayName("Business services should only be accessed by specific layers and components")
    @Test
    void businessServicesShouldOnlyBeAccessedByThemselvesAndControllersAndAuthorizationAndMappers() {
        JavaClasses importedClasses = getMainSourceClasses();
        ArchRule rule = classes()
                .that()
                .resideInAPackage("..service.business..")
                .should()
                .onlyBeAccessed()
                .byAnyPackage("..controller..", "..mapper..", "..service.business..");

        rule.check(importedClasses);
    }

    @DisplayName("Controllers should not directly call repositories")
    @Test
    void controllersShouldCallNoRepositories() {
        JavaClasses importedClasses = getMainSourceClasses();

        ArchRule rule = noClasses()
                .that()
                .resideInAPackage("ch.puzzle.pcts.controller..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..repository..")
                .andShould()
                .notBeInterfaces();

        rule.check(importedClasses);
    }

    @DisplayName("Controllers should not directly call persistence services")
    @Test
    void controllersShouldCallNoPersistenceServices() {
        JavaClasses importedClasses = getMainSourceClasses();

        ArchRule rule = noClasses()
                .that()
                .resideInAPackage("ch.puzzle.pcts.controller..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..persistence..");

        rule.check(importedClasses);
    }

    @DisplayName("Repositories should not call services")
    @Test
    void repositoriesShouldCallNoServices() {
        JavaClasses importedClasses = getMainSourceClasses();

        ArchRule rule = noClasses()
                .that()
                .resideInAPackage("ch.puzzle.pcts.repository")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..service..")
                .andShould()
                .beInterfaces();

        rule.check(importedClasses);
    }

    @DisplayName("Service classes should be annotated with @Service")
    @Test
    void servicesShouldBeAnnotatedWithService() {
        JavaClasses importedClasses = getMainSourceClasses();

        ArchRule rule = classes()
                .that()
                .areNotInterfaces()
                .and()
                .areNotAnonymousClasses()
                .and()
                .resideInAPackage("ch.puzzle.pcts.service..")
                .and(not(resideInAPackage("ch.puzzle.pcts.service.validation.util..")))
                .should()
                .beAnnotatedWith(Service.class)
                .andShould()
                .notBeInterfaces();

        rule.check(importedClasses);
    }

    @DisplayName("Controller classes should be annotated with @RestController")
    @Test
    void controllersShouldBeAnnotatedWithRestController() {
        JavaClasses importedClasses = getMainSourceClasses();

        ArchRule rule = classes()
                .that()
                .areNotAnonymousClasses()
                .and()
                .resideInAPackage("ch.puzzle.pcts.controller..")
                .should()
                .beAnnotatedWith(RestController.class)
                .andShould()
                .notBeInterfaces();

        rule.check(importedClasses);
    }

    @DisplayName("@RequestMappings should have common pre- and suffix")
    @Test
    void controllersShouldDefineCorrectRequestMapping() {
        JavaClasses importedClasses = getMainSourceClasses();

        ArchCondition<JavaAnnotation<JavaClass>> combinedCondition = and(haveSuffix("value", "s"),
                                                                         haveValuePrefix("/api/v1/"));

        ArchRule rule = all(annotations(RequestMapping.class)).should(combinedCondition);

        rule.check(importedClasses);
    }

    @DisplayName("Controller @Tags should be valid")
    @Test
    void controllerTagsShouldBeCompleteSentences() {
        JavaClasses importedClasses = getMainSourceClasses();

        ArchCondition<JavaAnnotation<JavaClass>> combinedCondition = and(shouldBeValidDescription("description"),
                                                                         haveSuffix("name", "s"));

        ArchRule rule = all(annotations(Tag.class)).should(combinedCondition);

        rule.check(importedClasses);
    }

    @DisplayName("Mapper classes should be annotated with @Component")
    @Test
    void mappersShouldBeAnnotatedWithComponent() {
        JavaClasses importedClasses = getMainSourceClasses();

        ArchRule rule = classes()
                .that()
                .areNotAnonymousClasses()
                .and()
                .resideInAPackage("ch.puzzle.pcts.mapper")
                .should()
                .beAnnotatedWith(Component.class)
                .andShould()
                .notBeInterfaces();

        rule.check(importedClasses);
    }

    @DisplayName("Repositories should be annotated with @Repository")
    @Test
    void repositoryShouldBeAnnotatedWithRepository() {
        JavaClasses importedClasses = getMainSourceClasses();

        ArchRule rule = classes()
                .that()
                .resideInAPackage("ch.puzzle.pcts.repository..")
                .should()
                .beAnnotatedWith(Repository.class)
                .orShould()
                .beAnnotatedWith(NoRepositoryBean.class)
                .andShould()
                .beInterfaces();

        rule.check(importedClasses);
    }

    @DisplayName("DTOs should be records")
    @Test
    void dtoShouldBeRecord() {
        JavaClasses importedClasses = getMainSourceClasses();

        ArchRule rule = classes()
                .that()
                .resideInAPackage("ch.puzzle.pcts.dto..")
                .and(not(resideInAPackage("ch.puzzle.pcts.dto.error..")))
                .should()
                .beRecords();

        rule.check(importedClasses);
    }

    @DisplayName("Models should override methods")
    @Test
    void modelShouldOverrideMethods() {
        JavaClasses importedClasses = getMainSourceClasses();

        ArchRule rule = classes()
                .that(resideInAPackage("ch.puzzle.pcts.model.."))
                .and()
                .areNotInterfaces()
                .and()
                .areNotEnums()
                .and()
                .areNotNestedClasses()
                .should(overrideEqualsMethod)
                .andShould(overrideHashCodeMethod)
                .andShould(overrideToStringMethod);

        rule.check(importedClasses);
    }

    @DisplayName("Models should have @Entity annotation")
    @Test
    void modelShouldHaveEntityAnnotation() {
        JavaClasses importedClasses = getMainSourceClasses();

        ArchRule rule = classes()
                .that(resideInAPackage("ch.puzzle.pcts.model.."))
                .and()
                .doNotHaveSimpleName("MemberOverviewId")
                .and()
                .areNotInterfaces()
                .and()
                .areNotEnums()
                .and()
                .areNotNestedClasses()
                .should()
                .beAnnotatedWith(Entity.class);

        rule.check(importedClasses);
    }

    @DisplayName("Models should implement interface")
    @Test
    void modelShouldImplementInterface() {
        JavaClasses importedClasses = getMainSourceClasses();

        ArchRule rule = classes()
                .that(resideInAPackage("ch.puzzle.pcts.model.."))
                .and(not(resideInAnyPackage("ch.puzzle.pcts.model.memberoverview..")))
                .and()
                .areNotInterfaces()
                .and()
                .areNotEnums()
                .and()
                .areNotNestedClasses()
                .should()
                .implement(Model.class);

        rule.check(importedClasses);
    }

    @DisplayName("Models with ManyToOne relations should have fetch type LAZY")
    @Test
    void modelShouldHaveFetchTypeLazy() {
        JavaClasses importedClasses = getMainSourceClasses();

        ArchRule rule = fields()
                .that()
                .areDeclaredInClassesThat()
                .resideInAPackage("ch.puzzle.pcts.model..")
                .and()
                .areAnnotatedWith(ManyToOne.class)
                .should(haveLazyManyToOne());

        rule.check(importedClasses);
    }

    @DisplayName("Classes should reside in the correct packages based on naming conventions")
    @ParameterizedTest
    @ValueSource(strings = { "controller", "service", "mapper", "repository", "dto", "exception" })
    void classesShouldBeInCorrectPackages(String passedName) {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("ch.puzzle.pcts");

        ArchRule rule = classes()
                .that()
                .haveSimpleNameEndingWith(StringUtils.capitalize(passedName))
                .and()
                .areTopLevelClasses()
                .should()
                .resideInAPackage("ch.puzzle.pcts." + passedName + "..");

        rule.check(importedClasses);
    }

    @DisplayName("Service layer architecture should comply with defined layer rules")
    @Test
    void serviceLayerCheck() {
        JavaClasses importedClasses = getMainSourceClasses();
        Architectures.LayeredArchitecture layeredArchitecture = layeredArchitecture()
                .consideringAllDependencies() //
                .layer("Controller")
                .definedBy("..controller..") //
                .layer("BusinessService")
                .definedBy("..service.business..") //
                .layer("ValidationService")
                .definedBy("..service.validation..") //
                .layer("PersistenceService")
                .definedBy("..service.persistence..") //
                .layer("Repository")
                .definedBy("..repository..") //
                .layer("Mapper")
                .definedBy("..mapper..") //

                .whereLayer("Controller")
                .mayNotBeAccessedByAnyLayer() //
                .whereLayer("BusinessService")
                .mayOnlyBeAccessedByLayers("Controller", "Mapper", "BusinessService") //
                .whereLayer("ValidationService")
                .mayOnlyBeAccessedByLayers("BusinessService") //
                .whereLayer("PersistenceService")
                .mayOnlyBeAccessedByLayers("BusinessService", "PersistenceService", "ValidationService") //
                .whereLayer("Repository")
                .mayOnlyBeAccessedByLayers("PersistenceService", "BusinessService"); //

        layeredArchitecture.check(importedClasses);
    }

    @DisplayName("Setting String-Fields must invoke a trim() call before setting")
    @Test
    void settingStringFieldsMustInvokeTrim() {
        JavaClasses importedClasses = getMainSourceClasses();

        ArchRule methodRule = methods()
                .that()
                .areDeclaredInClassesThat()
                .resideInAPackage("ch.puzzle.pcts.model..")
                .should(trimAssignedStringFields());

        ArchRule constructorRule = constructors()
                .that()
                .areDeclaredInClassesThat()
                .resideInAPackage("ch.puzzle.pcts.model..")
                .should(trimAssignedStringFields());

        methodRule.check(importedClasses);
        constructorRule.check(importedClasses);
    }

    private static JavaClasses getMainSourceClasses() {
        return new ClassFileImporter()
                .withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("ch.puzzle.pcts");
    }

    @DisplayName("All packages must contain only lowercase letters and dots, and cannot end on a dot")
    @Test
    void packagesMustFollowPattern() {
        // Package must only contain a-z and dots, it also cannot end on a dot
        final String pattern = "^[a-z.]+[a-z]$";
        JavaClasses importedClasses = getMainSourceClasses();

        ArchRule followsPatternRule = all(packages()).should(followPattern(pattern));

        followsPatternRule.check(importedClasses);
    }

    private static ArchCondition<JavaField> haveLazyManyToOne() {
        return new ArchCondition<>("have @ManyToOne(fetch = LAZY)") {
            @Override
            public void check(JavaField field, ConditionEvents events) {
                ManyToOne annotation = field.getAnnotationOfType(ManyToOne.class);

                if (annotation.fetch() != FetchType.LAZY) {
                    String message = String
                            .format("Field %s.%s has @ManyToOne with fetch=%s",
                                    field.getOwner().getName(),
                                    field.getName(),
                                    annotation.fetch());

                    events.add(SimpleConditionEvent.violated(field, message));
                }
            }
        };
    }
}
