package stinky.mycoasts.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.sql.SQLException;
import java.util.List;

import stinky.mycoasts.Dialogs;
import stinky.mycoasts.DrawerListAdapter;
import stinky.mycoasts.ListAdapter;
import stinky.mycoasts.NotFoundException;
import stinky.mycoasts.R;
import stinky.mycoasts.ScreenSlidePagerAdapter;
import stinky.mycoasts.Settings;
import stinky.mycoasts.Tools;
import stinky.mycoasts.model.entity.Account;
import stinky.mycoasts.model.entity.Category;
import stinky.mycoasts.model.entity.Coast;
import stinky.mycoasts.model.entity.PersistEntity;
import stinky.mycoasts.model.tools.HelperFactory;
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
    private int currentMothDiff = 0;
    Toolbar toolbar;

    private ViewPager mPager;
    private ScreenSlidePagerAdapter coastMonthAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountPresenter.setErrorView(this);

        try {
            HelperFactory.getHelper().truncateDataBase();
            HelperFactory.getHelper().generateDemoData();
        } catch (SQLException e) {
            this.onError(e);
        }

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mPager = (ViewPager) findViewById(R.id.pager);

        setSupportActionBar(toolbar);

        setupDrawer();

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
            selectAccount();
        }
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                coastMonthAdapter.setCurrentPosition(position);
            }
            @Override public void onPageScrollStateChanged(int state) {}
        });
    }

    private void setupDrawer(){
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        RecyclerView leftDrawer= (RecyclerView) findViewById(R.id.left_drawer);

        List<Category> categories = accountPresenter.getCategoryList();

        DrawerListAdapter adapter = new DrawerListAdapter(categories);

        leftDrawer.setAdapter(adapter);
        leftDrawer.setLayoutManager(new LinearLayoutManager(this));
    }

    private void selectAccount(){
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
        if (item.getItemId() == R.id.action_refresh) {
            coastMonthAdapter.notifyDataSetChanged();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreateAccount(Account account) {
        accountPresenter.selectAccount(account.getId());
    }

    @Override
    public void onSelectAccount(int account) {
        currentAccountId = account;
        coastMonthAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        coastMonthAdapter.setAccountId(account);
        mPager.setAdapter(coastMonthAdapter);
        mPager.setCurrentItem(coastMonthAdapter.getCount());
    }

    @Override
    public void onAddCoast(Coast coast) {
        coastMonthAdapter.refresh();
    }

    @Override
    public void onError(final Throwable e) {
        final Snackbar t = Snackbar.make(findViewById(R.id.fab2), e.getMessage(), Snackbar.LENGTH_INDEFINITE);
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
