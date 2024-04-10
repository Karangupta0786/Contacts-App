package com.example.contactsapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.contactsapp.model.Contact
import com.example.contactsapp.model.ContactDao

@Database(entities = [Contact::class], version = 1)
abstract class ContactDatabase : RoomDatabase(){

    abstract fun dao():ContactDao

    companion object{
        @Volatile
        var INSTANCE: ContactDatabase? = null

        fun initialiseDatabase(context:Context): ContactDatabase {
            if (INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(
                        context,
                        ContactDatabase::class.java,
                        "database_name"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}