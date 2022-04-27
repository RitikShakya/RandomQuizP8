package com.company.project8todoquiz;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginPage extends AppCompatActivity {


    EditText email,password;

    TextView forgotpass;
       SignInButton googlesignin;

    Button SignIn, Signup;
    GoogleSignInClient googleSignInClient ;

    ActivityResultLauncher<Intent> activityResultLauncher;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        email = findViewById(R.id.emailsignin);
        password =findViewById(R.id.passwordsignin);

        forgotpass = findViewById(R.id.forgotpasstext);
        googlesignin = findViewById(R.id.googlesignin);

        Signup = findViewById(R.id.signin);
        SignIn = findViewById(R.id.signup);

        registerActivityForGoogle();

        firebaseAuth = FirebaseAuth.getInstance();

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginPage.this, SignUpPage.class);
                startActivity(intent);
            }
        });

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String useremail = email.getText().toString();
                String userpass = password.getText().toString();


                signinwithemail(useremail,userpass);

            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPage.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        googlesignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signingoogle();
            }
        });
    }

    public void signinwithemail(String email,String password)
    {
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Toast.makeText(LoginPage.this, "Succes Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginPage.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginPage.this,"Failed Wrong username or pass", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public  void  signingoogle(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("461069679776-96ghrp63icbpk74uejtnuaojmul9u3uv.apps.googleusercontent.com").requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(LoginPage.this, gso);

        signinusingGoogle();
    }

    public  void signinusingGoogle(){

        Intent signinintent = googleSignInClient.getSignInIntent();
        activityResultLauncher.launch(signinintent);
    }

    public void registerActivityForGoogle(){


        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                int resultcode = result.getResultCode();
                Intent data = result.getData();

                if(resultcode== RESULT_OK && data!=null){
                    Task<GoogleSignInAccount > task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    firebaseSignInWithGoogle(task);
                }

            }
        });
    }

    public void firebaseSignInWithGoogle(Task<GoogleSignInAccount> task)
    {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);

            Toast.makeText(this, "SuccessFull", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(LoginPage.this,MainActivity.class);
            startActivity(intent);
            finish();
            firebaseGoogleAccount(account);
        } catch (ApiException e) { Toast.makeText(this, "erroor" +e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void firebaseGoogleAccount(GoogleSignInAccount account){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    //FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    //firebaseUser.
                }else{

                }
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser =firebaseAuth.getCurrentUser();

        if(firebaseUser!=null){

            Intent intent = new Intent(LoginPage.this,MainActivity.class);
            startActivity(intent);
        }
    }
}