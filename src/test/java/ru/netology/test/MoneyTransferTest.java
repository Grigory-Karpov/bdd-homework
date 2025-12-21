package ru.netology.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {

    @Test
    void shouldTransferMoneyBetweenOwnCards() {
        Configuration.headless = true;
        Configuration.timeout = 15000;

        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode();
        var dashboardPage = verificationPage.validVerify(verificationCode);

        var firstCardInfo = DataHelper.getFirstCardInfo();
        var secondCardInfo = DataHelper.getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo.getId());
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo.getId());
        var amount = 1000;

        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo.getId());
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);

        var actualFirstCardBalance = dashboardPage.getCardBalance(firstCardInfo.getId());
        var actualSecondCardBalance = dashboardPage.getCardBalance(secondCardInfo.getId());

        assertEquals(firstCardBalance - amount, actualFirstCardBalance);
        assertEquals(secondCardBalance + amount, actualSecondCardBalance);
    }
}