package com.app.cattlemanagement.activities.consumer;

import static java.security.AccessController.getContext;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.app.cattlemanagement.AlarmReceiver;
import com.app.cattlemanagement.R;
import com.app.cattlemanagement.activities.admin.AdminDashboardActivity;
import com.app.cattlemanagement.activities.auth.AuthLoginActivity;
import com.app.cattlemanagement.activities.driver.DriverDashboard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConsumerDashboard extends AppCompatActivity {
    ImageView alert;
    TextView percentage,name;
    private DatabaseReference userRef;
    String percent;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_dashboard);
       name=findViewById(R.id.name);


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userUid = currentUser.getUid();

        // Get the user reference in the database
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userUid);

        // Read the first name and last name from the database
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String firstName = dataSnapshot.child("strEtFirstName").getValue(String.class);
                    String lastName = dataSnapshot.child("strEtLastName").getValue(String.class);

                    String fullName = firstName + " " + lastName;
                    name.setText(fullName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error if any
            }
        });









        percentage=findViewById(R.id.percentage);

        alert=findViewById(R.id.alert);
        databaseReference = firebaseDatabase.getReference("Alert");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                if(!value.equals("No alert")){
                    alert.setVisibility(View.VISIBLE);
                    percent=value;
                  Alarmtrigger(value);


                }
                else{
                    alert.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ConsumerDashboard.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
        DatabaseReference ultrasonicRef = FirebaseDatabase.getInstance().getReference("Ultrasonic Sensor");

        ultrasonicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
//                    // Retrieve the value of the "percentage" child
                    Integer percenta = dataSnapshot.child("Percentage").getValue(Integer.class);

//                    // Do something with the retrieved value
                    if (percenta != null) {
                        // Use the percentage value
                     percentage.setText(percenta.toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that occur
            }
        });

    }

    public void switchToSeller(View view) {
        startActivity(new Intent(this, DriverDashboard.class));
        finish();
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, AuthLoginActivity.class));
        finish();
    }

    public void addLocation(View view) {
        startActivity(new Intent(this, AddLocationActivity.class));

    }

    public void addTrashSources(View view) {
        startActivity(new Intent(this, AddTrashSourceActivity.class));


    }

    private void Alarmtrigger(String value) {
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
                .setContentText(value)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

// Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(1, builder.build());
// Set the alarm to trigger after 5 seconds (adjust as needed)
        long triggerTime = SystemClock.elapsedRealtime() + 5000;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent);

    }


}