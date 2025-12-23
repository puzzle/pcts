package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestData.*;

import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.repository.ExperienceRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class ExperiencePersistenceServiceIT
        extends
            PersistenceBaseIT<Experience, ExperienceRepository, ExperiencePersistenceService> {

    @Autowired
    ExperiencePersistenceServiceIT(ExperiencePersistenceService service) {
        super(service);
    }

    @Override
    Experience getModel() {
        return new Experience.Builder()
                .withMember(MEMBER_1)
                .withName("Experience 4")
                .withEmployer("Employer 4")
                .withPercent(100)
                .withType(EXP_TYPE_1)
                .withComment("Comment test 4")
                .withStartDate(LocalDate.of(2021, 7, 15))
                .withEndDate(LocalDate.of(2022, 7, 15))
                .build();
    }

    List<Experience> getAll() {
        return EXPERIENCES;
    }
}