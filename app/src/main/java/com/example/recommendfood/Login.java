package com.example.recommendfood;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.recommendfood.DataBase.AppDatabase;
import com.example.recommendfood.DataBase.UserDao;
import com.example.recommendfood.Model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    EditText txtUsername, txtpassword;
    Button btnDangKy, btnDangNhap;


    SharedPreferences pref;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("ĐĂNG NHẬP");
        setContentView(R.layout.activity_login);
        txtUsername = findViewById(R.id.username);
        txtpassword = findViewById(R.id.password);
        btnDangNhap = findViewById(R.id.btnLogin);
        btnDangKy = findViewById(R.id.btnDangKy);
        createNotifycationChannel();
//        User user = new User("123","123");
//        user.setName("Trung Huy");
//        user.setAge("20");
//        user.setGender(true);
//        user.setHeight("170");
//        user.setWeight("60");
//        AppDatabase.getInstance(this).userDao().registerUser(user);



        String usernamePattern = "^[a-zA-Z0-9]{8,}$";
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

        pref=getSharedPreferences("acc",MODE_PRIVATE);
        if(pref.contains("username")&&pref.contains("password")){
            startActivity(new Intent(Login.this, HomePage.class));
        }
        btnDangNhap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final String username = txtUsername.getText().toString();
                final String password = txtpassword.getText().toString();
                //startActivity(new Intent(Login.this, HomePage.class));
                if (username.equals("admin") && password.equals("admin")) {

                    startActivity(new Intent(Login.this, PageAdmin.class));
                }
                else {
                    Pattern usernameP = Pattern.compile(usernamePattern);
                    Matcher usernameM = usernameP.matcher(username);
                    Pattern passwordP = Pattern.compile(passwordPattern);
                    Matcher passwordM = passwordP.matcher(password);

                    AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());
                    final UserDao userDao = appDatabase.userDao();

                    User user = userDao.getUser(username, password);

                    if (java.util.regex.Pattern.matches(usernamePattern, username) && java.util.regex.Pattern.matches(passwordPattern, password)) {
                        if (user == null) {
                            Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_LONG).show();
                        } else {
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("username", username);
                            editor.putString("password", password);
                            editor.putString("id",user.getId()+"");
                            editor.commit();
                            Toast.makeText(getApplicationContext(), "Logged in successfully", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Login.this, HomePage.class));
                        }
                    } else {

                        Toast.makeText(getApplicationContext(), "Invalid", Toast.LENGTH_LONG).show();
                    }

                }



            }

        });


        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(Login.this, Register.class);
//                startActivity(intent);




                Intent myIntent = new Intent(Login.this, Register.class);
                //myIntent.putExtra("key", '1'); //Optional parameters

                Login.this.startActivity(myIntent);

            }
        });
    }
    private void createNotifycationChannel(){
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            CharSequence name  ="RecommandFood";
            String description ="Channel for RecommadFood";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Notifycation",name,importance);
            channel.setDescription(description);



            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



    private void notifycate() {

        Intent intent = new Intent(Login.this,BroadCastTime.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast (Login.this,0,intent,PendingIntent.FLAG_IMMUTABLE);



        AlarmManager alarmManager =(AlarmManager) getSystemService(ALARM_SERVICE);

        long timeAtButton = System.currentTimeMillis();
        long tenSecond = 1000 *10;

        alarmManager.set(AlarmManager.RTC_WAKEUP,
                timeAtButton+tenSecond,
                pendingIntent);

        Toast.makeText(this,"Set",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy(){
        notifycate();
        super.onDestroy();

    }


}