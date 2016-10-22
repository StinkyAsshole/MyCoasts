package stinky.mycoasts.view;

import com.arellomobile.mvp.MvpView;

import stinky.mycoasts.model.entity.Account;

public interface AccountView extends MvpView, ErrorView {
    void createAccount(Account account);
}
