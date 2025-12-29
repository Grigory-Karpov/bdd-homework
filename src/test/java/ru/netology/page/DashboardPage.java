package ru.netology.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private final SelenideElement heading = $("[data-test-id=dashboard]");
    private final ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public DashboardPage() {
        // Требование преподавателя: указываем Duration в shouldBe
        heading.shouldBe(visible, Duration.ofSeconds(15));
    }

    public TransferPage selectCardToTransfer(String cardNumber) {
        cards.findBy(text(cardNumber.substring(15, 19))).$("button").click();
        return new TransferPage();
    }

    public int getCardBalance(String cardNumber) {
        String text = cards.findBy(text(cardNumber.substring(15, 19))).text();
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        int start = text.indexOf(balanceStart);
        int finish = text.indexOf(balanceFinish);
        String value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }
}