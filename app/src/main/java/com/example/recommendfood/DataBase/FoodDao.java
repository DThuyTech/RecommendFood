package com.example.recommendfood.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.recommendfood.Model.CategoryAndFood;
import com.example.recommendfood.Model.Food;
import com.example.recommendfood.Model.User;

import java.util.List;

@Dao
public interface FoodDao {
    @Query("SELECT * FROM foods")
    List<Food> getAllFood();

    //    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    List<User> loadAllByIds(int[] userIds);
//
//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    Food findByName(String first, String last);
    @Query("Select * from foods where category_id == :cateId  ")
    List<Food> getListFoodbyCateid(int cateId);

    @Query("Select * from foods where id == :IdFood")
    Food getFoodById(int IdFood);



    @Insert
    void insertAll(Food... foods);

    @Insert
    void insertUser(Food foods);


    @Delete
    void delete(Food food);
    @Query("SELECT * FROM foods ORDER BY id DESC LIMIT 1")
    List<Food> lastData();
    @Query("SELECT * FROM foods")
    List<CategoryAndFood> categoryAndFood();

    @Query("Select * from foods where id == :IdF")
    Food getFoodbyId(int IdF);

    @Query("Select id from foods where calo > :Mincalo and calo < :Maxcalo")
    int[] getIdFoodMaxMinSe(int Mincalo,int Maxcalo);


}

