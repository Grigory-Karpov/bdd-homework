package ru.netology.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;

class MoneyTransferTest {

    @Test
    void shouldTransferMoneyBetweenOwnCards() {
        // Используем localhost вместо 127.0.0.1 или 0.0.0.0
        var loginPage = open("http://localhost:9999", LoginPage.class);

        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);

        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);

        var firstCardInfo = DataHelper.getFirstCardInfo();
        var secondCardInfo = DataHelper.getSecondCardInfo();

        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo.getNumber());
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo.getNumber());

        var amount = 1000;

        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo.getNumber());
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);

        var actualFirstCardBalance = dashboardPage.getCardBalance(firstCardInfo.getNumber());
        var actualSecondCardBalance = dashboardPage.getCardBalance(secondCardInfo.getNumber());

        Assertions.assertEquals(firstCardBalance - amount, actualFirstCardBalance);
        Assertions.assertEquals(secondCardBalance + amount, actualSecondCardBalance);
    }
}