package stinky.mycoasts;

import android.support.v7.app.AppCompatActivity;

import com.arellomobile.mvp.MvpAppCompatActivity;

public class Settings extends MvpAppCompatActivity {

    public enum Type{
        ACCOUNT_ID("account_id");

        private String value;
        Type(String s){value=s;}

        public String getValue() {return value;}
        public void setValue(String value) {this.value = value;}
    }

    public Integer getCurrentAccount(){
        int id = SharedPrefsUtils.getIntegerPreference(this, Type.ACCOUNT_ID.getValue(), -1);
        if (id < 0){
            return null;
        }
        return id;
    }

    public boolean isSet(Type type){
        return SharedPrefsUtils.contains(this, type.getValue());
    }
}
