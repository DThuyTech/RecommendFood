package com.example.recommendfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recommendfood.DataBase.AppDatabase;
import com.example.recommendfood.Model.Food;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ShowFood extends AppCompatActivity {
    Button btn_back;
    StorageReference storageReference;
    ImageView imageShow1,imageShow2;
    TextView txtTenmon1,txtTenmon2,txtCalomon1,txtCalomon2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_food);
        btn_back=findViewById(R.id.btn_back);
        Init();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("mypackage");
        int idmon1 = bundle.getInt("Mon1");
        int idmon2 = bundle.getInt("Mon2");

        Food food1;
        food1 = AppDatabase.getInstance(ShowFood.this).foodDao().getFoodById(idmon1);

        storageReference = FirebaseStorage.getInstance().getReference("images/"+food1.getId());


        try {
            File file = File.createTempFile("tempfile",".jpg");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                    imageShow1.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        txtTenmon1.setText(food1.getName());
        txtCalomon1.setText(food1.getCalo());



        Food food2 = new Food();
        food2 = AppDatabase.getInstance(ShowFood.this).foodDao().getFoodById(idmon2);
        storageReference = FirebaseStorage.getInstance().getReference("images/"+food2.getId());


        try {
            File file = File.createTempFile("tempfile",".jpg");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                    imageShow2.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        txtTenmon2.setText(food2.getName());
        txtCalomon2.setText(food2.getCalo());
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowFood.this, HomePage.class));



            }
        });
    }
    private void Init(){
        txtTenmon1 =findViewById(R.id.nameShow1);
        txtTenmon2 =findViewById(R.id.nameShow2);
        txtCalomon1 = findViewById(R.id.caloShow1);
        txtCalomon2 = findViewById(R.id.caloShow2);
        imageShow1 = findViewById(R.id.imageShow1);
        imageShow2 = findViewById(R.id.imageShow2);
    }
}