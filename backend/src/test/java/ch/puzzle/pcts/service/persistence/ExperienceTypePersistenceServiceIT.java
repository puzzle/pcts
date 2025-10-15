package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ExperienceTypePersistenceServiceIT extends PersistenceCoreIT {

    @Autowired
    private ExperienceTypePersistenceService persistenceService;

    @DisplayName("Should get experienceType by id")
    @Test
    void shouldGetExperienceTypeById() {
        Optional<ExperienceType> experienceType = persistenceService.getById(2L);

        assertThat(experienceType).isPresent();
        assertThat(experienceType.get().getId()).isEqualTo(2L);
    }

    @DisplayName("Should get all experienceTypes")
    @Test
    void shouldGetAllExperienceTypes() {
        List<ExperienceType> all = persistenceService.getAll();

        assertThat(all).hasSize(2);
        assertThat(all)
                .extracting(ExperienceType::getName)
                .containsExactlyInAnyOrder("ExperienceType 1", "ExperienceType 2");
    }

    @DisplayName("Should create experienceType")
    @Transactional
    @Test
    void shouldCreate() {
        ExperienceType experienceType = new ExperienceType(null,
                                                           "ExperienceType 3",
                                                           BigDecimal.valueOf(10.055),
                                                           BigDecimal.valueOf(5.603),
                                                           BigDecimal.valueOf(2.005));

        ExperienceType result = persistenceService.create(experienceType);

        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo(experienceType.getName());
        assertThat(result.getHighlyRelevantPoints()).isEqualByComparingTo(BigDecimal.valueOf(10.055));
        assertThat(result.getLimitedRelevantPoints()).isEqualByComparingTo(BigDecimal.valueOf(5.603));
        assertThat(result.getLittleRelevantPoints()).isEqualByComparingTo(BigDecimal.valueOf(2.005));
    }

    @DisplayName("Should update experienceType")
    @Transactional
    @Test
    void shouldUpdate() {
        Long id = 2L;
        ExperienceType updated = new ExperienceType(null,
                                                    "Updated experienceType",
                                                    BigDecimal.valueOf(10.055),
                                                    BigDecimal.valueOf(5.603),
                                                    BigDecimal.valueOf(2.005));

        persistenceService.update(id, updated);
        Optional<ExperienceType> result = persistenceService.getById(id);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(result.get().getName()).isEqualTo("Updated experienceType");
        assertThat(result.get().getHighlyRelevantPoints()).isEqualByComparingTo(BigDecimal.valueOf(10.055));
        assertThat(result.get().getLimitedRelevantPoints()).isEqualByComparingTo(BigDecimal.valueOf(5.603));
        assertThat(result.get().getLittleRelevantPoints()).isEqualByComparingTo(BigDecimal.valueOf(2.005));
    }

    @DisplayName("Should delete experienceType")
    @Transactional
    @Test
    void shouldDelete() {
        Long id = 2L;

        persistenceService.delete(id);

        Optional<ExperienceType> result = persistenceService.getById(id);
        assertThat(result).isEmpty();
    }
}
