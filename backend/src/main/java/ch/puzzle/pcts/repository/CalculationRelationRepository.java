package ch.puzzle.pcts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * @param <T>
 *            the Type or entity of the repository
 * @param <I>
 *            the Type of the ID of the entity
 **/
@NoRepositoryBean
public interface CalculationRelationRepository<T, I> extends JpaRepository<T, I> {
    @Query("select t from #{#entityName} t where t.calculation.id = :calculationId")
    List<T> findByCalculationId(Long calculationId);
}
