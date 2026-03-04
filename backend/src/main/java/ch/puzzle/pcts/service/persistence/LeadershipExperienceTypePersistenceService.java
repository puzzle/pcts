package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.LEADERSHIP_EXPERIENCE_TYPE;

import ch.puzzle.pcts.model.leadershipexperiencetype.LeadershipExperienceType;
import ch.puzzle.pcts.repository.LeadershipExperienceTypeRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class LeadershipExperienceTypePersistenceService
        extends
            PersistenceBase<LeadershipExperienceType, LeadershipExperienceTypeRepository> {

    private final LeadershipExperienceTypeRepository repository;

    protected LeadershipExperienceTypePersistenceService(LeadershipExperienceTypeRepository leadershipExperienceTypeRepository) {
        super(leadershipExperienceTypeRepository);
        repository = leadershipExperienceTypeRepository;
    }

    public Optional<LeadershipExperienceType> getByName(String name) {
        return repository.findByNameOfLeadershipExperienceType(name);
    }

    @Override
    public List<LeadershipExperienceType> getAll() {
        return repository.findAllOfLeadershipExperienceType();
    }

    @Override
    public LeadershipExperienceType getById(Long id) {
        return repository.findByIdOfLeadershipExperienceType(id).orElseThrow(() -> throwNotFoundError(id.toString()));
    }

    @Override
    public String entityName() {
        return LEADERSHIP_EXPERIENCE_TYPE;
    }
}
