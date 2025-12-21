package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import lombok.val;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {
    private final SelenideElement heading = $("[data-test-id=dashboard]");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public int getCardBalance(String id) {
        // Ищем элемент напрямую по его ID.
        // Selenide будет ждать именно этот элемент до 15 секунд.
        val text = $("[data-test-id='" + id + "']").text();
        return extractBalance(text);
    }

    public TransferPage selectCardToTransfer(String id) {
        // Ищем кнопку "Пополнить" внутри конкретной карты
        $("[data-test-id='" + id + "'] button").click();
        return new TransferPage();
    }

    private int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }
}