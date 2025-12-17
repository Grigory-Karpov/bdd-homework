package ru.netology.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");
    private ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public int getCardBalance(String id) {
        // Ищем карту по атрибуту data-test-id
        val text = cards.findBy(com.codeborne.selenide.Condition.attribute("data-test-id", id)).text();
        return extractBalance(text);
    }

    public TransferPage selectCardToTransfer(String id) {
        // Ищем кнопку "Пополнить" именно у той карты, ID которой передали
        cards.findBy(com.codeborne.selenide.Condition.attribute("data-test-id", id))
                .$("button").click();
        return new TransferPage();
    }

    private int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }
}