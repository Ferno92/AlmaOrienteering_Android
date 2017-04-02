package com.almaorient.ferno92.almaorienteering;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;

import java.util.Date;

/**
 * Created by luca.fernandez on 13/03/2017.
 */

public class CalendarActivity extends BaseActivity {
//    http://stackoverflow.com/questions/21255158/want-to-add-custom-event-on-calendar-view-in-android

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity);

        CalendarView calendarview = (CalendarView) findViewById(R.id.calendar);
        calendarview.setFirstDayOfWeek(2);
        calendarview.setDate(new Date().getTime());
    }
}
