package stinky.mycoasts.model.tools;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {

    private Date date;

    private DateUtils(){
        date = new Date();
    }
    private DateUtils(Date date){
        this.date = date;
    }
    private DateUtils(long time){
        this.date = new Date(time);
    }

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

    public static DateUtils now(){
        return new DateUtils();
    }

    public static String toString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("d:MM:yyyy");
        return format.format(date);
    }

    public enum Unit{
        SECOND,
        MINUTE,
        HOUR,
        DAY,
        MONTH,
        YEAR
    }

    public static int compare(Date date, Date date2, Unit unit){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        int retVal = 0;

        retVal = compare(cal.get(Calendar.YEAR), cal2.get(Calendar.YEAR));
        if (unit == Unit.YEAR || retVal != 0){return retVal;}

        retVal = compare(cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
        if (unit == Unit.MONTH|| retVal != 0){return retVal;}

        retVal = compare(cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        if (unit == Unit.DAY|| retVal != 0){return retVal;}

        retVal = compare(cal.get(Calendar.HOUR), cal2.get(Calendar.HOUR));
        if (unit == Unit.HOUR|| retVal != 0){return retVal;}

        retVal = compare(cal.get(Calendar.MINUTE), cal2.get(Calendar.MINUTE));
        if (unit == Unit.MINUTE|| retVal != 0){return retVal;}

        retVal = compare(cal.get(Calendar.SECOND), cal2.get(Calendar.SECOND));
        if (unit == Unit.SECOND|| retVal != 0){return retVal;}

        return compare(cal.get(Calendar.MILLISECOND), cal2.get(Calendar.MILLISECOND));
    }

    private static int compare(int i1, int i2){
        return i1 > i2 ? 1 : i1==i2 ? 0 : -1;
    }

    private final long SECOND = 1000;
    private final long MINUTE = SECOND*60;
    private final long HOUR = MINUTE*60;
    private final long DAY = HOUR*24;

    public DateUtils plusHour(int hours){
        return new DateUtils(date.getTime() + hours * HOUR);
    }
    public DateUtils minusHour(int hours){
        return plusHour(-1 * hours);
    }

    public DateUtils plusDay(int days){
        return new DateUtils(date.getTime() + days * DAY);
    }
    public DateUtils minusDay(int days){
        return plusDay(-1 * days);
    }


    public Date getDate(){
        return date;
    }

}

