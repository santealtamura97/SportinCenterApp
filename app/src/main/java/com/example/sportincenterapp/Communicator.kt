package com.example.sportincenterapp

import android.widget.EditText

/*
Interface for communication between fragment
 */
interface Communicator {
    fun user_name_update (editTextInput: String) //Communication between fragment user and activity
    fun pass_data (editTextInput: String) //Communication between two fragment
}