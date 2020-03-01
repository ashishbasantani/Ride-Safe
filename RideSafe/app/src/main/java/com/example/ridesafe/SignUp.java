package com.example.ridesafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {

    EditText name,email,password;
    Button signUp;
    SQLiteconn db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();

        name=findViewById(R.id.signUpName);
        email=findViewById(R.id.signUpEmail);
        password=findViewById(R.id.signUpPassword);
        signUp=findViewById(R.id.signUpButton);

        db=new SQLiteconn(this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpFun();
            }
        });

    }

    private void signUpFun() {

        String n=name.getText().toString();
        String phone=email.getText().toString();
        String pass=password.getText().toString();

        if(n.trim().equals("")||phone.trim().equals("")||pass.trim().equals("")){
            Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
        }
        else{
            if(db.createUser(n,phone,pass)){
                Toast.makeText(this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(this,MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                finishAffinity();
                startActivity(i);
            }
            else{
                Toast.makeText(this, "Some problem occured", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void login(View view) {
        Intent i=new Intent(this,Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finishAffinity();
        startActivity(i);
    }
}
