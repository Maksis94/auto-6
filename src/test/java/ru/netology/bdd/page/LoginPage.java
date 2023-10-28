package ru.netology.bdd.page;

import ru.netology.bdd.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    public VerificationPage validLogin(DataHelper.AuthInfo authInfo) {
        $("[data-test-id=login] input").setValue(authInfo.getLogin());
        $("[data-test-id=password] input").setValue(authInfo.getPassword());
        $("[data-test-id=action-login]").click();
        return new VerificationPage();
    }
}
