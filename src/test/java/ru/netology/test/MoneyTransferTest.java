package ru.netology.test;
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
        // ЖЕЛЕЗОБЕТОННЫЕ НАСТРОЙКИ ДЛЯ GITHUB ACTIONS
        Configuration.headless = true;  // Работаем без монитора
        Configuration.timeout = 15000;  // Ждем загрузки элементов 15 секунд (вместо 4)

        // 1. Открываем страницу (localhost работает, если приложение поднялось)
        var loginPage = open("http://localhost:9999", LoginPage.class);

        // 2. Логинимся
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);

        // 3. Вводим код верификации
        var verificationCode = DataHelper.getVerificationCode();
        var dashboardPage = verificationPage.validVerify(verificationCode);

        // 4. Получаем инфо о картах
        var firstCardInfo = DataHelper.getFirstCardInfo();
        var secondCardInfo = DataHelper.getSecondCardInfo();

        // 5. Запоминаем балансы ДО
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo.getId());
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo.getId());

        // 6. Переводим деньги (1000 р с первой на вторую)
        var amount = 1000;
        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo.getId());
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);

        // 7. Получаем балансы ПОСЛЕ
        var actualFirstCardBalance = dashboardPage.getCardBalance(firstCardInfo.getId());
        var actualSecondCardBalance = dashboardPage.getCardBalance(secondCardInfo.getId());

        // 8. Сверяем
        assertEquals(firstCardBalance - amount, actualFirstCardBalance);
        assertEquals(secondCardBalance + amount, actualSecondCardBalance);
    }
}