package com.example.ridesafe;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    SQLiteconn db;
    Button loginButton;
    EditText userName,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        loginButton=findViewById(R.id.loginButton);
        userName=findViewById(R.id.loginEmailId);
        password=findViewById(R.id.loginPassword);
        db=new SQLiteconn(this);

        loginButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        login();
                    }
                }
        );
    }

    private void login() {

        String email=userName.getText().toString();
        String pass=password.getText().toString();

        if(email.trim().equals("")||pass.trim().equals("")){
            Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
        }
        else{
            Cursor c=db.getUser(email);
            if(c.getCount()!=0){
                c.moveToNext();
                if(pass.equals(c.getString(2))){
                    Intent i=new Intent(Login.this,MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    finishAffinity();
                    startActivity(i);
                }
                else{
                    Toast.makeText(this, "Invalid Password", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "User not registered", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void signUp(View view) {

            Intent i=new Intent(this, SignUp.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finishAffinity();
            startActivity(i);

    }
}
