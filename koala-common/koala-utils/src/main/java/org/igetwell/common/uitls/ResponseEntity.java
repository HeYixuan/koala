package org.igetwell.common.uitls;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.igetwell.common.enums.HttpStatus;

import java.io.Serializable;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseEntity<T> implements Serializable {
    private static final long serialVersionUID = -4660204966173673886L;
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;
    private int status;
    private String message;
    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private String exception;
    //@JsonInclude(JsonInclude.Include.NON_NULL)
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
}
