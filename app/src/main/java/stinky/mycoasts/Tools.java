package stinky.mycoasts;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;

import stinky.mycoasts.ui.MainActivity;
import stinky.mycoasts.view.ErrorView;

public class Tools {
    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        String stackTrace = sw.getBuffer().toString();
        Log.e(MainActivity.TAG, stackTrace);
        return stackTrace;
    }

    public static boolean isEmpty(EditText... args){
        boolean retVal = false;
        for (EditText et : args) {
            if (et.getText().toString().isEmpty()){
                // TODO: 03.11.2016 заменить на ресурс
                et.setError("Это поле не может быть пустым");
                retVal = true;
            } else {
                et.setError(null);
            }
        }
        return retVal;
    }
}
