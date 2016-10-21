package stinky.mycoasts.model.tools;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private static Calendar getMount(int mount) {
        if (mount < 1 || mount > 12) {
            throw new RuntimeException("Invalid number of mount");
        }
        // get date and clear time of day
        Calendar cal = getCurrentMount();
        cal.set(Calendar.MONTH, mount);
        return cal;
    }


    private static Calendar getCurrentMount() {
        // get current and clear time of day
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        return cal;
    }
    public static Date getStartOfMonth(){
        Calendar cal = getCurrentMount();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }
    public static Date getFinishOfMonth(){
        Calendar cal = getCurrentMount();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public static Date getStartOfMonth(int mount){
        Calendar cal = getMount(mount);

        // get start of the month
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    public static Date getFinishOfMonth(int mount){
        Calendar cal = getMount(mount);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

}

