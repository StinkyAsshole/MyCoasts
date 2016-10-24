package stinky.mycoasts;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.arellomobile.mvp.MvpAppCompatActivity;

public class Settings {

    private static Context context;

    public static void setContext(Context context) {
        Settings.context = context;
    }

    public enum Type{
        ACCOUNT_ID("account_id");

        private String value;
        Type(String s){value=s;}

        public String getValue() {return value;}
        public void setValue(String value) {this.value = value;}
    }

    public static Integer getCurrentAccount() throws NotFoundException {
        int id = SharedPrefsUtils.getIntegerPreference(context, Type.ACCOUNT_ID.getValue(), -1);
        if (id < 0){
            throw new NotFoundException();
        }
        return id;
    }

    public static void setCurrentAccount(int accountId){
        SharedPrefsUtils.setIntegerPreference(context, Type.ACCOUNT_ID.getValue(), accountId);
    }

    public static boolean isSet(Type type){
        return SharedPrefsUtils.contains(context, type.getValue());
    }
}
