package br.com.pauloultra.desafioluizalabs.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionalResponse {

    private LocalDateTime timestamp;
    private String message;
    private HttpStatus status;
}
