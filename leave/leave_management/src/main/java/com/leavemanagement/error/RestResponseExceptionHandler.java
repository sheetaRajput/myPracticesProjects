package com.leavemanagement.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@ResponseStatus
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(LeaveNotFoundException.class)
        public ResponseEntity<ErrorMessage> leaveNotFoundException(LeaveNotFoundException exception , WebRequest request) {
            ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        }
