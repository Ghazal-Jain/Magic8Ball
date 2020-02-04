package com.example.magic8ball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private ImageView ballIV;
    private TextView answerTV;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;


    private float zAxis = 0;//gravity acceleration along the z axis
    private int mEventCountSinceGZChanged = 0; // number of times the sensor detects a change
    private static final int MAX_COUNT_GZ_CHANGE = 2; //after 2 times of change - count it as a flip
    private static final String TAG = "MotionControlService";


    private String[] answersArray = {"It is certain", "It is decidedly so", "Without a doubt", "Yes definitely", "You may rely on it", "As I see it, yes",
            "Most likely", "Outlook good", "Yes", "Signs point to yes", "Reply hazy try again", "Ask again later",
            "Better not tell you now", "Cannot predict now", "Concentrate and ask again", "Don't count on it",
            "My reply is no", "My sources say no", "Outlook not so good", "Very doubtful"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
        ballIV = findViewById(R.id.btn);
        answerTV = findViewById(R.id.answer);
/*
        ballIV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btn:
                    int rand = new Random().nextInt(answersArray.length);
                    answerTV.setText(answersArray[rand]);
                    break;
                }
            }
        });
*/
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int type = sensorEvent.sensor.getType();
        if (type == Sensor.TYPE_ACCELEROMETER) {
            Log.d(TAG, "Sensors Active."); //Displays the message that the sensors are active and working

                float zValue = sensorEvent.values[2];
                if (zAxis == 0) {
                    zAxis = zValue;
                }

                else {
                    if ((zAxis * zValue) < 0) {
                        mEventCountSinceGZChanged++;
                        Log.d(TAG, "Change");
                        if (mEventCountSinceGZChanged == MAX_COUNT_GZ_CHANGE) {
                            zAxis = zValue;
                            mEventCountSinceGZChanged = 0;
                            if (zValue > 0) {
                                Log.d(TAG, "Phone face up");

                            } else if (zValue < 0) {
                                Log.d(TAG, "Phone face down");
                                getAnswer();
                            }
                        }
                    } else {
                        if (mEventCountSinceGZChanged > 0) {
                            zAxis = zValue;
                            mEventCountSinceGZChanged = 0;
                        }
                    }
                }

            }


        }




    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void getAnswer(){
        int rand = new Random().nextInt(answersArray.length);
        answerTV.setText(answersArray[rand]);
    }

}
