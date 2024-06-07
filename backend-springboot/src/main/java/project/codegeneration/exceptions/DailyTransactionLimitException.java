package project.codegeneration.exceptions;

public class DailyTransactionLimitException extends RuntimeException {
    public DailyTransactionLimitException() {
        super("Daily transaction limit exceeded.");
    }
}