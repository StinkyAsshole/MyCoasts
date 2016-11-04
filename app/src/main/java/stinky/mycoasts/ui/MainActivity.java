package stinky.mycoasts.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.ArrayList;
import java.util.List;

import stinky.mycoasts.Dialogs;
import stinky.mycoasts.ListAdapter;
import stinky.mycoasts.NotFoundException;
import stinky.mycoasts.R;
import stinky.mycoasts.Settings;
import stinky.mycoasts.Tools;
import stinky.mycoasts.model.entity.Account;
import stinky.mycoasts.model.entity.Coast;
import stinky.mycoasts.model.entity.PersistEntity;
import stinky.mycoasts.presenters.AccountPresenter;
import stinky.mycoasts.view.AccountView;
import stinky.mycoasts.view.ErrorView;

public class MainActivity extends MvpAppCompatActivity implements AccountView, ErrorView{

    public final static String TAG = "MainActivity";

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

    private int currentAccountId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountPresenter.setErrorView(this);

//        try {
//            HelperFactory.getHelper().truncateDataBase();
//            HelperFactory.getHelper().generateDemoData();
//        } catch (SQLException e) {
//            this.onError(e);
//        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAccountSelect();
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialogs.addCoast(MainActivity.this, MainActivity.this, new Dialogs.MyDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialogs.MyDialog d) {
                        String category = ((AutoCompleteTextView) d.findViewById(R.id.category_name)).getText().toString();
                        String subCategory = ((AutoCompleteTextView) d.findViewById(R.id.subcategory_name)).getText().toString();
                        String amount = ((TextView) d.findViewById(R.id.amount)).getText().toString();
                        accountPresenter.addCoast(category, subCategory, currentAccountId, Math.abs(Integer.parseInt(amount))* (int)d.getData());
                        d.dismiss();
                    }}).show(Dialogs.Tags.ADD_COST);
            }
        });

        try {
            currentAccountId = Settings.getCurrentAccount();
            accountPresenter.selectAccount(currentAccountId);
        } catch (NotFoundException e) {
            onAccountSelect();
        }
    }

    private void onAccountSelect(){
        List<Account> accList = accountPresenter.getAccountList();
        if (!accList.isEmpty()){
            Dialogs.selectAccount(this, accList, onCreateAccount, new ListAdapter.OnItemClickListener() {
                @Override
                public void onClick(ListAdapter parent, View view, PersistEntity selectedObj, int position) {
                    accountPresenter.selectAccount(selectedObj.getId());
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
    public void onCreateAccount(Account account) {
        accountPresenter.selectAccount(account.getId());
    }

    @Override
    public void onAddCoast(Coast coast) {

    }

    @Override
    public void showCoastList(List<Coast> coastList){
        Fragment fragment = new CoastListFragment();
        Bundle args = new Bundle();
        args.putSerializable(CoastListFragment.KEY_LIST, new ArrayList<>(coastList));
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public void onError(final Throwable e) {
        final Snackbar t = Snackbar.make(findViewById(R.id.fab), e.getMessage(), Snackbar.LENGTH_INDEFINITE);
        // TODO убрать
        Tools.getStackTrace(e);
        t.setAction(getString(R.string.action_details), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        t.dismiss();
                        Dialogs.showMessage(MainActivity.this, Tools.getStackTrace(e)).show(getSupportFragmentManager(),Dialogs.Tags.EXCEPTION);
                    }
                }
        );
        t.show();
    }

    @Override
    public void showMessage(String message) {
        View view = this.findViewById(android.R.id.content);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof Dialogs.MyDialog) {
                    view = fragment.getView();
                }
            }
        }

        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    private void log(String text){
        Log.d(getClass().getSimpleName(),text);
    }
}
