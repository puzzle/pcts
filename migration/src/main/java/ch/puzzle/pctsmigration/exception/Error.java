package ch.puzzle.pctsmigration.exception;

import org.springframework.http.HttpStatusCode;

public record Error(HttpStatusCode status, String message) {
}
