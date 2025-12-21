package ru.netology.test;

import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {

    @Test
    void shouldTransferMoneyBetweenOwnCards() {
        var loginPage = open("http://127.0.0.1:9999", LoginPage.class);

        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode();
        var dashboardPage = verificationPage.validVerify(verificationCode);

        var firstCardInfo = DataHelper.getFirstCardInfo();
        var secondCardInfo = DataHelper.getSecondCardInfo();

        // ВАЖНО: Теперь передаем getNumber(), а не getId()
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo.getNumber());
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo.getNumber());
        var amount = 1000;

        // И здесь тоже getNumber()
        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo.getNumber());
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);

        // И здесь getNumber()
        var actualFirstCardBalance = dashboardPage.getCardBalance(firstCardInfo.getNumber());
        var actualSecondCardBalance = dashboardPage.getCardBalance(secondCardInfo.getNumber());

        assertEquals(firstCardBalance - amount, actualFirstCardBalance);
        assertEquals(secondCardBalance + amount, actualSecondCardBalance);
    }
}