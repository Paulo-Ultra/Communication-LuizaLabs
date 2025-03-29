package br.com.pauloultra.desafioluizalabs.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message){
        super("Communication resource not found: " + message);
    }
}
