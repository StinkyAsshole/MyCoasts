package stinky.mycoasts;

import android.app.Application;
import android.util.Log;

import stinky.mycoasts.model.tools.HelperFactory;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        HelperFactory.setHelper(getApplicationContext());
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_COMPLETE){
            HelperFactory.releaseHelper();
        }
    }
}
