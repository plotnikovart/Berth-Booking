package ru.hse.coursework.berth.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TransactionHelper {

    public static void actionAfterTransaction(Runnable action) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    action.run();
                }
            });
        } else {
            action.run();
        }
    }
}
