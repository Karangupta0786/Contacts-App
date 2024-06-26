package com.example.contactsapp

import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsapp.adapter.ContactAdapter
import com.example.contactsapp.model.Contact
import com.example.contactsapp.util.Globals
import com.example.contactsapp.viewModel.ContactViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: ContactViewModel
    private lateinit var adapter: ContactAdapter
    lateinit var etSearch:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etSearch = findViewById(R.id.et_search)


        viewModel = ViewModelProvider(this)[ContactViewModel::class.java]

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_contacts)

        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        viewModel.getContact(this)?.observe(this){ list->
            adapter = ContactAdapter(this,list)
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()

            etSearch.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val filteredData = list.filter { contactData->
                        contactData.name.contains(s.toString(), ignoreCase = true) || contactData.number.contains(s.toString())
                    }
                    adapter.onSearch(filteredData)
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })


        }


        findViewById<Button>(R.id.btn_add).setOnClickListener {
            Globals.isUpdating = false
            openAddDialog()
        }


    }
    private fun openAddDialog() {

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_contact)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val name : EditText = dialog.findViewById(R.id.et_name)
        val number : EditText = dialog.findViewById(R.id.et_number)


        val btn_save : Button = dialog.findViewById(R.id.btn_save)



        btn_save.setOnClickListener {

            if (name.text.isEmpty()) {
                name.error = "Please enter the name"
            } else if (number.text!!.isEmpty()) {
                number.error = "Please enter the number"
                name.isEnabled = false
            } else {
                name.isEnabled = false
                number.isEnabled = false

                if (!Globals.isUpdating) {
                    insertData(applicationContext,name.text.toString(), number.text.toString())
                }
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun insertData(context: Context,name: String, number: String) {
        viewModel.insert(context,Contact(name,number))
    }

    private fun updateData(context: Context,id: Long, name: String, number: String) {
        viewModel.update(context,Contact(id,name,number))
    }


}