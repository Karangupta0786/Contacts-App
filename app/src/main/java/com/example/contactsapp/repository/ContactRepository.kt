package com.example.contactsapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.contactsapp.ContactDatabase
import com.example.contactsapp.model.Contact
import com.example.contactsapp.model.ContactDao

class ContactRepository {

    companion object{

        var instance:ContactDatabase? = null

        fun getDatabase(context: Context): ContactDatabase?{
            instance = ContactDatabase.initialiseDatabase(context)
            return instance
        }

        suspend fun insert(context: Context,contact: Contact){
            instance = getDatabase(context)
            instance?.dao()?.insert(contact)
        }

        suspend fun update(context: Context,contact: Contact){
            instance = getDatabase(context)
            instance?.dao()?.update(contact)
        }

        suspend fun delete(context: Context,contact: Contact){
            instance = getDatabase(context)
            instance?.dao()?.delete(contact)
        }

        fun getContact(context: Context):LiveData<List<Contact>>?{
            instance = getDatabase(context)
            return instance?.dao()?.getContacts()
        }

    }

}