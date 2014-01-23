package com.example.stepcounter;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class StepActivity extends Activity implements SensorEventListener {

    private static SensorManager mManager;
    private static Sensor mStepCounter;
    
    private static long STEP_COUNT = 0;
    
    private TextView mTvStep;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        mManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // 껐다가 켠 후부터 전체 카운트
//        mStepCounter = mManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        // 시작한 순간부터 1씩 올라감
        mStepCounter = mManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        
        mTvStep = (TextView) findViewById(R.id.tv_step);
        findViewById(R.id.btn_clear).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                STEP_COUNT = 0;
                setStep(STEP_COUNT);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mManager.registerListener(this, mStepCounter, SensorManager.SENSOR_DELAY_NORMAL);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        mManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i("yeojoy", sensor.getName() + " sensor is activated. the Accuracy is " + accuracy);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("Sensor name : ").append(event.sensor.getName()).append("\n");
        sb.append("TimeStamp : ").append(event.timestamp).append("\n");
        for (float v : event.values) {
            sb.append(" value >>> ").append(v).append("\n");
            STEP_COUNT = STEP_COUNT + ((long) v);
        }
        sb.append("누적 발걸음 : ").append(STEP_COUNT).append("\n");
        Log.d("yeojoy", sb.toString());
        setStep(STEP_COUNT);
    }
    
    private void setStep(long step) {
        mTvStep.setText("STEP : " + String.valueOf(STEP_COUNT));
    }
}
