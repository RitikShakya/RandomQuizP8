package com.company.project8todoquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpPage extends AppCompatActivity {

    EditText email,password;
    Button signin,signup;


    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);


        email = findViewById(R.id.emailsignup);
        password =findViewById(R.id.passwordsignup);


        signin = findViewById(R.id.signinbtn);
        signup = findViewById(R.id.signupbtn);

        firebaseAuth = FirebaseAuth.getInstance();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpPage.this, LoginPage.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String useremail=email.getText().toString();

                String userpass = password.getText().toString();
                signupwithemail(useremail,userpass);
            }
        });
    }

    public void signupwithemail(String email,String password){

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    signup.setClickable(false);
                    Toast.makeText(SignUpPage.this, "succes ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpPage.this,MainActivity.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(SignUpPage.this,"failed",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}