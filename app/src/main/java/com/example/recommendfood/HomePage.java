package com.example.recommendfood;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomePage extends AppCompatActivity {
    SharedPreferences prf;
    Button btn_logout;
    Button btn_chosse_q,btn_rc_food;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        btn_logout= findViewById(R.id.btn_logout);
        btn_chosse_q= findViewById(R.id.btn_choose_q);
        prf=getSharedPreferences("acc",MODE_PRIVATE);
        btn_rc_food=findViewById(R.id.btn_recomedn);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor=prf.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(HomePage.this,Login.class));
            }
        });
        btn_chosse_q.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomePage.this,Question.class));
            }
        });
        btn_rc_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomePage.this,CountingBMR.class));
            }
        });
    }

}