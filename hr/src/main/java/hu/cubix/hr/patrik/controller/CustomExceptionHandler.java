package hu.cubix.hr.patrik.controller;

import hu.cubix.hr.patrik.exception.EmployeeNotFoundException;
import hu.cubix.hr.patrik.exception.VacationAlreadyExistsException;
import hu.cubix.hr.patrik.exception.VacationAlreadyProcessedException;
import hu.cubix.hr.patrik.exception.VacationNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<MyError> handleEmployeeNotFoundException(EmployeeNotFoundException e, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new MyError(e.getMessage()));
    }

    @ExceptionHandler(VacationNotFoundException.class)
    public ResponseEntity<MyError> handleVacationNotFoundException(VacationNotFoundException e, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new MyError(e.getMessage()));
    }

    @ExceptionHandler(VacationAlreadyExistsException.class)
    public ResponseEntity<MyError> handleVacationAlreadyExistsException(VacationAlreadyExistsException e, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MyError(e.getMessage()));
    }

    @ExceptionHandler(VacationAlreadyProcessedException.class)
    public ResponseEntity<MyError> handleVacationAlreadyProcessedException(VacationAlreadyProcessedException e, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MyError(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MyError> handleMethodArgumentNotValid(MethodArgumentNotValidException e, WebRequest request) {

        MyError myError = new MyError(e.getMessage());
        myError.setFieldErrors(e.getBindingResult().getFieldErrors());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(myError);
    }
}
