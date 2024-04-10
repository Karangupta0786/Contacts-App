package com.example.contactsapp.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "contact_table")
class Contact {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var name: String = ""
    var number: String = ""

    constructor(id: Long, name: String, number: String) {
        this.id = id
        this.name = name
        this.number = number
    }

    @Ignore
    constructor(name: String, number: String) {
        this.name = name
        this.number = number
    }


}