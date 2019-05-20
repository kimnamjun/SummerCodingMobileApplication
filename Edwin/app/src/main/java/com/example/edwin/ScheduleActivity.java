package com.example.edwin;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.edwin.MainActivity.str;
import static com.example.edwin.MainActivity.database;

public class ScheduleActivity extends AppCompatActivity {

    TextView text;
    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        text = (TextView) findViewById(R.id.textViewSchedule);
        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.buttonPush);

        text.setText(str.substring(0,4) + "년 " + str.substring(4,6) + "월 " + str.substring(6,8) + "일");

        button.setOnClickListener(new Button.OnClickListener() {

            // DB에 스케줄을 저장합니다.
            @Override
            public void onClick(View v) {
                // 스케줄을 입력한다.
                Cursor cursor = database.rawQuery("SELECT day FROM schedule WHERE day = '" + str +"'", null);
                if (cursor.getCount() == 1) {
                    database.execSQL("UPDATE schedule SET to_do = '" + editText.getText() + "' WHERE day = '" + str + "'");
                } else {
                    database.execSQL("INSERT INTO  schedule VALUES ('" + str + "', '" + editText.getText() + "')");
                }
                Toast.makeText(getApplication(),"스케줄을 입력하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
