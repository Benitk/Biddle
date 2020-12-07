package Utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biddle.Activites.MainActivity;
import com.example.biddle.R;

import static com.example.biddle.R.*;

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
