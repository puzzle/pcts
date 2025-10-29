package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.repository.ExperienceRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class ExperiencePersistenceServiceIT
        extends
            PersistenceBaseIT<Experience, ExperienceRepository, ExperiencePersistenceService> {
    private final List<Member> members;
    private final List<ExperienceType> experienceTypes;

    @Autowired
    ExperiencePersistenceServiceIT(ExperiencePersistenceService service,
                                   MemberPersistenceService memberPersistenceService,
                                   ExperienceTypePersistenceService experienceTypePersistenceService) {
        super(service);
        this.members = new MemberPersistenceServiceIT(memberPersistenceService).getAll();
        this.experienceTypes = new ExperienceTypePersistenceServiceIT(experienceTypePersistenceService).getAll();
    }

    @Override
    Experience getModel() {
        return new Experience.Builder()
                .withMember(members.getFirst())
                .withName("Experience 4")
                .withEmployer("Employer 4")
                .withPercent(100)
                .withType(experienceTypes.getFirst())
                .withComment("Comment test 4")
                .withStartDate(LocalDate.of(2021, 7, 15))
                .withEndDate(LocalDate.of(2022, 7, 15))
                .build();
    }

    List<Experience> getAll() {
        List<Experience> experiences = new ArrayList<>();
        experiences
                .add(new Experience.Builder()
                        .withId(2L)
                        .withMember(members.getFirst())
                        .withName("Experience 2")
                        .withEmployer("Employer 2")
                        .withPercent(80)
                        .withType(experienceTypes.get(1))
                        .withComment("Comment test 2")
                        .withStartDate(LocalDate.of(2022, 7, 16))
                        .withEndDate(LocalDate.of(2023, 7, 15))
                        .build());

        experiences
                .add(new Experience.Builder()
                        .withId(3L)
                        .withMember(members.get(1))
                        .withName("Experience 3")
                        .withEmployer("Employer 3")
                        .withPercent(60)
                        .withType(experienceTypes.getFirst())
                        .withComment("Comment test 3")
                        .withStartDate(LocalDate.of(2023, 7, 16))
                        .withEndDate(LocalDate.of(2024, 7, 15))
                        .build());

        return experiences;
    }
}