package com.example.contactsapp.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactsapp.model.Contact
import com.example.contactsapp.repository.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactViewModel:ViewModel() {

    fun insert(context: Context, contact: Contact){
        viewModelScope.launch(Dispatchers.IO){
            ContactRepository.insert(context,contact)
        }
    }

    fun update(context: Context,contact: Contact){
        viewModelScope.launch(Dispatchers.IO){
            ContactRepository.update(context,contact)
        }
    }

    fun delete(context: Context,contact: Contact){
        viewModelScope.launch(Dispatchers.IO){
            ContactRepository.delete(context,contact)
        }
    }

    fun getContact(context: Context): LiveData<List<Contact>>? {
        return ContactRepository.getContact(context)
    }

}