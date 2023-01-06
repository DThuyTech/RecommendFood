package com.example.recommendfood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recommendfood.DataBase.AppDatabase;
import com.example.recommendfood.Model.User;

import java.util.ArrayList;
import java.util.List;

public class CountingBMR extends AppCompatActivity {
    User user;
    TextView txtdotuoi,txtchieucao,txtcannang,txtBmr,txtgioitinh,txtBmi;
    SeekBar seekBar_bmr;
    ProgressBar progressBar_bmr;
    ListView listViewActi;
    Button btnChangel;
    SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_coutingbmr);
        pref=getSharedPreferences("acc",MODE_PRIVATE);

        String id = (pref.getString("id", null));
        user = AppDatabase.getInstance(this).userDao().findUser(id);

        Init();

        List<String> list = new ArrayList<>();
        list.add("Giam can");
        list.add("Tang can");
        list.add("Giu dang");

        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,list);
        listViewActi.setAdapter(arrayAdapter);


        btnChangel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listViewActi.getVisibility() == View.INVISIBLE) {
                    listViewActi.setVisibility(View.VISIBLE);
                }
                else{
                    listViewActi.setVisibility(View.INVISIBLE);
                }


            }
        });

        listViewActi.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position,long id){
                String type = listViewActi.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),type,Toast.LENGTH_SHORT).show();
                int calo = (int)(CounttingBmr(user));
                createItent(type,calo,user);

            }
        });



    }

    private void Init() {
        txtcannang = findViewById(R.id.txtcannang);
        txtchieucao = findViewById(R.id.txtchieucao);
        txtdotuoi = findViewById(R.id.txtdotuoi);
        txtBmr = findViewById(R.id.tetx_bmr);
        txtgioitinh =findViewById(R.id.txtgioitinh);
        progressBar_bmr = findViewById(R.id.progress_bmrrrr);
        listViewActi =findViewById(R.id.listview_activity);
        progressBar_bmr.setMax(100);
        txtBmi =findViewById(R.id.txr_bmi);

        progressBar_bmr.setProgress((int)CounttingBmi(user));
        btnChangel = findViewById(R.id.btn_change);
        txtBmi.setText("BMI: "+(int)CounttingBmi(user));
        txtBmr.setText("Calories:  "+(int)CounttingBmr(user));
        txtcannang.setText("Can nang: "+user.getWeight());
        txtdotuoi.setText("Do tuoi:"+user.getAge());
        txtchieucao.setText("Chieu cao: "+user.getHeight());
        if(user.isGender()==true) {
            txtgioitinh.setText("Gioi tinh: Nam");
        }
        else{
            txtgioitinh.setText("Gioi tinh: Nu");
        }




    }

    ///BMI max 40
    public double CounttingBmi(User user){
        double Bmi = Integer.parseInt(user.getWeight())/(( Integer.parseInt(user.getHeight())/(float)100) * (Integer.parseInt(user.getHeight())/(float)100));
        if(Bmi > 40){
            return 99;
        }

        else{
            return (float)Bmi/(float)40*100;
        }
    }

    public double CounttingBmr(User user){

        Double bmr;
        if(user.isGender()) {
            bmr = 10 * ( Integer.parseInt(user.getWeight()) / (float) 0.45359237) + 6.25 * 0.39370 * Integer.parseInt(user.getHeight()) - 5 *
                    Integer.parseInt(user.getAge())+ 5;
        }
        else{
            bmr = 10 * ( Integer.parseInt(user.getWeight()) / (float) 0.45359237) + 6.25 * 0.39370 * Integer.parseInt(user.getHeight()) - 5 *
                    Integer.parseInt(user.getAge())-161;
        }
        return bmr;
    }

    public void createItent(String loaiGoiy,int calories,User user){
        Intent intent = new Intent(CountingBMR.this,RecommanFoodBmi.class);

        Bundle bundle = new Bundle();
        intent.putExtra("type",loaiGoiy);
        bundle.putInt("user",calories);

        bundle.putInt("id",user.getId());
        intent.putExtra("mypackage",bundle);
        startActivity(intent);
    }

}
