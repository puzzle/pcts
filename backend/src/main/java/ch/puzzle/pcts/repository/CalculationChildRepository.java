package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.calculation.CalculationChild;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * This repository contains a method to get the entity by calculation id
 * @param <T> the Type or entity of the repository has to be a calculation child.
 * @param <I> the Type of the ID of the entity
 **/
@NoRepositoryBean
public interface CalculationChildRepository<T extends CalculationChild, I> extends JpaRepository<T, I> {
    List<T> findByCalculationId(Long calculationId);
}
