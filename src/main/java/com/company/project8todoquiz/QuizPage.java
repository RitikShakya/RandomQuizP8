package com.company.project8todoquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuizPage extends AppCompatActivity {

    TextView time,correct,wrong;
    TextView question, a,b,c,d;

    Button next,finish;

    FirebaseDatabase firebaseDatabase ;
    DatabaseReference databaseReference,databaseReference2;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;


    String quest,optiona,optionb,optionc,optiond,correctoption;
     int questioncount;
     int questionum=1;
     String useranswer;


     int correctcount,wrongcount;
     CountDownTimer countDownTimer;
     static final long Total_time=25000;
     Boolean timerset;
     long time_left=Total_time;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_page);

        time = findViewById(R.id.timertext);
        correct = findViewById(R.id.correcttext);
        wrong = findViewById(R.id.wrongtext);

        question = findViewById(R.id.q);
        a=findViewById(R.id.a);
        b=findViewById(R.id.b);
        c=findViewById(R.id.c);
        d=findViewById(R.id.d);

        next=findViewById(R.id.nextbtn);
        finish = findViewById(R.id.finishbtn);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference =firebaseDatabase.getReference().child("questions");


        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        databaseReference2 = firebaseDatabase.getReference();
        showdatagame();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
                showdatagame();

            }
        });


        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pauseTimer();
                useranswer="a";
                if(useranswer.equals(correctoption)){

                    correctcount++;
                    correct.setText(""+correctcount);
                    a.setBackgroundColor(Color.GREEN);
                }else{
                    wrongcount++;
                    wrong.setText(""+wrongcount);
                    a.setBackgroundColor(Color.RED);
colorset();
                }

            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseTimer();
                useranswer="b";
                if(useranswer.equals(correctoption)){

                    correctcount++;
                    correct.setText(""+correctcount);
                    b.setBackgroundColor(Color.GREEN);
                }else{
                    wrongcount++;
                    wrong.setText(""+wrongcount);
                    b.setBackgroundColor(Color.RED);
                    colorset();

                }
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseTimer();
                useranswer="c";
                if(useranswer.equals(correctoption)){

                    correctcount++;
                    correct.setText(""+correctcount);
                    c.setBackgroundColor(Color.GREEN);
                }else{
                    wrongcount++;
                    wrong.setText(""+wrongcount);
                    c.setBackgroundColor(Color.RED);

                    colorset();
                }

            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseTimer();
                useranswer="d";
                if(useranswer.equals(correctoption)){

                    correctcount++;
                    correct.setText(""+correctcount);
                    d.setBackgroundColor(Color.GREEN);
                }else{
                    wrongcount++;
                    wrong.setText(""+wrongcount);
                    d.setBackgroundColor(Color.RED);

                    colorset();
                }
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendScore();
                Intent intent = new Intent(QuizPage.this,ScorePage.class);
                startActivity(intent);
                finish();


            }
        });
    }

    public void showdatagame(){

        starttimer();
        a.setBackgroundColor(Color.WHITE);
        b.setBackgroundColor(Color.WHITE);
        c.setBackgroundColor(Color.WHITE);
        d.setBackgroundColor(Color.WHITE);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                questioncount =(int) snapshot.getChildrenCount();

                quest = snapshot.child(String.valueOf(questionum)).child("q").getValue().toString();
                optiona = snapshot.child(String.valueOf(questionum)).child("a").getValue().toString();
                optionb = snapshot.child(String.valueOf(questionum)).child("b").getValue().toString();
                optionc = snapshot.child(String.valueOf(questionum)).child("c").getValue().toString();
                optiond = snapshot.child(String.valueOf(questionum)).child("d").getValue().toString();
                correctoption = snapshot.child(String.valueOf(questionum)).child("ans").getValue().toString();


                question.setText(quest);
                a.setText(optiona);
                b.setText(optionb);
                c.setText(optionc);
                d.setText(optiond);

                if(questionum<questioncount){
                    questionum++;
                }
                else{
                    Toast.makeText(QuizPage.this, "You have answered all Thankyou", Toast.LENGTH_SHORT).show();
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(QuizPage.this,"error",Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void colorset(){
        if(correctoption.equals("a")){
            a.setBackgroundColor(Color.GREEN);
        }else if(correctoption.equals("b")){
            b.setBackgroundColor(Color.GREEN);
        }
        else if(correctoption.equals("c")){
            c.setBackgroundColor(Color.GREEN);
        }
       else if(correctoption.equals("d")){
            d.setBackgroundColor(Color.GREEN);
        }
    }

    public void starttimer(){
        countDownTimer = new CountDownTimer(time_left,1000) {
            @Override
            public void onTick(long l) {

                time_left = l ;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timerset=false;
                pauseTimer();
                question.setText("The time is Up ! Sorry");

            }
        }.start();

        timerset=true;
    }

    public void resetTimer(){
        time_left=Total_time;
        updateCountDownText();
    }

    public void updateCountDownText(){
        int second = (int) (time_left/1000)%60;

        time.setText(""+second);

    }

    public void pauseTimer(){
        countDownTimer.cancel();
        timerset=false;
    }

    public void sendScore(){
        String userid = firebaseUser.getUid();

        databaseReference2.child("score").child(userid).child("correct").setValue(correctcount).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(QuizPage.this,"Success score send",Toast.LENGTH_SHORT).show();

            }
        });
        databaseReference2.child("score").child(userid).child("wrong").setValue(wrongcount);


    }
}