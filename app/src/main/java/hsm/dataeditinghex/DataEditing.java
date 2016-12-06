package hsm.dataeditinghex;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Formatter;
import java.util.List;

/**
 * Created by E841719 on 22.01.2016.
 */
public class DataEditing  extends BroadcastReceiver
{
    private final String TAG = "DataEditingHex: ";
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String ScanResult = intent.getStringExtra("data");//Read the scan result from the Intent
        int version = intent.getIntExtra("version", 0);
        String sAimId;
        if(version==0)
            sAimId="not supported";
        else
            sAimId = intent.getStringExtra("aimId");

        Log.d(TAG, "data="+ScanResult+", aimId=" + sAimId);
        //---------------------------------------------
        //Modify the scan result as needed.
        //---------------------------------------------

        //Return the Modified scan result string
        Bundle bundle = new Bundle();
        //convert data to hex string
        byte[] digits = ScanResult.getBytes();

        //see http://stackoverflow.com/questions/19450452/how-to-convert-byte-array-to-hex-format-in-java
        Formatter formatter = new Formatter();
        for (byte b : digits) {
            formatter.format("%02x", b);
        }
        String hex = formatter.toString();

        //return edited data
        bundle.putString("data", hex);

        if (isAppForground(context)) {
            // App is in Foreground
            Toast.makeText(context, "Intent Detected: " + ScanResult + " / " + hex, Toast.LENGTH_LONG).show();
        } else {
            // App is in Background
            ;
        }

        Log.d("DataEditingHex: ", "Intent Detected: " + ScanResult + " / " + hex);

        setResultExtras(bundle);
    }
    public boolean isAppForground(Context mContext) {

        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(mContext.getPackageName())) {
                return false;
            }
        }

        return true;
    }
}