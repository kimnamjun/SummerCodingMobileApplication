package com.example.edwin;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

import static com.example.edwin.MainActivity.database;

public class WeeklyFragment extends Fragment {

    TextView textDay[] = new TextView[7];
    TextView textToDo[] = new TextView[7];
    Cursor cursor;

    final String[] dayString = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};

    public WeeklyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_weekly, container, false);

        textDay[0] = (TextView) v.findViewById(R.id.textSun);
        textDay[1] = (TextView) v.findViewById(R.id.textMon);
        textDay[2] = (TextView) v.findViewById(R.id.textTue);
        textDay[3] = (TextView) v.findViewById(R.id.textWed);
        textDay[4] = (TextView) v.findViewById(R.id.textThu);
        textDay[5] = (TextView) v.findViewById(R.id.textFri);
        textDay[6] = (TextView) v.findViewById(R.id.textSat);
        textToDo[0] = (TextView) v.findViewById(R.id.textSunToDo);
        textToDo[1] = (TextView) v.findViewById(R.id.textMonToDo);
        textToDo[2] = (TextView) v.findViewById(R.id.textTueToDo);
        textToDo[3] = (TextView) v.findViewById(R.id.textWedToDo);
        textToDo[4] = (TextView) v.findViewById(R.id.textThuToDo);
        textToDo[5] = (TextView) v.findViewById(R.id.textFriToDo);
        textToDo[6] = (TextView) v.findViewById(R.id.textSatToDo);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setDay();
    }

    // Text View에 들어갈 문자열을 결정한다.
    public void setDay(){
        int day = ((MainActivity)getActivity()).calendar.get(Calendar.DAY_OF_WEEK);

        if(day < 4)
            ((MainActivity)getActivity()).calendar.add(Calendar.DATE, - day - 3);
        else
            ((MainActivity)getActivity()).calendar.add(Calendar.DATE, - day + 4);

        for(int i = 0; i < 7; i++){
            textDay[i].setText(makeString(i) + dayString[i]);

            String s = String.valueOf(((MainActivity)getActivity()).calendar.get(Calendar.YEAR));
            if(((MainActivity)getActivity()).calendar.get(Calendar.MONTH) + 1 < 10)
                s += "0";
            s += String.valueOf(((MainActivity)getActivity()).calendar.get(Calendar.MONTH) + 1);
            if(((MainActivity)getActivity()).calendar.get(Calendar.DATE) < 10)
                s += "0";
            s += String.valueOf(((MainActivity)getActivity()).calendar.get(Calendar.DATE));

            cursor = database.rawQuery("SELECT day, to_do FROM schedule WHERE day = '" + s + "'", null);

            if(cursor.getCount() == 1){
                cursor.moveToNext();
                textToDo[i].setText(cursor.getString(1));
            } else {
                textToDo[i].setText("");
            }
            ((MainActivity)getActivity()).calendar.add(Calendar.DATE,1);
        }

        if(day < 4)
            ((MainActivity)getActivity()).calendar.add(Calendar.DATE,day -4);
        else
            ((MainActivity)getActivity()).calendar.add(Calendar.DATE,day - 11);
    }

    public String makeString(int i){
        int y, m, d;
        String s;
        y = ((MainActivity)getActivity()).calendar.get(Calendar.YEAR);
        m = ((MainActivity)getActivity()).calendar.get(Calendar.MONTH) + 1;
        d = ((MainActivity)getActivity()).calendar.get(Calendar.DATE);
        s = y + "/" + m + "/"  + d + "/";
        return s;
    }
}

