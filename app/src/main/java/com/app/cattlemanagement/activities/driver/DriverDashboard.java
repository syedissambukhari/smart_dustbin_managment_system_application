package com.app.cattlemanagement.activities.driver;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.app.cattlemanagement.AlarmReceiver;
import com.app.cattlemanagement.R;
import com.app.cattlemanagement.activities.auth.AuthLoginActivity;
import com.app.cattlemanagement.activities.consumer.ConsumerDashboard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DriverDashboard extends AppCompatActivity {
    ImageView alert;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    String alarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_dashboard);

        alert=findViewById(R.id.alert);
        databaseReference = firebaseDatabase.getReference("Alert");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                if(!value.equals("No alert")){
                    alarm=value;
                    Alarmtrigger();
                    alert.setVisibility(View.VISIBLE);
                }
                else{
                    alert.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DriverDashboard.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });



    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, AuthLoginActivity.class));
        finish();
    }

    public void switchToBuyer(View view) {
        startActivity(new Intent(this, ConsumerDashboard.class));
        finish();
    }

    public void assignedTrashLocations(View view) {
        startActivity(new Intent(this, AssignedTrashLocationsActivity.class));

    }
    private void Alarmtrigger() {
        Intent alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_MUTABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Channel Name";
            String channelDescription = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel_id", channelName, importance);
            channel.setDescription(channelDescription);

            // Register the notification channel with the system
            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "channel_id")
                .setSmallIcon(R.drawable.alert)

                .setContentTitle("Alert!")
                .setContentText((CharSequence) alarm)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

// Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(1, builder.build());
// Set the alarm to trigger after 5 seconds (adjust as needed)
        long triggerTime = SystemClock.elapsedRealtime() + 5000;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent);

    }


}