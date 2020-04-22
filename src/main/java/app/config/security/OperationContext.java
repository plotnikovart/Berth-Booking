package app.config.security;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OperationContext {

    private static final ThreadLocal<Long> container = new ThreadLocal<>();

    public static Long accountId() {
        return container.get();
    }

    public static void accountId(Long accountId) {
        container.set(accountId);
    }

    public static void clear() {
        container.remove();
    }
}
