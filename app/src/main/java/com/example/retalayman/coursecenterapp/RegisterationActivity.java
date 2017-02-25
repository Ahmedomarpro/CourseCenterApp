package com.example.retalayman.coursecenterapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.Firebase;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterationActivity extends AppCompatActivity {

    EditText username,password,email,phone,bdate,name;
     ImageButton btnreg;
    ProgressDialog p;
    private FirebaseAuth firebaseAuth;
    AlertDialog.Builder msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        username=(EditText)findViewById(R.id.txtuser);
        password=(EditText)findViewById(R.id.txtpass);
        email=(EditText)findViewById(R.id.txtemail);
        phone=(EditText)findViewById(R.id.txtphone);
        name=(EditText)findViewById(R.id.txtname);
        bdate=(EditText)findViewById(R.id.txtbdate);
         btnreg=(ImageButton)findViewById(R.id.imgreg) ;

        username.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == 66) {
                    password.requestFocus();
                }
                return false;
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();


        final Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH)+1;
        final int day = c.get(Calendar.DAY_OF_MONTH);
        final int hour = c.get(Calendar.HOUR);
        final int minute1 = c.get(Calendar.MINUTE);
        final int second = c.get(Calendar.SECOND);



        p=new ProgressDialog(this);
        msg = new AlertDialog.Builder(this);
        Firebase.setAndroidContext(this);



        bdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker = new DatePickerDialog(RegisterationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        bdate.setText(year + "/" + monthOfYear + "/" + dayOfMonth);
                    }

                }, year, month, day);
                datePicker.setTitle("Choose Date");
                datePicker.show();

            }
        });

        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //checking if email and passwords are empty


                Firebase ref = new Firebase(Config.FIREBASE_URL);
                Person person = new Person();
                //Adding values
                String n,pass,user,ema,ph,bd;
                n=name.getText().toString();
                pass=password.getText().toString();
                user=username.getText().toString();
                ph=phone.getText().toString();
                ema=email.getText().toString();
                bd=bdate.getText().toString();

                person.setUsername(user);
                person.setPassword(pass);
                person.setEmail(ema);
                person.setPhone(ph);
                person.setBdate(bd);
                person.setName(n);


                //Storing values to firebase
                Firebase newRef = ref.child("Person").push();
                newRef.setValue(person);

                p.setMessage("Please wait");
                p.show();
                registerUser();
               // progressDialog.dismiss();
                //Toast.makeText(RegisterationActivity.this, "User has been created succeed !!!", Toast.LENGTH_SHORT).show();


            }
        });

    }

    private void registerUser(){

        //getting email and password from edit texts
        String email2 = email.getText().toString().trim();
        String password2  = password.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email2)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password2)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }


        p.setMessage("Registering Please Wait...");
        p.show();

        //creating a new user

        firebaseAuth.createUserWithEmailAndPassword(email2, password2)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){

                            //display some message here
                           // Toast.makeText(RegisterationActivity.this,"Successfully registered",Toast.LENGTH_LONG).show();
                            msg.setTitle("Successfully registered:");
                            msg.setMessage("Successfully registered !!!");
                            msg.setIcon(R.drawable.logocongratulation);
                            msg.setPositiveButton("Ok", null);
                            msg.create();
                            msg.show();
                        }else{
                            //display some message here
                            msg.setTitle("Registration Error");
                            msg.setMessage("Registration Error is !!!"+task.getException().getMessage());
                            msg.setIcon(R.drawable.logoalertwarning);
                            msg.setPositiveButton("Ok", null);
                            msg.create();
                            msg.show();
                           // Toast.makeText(RegisterationActivity.this,"Registration Error is : "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                        p.dismiss();
                    }
                });
    }
}
