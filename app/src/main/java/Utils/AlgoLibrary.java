package Utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AlgoLibrary {

    public static String DateFormating(Date date){
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Log.d("test",simpleDateFormat.format(date));
        return simpleDateFormat.format(date);
    }
}
