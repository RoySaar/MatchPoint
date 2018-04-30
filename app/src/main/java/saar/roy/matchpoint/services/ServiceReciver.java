package saar.roy.matchpoint.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Roy on 30/04/2018.
 */

public class ServiceReciver extends Service {

    int status = 0;
    String batteryString = "0";

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int battery = intent.getIntExtra("level",0);
            batteryString = String.valueOf(status);


            changeBrightness(battery);
        }
    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.registerReceiver(this.broadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    public void changeBrightness(int buttatyPercents) {
        if(buttatyPercents<15)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.System.canWrite(getApplicationContext())); {
                    Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                    Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 30);
                }
            }
    }
}