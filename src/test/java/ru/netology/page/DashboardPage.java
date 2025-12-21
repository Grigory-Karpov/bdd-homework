package ru.netology.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private final SelenideElement heading = $("[data-test-id=dashboard]");
    private final ElementsCollection cards = $$(".list__item div"); // Берем список всех карт
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public int getCardBalance(String cardNumber) {
        // Вырезаем последние 4 цифры из номера карты
        String cardLast4 = cardNumber.substring(15);
        // Ищем карту, в которой есть эти цифры
        val text = cards.findBy(text(cardLast4)).text();
        return extractBalance(text);
    }

    public TransferPage selectCardToTransfer(String cardNumber) {
        String cardLast4 = cardNumber.substring(15);
        // Ищем кнопку "Пополнить" у нужной карты
        cards.findBy(text(cardLast4)).$("button").click();
        return new TransferPage();
    }

    private int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }
}