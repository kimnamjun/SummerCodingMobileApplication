package com.example.edwin;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import static com.example.edwin.MainActivity.database;
import static com.example.edwin.MainActivity.str;

public class MonthlyFragment extends Fragment {

    TextView textDate;
    DatePicker datePicker;
    Cursor cursor;

    public MonthlyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_monthly, container, false);
        textDate = (TextView) v.findViewById(R.id.textDate);
        datePicker = (DatePicker) v.findViewById(R.id.datePicker);

        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if(monthOfYear + 1 < 10)
                    if(dayOfMonth < 10)
                        ((MainActivity)getActivity()).setDate("" + year + "0" + (monthOfYear + 1) + "0" + dayOfMonth);
                    else
                        ((MainActivity)getActivity()).setDate("" + year + "0" + (monthOfYear + 1) + dayOfMonth);
                else
                    if(dayOfMonth < 10)
                        ((MainActivity)getActivity()).setDate("" + year + (monthOfYear + 1) + "0" + dayOfMonth);
                    else
                        ((MainActivity)getActivity()).setDate("" + year + (monthOfYear + 1) + dayOfMonth);
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setCalendar();
    }

    // 날짜가 바꾸기 작업을 처리한다.
    public void setCalendar(){
        datePicker.updateDate(Integer.parseInt(str.substring(0,4)), Integer.parseInt(str.substring(4,6)) - 1, Integer.parseInt(str.substring(6,8)));

        cursor = database.rawQuery("SELECT day FROM schedule WHERE substr(day,0,7) = '" + str.substring(0,6) + "'", null);

        String s = "스케줄 : ";
        for(int i = 0; i < cursor.getCount(); i++){
            cursor.moveToNext();
            s += cursor.getString(0).substring(6,8) + " ";
        }
        textDate.setText(s);
    }
}