package com.example.demo.controller;

import com.example.demo.model.dto.ErrorDTO;
import com.example.demo.model.exceptions.BadRequestException;
import com.example.demo.model.exceptions.NotFoundException;
import com.example.demo.model.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;

public abstract class AbstractController {
    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ErrorDTO notFoundHandler(Exception exception){
        return buildErrorInfo(exception,HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(value = BadRequestException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorDTO badRequestHandler(Exception exception){
        return buildErrorInfo(exception,HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ErrorDTO unauthorizedExceptionHandler(Exception exception){
        return buildErrorInfo(exception,HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO allOthersExceptionHandler(Exception exception){
        return buildErrorInfo(exception,HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private ErrorDTO buildErrorInfo(Exception exception, int status){
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setStatus(status);
        errorDTO.setMessage(exception.getMessage());
        exception.printStackTrace();
        errorDTO.setTime(LocalDate.now());
        return errorDTO;
    }
}
