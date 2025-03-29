package br.com.pauloultra.desafioluizalabs.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class StatusException extends RuntimeException {
    public StatusException(String message){
        super(message);
    }
}
