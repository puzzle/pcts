package ch.puzzle.pcts;

import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.mapper.ErrorMapper;
import ch.puzzle.pcts.model.error.ErrorKey;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final ErrorMapper errorMapper;

    public GlobalExceptionHandler(ErrorMapper errorMapper) {
        this.errorMapper = errorMapper;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericErrorDto> handleGenericException(Exception ex) {
        logger.error("Unhandled exception: ", ex);
        return ResponseEntity.internalServerError().body(new GenericErrorDto(ErrorKey.INTERNAL));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<GenericErrorDto> handle(BindException ex) {
        logger.error("Bind exception: ", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericErrorDto(ErrorKey.VALIDATION));
    }

    @ExceptionHandler(PCTSException.class)
    public ResponseEntity<List<GenericErrorDto>> handlePCTSException(PCTSException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(this.errorMapper.toDto(ex.getErrors()));
    }
}
