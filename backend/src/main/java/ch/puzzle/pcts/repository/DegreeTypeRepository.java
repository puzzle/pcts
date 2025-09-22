package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.dto.degreeType.DegreeTypeNameDto;
import ch.puzzle.pcts.model.degreeType.DegreeType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DegreeTypeRepository extends JpaRepository<DegreeType, Long> {

    @Query("SELECT degreeTypeId, name FROM DegreeType")
    List<DegreeTypeNameDto> findAllNames();
}
