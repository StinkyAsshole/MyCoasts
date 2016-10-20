package stinky.mycoasts;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.sql.SQLException;

import stinky.mycoasts.model.tools.HelperFactory;
import stinky.mycoasts.model.tools.dao.AccountDAO;
import stinky.mycoasts.model.tools.entity.Account;
import static org.junit.Assert.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class DBHelperTest {

    @Test
    public void test1() throws SQLException {
        MainActivity activity = Robolectric.buildActivity(MainActivity.class).create().get();
//        HelperFactory.setHelper(context);

        Account acc = new Account();
        acc.setName("testAcc");
        acc.setAmount(22);

        AccountDAO accountRepository = HelperFactory.getHelper().getAccountDao();
        accountRepository.create(acc);

        assertEquals(accountRepository.getAllAccounts().get(0).getName(),"testAcc");

//        HelperFactory.releaseHelper();
    }
}
