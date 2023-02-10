package com.example.contactwave.DB;




import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.contactwave.Pessoa;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     void insertContact(Pessoa pessoa) ;

    @Query("SELECT * FROM person_table ORDER BY name ASC")
    List<Pessoa> getAll();

    @Update()
    void updateContact(Pessoa pessoa);

    @Delete
    void deleteContact(Pessoa pessoa);

    @Query(("DELETE from person_table"))
    void deletartabela();



}
