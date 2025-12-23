package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.calculation.CalculationChild;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @param <T>
 *            the Type or entity of the repository
 * @param <I>
 *            the Type of the ID of the entity
 **/
@NoRepositoryBean
public interface CalculationChildRepository<T extends CalculationChild, I> extends JpaRepository<T, I> {
    @Query("select t from #{#entityName} t where t.calculation.id = :calculationId")
    List<T> findByCalculationId(Long calculationId);
}
