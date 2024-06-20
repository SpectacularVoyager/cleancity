package com.ankush.cleancity.ControllerAdvice;

import jakarta.validation.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class Exceptions extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
//        var apiError = new ApiError(HttpStatus.resolve(status.value()), ex.getMessage());
        Map<String, Map<String, String>> errors = new HashMap<>();

//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            Map<String, String> obj = Map.of("error", fieldName, "des", errorMessage);
//            errors.put(fieldName, obj);
//        });
        FieldError error = (FieldError) ex.getBindingResult().getAllErrors().get(0);
        return new ResponseEntity<>(Map.of("error", error.getField(), "desc", error.getDefaultMessage()), status);

//        return new ResponseEntity<>(errors, status);
    }
}