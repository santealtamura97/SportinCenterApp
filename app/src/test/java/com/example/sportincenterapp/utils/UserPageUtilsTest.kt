package com.example.sportincenterapp.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.fragment.app.Fragment
import org.junit.Test

import org.junit.Assert.*

class UserPageUtilsTest : Fragment(){


    @Test
    fun BMI() {

        var weight: String = "80"
        var tall: String = "180"
        var computedBmi = UserPageUtils.BMI(weight,tall)
        var myBmi = "24.7"
        assertEquals(myBmi, computedBmi.toString())

    }

    @Test
    fun decodeBase64() {
    }

    @Test
    fun encodeTobase64() {

    }
}