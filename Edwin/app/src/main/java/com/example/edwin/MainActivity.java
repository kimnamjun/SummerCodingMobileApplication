package com.example.edwin;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Date date;
    TextView text;
    Button buttonM;
    Button buttonW;
    Button buttonD;
    Button buttonS;
    Button buttonPrev;
    Button buttonNext;
    MonthlyFragment fragmentM;
    WeeklyFragment fragmentW;
    DailyFragment fragmentD;
    Calendar calendar;
    Cursor cursor;
    FragmentManager fm;
    static String str;
    static SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 변수 관련
        fragmentM = new MonthlyFragment();
        fragmentW = new WeeklyFragment();
        fragmentD = new DailyFragment();

        date = new Date(System.currentTimeMillis());
        text = (TextView) findViewById(R.id.textView);
        buttonPrev = (Button) findViewById(R.id.buttonPrevious);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonM = (Button) findViewById(R.id.buttonMonthly);
        buttonW = (Button) findViewById(R.id.buttonWeekly);
        buttonD = (Button) findViewById(R.id.buttonDaily);
        buttonS = (Button) findViewById(R.id.buttonSchedule);
        calendar = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        // DB 생성 / schedule 테이블은 날짜와 할 일을, state 테이블은 마지막 화면의 정보를 저장한다.
        database = openOrCreateDatabase("Schedule", MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS schedule (day text PRIMARY KEY, to_do text)");
        database.execSQL("CREATE TABLE IF NOT EXISTS state (stat int)");

        cursor = database.rawQuery("SELECT stat FROM state", null);
        if (cursor.getCount() == 0) {
            database.execSQL("INSERT INTO  state VALUES (1)");
            cursor = database.rawQuery("SELECT stat FROM state", null);
        }
        cursor.moveToNext();

        // DB를 이용한 첫 화면 결정
        // 1 - monthly / 2 - weekly / 3 - daily
        fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (cursor.getInt(0) == 1)
            fragmentTransaction.add(R.id.Layout2, fragmentM, "M");
        else if (cursor.getInt(0) == 2)
            fragmentTransaction.add(R.id.Layout2, fragmentW, "W");
        else if (cursor.getInt(0) == 3)
            fragmentTransaction.add(R.id.Layout2, fragmentD, "D");
        fragmentTransaction.commit();

        // 어플 실행시 오늘 화면으로 설정
        str = sdf.format(date);
        setDate(str);

        buttonPrev.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // state 상태 별로 날짜를 수정한다.
                cursor = database.rawQuery("SELECT stat FROM state", null);
                cursor.moveToNext();

                switch (cursor.getInt(0)){
                    case 1:
                        calendar.add(Calendar.MONTH, -1);
                        break;
                    case 2:
                        calendar.add(Calendar.DATE, -7);
                        break;
                    case 3:
                        calendar.add(Calendar.DATE,-1);
                        break;
                }

                String s = String.valueOf(calendar.get(Calendar.YEAR));
                if(calendar.get(Calendar.MONTH) + 1 < 10)
                    s += "0";
                s += String.valueOf(calendar.get(Calendar.MONTH) + 1);
                if(calendar.get(Calendar.DATE) < 10)
                    s += "0";
                s += String.valueOf(calendar.get(Calendar.DATE));
                setDate(s);

                // 버튼 클릭 결과를 fragment에 반영한다.
                if(cursor.getInt(0) == 1){
                    MonthlyFragment mf = (MonthlyFragment) fm.findFragmentById(R.id.Layout2);
                    mf.setCalendar();
                }
                else if(cursor.getInt(0) == 2){
                    WeeklyFragment mf = (WeeklyFragment) fm.findFragmentById(R.id.Layout2);
                    mf.setDay();
                }
                else if(cursor.getInt(0) == 3){
                    DailyFragment mf = (DailyFragment) fm.findFragmentById(R.id.Layout2);
                    mf.setToDo();
                }
            }
        });

        buttonNext.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor = database.rawQuery("SELECT stat FROM state", null);
                cursor.moveToNext();

                switch (cursor.getInt(0)){
                    case 1:
                        calendar.add(Calendar.MONTH, 1);
                        break;
                    case 2:
                        calendar.add(Calendar.DATE, 7);
                        break;
                    case 3:
                        calendar.add(Calendar.DATE,1);
                        break;
                }

                String s = String.valueOf(calendar.get(Calendar.YEAR));
                if(calendar.get(Calendar.MONTH) + 1 < 10)
                    s += "0";
                s += String.valueOf(calendar.get(Calendar.MONTH) + 1);
                if(calendar.get(Calendar.DATE) < 10)
                    s += "0";
                s += String.valueOf(calendar.get(Calendar.DATE));
                setDate(s);

                if(cursor.getInt(0) == 1){
                    MonthlyFragment mf = (MonthlyFragment) fm.findFragmentById(R.id.Layout2);
                    mf.setCalendar();
                }
                else if(cursor.getInt(0) == 2){
                    WeeklyFragment mf = (WeeklyFragment) fm.findFragmentById(R.id.Layout2);
                    mf.setDay();
                }
                else if(cursor.getInt(0) == 3) {
                    DailyFragment mf = (DailyFragment) fm.findFragmentById(R.id.Layout2);
                    mf.setToDo();
                }
            }
        });

        // Fragment 및 Activity 이동
        buttonM.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragmentMonthly() ;
            }
        });

        buttonW.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragmentWeekly() ;
            }
        });

        buttonD.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragmentDaily() ;
            }
        });

        buttonS.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplication(), ScheduleActivity.class);
                startActivity(intent);
            }
        });
    }

    // 날짜가 바꾸기 작업을 처리한다.
    public void setDate(String s){
        str = s;
        calendar.set(Integer.parseInt(str.substring(0,4)), Integer.parseInt(str.substring(4,6)) - 1, Integer.parseInt(str.substring(6,8)));
        text.setText("" + str.substring(0,4) + "년 " + str.substring(4,6) + "월 " + str.substring(6,8) + "일");
    }


    // Fragment 이동
    public void switchFragmentMonthly(){
        database.execSQL("UPDATE state SET stat = 1");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.Layout2, fragmentM);
        fragmentTransaction.commit();
    }

    public void switchFragmentWeekly(){
        database.execSQL("UPDATE state SET stat = 2");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.Layout2, fragmentW);
        fragmentTransaction.commit();
    }

    public void switchFragmentDaily(){
        database.execSQL("UPDATE state SET stat = 3");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.Layout2, fragmentD);
        fragmentTransaction.commit();
    }
}