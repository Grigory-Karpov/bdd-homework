package ru.netology.test;

import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {

    @Test
    void shouldTransferMoneyBetweenOwnCards() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode();
        var dashboardPage = verificationPage.validVerify(verificationCode);

        // Получаем данные о картах
        var firstCardInfo = DataHelper.getFirstCardInfo();
        var secondCardInfo = DataHelper.getSecondCardInfo();

        // Запоминаем баланс до перевода
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo.getId());
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo.getId());

        // Сколько хотим перевести
        var amount = 1000;

        // Выполняем перевод на вторую карту с первой
        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo.getId());
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);

        // Проверяем баланс после перевода
        var actualFirstCardBalance = dashboardPage.getCardBalance(firstCardInfo.getId());
        var actualSecondCardBalance = dashboardPage.getCardBalance(secondCardInfo.getId());

        // Проверки (Assertions)
        // На первой должно убавиться
        assertEquals(firstCardBalance - amount, actualFirstCardBalance);
        // На второй должно прибавиться
        assertEquals(secondCardBalance + amount, actualSecondCardBalance);
    }
}