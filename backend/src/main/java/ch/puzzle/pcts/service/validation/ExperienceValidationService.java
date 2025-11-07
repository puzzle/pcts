package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.experience.Experience;
import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ExperienceValidationService extends ValidationBase<Experience> {

    @Override
    public void validateOnCreate(Experience experience) {
        super.validateOnCreate(experience);
        validateStartDateIsBeforeEndDate(experience.getStartDate(), experience.getEndDate());
    }

    @Override
    public void validateOnUpdate(Long id, Experience experience) {
        super.validateOnUpdate(id, experience);
        validateStartDateIsBeforeEndDate(experience.getStartDate(), experience.getEndDate());
    }

    public void validateStartDateIsBeforeEndDate(LocalDate startDate, LocalDate endDate) {
        if (endDate != null && startDate.isAfter(endDate)) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "Experience.endDate must be after the startDate, given endDate: " + endDate
                                                            + " and startDate: " + startDate + ".",
                                    ErrorKey.INVALID_ARGUMENT);
        }
    }
}
