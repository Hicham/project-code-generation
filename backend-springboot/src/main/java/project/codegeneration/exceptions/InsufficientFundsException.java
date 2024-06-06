package project.codegeneration.exceptions;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException() {
        super("Account has insuffient funds");
    }
}
