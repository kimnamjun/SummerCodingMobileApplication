package com.example.edwin;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.example.edwin.MainActivity.str;
import static com.example.edwin.MainActivity.database;

public class DailyFragment extends Fragment {

    TextView textDaily;

    public DailyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_daily, container, false);

        textDaily = (TextView) v.findViewById(R.id.textDaily);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setToDo();
    }

    public void setToDo(){
        // DB에서 오늘의 스케줄을 받아 출력한다.
        Cursor cursor = database.rawQuery("SELECT day, to_do FROM schedule WHERE day = '" + str + "'", null);
        if(cursor.getCount() == 1){
            cursor.moveToNext();
            textDaily.setText(cursor.getString(1));
        } else {
            textDaily.setText("");
        }
    }
}