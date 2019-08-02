package app.common;

public class OperationContext {

    private static ThreadLocal<Long> container = new ThreadLocal<>();

    public static Long getAccountId() {
        return container.get();
    }

    public static void setAccountId(Long accountId) {
        container.set(accountId);
    }
}
