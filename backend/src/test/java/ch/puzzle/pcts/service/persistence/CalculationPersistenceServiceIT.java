package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.repository.CalculationRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class CalculationPersistenceServiceIT
        extends
            PersistenceBaseIT<Calculation, CalculationRepository, CalculationPersistenceService> {

    MemberPersistenceServiceIT memberPersistenceServiceIT;
    RolePersistenceServiceIT rolePersistenceServiceIT;

    @Autowired
    CalculationPersistenceServiceIT(CalculationPersistenceService service,
                                    MemberPersistenceService memberPersistenceService,
                                    RolePersistenceService rolePersistenceService) {
        super(service);
        memberPersistenceServiceIT = new MemberPersistenceServiceIT(memberPersistenceService);
        rolePersistenceServiceIT = new RolePersistenceServiceIT(rolePersistenceService);
    }

    @Override
    Calculation getModel() {
        return new Calculation(null,
                               memberPersistenceServiceIT.getAll().getFirst(),
                               rolePersistenceServiceIT.getAll().getFirst(),
                               CalculationState.ACTIVE,
                               LocalDate.of(2021, 12, 9),
                               "Ldap User");
    }

    @Override
    List<Calculation> getAll() {
        return List
                .of(new Calculation(1L,
                                    memberPersistenceServiceIT.getAll().getFirst(),
                                    rolePersistenceServiceIT.getAll().getLast(),
                                    CalculationState.DRAFT,
                                    LocalDate.of(2025, 1, 14),
                                    "Ldap User"),
                    new Calculation(2L,
                                    memberPersistenceServiceIT.getAll().getLast(),
                                    rolePersistenceServiceIT.getAll().getLast(),
                                    CalculationState.ARCHIVED,
                                    null,
                                    "Ldap User 2"),
                    new Calculation(3L,
                                    memberPersistenceServiceIT.getAll().getLast(),
                                    rolePersistenceServiceIT.getAll().getLast(),
                                    CalculationState.ACTIVE,
                                    LocalDate.of(2025, 1, 14),
                                    null));
    }
}
