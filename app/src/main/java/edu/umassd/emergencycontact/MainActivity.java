package edu.umassd.emergencycontact;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private LinearLayout mDrawerList;
    private Intent emergencyCallIntent;
    private NotificationCompat.Builder mNotifyBuilder;
    private int mnotID;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button button = (Button) findViewById(R.id.emergencyCall);
        mDrawerList = (LinearLayout)findViewById(R.id.navList);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callEmergency();
            }
        });

        ctx = getApplicationContext();

        NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);


        emergencyCallIntent = new Intent(this, MainActivity.class);
        emergencyCallIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent emergencyCallPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, emergencyCallIntent, Intent.FILL_IN_ACTION);

        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Emergency Caller")
                .setContentText("Click to make and emergency call")
                .setContentIntent(emergencyCallPendingIntent)
                .setAutoCancel(true);
        Notification not = mNotifyBuilder.build();
        not.flags |= Notification.FLAG_AUTO_CANCEL;
        nm.notify(mnotID,not);
    }

    protected void onNewIntent(Intent intent) {
        callEmergency();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.drawer){
            DrawerLayout drawer = (DrawerLayout) this.findViewById(R.id.drawer_layout);
            drawer.openDrawer(mDrawerList);
        }

        return super.onOptionsItemSelected(item);
    }

    public void callEmergency() {
        Calendar c = Calendar.getInstance();
        Log.e("Hour", "Hour of day : " + c.get(Calendar.HOUR_OF_DAY) );
        int hour = c.get(Calendar.HOUR_OF_DAY);

        if (9 < hour && hour < 17){
            Log.e("Hour", "Work time!");
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "8452331851"));
            startActivity(intent);
        }
        else{

            Log.e("Hour", "Play Time!");
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "5089998596"));
            startActivity(intent);

        }

    }
}
