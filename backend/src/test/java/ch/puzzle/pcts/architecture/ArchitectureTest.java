package ch.puzzle.pcts.architecture;

import static ch.puzzle.pcts.architecture.CustomConditions.trimAssignedStringFields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

import com.tngtech.archunit.core.domain.*;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ArchitectureTest {

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
}
