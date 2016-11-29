package stinky.mycoasts.view;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import stinky.mycoasts.model.entity.Account;
import stinky.mycoasts.model.entity.Coast;

public interface CoastView extends MvpView {
    void onShowCoastList(List<Coast> list);
}
