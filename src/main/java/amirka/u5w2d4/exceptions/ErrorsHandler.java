package amirka.u5w2d4.exceptions;

import amirka.u5w2d4.payloads.ErrorDTO;
import amirka.u5w2d4.payloads.ErrorListDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorsHandler {

    @ExceptionHandler(NotFoundEx.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNotFound(NotFoundEx ex) {

        return new ErrorDTO(
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(BadRequestEx.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleBadRequest(BadRequestEx ex) {

        return new ErrorDTO(
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(ValidationEx.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorListDTO handleValidation(ValidationEx ex) {
        return new ErrorListDTO(ex.getMessage(), LocalDateTime.now(), ex.getErrorsList());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleValidation(MethodArgumentNotValidException ex) {

        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return new ErrorDTO(
                errors,
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleGenericEx(Exception ex) {

        ex.printStackTrace();

        return new ErrorDTO(
                "Internal server error. We are currently working on it.",
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(FileUploadEx.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleFileUpload(FileUploadEx ex) {

        return new ErrorDTO(
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleMaxUploadSize(MaxUploadSizeExceededException ex) {

        return new ErrorDTO(
                "File size exceeds the maximum allowed size",
                LocalDateTime.now()
        );
    }
}
