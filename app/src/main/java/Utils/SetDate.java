package Utils;

import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.biddle.Activites.MainActivity;

public class SetDate implements DatePickerDialog.OnDateSetListener {

    private int year;
    private int month;
    private int day;

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear = monthOfYear +1;
        String str = "You selected :" + dayOfMonth + "/" + monthOfYear +"/" + year;
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }
}
