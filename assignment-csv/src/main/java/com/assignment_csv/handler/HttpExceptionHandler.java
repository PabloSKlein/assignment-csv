package com.assignment_csv.handler;

import com.assignment_csv.dto.ApiErrorDTO;
import com.assignment_csv.exceptions.InternalErrorException;
import com.assignment_csv.exceptions.NotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class HttpExceptionHandler {

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    ApiErrorDTO handleNotFoundException(NotFoundException exception) {
        return new ApiErrorDTO(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    ApiErrorDTO handleInternalErrorException(InternalErrorException exception) {
        return new ApiErrorDTO(exception.getMessage());
    }
}
