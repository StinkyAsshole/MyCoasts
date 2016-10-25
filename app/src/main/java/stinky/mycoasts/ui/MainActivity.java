package stinky.mycoasts.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import stinky.mycoasts.Dialogs;
import stinky.mycoasts.ListAdapter;
import stinky.mycoasts.NotFoundException;
import stinky.mycoasts.R;
import stinky.mycoasts.Settings;
import stinky.mycoasts.Tools;
import stinky.mycoasts.model.entity.Account;
import stinky.mycoasts.model.entity.Category;
import stinky.mycoasts.model.entity.Coast;
import stinky.mycoasts.model.entity.SubCategory;
import stinky.mycoasts.model.tools.DateUtils;
import stinky.mycoasts.model.tools.HelperFactory;
import stinky.mycoasts.presenters.AccountPresenter;
import stinky.mycoasts.view.AccountView;
import stinky.mycoasts.view.ErrorView;

public class MainActivity extends MvpAppCompatActivity implements AccountView, ErrorView{

    @InjectPresenter
    AccountPresenter accountPresenter;

    Dialogs.MyDialog.OnClickListener onCreateAccount = new Dialogs.MyDialog.OnClickListener() {
        @Override
        public void onClick(Dialogs.MyDialog d) {
            EditText et = (EditText) d.findViewById(R.id.account_name);
            accountPresenter.createAccount(et.getText().toString());
            d.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountPresenter.setErrorView(this);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                selectAccount();
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            Account account;
            @Override
            public void onClick(View view) {
                try {
                    HelperFactory.getHelper().truncateDataBase();
                    account = HelperFactory.getHelper().getAccountDao().queryForId(Settings.getCurrentAccount());

                    Category category = new Category();
                    category.setName("Категория 1");
                    SubCategory subCategory = new SubCategory();
                    subCategory.setName("Подкатегория 1");
                    subCategory.setCategory(category);
                    Coast coast = new Coast();
                    coast.setAccount(account);
                    coast.setAmount(200);
                    coast.setDate(DateUtils.now());
                    coast.setSubCategory(subCategory);

                    HelperFactory.getHelper().getCoastDao().create(coast);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
                // TODO: Диалог для трат
            }
        });


        if (!Settings.isSet(Settings.Type.ACCOUNT_ID)){
            selectAccount();
        }
    }

    private void selectAccount(){
        List<Account> accList = accountPresenter.getAccountList();
        if (!accList.isEmpty()){
            Dialogs.selectAccount(this, accList, onCreateAccount, new ListAdapter.OnItemClickListener() {
                @Override
                public void onClick(ListAdapter parent, View view, Object selectedObj, int position) {
                    accountPresenter.selectAccount((Account) selectedObj);
                }
            }).show(Dialogs.Tags.selectAccount);
        } else {
            Dialogs.createAccount(this, onCreateAccount).show(Dialogs.Tags.createAccount);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void createAccount(Account account) {
        accountPresenter.selectAccount(account);
    }

    @Override
    public void selectAccount(Account account, List<Coast> coastList) {
        Settings.setCurrentAccount(account.getId());

        Fragment fragment = new CoastListFragment();
        Bundle args = new Bundle();
        args.putSerializable(CoastListFragment.KEY_LIST, new ArrayList<>(coastList));
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public void onError(final Throwable e) {
        final Snackbar t = Snackbar.make(findViewById(R.id.fab), e.getMessage(), Snackbar.LENGTH_INDEFINITE);
        t.setAction(getString(R.string.action_settings), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        t.dismiss();
                        Dialogs.showMessage(MainActivity.this, Tools.getStackTrace(e));
                    }
                }
        );
        t.show();
    }

    @Override
    public void showMessage(String message) {
        View view = this.findViewById(android.R.id.content);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof Dialogs.MyDialog){
                view = fragment.getView();
            }
        }

        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    private void log(String text){
        Log.d(getClass().getSimpleName(),text);
    }
}
