package ru.netology.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;

class MoneyTransferTest {

    @Test
    void shouldTransferMoneyBetweenOwnCards() {
        // НАСТРОЙКИ ДЛЯ GITHUB ACTIONS
        Configuration.browserSize = "1920x1080"; // Большой экран
        Configuration.timeout = 20000;           // Ждем загрузки элементов до 20 секунд
        Configuration.headless = true;           // Явно указываем headless режим

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