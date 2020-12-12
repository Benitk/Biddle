package Utils;

import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;

public class SetDate implements DatePickerDialog.OnDateSetListener {

    private int year ;
    private int month ;
    private int day ;
    private EditText editText;


    public SetDate(EditText ed){
        this.editText = ed;
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear = monthOfYear +1;
        String date = dayOfMonth + "/" + monthOfYear +"/" + year;
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;

        this.editText.setError(null);
        this.editText.setText(date);

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
