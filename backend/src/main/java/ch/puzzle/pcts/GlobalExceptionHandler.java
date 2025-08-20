package ch.puzzle.pcts;

import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericErrorDto> handleGenericException(Exception ex) {
        logger.error("Unhandled exception: ", ex);
        List<String> message = List.of("Something has gone wrong on the server, please check the logs");
        return ResponseEntity.internalServerError().body(new GenericErrorDto(ErrorKey.INTERNAL, message));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<GenericErrorDto> handle(BindException ex) {
        logger.error("Bind exception: ", ex);
        List<String> reasons = ex
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericErrorDto(ErrorKey.VALIDATION, reasons));
    }

    @ExceptionHandler(PCTSException.class)
    public ResponseEntity<GenericErrorDto> handlePCTSException(PCTSException ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(new GenericErrorDto(ex.getErrorKey(), List.of(ex.getReason())));
    }
}
