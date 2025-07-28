package ru.pro.model.enums;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.pro.exception.InsufficientFundsException;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class OperationTypeTest {

    private UUID walletId;
    private BigDecimal balance;

    @BeforeEach
    void setUp() {
        walletId = UUID.randomUUID();
        balance = new BigDecimal("100.00");
    }

    @Nested
    @DisplayName("Тесты для депозита")
    class DepositTests {

        @Test
        @DisplayName("Успешное выполнение депозита увеличивает баланс")
        void testDeposit() {
            BigDecimal depositAmount = new BigDecimal("50.00");
            BigDecimal expectedBalance = new BigDecimal("150.00");

            BigDecimal updatedBalance = OperationType.DEPOSIT.apply(walletId, balance, depositAmount);

            assertEquals(expectedBalance, updatedBalance, "Баланс должен увеличиться на депозитную сумму");
        }

        @ParameterizedTest
        @ValueSource(strings = {"0.00", "100.00", "500.50"})
        @DisplayName("Тесты для депозита с различными суммами")
        void testDepositVariousAmounts(String amountStr) {
            BigDecimal depositAmount = new BigDecimal(amountStr);
            BigDecimal expectedBalance = balance.add(depositAmount);

            BigDecimal updatedBalance = OperationType.DEPOSIT.apply(walletId, balance, depositAmount);

            assertEquals(expectedBalance, updatedBalance, "Баланс должен увеличиться на депозитную сумму");
        }
    }

    @Nested
    @DisplayName("Тесты для снятия средств")
    class WithdrawTests {

        @Test
        @DisplayName("Успешное выполнение снятия средств уменьшает баланс")
        void testWithdrawSuccess() {
            BigDecimal withdrawAmount = new BigDecimal("30.00");
            BigDecimal expectedBalance = new BigDecimal("70.00");

            BigDecimal updatedBalance = OperationType.WITHDRAW.apply(walletId, balance, withdrawAmount);

            assertEquals(expectedBalance, updatedBalance, "Баланс должен уменьшиться на сумму вывода");
        }

        @Test
        @DisplayName("Проверка на недостаточность средств при снятии")
        void testWithdrawInsufficientFunds() {
            BigDecimal withdrawAmount = new BigDecimal("150.00"); // сумма больше текущего баланса

            assertThrows(InsufficientFundsException.class, () -> {
                OperationType.WITHDRAW.apply(walletId, balance, withdrawAmount);
            }, "При недостаточности средств должно быть выброшено исключение");
        }

        @ParameterizedTest
        @ValueSource(doubles = {200.00, 500.00, 1000.00})
        @DisplayName("Проверка снятия сумм, которые превышают баланс")
        void testWithdrawInsufficientFundsParam(double amount) {
            BigDecimal withdrawAmount = BigDecimal.valueOf(amount);

            assertThrows(InsufficientFundsException.class, () -> {
                OperationType.WITHDRAW.apply(walletId, balance, withdrawAmount);
            }, "При недостаточности средств должно быть выброшено исключение");
        }
    }

    @Test
    @DisplayName("Тест на корректность применения операции с нулевыми значениями")
    void testApplyWithZero() {
        BigDecimal zeroAmount = BigDecimal.ZERO;
        BigDecimal expectedBalance = balance;

        BigDecimal updatedBalance = OperationType.DEPOSIT.apply(walletId, balance, zeroAmount);
        assertEquals(expectedBalance, updatedBalance, "Баланс не должен измениться при депозите на 0");

        updatedBalance = OperationType.WITHDRAW.apply(walletId, balance, zeroAmount);
        assertEquals(expectedBalance, updatedBalance, "Баланс не должен измениться при снятии 0");
    }
}