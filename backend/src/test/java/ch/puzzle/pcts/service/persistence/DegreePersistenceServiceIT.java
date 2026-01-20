package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestDataModels.*;

import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.repository.DegreeRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class DegreePersistenceServiceIT extends PersistenceBaseIT<Degree, DegreeRepository, DegreePersistenceService> {

    @Autowired
    DegreePersistenceServiceIT(DegreePersistenceService service) {
        super(service);
    }

    @Override
    Degree getModel() {
        return DEGREE_2;
    }

    @Override
    List<Degree> getAll() {
        return DEGREES;
    }
}
