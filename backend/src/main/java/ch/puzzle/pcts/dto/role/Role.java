package ch.puzzle.pcts.dto.role;

import java.time.LocalDateTime;

public record Role(Long id, String name, LocalDateTime deleted_at) {
}
