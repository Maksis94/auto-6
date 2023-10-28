package ru.netology.bdd.page;

import ru.netology.bdd.data.DataHelper.CardInfo;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Condition.*;

public class TransferPage {
    public TransferPage() {
        $("h1.heading").shouldHave(exactText("Пополнение карты"));
    }

    public DashboardPage transfer(CardInfo from, int amount) {
        $("[data-test-id=amount] input").setValue(Integer.toString(amount));
        $("[data-test-id=from] input").setValue(from.getNumber());
        $("[data-test-id=action-transfer").click();
        return new DashboardPage();
    }

    public void checkErrorNotification(String message) {
        $("[data-test-id=error-notification]").shouldBe(visible, Duration.ofMillis(500));
        $("[data-test-id=error-notification] .notification__content").shouldHave(exactText(message), Duration.ofMillis(500));
    }
}
