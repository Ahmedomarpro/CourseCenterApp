package com.example.retalayman.coursecenterapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextView txtreg;
    ImageButton img;
    EditText email,pass;
    CheckBox ch;
    AlertDialog.Builder msg;
    SharedPreferences.Editor editor;
    private FirebaseAuth firebaseAuth;
    ProgressDialog p;
    public static final String EXTRA_MESSAGE="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        p=new ProgressDialog(this);
        msg = new AlertDialog.Builder(this);
        txtreg = (TextView) findViewById(R.id.textView3);
        img = (ImageButton) findViewById(R.id.imageButton);
         msg = new AlertDialog.Builder(this);
        ch=(CheckBox)findViewById(R.id.Chk);
        email=(EditText)findViewById(R.id.txtloginuser);
        pass=(EditText)findViewById(R.id.txtloginpass);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        //remembe me
        SharedPreferences pref = getSharedPreferences("MyPref",MODE_PRIVATE);
        String username = pref.getString("USERNAME", null);
        String password = pref.getString("PASSWORD", null);
        if (username != null || password != null) {
            Intent m = new Intent(LoginActivity.this, MainListActivity.class);
            m.putExtra(EXTRA_MESSAGE,username);
            startActivity(m);
        }

        txtreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterationActivity.class));
            }
        });
    }
    private void Login(){

        //getting email and password from edit texts
        final String email2 = email.getText().toString().trim();
        final String password2  = pass.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email2)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password2)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        //displaying a progress dialog
        p.setMessage("Login Please Wait...");
        p.show();

        firebaseAuth.signInWithEmailAndPassword(email2, password2)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            //display some message here
                           // startActivity(new Intent(LoginActivity.this, MainListActivity.class));
                            if(ch.isChecked()) {
                                getSharedPreferences("MyPref", MODE_PRIVATE)
                                        .edit()
                                        .putString("USERNAME", email2)
                                        .putString("PASSWORD", password2)
                                        .commit();
                            }

                            Intent m = new Intent(LoginActivity.this, MainListActivity.class);
                            m.putExtra(EXTRA_MESSAGE,email2);
                            startActivity(m);
                        }else{
                            //display some message here
                            msg.setTitle("Login Error");
                            msg.setMessage("Login Error is !!!"+task.getException().getMessage());
                            msg.setIcon(R.drawable.logoalertwarning);
                            msg.setPositiveButton("Ok", null);
                            msg.create();
                            msg.show();
                          //  Toast.makeText(LoginActivity.this,"Login Error is : "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                        p.dismiss();
                    }
                });
    }
}

