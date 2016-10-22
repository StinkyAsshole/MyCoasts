package stinky.mycoasts;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.sql.SQLException;

import stinky.mycoasts.model.entity.Account;
import stinky.mycoasts.model.tools.HelperFactory;
import stinky.mycoasts.presenters.AccountPresenter;
import stinky.mycoasts.view.AccountView;

public class MainActivity extends Settings implements AccountView{

    @InjectPresenter
    AccountPresenter accountPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HelperFactory.setHelper(getApplicationContext());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        if (!isSet(Type.ACCOUNT_ID)){
            Dialogs.createAccount(this, new Dialogs.MyDialog.OnClickListener() {
                @Override
                public void onClick(Dialogs.MyDialog d) {
                    EditText et = (EditText) d.findViewById(R.id.account_name);
                    accountPresenter.createAccount(et.getText().toString());
                }
            }).show(getSupportFragmentManager(), Dialogs.Tags.createAccount);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        HelperFactory.releaseHelper();
        super.onDestroy();
    }

    @Override
    public void createAccount(Account account) {

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
}
