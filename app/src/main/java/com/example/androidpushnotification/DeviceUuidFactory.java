package com.example.androidpushnotification;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class DeviceUuidFactory {

    private static volatile String sUuid;

    public static String getDeviceUuid(Context context) {
        if (sUuid == null) {
            synchronized (DeviceUuidFactory.class) {
                if (sUuid == null) {
                    final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

                    // Use the Android ID unless it's null, then generate a random UUID
                    sUuid = (androidId != null && !androidId.equals("9774d56d682e549c")) ? androidId : generateUuid();

                }
            }
        }
        return sUuid;
    }

    private static String generateUuid() {
        return java.util.UUID.randomUUID().toString();
    }
    public static String getIMEI(Context context) {
        String deviceId = null;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                deviceId = telephonyManager.getImei();
                Toast.makeText(context, "IMEI: " + deviceId, Toast.LENGTH_SHORT).show();
            } else {
                // For devices below Android Oreo
                deviceId = telephonyManager.getDeviceId();
                Toast.makeText(context, "IMEI not available on this device.", Toast.LENGTH_SHORT).show();
            }
        }
        return deviceId;
    }
    public static String getDeviceId(Context context) {

        String deviceId;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null) {
                deviceId = mTelephony.getDeviceId();
            } else {
                deviceId = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }

        return deviceId;
    }
}

