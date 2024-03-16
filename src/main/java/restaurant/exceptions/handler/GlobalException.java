package restaurant.exceptions.handler;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import restaurant.exceptions.ForbiddenException;
import restaurant.exceptions.NotFoundException;
import restaurant.exceptions.response.ExceptionResponse;

@RestControllerAdvice
public class GlobalException {

    //404
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse notFound(NotFoundException notFoundException) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND).
                exceptionClassName(NotFoundException.class.getSimpleName()).
                message(notFoundException.getMessage())
                .build();
    }

    //403
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse forbidden(ForbiddenException forbiddenException) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.FORBIDDEN).
                exceptionClassName(ForbiddenException.class.getSimpleName()).
                message(forbiddenException.getMessage())
                .build();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse bagRequest(BadRequestException badRequestException) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST).
                exceptionClassName(BadRequestException.class.getSimpleName()).
                message(badRequestException.getMessage())
                .build();
    }

    //For def validation;
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse methodArgNotValid(MethodArgumentNotValidException methodArgumentNotValidException) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST).
                exceptionClassName(methodArgumentNotValidException.getClass().getSimpleName()).
                message(methodArgumentNotValidException.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse methodArgNotValidType(MethodArgumentTypeMismatchException methodArgumentNotValidTypeException) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST).
                exceptionClassName(methodArgumentNotValidTypeException.getClass().getSimpleName()).
                message("Invalid type: " + methodArgumentNotValidTypeException.getMessage().substring(256) + " select: 'EVROPEISKII,     TURETSKII,     VOSTOCHNYI,     PITSERII,   UZBEKSKII , FASTFOOD'")
                .build();
    }


}
