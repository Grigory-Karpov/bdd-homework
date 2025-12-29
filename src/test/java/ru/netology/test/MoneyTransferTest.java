package ru.netology.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;

class MoneyTransferTest {

    @Test
    void shouldTransferMoneyBetweenOwnCards() {
        var loginPage = open("http://localhost:9999", LoginPage.class);

        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);

        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);

        // Получаем данные карт
        var firstCardInfo = DataHelper.getFirstCardInfo();
        var secondCardInfo = DataHelper.getSecondCardInfo();

        // Запоминаем баланс ДО перевода
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo.getNumber());
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo.getNumber());

        var amount = 1000;

        // ПЕРЕВОД: Хотим перевести на ВТОРУЮ карту с ПЕРВОЙ.
        // 1. Выбираем ВТОРУЮ карту (destination)
        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo.getNumber());

        // 2. Вводим сумму и номер ПЕРВОЙ карты (source)
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);

        // Получаем баланс ПОСЛЕ перевода
        var actualFirstCardBalance = dashboardPage.getCardBalance(firstCardInfo.getNumber());
        var actualSecondCardBalance = dashboardPage.getCardBalance(secondCardInfo.getNumber());

        // ПРОВЕРКИ
        Assertions.assertEquals(firstCardBalance - amount, actualFirstCardBalance);
        Assertions.assertEquals(secondCardBalance + amount, actualSecondCardBalance);
    }
}