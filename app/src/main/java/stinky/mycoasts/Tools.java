package stinky.mycoasts;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

import stinky.mycoasts.ui.MainActivity;

public class Tools {
    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        String stackTrace = sw.getBuffer().toString();
        Log.e(MainActivity.TAG, stackTrace);
        return stackTrace;
    }
}
