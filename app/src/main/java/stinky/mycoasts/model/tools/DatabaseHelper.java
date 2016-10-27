package stinky.mycoasts.model.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

import stinky.mycoasts.Settings;
import stinky.mycoasts.model.dao.AccountDAO;
import stinky.mycoasts.model.dao.CategoryDAO;
import stinky.mycoasts.model.dao.CoastDAO;
import stinky.mycoasts.model.dao.SubCategoryDAO;
import stinky.mycoasts.model.entity.Account;
import stinky.mycoasts.model.entity.Category;
import stinky.mycoasts.model.entity.Coast;
import stinky.mycoasts.model.entity.SubCategory;
import stinky.mycoasts.ui.MainActivity;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "my_coast.db";

    //с каждым увеличением версии, при нахождении в устройстве БД с предыдущей версией будет выполнен метод onUpgrade();
    private static final int DATABASE_VERSION = 1;

    //ссылки на DAO соответсвующие сущностям, хранимым в БД
    private AccountDAO accountDao = null;
    private CategoryDAO categoryDao = null;
    private SubCategoryDAO subCategoryDao = null;
    private CoastDAO coastDao = null;

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Выполняется, когда файл с БД не найден на устройстве
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource){
        try {
            TableUtils.createTable(connectionSource, Account.class);
            TableUtils.createTable(connectionSource, Category.class);
            TableUtils.createTable(connectionSource, SubCategory.class);
            TableUtils.createTable(connectionSource, Coast.class);
        }
        catch (SQLException e){
            Log.e(TAG, "error creating DB " + DATABASE_NAME);
            throw new RuntimeException(e);
        }
    }

    public void truncateDataBase() throws SQLException {
            ConnectionSource connectionSource = HelperFactory.getHelper().getConnectionSource();
//            TableUtils.clearTable(connectionSource, Account.class);
            TableUtils.clearTable(connectionSource, Category.class);
            TableUtils.clearTable(connectionSource, SubCategory.class);
            TableUtils.clearTable(connectionSource, Coast.class);
    }

    public void generateDemoData() throws SQLException {
        truncateDataBase();
        List<Account> accounts = getAccountDao().getAll();
        Account account;
        if (accounts == null || accounts.isEmpty()){
            account = new Account("demo");
            getAccountDao().create(account);
        } else {
            account = accounts.get(0);
        }

        Category category = new Category();
        category.setName("Категория 1");
        getCategoryDao().create(category);

        SubCategory subCategory = new SubCategory();
        subCategory.setName("Подкатегория 1");
        subCategory.setCategory(category);
        getSubCategoryDao().create(subCategory);
        for (int i = 0; i < 10; i++) {
            Coast coast = new Coast();
            coast.setAccount(account);
            coast.setAmount(200);
            coast.setDate(DateUtils.now().plusHour(i));
            coast.setSubCategory(subCategory);
            getCoastDao().create(coast);
            Log.d(MainActivity.TAG,coast.getSubCategory().getName());
        }
    }

    //Выполняется, когда БД имеет версию отличную от текущей
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVer, int newVer){
//        try{
//            //Так делают ленивые, гораздо предпочтительнее не удаляя БД аккуратно вносить изменения
//            TableUtils.dropTable(connectionSource, Goal.class, true);
//            onCreate(db, connectionSource);
//        }
//        catch (SQLException e){
//            Log.e(TAG,"error upgrading db "+DATABASE_NAME+"from ver "+oldVer);
//            throw new RuntimeException(e);
//        }
    }

    public AccountDAO getAccountDao() throws SQLException{
        if(accountDao == null){
            accountDao = new AccountDAO(getConnectionSource(), Account.class);
        }
        return accountDao;
    }

    public CategoryDAO getCategoryDao() throws SQLException{
        if(categoryDao == null){
            categoryDao= new CategoryDAO(getConnectionSource(), Category.class);
        }
        return categoryDao;
    }

    public SubCategoryDAO getSubCategoryDao() throws SQLException{
        if(subCategoryDao == null){
            subCategoryDao = new SubCategoryDAO(getConnectionSource(), SubCategory.class);
        }
        return subCategoryDao;
    }

    public CoastDAO getCoastDao() throws SQLException{
        if(coastDao == null){
            coastDao = new CoastDAO(getConnectionSource(), Coast.class);
        }
        return coastDao;
    }

    //выполняется при закрытии приложения
    @Override
    public void close(){
        super.close();
        accountDao = null;
    }
}
