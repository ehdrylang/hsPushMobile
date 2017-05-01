package com.example.dell.capston;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

/**
 * Created by Administrator on 2016-05-29.
 */
public class Setting extends FragmentActivity {

    Switch switch_Notification;
    MyApplication myApplication = MyApplication.instance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        switch_Notification = (Switch) findViewById(R.id.switch_Notification);


        switch_Notification.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    myApplication.setting_Notification = true;      //이거는 푸시 안받음
                else
                    myApplication.setting_Notification = false;
            }
        });
    }


    @Override
    public synchronized void onStart() {
        super.onStart();
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
