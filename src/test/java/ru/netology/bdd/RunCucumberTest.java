package ru.netology.bdd;

import com.codeborne.selenide.Condition;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import ru.netology.bdd.data.DataHelper;
import ru.netology.bdd.page.DashboardPage;
import ru.netology.bdd.page.LoginPage;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.bdd.data.DataHelper.getVerificationCodeFor;

import static com.codeborne.selenide.Selenide.*;

public class RunCucumberTest {
    private static DashboardPage dashboardPage;

    @Пусть("пользователь залогинен с именем {string} и паролем {string}")
    public void loginUser(String login, String password) {
        open("http://localhost:9999");

        var loginPage = new LoginPage();
        var authInfo = new DataHelper.AuthInfo(login, password);

        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = getVerificationCodeFor(authInfo);

        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Когда("пользователь переводит {int} рублей с карты с номером {string} на свою {int} карту с главной страницы")
    public void transferToCard(int amount, String fromNumber, int toId) {
        var transferPage = dashboardPage.transferToCardByIndex(toId);
        dashboardPage = transferPage.transfer(new DataHelper.CardInfo(fromNumber, "-"), amount);
    }

    @Тогда("баланс его {int} карты из списка на главной странице должен стать {int} рублей")
    public void checkBalance(int cardId, int expectedBalance) {
        assertEquals(dashboardPage.getCardBalanceByIndex(cardId), expectedBalance);
    }
}
