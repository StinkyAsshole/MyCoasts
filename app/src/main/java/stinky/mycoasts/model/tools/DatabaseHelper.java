package stinky.mycoasts.model.tools;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    // TODO сбросить в 1
    private static final int DATABASE_VERSION = 2;

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
        for (int i = 0; i < 10; i++) {
            Category category = new Category();
            category.setName("Категория "+i);
            getCategoryDao().create(category);

            for (int j = 0; j < 10; j++) {
                SubCategory subCategory = new SubCategory();
                subCategory.setName("Подкатегория " + i + " " + j);
                subCategory.setCategory(category);
                getSubCategoryDao().create(subCategory);
                Coast coast = new Coast();
                coast.setAccount(account);
                coast.setAmount(10*i + j);
                coast.setDate(DateUtils.now().plusMonth(i));
                coast.setSubCategory(subCategory);
                getCoastDao().create(coast);
            }
        }
        Log.d(MainActivity.TAG, "generate done");
    }

    //Выполняется, когда БД имеет версию отличную от текущей
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVer, int newVer){
        try{
            //Так делают ленивые, гораздо предпочтительнее не удаляя БД аккуратно вносить изменения
            TableUtils.dropTable(connectionSource, Account.class, true);
            TableUtils.dropTable(connectionSource, Category.class, true);
            TableUtils.dropTable(connectionSource, SubCategory.class, true);
            TableUtils.dropTable(connectionSource, Coast.class, true);
            onCreate(db, connectionSource);
        }
        catch (SQLException e){
            Log.e(TAG,"error upgrading db "+DATABASE_NAME+"from ver "+oldVer);
            throw new RuntimeException(e);
        }
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

    public Cursor getCursorFindSubCategory(String str) throws SQLException {
        str = "%"+str+"%";
        return getWritableDatabase().rawQuery(SubCategory.NamedQuery.getAllSubCategoryWithCategory, new String[]{str});
    }

    public Cursor getCursorFindCategory(String str) throws SQLException {
        str = "%"+str+"%";
        return getWritableDatabase().rawQuery(Category.NamedQuery.getAllSubCategoryWithCategory, new String[]{str});
    }

    //выполняется при закрытии приложения
    @Override
    public void close(){
        super.close();
        accountDao = null;
    }
}
