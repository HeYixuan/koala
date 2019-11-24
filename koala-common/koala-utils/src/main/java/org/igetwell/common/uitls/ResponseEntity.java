package org.igetwell.common.uitls;

import lombok.Data;
import org.igetwell.common.enums.HttpStatus;

import java.io.Serializable;
import java.util.Date;

@Data
public class ResponseEntity<T> implements Serializable {
    private static final long serialVersionUID = -4660204966173673886L;
    private Date timestamp;
    private int status;
    private String message;
    private String exception;
    private T data;


    public ResponseEntity(HttpStatus status, String exception) {
        this.status = status.value();
        this.message = status.getMessage();
        this.exception = exception;
        this.timestamp = new Date();
    }

    public ResponseEntity(T data) {
        this.status = HttpStatus.OK.value();
        this.message = HttpStatus.OK.getMessage();
        this.data = data;
        this.timestamp = new Date();
    }

    public ResponseEntity() {
        this.status = HttpStatus.OK.value();
        this.message = HttpStatus.OK.getMessage();
        this.timestamp = new Date();
    }

    public static ResponseEntity ok() {
        return new ResponseEntity().status(HttpStatus.OK.value()).message(HttpStatus.OK.getMessage());
    }

    public static ResponseEntity ok(Object data) {
        return new ResponseEntity()
                .status(HttpStatus.OK.value())
                .data(data)
                .message(HttpStatus.OK.getMessage());
    }

    public static ResponseEntity error() {
        return new ResponseEntity().status(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(HttpStatus.INTERNAL_SERVER_ERROR.getMessage());
    }

    public static ResponseEntity error(HttpStatus status, String message, String exception) {
        return new ResponseEntity().status(status.value()).message(message).exception(exception);
    }
    public static ResponseEntity error(HttpStatus status, String exception) {
        return new ResponseEntity().status(status.value()).message(status.getMessage()).exception(exception);
    }

    public static ResponseEntity error(HttpStatus status) {
        return new ResponseEntity().status(status.value()).message(status.getMessage());
    }

    public ResponseEntity data(T data) {
        this.data = data;
        return this;
    }

    public ResponseEntity timestamp() {
        this.timestamp = new Date();
        return this;
    }

    public ResponseEntity status(int status) {
        this.status = status;
        return this;
    }

    public ResponseEntity message(String message) {
        this.message = message;
        return this;
    }

    public ResponseEntity exception(String exception) {
        this.exception = exception;
        return this;
    }
}
