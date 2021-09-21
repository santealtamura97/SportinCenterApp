package com.example.sportincenterapp.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Base64
import android.widget.TextView
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream
import kotlin.math.round

object UserPageUtils {

    fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }

    fun BMI (weight: String, tall: String): Double {
        var bmiScore = 0.0
        if (weight.length > 1 && tall.length > 1) {
            val numerator = weight.toDouble()
            val denominator = (tall.toDouble() / 100) * (tall.toDouble() / 100)
            bmiScore = (numerator / denominator)
        }
        return bmiScore.round(1)
    }

    fun calculateBMI(weight: String, tall: String, result: TextView, bmi: TextView): String {
        var bmiScore = BMI(weight, tall)
        if (bmiScore > 0) {
            if (bmiScore < 18.5){
                result.text = "SOTTOPESO"
                result.setTextColor(Color.parseColor("#2E2EFF"))
                bmi.setTextColor(Color.parseColor("#2E2EFF"))
            }else if (bmiScore in 18.5..24.9) {
                result.text = "PESO NORMALE"
                result.setTextColor(Color.parseColor("#00D100"))
                bmi.setTextColor(Color.parseColor("#00D100"))
            }else if (bmiScore in 25.0..29.9) {
                result.text = "SOVRAPPESO"
                result.setTextColor(Color.parseColor("#FF7518"))
                bmi.setTextColor(Color.parseColor("#FF7518"))
            }else if (bmiScore in 30.0..34.9) {
                result.text = "OBESO!"
                result.setTextColor(Color.parseColor("#FF0000"))
                bmi.setTextColor(Color.parseColor("#FF0000"))
            }else if (bmiScore > 35) {
                result.text = "ESTREMAMENTE OBESO!"
                result.setTextColor(Color.parseColor("#800000"))
                bmi.setTextColor(Color.parseColor("#800000"))
            }else if (bmiScore < 18.5) {
                result.text = "PESO O ALTEZZA NON VALIDI!"
            }
        }
        return bmiScore.toString()
    }

    // method for base64 to bitmap
   fun decodeBase64(input: String?): Bitmap? {
        val decodedByte: ByteArray = Base64.decode(input, 0)
        return BitmapFactory
            .decodeByteArray(decodedByte, 0, decodedByte.size)
    }

    // method for bitmap to base64
    fun encodeTobase64(image: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        val imageEncoded: String = Base64.encodeToString(b, Base64.DEFAULT)
        return imageEncoded
    }
}