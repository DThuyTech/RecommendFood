package com.example.recommendfood.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.recommendfood.Model.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user where tk= (:tk) and password = (:password)")
    User getUser(String tk, String password);

    @Query("SELECT * FROM user where id= (:id)")
    User findUser(String id);

    @Insert
    void registerUser(User users);

    @Delete
    void delete(User user);

    @Query("Select * from user ")
    List<User> getAlluser();
}

