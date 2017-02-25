package com.example.retalayman.coursecenterapp;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.core.view.View;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Map;
public class MainListActivity extends AppCompatActivity {
    ListView lv;
    Button log;
    AlertDialog.Builder msg;
    public static final String EXTRA_MESSAGE="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
        msg = new AlertDialog.Builder(this);
       log=(Button)findViewById(R.id.logout);

        Intent intent = getIntent();
        final String message = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);
         Toast.makeText(MainListActivity.this,"Welcome : "+ message, Toast.LENGTH_LONG).show();

        log.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                SharedPreferences preferences =getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                finish();

                startActivity(new Intent(MainListActivity.this, LoginActivity.class));
            }
        });

        final DatabaseReference mRef= FirebaseDatabase.getInstance().getReferenceFromUrl(Config.FIREBASE_URL);
        lv = (ListView) findViewById(R.id.listView);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {

                String selectedFromList =(String) (lv.getItemAtPosition(position));
               Intent m = new Intent(MainListActivity.this, MainDetailsActivity.class);
                m.putExtra(EXTRA_MESSAGE,selectedFromList);

                startActivity(m);

            }


        });

        DatabaseReference ref= FirebaseDatabase.getInstance().getReferenceFromUrl(Config.FIREBASE_URL);
        ref = FirebaseDatabase.getInstance().getReference().child("Course");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        collectCourse((Map<String,Object>) dataSnapshot.getValue());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
    }

    private void collectCourse(Map<String,Object> users) {

        ArrayList<String> Users = new ArrayList<>();
        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){
            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            Users.add((String) singleUser.get("CourseName"));
        }
        lv.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, Users));
     }

}