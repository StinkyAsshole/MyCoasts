package stinky.mycoasts.view;

import com.arellomobile.mvp.MvpView;

public interface ErrorView extends MvpView {
    void onError(Throwable e);
    void showMessage(String message);
}
