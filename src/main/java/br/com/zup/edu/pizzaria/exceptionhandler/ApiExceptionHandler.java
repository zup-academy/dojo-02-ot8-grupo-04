package br.com.zup.edu.pizzaria.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErrorResponse> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        List<ErrorResponse> erros = new ArrayList<>();
        ex.getFieldErrors().forEach(erro -> {
            erros.add(new ErrorResponse(erro.getField(), erro.getDefaultMessage()));
        });
        return erros;
    }
}
