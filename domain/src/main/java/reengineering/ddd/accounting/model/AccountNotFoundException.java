package reengineering.ddd.accounting.model;

public class AccountNotFoundException extends RuntimeException {
    private String account;

    public AccountNotFoundException(String account) {
        super(account + " not found");
        this.account = account;
    }
}
