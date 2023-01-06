package com.example.recommendfood;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.recommendfood.DataBase.AppDatabase;
import com.example.recommendfood.Model.Food;
import com.example.recommendfood.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RecommanFoodBmi extends AppCompatActivity {
    TextView txtTenmonsang,txtTenmontrua,txtTenmmontoi,txtCalosang,txtCalotrua,userName,
            txtCalotoi,txtCalotang,txtCalo;
    Calendar calendar;
    AlarmManager alarmManager;
    ImageView img1,img2,img3;
    PendingIntent pendingIntent;
    CardView cv;
    Button btn_doimonsang,btn_doimontoi,btn_doimontrua,btn_datgiomonsang,btn_datgiomontrua,btn_datgiomontoi;
    List<Food> mlistFoodRc;
    String type;
    int caloriesneed;
    int Calosangmax,Calosangmin;
    int Calotruamax,Calotruamin;
    int Calotoimax,Calotoimin;
    int[] listIdfoodS,listIdfoodT,listIdfoodTr;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomendfoodbmr);

        //get intent
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("mypackage");
        type = intent.getStringExtra("type");
        caloriesneed = bundle.getInt("user");
        String id = ""+bundle.getInt("id");
        User user = AppDatabase.getInstance(this).userDao().findUser(id);

        //anh xa
        Init();
        userName.setText("  "+user.getTk()+"");
        txtCalo.setText("  Calo: "+caloriesneed+"");
        createNotifycationChannel();




        if(type.compareTo("Giam can")==0){
            Giamcan();
        }
        else if(type.compareTo("Tang can")==0){
            Tangcan();

        }else if(type.compareTo("Giu dang")==0){
            Giudang();
        }

        //calo sau khi xu ly

        Computing();
        Log.d("test", "Initcaloris: "+caloriesneed);
       Initcaloris();
       mlistFoodRc = Xuly(0,0,0);
        Show(mlistFoodRc);


        //xu ly khi khang hang nhan doi mon
        int idmontheobuoi = mlistFoodRc.get(1).getId();

        btn_doimonsang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistFoodRc.set(0,Xuly(mlistFoodRc.get(0).getId(),0,0).get(0));
                Show(mlistFoodRc);


            }
        });
        btn_doimontrua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistFoodRc.set(1,Xuly(0,mlistFoodRc.get(0).getId(),0).get(1));
                Show(mlistFoodRc);

            }
        });
        btn_doimontoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistFoodRc.set(2,Xuly(0,0,mlistFoodRc.get(0).getId()).get(2));
                Show(mlistFoodRc);

            }
        });

        //hen gio
        btn_datgiomonsang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetReportFood();
            }
        });



//         int high = 500;
//        Log.d("test", "do cao"+cv.getHeight());
//        cv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(cv.getHeight() < high) {
//                    cv.setBottom(cv.getBottom() + 200);
//
//                }
//                else{
//                    cv.setBottom(cv.getBottom() - 200);
//                    Log.d("test", "doi bottom "+cv.getBottom());
//                }
//
//            }
//        });


    }



    private void Test(){
        txtTenmonsang.setText(""+Calosangmin);
        txtCalosang.setText(""+Calosangmax);
        txtCalotoi.setText(""+Calotoimax);
        txtTenmmontoi.setText(""+Calotoimin);

    }
    private List<Food> Xuly(int idsang,int idtrua, int idtoi){
        List<Food> mlistFoodRc = new ArrayList<Food>();
        Food foods,foodtr,foodt;
        int random ;
        do {
             random = (int) Math.floor(Math.random() * (listIdfoodS.length) + 0);
             foods = AppDatabase.getInstance(this).foodDao().getFoodbyId(listIdfoodS[random]);
        }while(foods.getId() == idsang);


        do {
            random = (int) Math.floor(Math.random() * (listIdfoodTr.length) + 0);
             foodtr = AppDatabase.getInstance(this).foodDao().getFoodbyId(listIdfoodTr[random]);
        }while (foodtr.getId() == idtrua);

        do {
            random = (int) Math.floor(Math.random() * (listIdfoodT.length) + 0);
             foodt = AppDatabase.getInstance(this).foodDao().getFoodbyId(listIdfoodT[random]);
        }while(foodt.getId() == idtoi);


        mlistFoodRc.add(foods);
        mlistFoodRc.add(foodtr);
        mlistFoodRc.add(foodt);
        return mlistFoodRc;

    }
    private List<Food> Sesion(String sesion,List<Food> foodList){
        List<Food> foods = new ArrayList<Food>();
        for(Food food : foodList){
            if(food.getSession().compareTo(sesion)==0){
                foods.add(food);
            }
        }
        return  foods;
    }
    private void Giudang() {
        txtCalotang.setText(" 0%");
    }
    private void Tangcan() {
        caloriesneed = caloriesneed*115/100;
        txtCalotang.setText("  Tăng : "+15+"%");
    }
    private void Giamcan() {
        caloriesneed = caloriesneed*73/100;
        txtCalotang.setText("  Giảm :"+(100-73)+"%");
    }
    public void Init(){
        cv = findViewById(R.id.cdv_food1);
        txtTenmonsang =findViewById(R.id.txtNamefood1);
        txtTenmontrua = findViewById(R.id.txtNamefood2);
        txtTenmmontoi = findViewById(R.id.txtNamefood3);
        txtCalosang = findViewById(R.id.txtCalofood1);
        txtCalotrua = findViewById(R.id.txtCalofood2);
        txtCalotoi = findViewById(R.id.txtCalofood3);
        userName = findViewById(R.id.txt_nameuser);
        txtCalotang = findViewById(R.id.txt_calotang);
        txtCalo = findViewById(R.id.txt_calouser);
        img1 = findViewById(R.id.imvFood1);
        img2= findViewById(R.id.imvFood2);
        img3=findViewById(R.id.imvFood3);
        btn_doimonsang= findViewById(R.id.btn_doimonsang);
        btn_doimontoi = findViewById(R.id.btn_doimontoi);
        btn_doimontrua = findViewById(R.id.btn_doimontrua);
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,12);
        calendar.set(Calendar.MINUTE,48);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        btn_datgiomonsang= findViewById(R.id.btn_datgiomonsang);
        btn_datgiomontrua = findViewById(R.id.btn_datgiomontrua);
        btn_datgiomontoi = findViewById(R.id.btn_datgiomontoi);





    }
    //thong ban bua sang
    private  void SetReportFood(){
        Toast.makeText(this,"Đã đặt thông báo",Toast.LENGTH_SHORT).show();

        alarmManager =(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,BroadCastTime.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,pendingIntent);
    }

    private void createNotifycationChannel(){
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            CharSequence name  ="RecommandFood";
            String description ="Bữa sáng với  "+txtTenmonsang.getText()+"  đang đợi bạn" ;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Notifycation",name,importance);
            channel.setDescription(description);



            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void Computing(){
        Calosangmax = caloriesneed*30/100;
        Calosangmin = caloriesneed*25/100;
        Calotruamax = caloriesneed*40/100;
        Calotruamin = caloriesneed*30/100;
        Calotoimin = caloriesneed*30/100;
        Calotoimax = caloriesneed*40/100;
    }

    public void Initcaloris(){
        listIdfoodS = AppDatabase.getInstance(this).foodDao().getIdFoodMaxMinSe(Calosangmin,Calosangmax);
        listIdfoodTr = AppDatabase.getInstance(this).foodDao().getIdFoodMaxMinSe(Calotruamin,Calotruamax);
        Log.d("test", "Initcaloris: "+Calotruamax+" "+Calotruamin);
        listIdfoodT = AppDatabase.getInstance(this).foodDao().getIdFoodMaxMinSe(Calotoimin,Calotoimax);
    }

    public void Show(List<Food> mlistFood){
        Food food = mlistFood.get(0);
        storageReference = FirebaseStorage.getInstance().getReference("images/"+food.getId());


        try {
            File file = File.createTempFile("tempfile",".jpg");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                    img1.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        txtTenmonsang.setText(food.getName());
        txtCalosang.setText(food.getCalo());

        food = mlistFood.get(1);
        storageReference = FirebaseStorage.getInstance().getReference("images/"+food.getId());


        try {
            File file = File.createTempFile("tempfile",".jpg");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                    img2.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        txtTenmontrua.setText(food.getName());
        txtCalotrua.setText(food.getCalo());

        food = mlistFood.get(2);
        storageReference = FirebaseStorage.getInstance().getReference("images/"+food.getId());


        try {
            File file = File.createTempFile("tempfile",".jpg");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                    img3.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        txtTenmmontoi.setText(food.getName());
        txtCalotoi.setText(food.getCalo());


    }
}
