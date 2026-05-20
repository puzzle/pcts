package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.EXPERIENCE_TYPE;

import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.repository.ExperienceTypeRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ExperienceTypePersistenceService extends PersistenceBase<ExperienceType, ExperienceTypeRepository> {
    private final ExperienceTypeRepository experienceTypeRepository;

    public ExperienceTypePersistenceService(ExperienceTypeRepository experienceTypeRepository) {
        super(experienceTypeRepository);
        this.experienceTypeRepository = experienceTypeRepository;
    }

    @Override
    public String entityName() {
        return EXPERIENCE_TYPE;
    }

    public Optional<ExperienceType> getByName(String name) {
        return experienceTypeRepository.findByName(name);
    }
}
