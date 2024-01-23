package br.com.askcode.askcode.Exception.Runtime;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}
