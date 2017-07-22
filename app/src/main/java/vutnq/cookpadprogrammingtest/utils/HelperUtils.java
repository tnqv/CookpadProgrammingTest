package vutnq.cookpadprogrammingtest.utils;



import android.net.ConnectivityManager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bipug on 7/19/17.
 */

public class HelperUtils {

    public static String convertUnixTimeStampToDate(double time,String pattern){
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        date.setTime((long)time*1000);
        return dateFormat.format(date);
    }
}
