package com.example.contactwave;




import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "person_table")
public class Pessoa{

   @PrimaryKey(autoGenerate = true)
   public int id =0;
    @ColumnInfo(name = "name")
    public String name="";
   @ColumnInfo(name = "phone")
   public String phone="";


    public Pessoa(int id,String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    @Ignore
    public Pessoa(String name, String phone)
    {
        this.name = name;
        this.phone = phone;
    }
};

