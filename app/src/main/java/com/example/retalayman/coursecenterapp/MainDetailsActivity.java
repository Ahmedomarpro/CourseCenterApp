package com.example.retalayman.coursecenterapp;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainDetailsActivity extends AppCompatActivity {
    ImageButton btnk;
    TextView name,cost,details,teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_details);

        name=(TextView)findViewById(R.id.txtname);
        teacher=(TextView)findViewById(R.id.txtteacher);
        cost=(TextView)findViewById(R.id.txtcost);
        details=(TextView)findViewById(R.id.txtdetails);



        btnk = (ImageButton) findViewById(R.id.btnabdoo);

        btnk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /////
            }
        });
        Intent intent = getIntent();
        final String message = intent.getStringExtra(MainListActivity.EXTRA_MESSAGE);
        // Toast.makeText(MainDetailsActivity.this, message, Toast.LENGTH_LONG).show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Course");
        myRef.orderByChild("CourseName").equalTo(message).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    name.setText(message);
                    teacher.setText(""+ childDataSnapshot.child("Teacher name").getValue());
                    cost.setText(""+ childDataSnapshot.child("Cost").getValue());
                    details.setText(""+ childDataSnapshot.child("Details").getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
    }
}
