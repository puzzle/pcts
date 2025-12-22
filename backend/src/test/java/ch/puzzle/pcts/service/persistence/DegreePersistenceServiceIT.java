package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.repository.DegreeRepository;
import ch.puzzle.pcts.util.TestData;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class DegreePersistenceServiceIT extends PersistenceBaseIT<Degree, DegreeRepository, DegreePersistenceService> {

    @Autowired
    DegreePersistenceServiceIT(DegreePersistenceService service) {
        super(service);
    }

    @Override
    Degree getModel() {
        return Degree.Builder
                .builder()
                .withId(null)
                .withMember(TestData.MEMBER_1)
                .withName("Degree 1")
                .withInstitution("Institution")
                .withCompleted(true)
                .withDegreeType(TestData.DEGREE_TYPE_1)
                .withStartDate(LocalDate.of(2015, 9, 1))
                .withEndDate(LocalDate.of(2020, 6, 1))
                .withComment("Comment")
                .build();
    }

    @Override
    List<Degree> getAll() {
        return TestData.DEGREES;
    }
}
