package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.dto.degreeType.DegreeTypeNameDto;
import ch.puzzle.pcts.model.degreeType.DegreeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DegreeTypeRepository extends JpaRepository<DegreeType, Long> {

    @Query("SELECT id, name FROM DegreeType")
    List<DegreeTypeNameDto> findAllNames();
}
