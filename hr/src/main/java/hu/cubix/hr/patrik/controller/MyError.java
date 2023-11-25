package hu.cubix.hr.patrik.controller;

import org.springframework.validation.FieldError;

import java.util.List;

public class MyError {

    private String message;
    private List<FieldError> fieldErrors;

    public MyError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
