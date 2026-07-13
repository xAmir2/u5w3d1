package amirka.u5w3d1.exceptions;

import amirka.u5w3d1.payloads.ErrorDTO;
import amirka.u5w3d1.payloads.ErrorListDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;

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
        return new ErrorListDTO(ex.getMessage(), LocalDateTime.now(), ex.getErrorMessages());
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

    @ExceptionHandler(UnauthorizedEx.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
    public ErrorDTO handleUnauthorized(UnauthorizedEx ex) {
        return new ErrorDTO(ex.getMessage(), LocalDateTime.now());
    }
}
