package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestDataModels.*;

import ch.puzzle.pcts.model.leadershipexperiencetype.LeadershipExperienceType;
import ch.puzzle.pcts.repository.LeadershipExperienceTypeRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class LeadershipTypePersistenceServiceIT
        extends
            PersistenceBaseIT<LeadershipExperienceType, LeadershipExperienceTypeRepository, LeadershipExperienceTypePersistenceService> {

    @Autowired
    LeadershipTypePersistenceServiceIT(LeadershipExperienceTypePersistenceService service) {
        super(service);
    }

    @Override
    LeadershipExperienceType getModel() {
        return LEADERSHIP_TYPE_2;
    }

    @Override
    List<LeadershipExperienceType> getAll() {
        return LEADERSHIP_EXPERIENCE_TYPES;
    }
}