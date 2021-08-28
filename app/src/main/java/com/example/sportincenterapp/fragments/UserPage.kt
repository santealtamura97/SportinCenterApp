package com.example.sportincenterapp.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.sportincenterapp.R
import com.example.sportincenterapp.data.ApiClient
import com.example.sportincenterapp.data.models.User
import com.example.sportincenterapp.data.responses.ApiResponse
import com.example.sportincenterapp.data.responses.SubscriptionResponse
import com.example.sportincenterapp.interfaces.Communicator
import com.example.sportincenterapp.utils.ApplicationContextProvider
import com.example.sportincenterapp.utils.SessionManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class UserPage : Fragment() {

    private var userSubscriptionType: TextView? = null
    private var userSubscriptionDeadline: TextView? = null
    private var userSubscriptionStatus: TextView? = null
    private var userSubscriptionStatusIconActive: ImageView? = null
    private var userSubscriptionStatusIconExpired: ImageView? = null
    private var userEntries: TextView? = null
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private lateinit var result: TextView
    private lateinit var bmi: TextView
    private lateinit var imageProfile: ImageView
    private lateinit var changeProfileImage: FloatingActionButton
    private lateinit var communicator: Communicator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //Fragment view
        val v = inflater.inflate(R.layout.fragment_user_page, container, false)

        communicator  = activity as Communicator

        // Component of the view //
        // RelativeLayout
        val rLayout = v.findViewById<RelativeLayout>(R.id.relative_layout_user)
        //User name
        var user_title = v.findViewById<TextView>(R.id.info_user_text)
        var physycall_title = v.findViewById<TextView>(R.id.physycal_car_title)
        var user = v.findViewById<TextView>(R.id.user_text)
        // Telephone
        var telephone_view = v.findViewById<TextView>(R.id.user_phonenumber_view) //view
        var telephone_edit = v.findViewById<EditText>(R.id.user_phonenumber_edit) //set

        // Subscription
        userSubscriptionType = v.findViewById<TextView>(R.id.subscription_type) //View
        userSubscriptionDeadline = v.findViewById<TextView>(R.id.subscription_deadline)
        userSubscriptionStatus = v.findViewById<TextView>(R.id.subscription_status)
        userSubscriptionStatusIconActive = v.findViewById(R.id.subscription_active)
        userSubscriptionStatusIconExpired = v.findViewById(R.id.subscription_expired)
        userEntries = v.findViewById<TextView>(R.id.remaining_entries)

        getSubscriptionName()
        getUserSubscriptionInfo()


        //Email
        var email = v.findViewById<TextView>(R.id.user_emailaddress) //view
        //Age
        val age_text = v.findViewById<TextView>(R.id.age_text) //view
        val age_view = v.findViewById<TextView>(R.id.user_age_view) //view
        val age_set = v.findViewById<EditText>(R.id.user_age_edit) //set
        //Weight
        val weight_text = v.findViewById<TextView>(R.id.weight_text) //view
        val weight_view = v.findViewById<TextView>(R.id.user_weight_view) //view
        val weight_set = v.findViewById<EditText>(R.id.user_weight_edit)
        //Tall
        val tall_text = v.findViewById<TextView>(R.id.height_text) //view
        var tall_view = v.findViewById<TextView>(R.id.user_tall_view) //view
        val tall_edit = v.findViewById<EditText>(R.id.user_tall_edit) //edit
        //unit misure
        var um_1 =  v.findViewById<TextView>(R.id.um_1)
        var um_2 =  v.findViewById<TextView>(R.id.um_2)
        //bmi
        bmi = v.findViewById<TextView>(R.id.bmi_text) //bmi value
        val bmi_text = v.findViewById<TextView>(R.id.bmi) //bmi text
        result = v.findViewById<TextView>(R.id.result)
        //Edit/Save buttons
        val editbtn_1 = v.findViewById<Button>(R.id.button_infouser)
        val editbtn_1_save = v.findViewById<Button>(R.id.button_infouser_save)
        val editbtn_2 = v.findViewById<Button>(R.id.button_physics)
        val editbtn_2_save = v.findViewById<Button>(R.id.button_physics_save)

        /* ASSIGN DEFAULT VALUE */
        bmi.text = calculateBMI(weight_view.text.toString(), tall_view.text.toString())
        user.text = arguments?.getString("username")
        email.text = arguments?.getString("email")
        um_1.text = arguments?.getString("um1")
        um_2.text = arguments?.getString("um2")
        println(arguments?.getString("email"))

        /* ASSIGN DEFAULT COLOR */
        //rLayout.setBackgroundResource(arguments!!.getInt("color"))
        bmi_text.setTextColor(getResources().getColor(arguments!!.getInt("color")))
        editbtn_1.setTextColor(getResources().getColor(arguments!!.getInt("color")))
        editbtn_1_save.setTextColor(getResources().getColor(arguments!!.getInt("color")))
        editbtn_2.setTextColor(getResources().getColor(arguments!!.getInt("color")))
        editbtn_2_save.setTextColor(getResources().getColor(arguments!!.getInt("color")))

        /* ASSIGN DEFAULT STRING */
        user_title.setText(getResources().getString(arguments!!.getInt("string_user_1")))
        physycall_title.setText(getResources().getString(arguments!!.getInt("string_user_2")))
        age_text.setText(getResources().getString(arguments!!.getInt("string_user_3")))
        weight_text.setText(getResources().getString(arguments!!.getInt("string_user_4")))
        tall_text.setText(getResources().getString(arguments!!.getInt("string_user_5")))


        /* LISTENERS */

        imageProfile = v.findViewById(R.id.image_profile)
        imageProfile.setImageBitmap(decodeBase64(sessionManager.fetchImage()))
        changeProfileImage = v.findViewById(R.id.change_photo)

        changeProfileImage.setOnClickListener(View.OnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)*/
                .start()
        })



        //Telephone edit text listener
        telephone_edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                telephone_view.text = telephone_edit.text
            }
        })


        //Age edit text listener
        age_set.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                age_view.text = age_set.text
            }
        })

        //Weight edit text listener
        weight_set.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length > 0 && tall_view.text.length > 0) {
                    bmi.text = calculateBMI(s.toString(), tall_view.text.toString())
                } else {
                    bmi.text = "-.--"
                }
                weight_view.text = weight_set.text
            }
        })

        //Tall edit text listener
        tall_edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length > 0 && weight_view.text.length > 0) {
                    bmi.text = calculateBMI(weight_view.text.toString(), s.toString())
                } else {
                    bmi.text = "-.--"
                }
                tall_view.text = tall_edit.text
            }
        })

        //Edit/save button listener
        //Edit1
        editbtn_1.setOnClickListener {
            editbtn_1.visibility = View.GONE
            editbtn_1_save.visibility = View.VISIBLE
            telephone_view.visibility = View.GONE
            telephone_edit.visibility = View.VISIBLE
        }
        //Save1
        editbtn_1_save.setOnClickListener {
            editbtn_1.visibility = View.VISIBLE
            editbtn_1_save.visibility = View.GONE
            telephone_view.visibility = View.VISIBLE
            telephone_edit.visibility = View.GONE
        }
        //Edit2
        editbtn_2.setOnClickListener {
            editbtn_2.visibility = View.GONE
            editbtn_2_save.visibility = View.VISIBLE
            age_view.visibility = View.GONE
            age_set.visibility = View.VISIBLE
            weight_view.visibility = View.GONE
            weight_set.visibility = View.VISIBLE
            tall_view.visibility = View.GONE
            tall_edit.visibility = View.VISIBLE
        }
        //Save2
        editbtn_2_save.setOnClickListener {
            editbtn_2.visibility = View.VISIBLE
            editbtn_2_save.visibility = View.GONE
            age_view.visibility = View.VISIBLE
            age_set.visibility = View.GONE
            weight_view.visibility = View.VISIBLE
            weight_set.visibility = View.GONE
            tall_view.visibility = View.VISIBLE
            tall_edit.visibility = View.GONE
        }


        return v
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK){
            val dataImage: Uri = data?.data!!
            uploadProfileImage(dataImage)
        }
    }

    private fun uploadProfileImage(dataImage: Uri) {
        val imageFile = File(dataImage.path)
        val reqBody: RequestBody =
            imageFile.asRequestBody("multipart/form-file".toMediaTypeOrNull())
        val partImage: MultipartBody.Part = MultipartBody.Part.createFormData("img", imageFile.name, reqBody)
        apiClient = ApiClient()
        sessionManager = SessionManager(ApplicationContextProvider.getContext())
        activity?.let {
            context?.let { it1 ->
                sessionManager.fetchUserId()?.let { it2 ->
                    apiClient.getApiServiceAuth(it1).uploadImageProfile(partImage, it2)
                        .enqueue(object : Callback<ApiResponse> {
                            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(ApplicationContextProvider.getContext(), response.body()?.message, Toast.LENGTH_LONG).show()
                                    imageProfile.setImageURI(dataImage)
                                    communicator.changeProfileImageNavHeader(dataImage)
                                }
                            }

                            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                                Toast.makeText(ApplicationContextProvider.getContext(), "ERRORE DI CARICAMENTO!", Toast.LENGTH_LONG).show()
                            }
                        })
                }
            }
        }
    }


    /*
    Function used for calculate the BMI
     */
    private fun calculateBMI(weight: String, tall: String): String {
        var bmi = 0.0
        if (weight.length > 1 && tall.length > 1) {
            val numerator = weight.toDouble()
            val denominator = (tall.toDouble() / 100) * (tall.toDouble() / 100)
            bmi = (numerator / denominator)
            if (bmi < 18.5){
                result.text = "SOTTOPESO"
                result.setTextColor(Color.parseColor("#2E2EFF"))
                this.bmi.setTextColor(Color.parseColor("#2E2EFF"))
            }else if (bmi in 18.5..24.9) {
                result.text = "PESO NORMALE"
                result.setTextColor(Color.parseColor("#00D100"))
                this.bmi.setTextColor(Color.parseColor("#00D100"))
            }else if (bmi in 25.0..29.9) {
                result.text = "SOVRAPPESO"
                result.setTextColor(Color.parseColor("#FF7518"))
                this.bmi.setTextColor(Color.parseColor("#FF7518"))
            }else if (bmi in 30.0..34.9) {
                result.text = "OBESO!"
                result.setTextColor(Color.parseColor("#FF0000"))
                this.bmi.setTextColor(Color.parseColor("#FF0000"))
            }else if (bmi > 35) {
                result.text = "ESTREMAMENTE OBESO!"
                result.setTextColor(Color.parseColor("#800000"))
                this.bmi.setTextColor(Color.parseColor("#800000"))
            }else if (bmi < 18.5) {
                result.text = "PESO E ALTEZZA NON VALIDI!"
            }
        }
        return bmi.toString()
    }

    private fun setPhoneNumber() {

    }


    // method for base64 to bitmap
    private fun decodeBase64(input: String?): Bitmap? {
        val decodedByte: ByteArray = Base64.decode(input, 0)
        return BitmapFactory
            .decodeByteArray(decodedByte, 0, decodedByte.size)
    }

    private fun getSubscriptionName() {
        apiClient = ApiClient()
        sessionManager = SessionManager(ApplicationContextProvider.getContext())
        activity?.let {
            sessionManager.fetchIdAbbonamento()?.let { it1 ->
                apiClient.getApiServiceGateway(it).getSubfromid(it1)
                    .enqueue(object : Callback<SubscriptionResponse> {

                        override fun onResponse(
                            call: Call<SubscriptionResponse>,
                            response: Response<SubscriptionResponse>
                        ) {
                            userSubscriptionType!!.text = userSubscriptionType!!.text.toString() + response.body()!!.name
                        }
                        override fun onFailure(call: Call<SubscriptionResponse>, t: Throwable) {

                        }
                    })
            }
        }
    }

    private fun getUserSubscriptionInfo() {
        apiClient = ApiClient()
        sessionManager = SessionManager(ApplicationContextProvider.getContext())
        println(sessionManager.fetchUserId())
        context?.let {
            sessionManager.fetchUserId()?.let { it1 ->
                apiClient.getApiServiceAuth(it).getMyUserInfo(it1)
                    .enqueue(object : Callback<User>{
                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            println(response.body()!!.scadenzaAbbonamento)
                            userSubscriptionDeadline!!.text =  userSubscriptionDeadline!!.text.toString() + response.body()!!.scadenzaAbbonamento
                            userEntries!!.text = userEntries!!.text.toString() + response.body()!!.ingressi.toString()
                            view?.findViewById<CircularProgressBar>(R.id.circularProgressBar)?.progress = response.body()!!.ingressi.toString().toFloat()
                            if (!response.body()!!.expired) {
                                userSubscriptionStatus!!.text = "Attivo"
                                userSubscriptionStatus!!.setTextColor(Color.parseColor("#00FF00"))
                                userSubscriptionStatusIconExpired?.visibility = View.GONE
                                userSubscriptionStatusIconActive?.visibility = View.VISIBLE
                            }
                        }

                        override fun onFailure(call: Call<User>, t: Throwable) {

                        }

                    })
            }
        }
    }


}