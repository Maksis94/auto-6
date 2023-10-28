package ru.netology.bdd.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.bdd.data.DataHelper.CardInfo;

import java.util.Random;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    public DashboardPage() {
        $("[data-test-id=dashboard]").shouldBe(Condition.visible);
    }

    private SelenideElement getCard(CardInfo cardInfo) {
        return $("ul.list > li.list__item > div[data-test-id='" + cardInfo.getId() + "']");
    }

    private SelenideElement getCardByIndex(int index) {
        return $$("ul.list > li.list__item > div[data-test-id]").get(index - 1);
    }

    public int getCardBalance(CardInfo cardInfo) {
        return extractBalance(getCard(cardInfo).getText());
    }

    public int getCardBalanceByIndex(int index) {
        return extractBalance(getCardByIndex(index).getText());
    }

    public int getRandomTransferAmount(CardInfo cardInfo) {
        var balance = extractBalance(getCard(cardInfo).getText());
        if (balance <= 0) {
            return 0;
        }

        return (new Random()).nextInt(balance) + 1;
    }

    public TransferPage transferToCard(CardInfo cardInfo) {
        getCard(cardInfo).$("[data-test-id=action-deposit]").click();
        return new TransferPage();
    }

    public TransferPage transferToCardByIndex(int index) {
        getCardByIndex(index).$("[data-test-id=action-deposit]").click();
        return new TransferPage();
    }

    private int extractBalance(String text) {
        String balanceStart = "баланс: ";
        String balanceFinish = " р.";

        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }
}
