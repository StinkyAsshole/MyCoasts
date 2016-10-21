package stinky.mycoasts;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import stinky.mycoasts.model.tools.DateUtils;
import stinky.mycoasts.model.tools.HelperFactory;
import stinky.mycoasts.model.tools.dao.AccountDAO;
import stinky.mycoasts.model.tools.dao.CategoryDAO;
import stinky.mycoasts.model.tools.dao.CoastDAO;
import stinky.mycoasts.model.tools.dao.SubCategoryDAO;
import stinky.mycoasts.model.tools.entity.Account;
import stinky.mycoasts.model.tools.entity.Category;
import stinky.mycoasts.model.tools.entity.Coast;
import stinky.mycoasts.model.tools.entity.SubCategory;

import static org.junit.Assert.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class DBHelperTest {

    public static String TAG = "testLog";

    @Before
    public void setUp() throws Exception {
        ShadowLog.stream = System.out;
        //you other setup here
    }

    @Test
    public void test1() throws SQLException {
        MainActivity activity = Robolectric.buildActivity(MainActivity.class).create().get();

        AccountDAO accountRepository = HelperFactory.getHelper().getAccountDao();
        CategoryDAO categoryRepository = HelperFactory.getHelper().getCategoryDao();
        SubCategoryDAO subCategoryRepository = HelperFactory.getHelper().getSubCategoryDao();
        CoastDAO coastRepository = HelperFactory.getHelper().getCoastDao();

        Account acc = new Account("testAcc");
        Category cat = new Category("testCat");

        accountRepository.create(acc);
        categoryRepository.create(cat);

        SubCategory subCat = new SubCategory("testSubCat",cat);
        subCategoryRepository.create(subCat);

        Coast coast = new Coast();
        coast.setAmount(200);
        coast.setAccount(acc);
        coast.setSubCategory(subCat);
        coast.setDate(new Date(System.currentTimeMillis() - 60*60*24));

        coastRepository.create(coast);

        List<Coast> list = coastRepository.getByDate(DateUtils.getStartOfMonth(), DateUtils.getFinishOfMonth(),0);
        assertEquals(1, list.size());
    }
}
