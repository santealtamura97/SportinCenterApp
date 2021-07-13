package com.example.sportincenterapp.interfaces

import android.widget.EditText

/*
Interface for communication between fragment
 */
interface Communicator {
    //fun user_name_update (editTextInput: String) //Communication between fragment user and activity
    //fun user_email_update (editTextInput: String) //Communication between fragment user and activity
    fun um_update(um_1: String, um_2: String)
    fun theme(index : Int)
    fun language(index : Int)
}