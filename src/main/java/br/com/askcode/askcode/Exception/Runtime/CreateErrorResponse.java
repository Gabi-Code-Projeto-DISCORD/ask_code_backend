package br.com.askcode.askcode.Exception.Runtime;

import br.com.askcode.askcode.Domain.Response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class CreateErrorResponse {
    public static ResponseEntity<Object> response(HttpStatus status, String message, String path) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(status.value(), message, path, LocalDateTime.now()));
    }
}
