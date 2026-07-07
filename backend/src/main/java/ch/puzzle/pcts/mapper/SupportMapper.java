package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.support.SupportDto;
import org.springframework.stereotype.Component;

@Component
public class SupportMapper {
    public SupportDto toDto(String url) {
        return new SupportDto(url);
    }
}
