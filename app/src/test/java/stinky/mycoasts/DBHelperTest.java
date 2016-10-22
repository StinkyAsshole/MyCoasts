package stinky.mycoasts;

import com.j256.ormlite.dao.ForeignCollection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.sql.SQLException;
import java.util.List;

import stinky.mycoasts.model.dao.AccountDAO;
import stinky.mycoasts.model.dao.CategoryDAO;
import stinky.mycoasts.model.dao.CoastDAO;
import stinky.mycoasts.model.dao.SubCategoryDAO;
import stinky.mycoasts.model.entity.Account;
import stinky.mycoasts.model.entity.Category;
import stinky.mycoasts.model.entity.SubCategory;
import stinky.mycoasts.model.tools.HelperFactory;

import static org.junit.Assert.assertEquals;

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

        for (int i = 0; i < 5; i++) {
            SubCategory subCat = new SubCategory("testSubCat"+i, cat);
            subCategoryRepository.create(subCat);
        }

        List<Category> listCategory = categoryRepository.getAll();
        assertEquals(1, listCategory.size());

        ForeignCollection<SubCategory> list = listCategory.get(0).getSubCategories();

        assertEquals(5, list.size());
    }
}
