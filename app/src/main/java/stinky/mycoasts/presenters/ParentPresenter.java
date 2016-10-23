package stinky.mycoasts.presenters;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;

import stinky.mycoasts.view.AccountView;
import stinky.mycoasts.view.ErrorView;

public class ParentPresenter <View extends MvpView> extends MvpPresenter<View> {
    private ErrorView errorView;

    public void setErrorView(ErrorView errorView) {
        this.errorView = errorView;
    }
    public ErrorView getErrorView(){return errorView;}
}
