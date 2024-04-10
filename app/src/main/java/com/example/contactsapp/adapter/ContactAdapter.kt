package com.example.contactsapp.adapter


import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsapp.MainActivity
import com.example.contactsapp.R
import com.example.contactsapp.model.Contact
import com.example.contactsapp.repository.ContactRepository
import com.example.contactsapp.util.Globals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ContactAdapter(val applicationContext: Context, val contactList: List<Contact>) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById(R.id.name)
        val number :TextView = itemView.findViewById(R.id.number)
        val card: CardView = itemView.findViewById(R.id.card_contact)
//        val img_call: ImageView = itemView.findViewById(R.id.img_call)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_contact,parent,false)
        return ContactViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val currData = contactList[position]
        holder.name.text = currData.name
        holder.number.text = currData.number

        holder.card.setOnClickListener {
            Globals.isUpdating = true
            openDialog(currData.name,currData.number)
        }

        holder.card.setOnLongClickListener {
            openDeleteDialog(applicationContext,currData.id,currData.name,currData.number)
            true
        }

//        val intent = Intent()
//        intent.action = Intent.ACTION_CALL

//        holder.img_call.setOnClickListener {
//            if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
//                // Permission is granted, proceed with making the call
//                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + holder.number))
//                applicationContext.startActivity(intent)
//            } else {
//                // Permission is not granted, request it from the user
//                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL_PERMISSION)
//            }
//
//            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + holder.number))
//            applicationContext.startActivity(intent)
//        }
    }

    private fun openDeleteDialog(context: Context,id: Long,name: String,number: String) {
        val dialog = Dialog(applicationContext)
        dialog.setContentView(R.layout.dialog_delete)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)

        val yes: CheckBox = dialog.findViewById(R.id.checkbox_yes)
        val no: CheckBox = dialog.findViewById(R.id.checkbox_no)
        val done:Button = dialog.findViewById(R.id.btn_done)

        done.setOnClickListener {
            if (yes.isChecked){
                deleteData(context,id,name,number)
                dialog.dismiss()
            }
            else{
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun openDialog(name1: String,number1: String) {

        val dialog = Dialog(applicationContext)
        dialog.setContentView(R.layout.dialog_contact)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val name : EditText = dialog.findViewById(R.id.et_name)
        val number : EditText = dialog.findViewById(R.id.et_number)

        name.setText(name1)
        number.setText(number1)

        val btn_save : Button = dialog.findViewById(R.id.btn_save)


        btn_save.setOnClickListener {

            if (name.text!!.isEmpty()) {
                name.error = "Please enter the name"
            } else if (number.text!!.isEmpty()) {
                number.error = "Please enter the number"
                name.isEnabled = false
            } else {
                name.isEnabled = false
                number.isEnabled = false

                if (Globals.isUpdating) {
                    updateData(applicationContext,1, name.text.toString(), number.text.toString())
                } else {
                    Toast.makeText(MainActivity(), "Do nothing!!", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun updateData(context: Context,id: Long, name: String, number: String) {
        CoroutineScope(Dispatchers.IO).launch {
            ContactRepository.update(context,Contact(id,name,number))
        }
    }

    private fun deleteData(context: Context,id: Long,name: String,number: String){
        CoroutineScope(Dispatchers.IO).launch {
            ContactRepository.delete(context, Contact(id,name,number))
        }
    }

}