package ru.netology.bdd.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import ru.netology.bdd.page.DashboardPage;
import ru.netology.bdd.page.LoginPage;

import static com.codeborne.selenide.Selenide.*;
import static ru.netology.bdd.data.DataHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardTransferTest {
    DashboardPage dashboardPage;

    @BeforeEach
    void setup() {
        open("http://localhost:9999");

        var loginPage = new LoginPage();
        var authInfo = getAuthInfo();

        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = getVerificationCodeFor(authInfo);

        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    void transferMoneyBetweenCards(CardInfo from, CardInfo to, int amount) {
        var amountAfterTransfer1 = dashboardPage.getCardBalance(from) - amount;
        var amountAfterTransfer2 = dashboardPage.getCardBalance(to) + amount;

        var transferPage = dashboardPage.transferToCard(to);
        dashboardPage = transferPage.transfer(from, amount);

        assertEquals(dashboardPage.getCardBalance(from), amountAfterTransfer1);
        assertEquals(dashboardPage.getCardBalance(to), amountAfterTransfer2);
    }

    void transferMoneyBetweenCardsAboveBalance(CardInfo from, CardInfo to) {
        var balance = dashboardPage.getCardBalance(from);
        var transferPage = dashboardPage.transferToCard(to);

        transferPage.transfer(from, balance + 1);
        transferPage.checkErrorNotification("Сумма перевода превышает сумму остатка на карте");
    }

    @Test
    @Order(1)
    void shouldTransferMoneyFrom1To2() {
        transferMoneyBetweenCards(getCard1(), getCard2(), dashboardPage.getRandomTransferAmount(getCard1()));
    }

    @Test
    @Order(2)
    void shouldTransferMoneyFrom2To1() {
        transferMoneyBetweenCards(getCard2(), getCard1(), dashboardPage.getRandomTransferAmount(getCard1()));
    }

    @Test
    void shouldFailOnOverdraftFrom1To2() {
        transferMoneyBetweenCardsAboveBalance(getCard1(), getCard2());
    }

    @Test
    void shouldFailOnOverdraftFrom2To1() {
        transferMoneyBetweenCardsAboveBalance(getCard2(), getCard1());
    }

    @Test
    @Order(3)
    void shouldFailOnUnknownCard() {
        var transferPage = dashboardPage.transferToCard(getCard2());
        transferPage.transfer(getUnknownCard(), 1);
        transferPage.checkErrorNotification("Ошибка! Произошла ошибка");
    }

    @Test
    @Order(4)
    void shouldFailOnSameCard() {
        var transferPage = dashboardPage.transferToCard(getCard2());
        transferPage.transfer(getCard2(), 1);
        transferPage.checkErrorNotification("Нельзя списать и перевести средства с одной и той же карты");
    }
}
