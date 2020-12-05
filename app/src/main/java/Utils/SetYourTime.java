package Utils;

import android.app.TimePickerDialog;
import android.widget.TimePicker;

public class SetYourTime implements TimePickerDialog.OnTimeSetListener {

    private int hour;
    private int minute;

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String str = "Time is: " + hourOfDay + ":" + minute;
        this.hour = hourOfDay;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }
}
