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
//import com.github.dhaval2404.imagepicker.ImagePicker
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

    private lateinit var userSubscriptionType: TextView
    private lateinit var userSubscriptionDeadline: TextView
    private lateinit var userSubscriptionStatus: TextView
    private lateinit var userSubscriptionStatusIconActive: ImageView
    private lateinit var userSubscriptionStatusIconExpired: ImageView
    private lateinit var userEntries: TextView
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

        //Communicator
        communicator = activity as Communicator

        var user_pageTitle = v.findViewById<TextView>(R.id.user_pageTitle)
        var user_firstSection = v.findViewById<LinearLayout>(R.id.user_firstProfileSection)
        var user_profileNameText = v.findViewById<TextView>(R.id.user_profileNameText)
        var user_informationSection = v.findViewById<LinearLayout>(R.id.user_informationSection)
        var user_informationSectionTitle = v.findViewById<TextView>(R.id.user_informationSectionText)
        var user_informationSectionButton = v.findViewById<Button>(R.id.user_informationSectionButton)
        var user_informationSectionButtonSave = v.findViewById<Button>(R.id.user_informationSectionButtonSave)
        var user_informationSectionTelephoneIcon = v.findViewById<ImageView>(R.id.user_informationSectionTelephoneIcon)
        var user_informationSectionTelephoneEdit = v.findViewById<EditText>(R.id.user_informationSectionTelephoneEdit)
        var user_informationSectionTelephoneText = v.findViewById<TextView>(R.id.user_informationSectionTelephoneText)
        var user_informationSectionEmailIcon = v.findViewById<ImageView>(R.id.user_informationSectionEmailIcon)
        var user_subscriptionSectionTipologyIcon = v.findViewById<ImageView>(R.id.user_subscriptionSectionTipologyIcon)
        var user_subscriptionSectionExpiredIcon = v.findViewById<ImageView>(R.id.user_subscriptionSectionExpiredIcon)
        var user_subscriptionSection = v.findViewById<LinearLayout>(R.id.user_subscriptionSection)
        var user_subscriptionSectionTitle = v.findViewById<TextView>(R.id.user_subscriptionSectionTitle)
        var user_informationSectionEmailText = v.findViewById<TextView>(R.id.user_informationSectionEmailText)
        var user_physichsSection = v.findViewById<LinearLayout>(R.id.user_physicsSection)
        var user_physicsSectionTitle = v.findViewById<TextView>(R.id.user_physicsSectionTitle)
        var user_physicsSectionAgeText = v.findViewById<TextView>(R.id.user_physicsSectionAgeText)
        var user_physicsSectionAgeEdit = v.findViewById<EditText>(R.id.user_physicsSectionAgeEdit)
        var user_physicsSectionAgeTextValue = v.findViewById<TextView>(R.id.user_physicsSectionAgeTextValue)
        var user_physicsSectionWeightText = v.findViewById<TextView>(R.id.user_physicsSectionWeightText)
        var user_physicsSectionWeightEdit = v.findViewById<EditText>(R.id.user_physicsSectionWeightEdit)
        var user_physicsSectionWeightTextValue = v.findViewById<TextView>(R.id.user_physicsSectionWeightTextValue)
        var user_physicsSectionHeightText = v.findViewById<TextView>(R.id.user_physicsSectionHeightText)
        var user_physicsSectionHeightEdit = v.findViewById<EditText>(R.id.user_physicsSectionHeightEdit)
        var user_physicsSectionHeightTextValue = v.findViewById<TextView>(R.id.user_physicsSectionHeightTextValue)
        var user_physicsSectionUM1 = v.findViewById<TextView>(R.id.user_physicsSectionUM1)
        var user_physicsSectionUM2 = v.findViewById<TextView>(R.id.user_physicsSectionUM2)
        var user_physicsSectionButton = v.findViewById<Button>(R.id.user_physicsSectionButton)
        var user_physicsSectionButtonSave = v.findViewById<Button>(R.id.user_physicsSectionButtonSave)
        var user_BMISection = v.findViewById<LinearLayout>(R.id.user_BMISection)
        var user_physicsSectionBMIValue= v.findViewById<TextView>(R.id.user_physicsSectionBMIValue)


        bmi = v.findViewById<TextView>(R.id.user_physicsSectionBMIValue)
        result = v.findViewById(R.id.result)

        // Subscription
        userSubscriptionType = v.findViewById<TextView>(R.id.user_subscriptionSectionTipologyText) //View
        userSubscriptionDeadline = v.findViewById<TextView>(R.id.user_subscriptionSectionExpiredText)
        userSubscriptionStatus = v.findViewById<TextView>(R.id.user_subscriptionSectionStatus)
        userSubscriptionStatusIconActive = v.findViewById(R.id.user_subscriptionSectionActive)
        userSubscriptionStatusIconExpired = v.findViewById(R.id.user_subscriptionSectionExpired)
        userEntries = v.findViewById<TextView>(R.id.user_subscriptionSectionCircularProgressText)

        /* User's functions */
        getSubscriptionName()
        getUserSubscriptionInfo()

        /* LISTENERS */

        //Telephone edit text listener
        user_informationSectionTelephoneEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                user_informationSectionTelephoneText.text = user_informationSectionTelephoneEdit.text
            }
        })

        imageProfile = v.findViewById(R.id.image_profile)
        if (!sessionManager.fetchImage().isNullOrEmpty()) {
            imageProfile.setImageBitmap(decodeBase64(sessionManager.fetchImage()))
        }
        changeProfileImage = v.findViewById(R.id.change_photo)

       /* changeProfileImage.setOnClickListener(View.OnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        })*/


        //Telephone edit text listener
        /*user_informationSectionEmailEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                user_informationSectionEmailText.text = user_informationSectionEmailEdit.text
            }
        })*/


        //Age edit text listener
        user_physicsSectionAgeEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                user_physicsSectionAgeTextValue.text = user_physicsSectionAgeEdit.text
            }
        })

        //Weight edit text listener
        user_physicsSectionWeightEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length > 0 && user_physicsSectionHeightTextValue.text.length > 0) {
                    bmi.text = calculateBMI(s.toString(), user_physicsSectionHeightTextValue.text.toString())
                } else {
                    bmi.text = "-.--"
                }
                user_physicsSectionWeightTextValue.text = user_physicsSectionWeightEdit.text
            }
        })

        //Tall edit text listener
        user_physicsSectionHeightEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length > 0 && user_physicsSectionWeightTextValue.text.length > 0) {
                    bmi.text = calculateBMI(user_physicsSectionWeightTextValue.text.toString(), s.toString())
                } else {
                    bmi.text = "-.--"
                }
                user_physicsSectionHeightTextValue.text = user_physicsSectionHeightEdit.text
            }
        })

        //Edit/save button listener
        //Edit1
        user_informationSectionButton.setOnClickListener {
            user_informationSectionButton.visibility = View.GONE
            user_informationSectionButtonSave.visibility = View.VISIBLE
            user_informationSectionTelephoneText.visibility = View.GONE
            user_informationSectionTelephoneEdit.visibility = View.VISIBLE
            //user_informationSectionEmailText.visibility = View.GONE
            //user_informationSectionEmailEdit.visibility = View.VISIBLE
        }

        //Save1
        user_informationSectionButtonSave.setOnClickListener {
            user_informationSectionButton.visibility = View.VISIBLE
            user_informationSectionButtonSave.visibility = View.GONE
            user_informationSectionTelephoneText.visibility = View.VISIBLE
            user_informationSectionTelephoneEdit.visibility = View.GONE
            //user_informationSectionEmailText.visibility = View.VISIBLE
            //user_informationSectionEmailEdit.visibility = View.GONE
        }

        //Edit2
        user_physicsSectionButton.setOnClickListener {
            user_physicsSectionButton.visibility = View.GONE
            user_physicsSectionButtonSave.visibility = View.VISIBLE
            user_physicsSectionAgeTextValue.visibility = View.GONE
            user_physicsSectionAgeEdit.visibility = View.VISIBLE
            user_physicsSectionWeightTextValue.visibility = View.GONE
            user_physicsSectionWeightEdit.visibility = View.VISIBLE
            user_physicsSectionHeightTextValue.visibility = View.GONE
            user_physicsSectionHeightEdit.visibility = View.VISIBLE
        }

        //Save2
        user_physicsSectionButtonSave.setOnClickListener {
            user_physicsSectionButton.visibility = View.VISIBLE
            user_physicsSectionButtonSave.visibility = View.GONE
            user_physicsSectionAgeTextValue.visibility = View.VISIBLE
            user_physicsSectionAgeEdit.visibility = View.GONE
            user_physicsSectionWeightTextValue.visibility = View.VISIBLE
            user_physicsSectionWeightEdit.visibility = View.GONE
            user_physicsSectionHeightTextValue.visibility = View.VISIBLE
            user_physicsSectionHeightEdit.visibility = View.GONE
        }

        /* ASSIGN DEFAULT VALUE */
        bmi.text = calculateBMI(user_physicsSectionWeightTextValue.text.toString(), user_physicsSectionHeightTextValue.text.toString())
        user_profileNameText.text = arguments?.getString("username")
        user_informationSectionEmailText.text = arguments?.getString("email")
        user_physicsSectionUM1.text = arguments?.getString("um1")
        user_physicsSectionUM2.text = arguments?.getString("um2")

        /* ASSIGN DEFAULT COLOR */
        user_firstSection.setBackgroundResource(arguments!!.getInt("cl_user_Layoutbackground"))
        user_profileNameText.setTextColor(getResources().getColor(arguments!!.getInt("cl_user_text")))
        user_informationSection.setBackgroundResource(arguments!!.getInt("cl_user_Layoutbackground"))
        user_informationSectionTitle.setTextColor(getResources().getColor(arguments!!.getInt("cl_user_text")))
        user_informationSectionButton.setBackgroundResource(arguments!!.getInt("cl_user_background"))
        user_informationSectionButtonSave.setBackgroundResource(arguments!!.getInt("cl_user_background"))
        user_informationSectionTelephoneEdit.setTextColor(getResources().getColor(arguments!!.getInt("cl_user_text")))
        user_informationSectionTelephoneText.setTextColor(getResources().getColor(arguments!!.getInt("cl_user_text")))
        user_informationSectionTelephoneIcon.setBackgroundResource(arguments!!.getInt("cl_user_Layoutbackground"))
        user_informationSectionEmailIcon.setBackgroundResource(arguments!!.getInt("cl_user_Layoutbackground"))
        user_subscriptionSection.setBackgroundResource(arguments!!.getInt("cl_user_Layoutbackground"))
        userSubscriptionType.setTextColor(getResources().getColor(arguments!!.getInt("cl_user_text")))
        userSubscriptionDeadline.setTextColor(getResources().getColor(arguments!!.getInt("cl_user_text")))
        userSubscriptionStatusIconActive.setBackgroundResource(arguments!!.getInt("cl_user_Layoutbackground"))
        userSubscriptionStatusIconExpired.setBackgroundResource(arguments!!.getInt("cl_user_Layoutbackground"))
        userEntries.setTextColor(getResources().getColor(arguments!!.getInt("cl_user_text")))
        user_subscriptionSectionTitle.setTextColor(getResources().getColor(arguments!!.getInt("cl_user_text")))
        user_subscriptionSectionTipologyIcon.setBackgroundResource(arguments!!.getInt("cl_user_Layoutbackground"))
        user_subscriptionSectionExpiredIcon.setBackgroundResource(arguments!!.getInt("cl_user_Layoutbackground"))
        user_physichsSection.setBackgroundResource(arguments!!.getInt("cl_user_Layoutbackground"))
        user_physicsSectionTitle.setTextColor(getResources().getColor(arguments!!.getInt("cl_user_text")))
        user_physicsSectionAgeText.setTextColor(getResources().getColor(arguments!!.getInt("cl_user_text")))
        user_physicsSectionAgeTextValue.setTextColor(getResources().getColor(arguments!!.getInt("cl_user_text")))
        user_physicsSectionAgeEdit.setTextColor(getResources().getColor(arguments!!.getInt("cl_user_text")))
        user_physicsSectionWeightText.setTextColor(getResources().getColor(arguments!!.getInt("cl_user_text")))
        user_physicsSectionWeightTextValue.setTextColor(getResources().getColor(arguments!!.getInt("cl_user_text")))
        user_physicsSectionWeightEdit.setTextColor(getResources().getColor(arguments!!.getInt("cl_user_text")))
        user_physicsSectionHeightText.setTextColor(getResources().getColor(arguments!!.getInt("cl_user_text")))
        user_physicsSectionHeightTextValue.setTextColor(getResources().getColor(arguments!!.getInt("cl_user_text")))
        user_physicsSectionHeightEdit.setTextColor(getResources().getColor(arguments!!.getInt("cl_user_text")))
        user_physicsSectionUM1.setTextColor(getResources().getColor(arguments!!.getInt("cl_user_text")))
        user_physicsSectionUM2.setTextColor(getResources().getColor(arguments!!.getInt("cl_user_text")))
        user_physicsSectionButton.setBackgroundResource(arguments!!.getInt("cl_user_background"))
        user_physicsSectionButtonSave.setBackgroundResource(arguments!!.getInt("cl_user_background"))
        user_BMISection.setBackgroundResource(arguments!!.getInt("cl_user_background"))
        bmi.setTextColor(getResources().getColor(arguments!!.getInt("cl_user_text")))


        /* ASSIGN DEFAULT STRING */
        user_pageTitle.setText(getResources().getString(arguments!!.getInt("st_user_pageTitle")))
        user_informationSectionTitle.setText(getResources().getString(arguments!!.getInt("st_user_informationSectionTitle")))
        user_informationSectionButton.setText(getResources().getString(arguments!!.getInt("st_user_informationSectionButton")))
        user_informationSectionButtonSave.setText(getResources().getString(arguments!!.getInt("st_user_informationSectionButtonSave")))
        userSubscriptionType.setText(getResources().getString(arguments!!.getInt("st_user_subscriptionSectionTipology")))
        userSubscriptionDeadline.setText(getResources().getString(arguments!!.getInt("st_user_subscriptionSectionExpired")))
        user_subscriptionSectionTitle.setText(getResources().getString(arguments!!.getInt("st_user_subscriptionSectionTitle")))
        userEntries.setText(getResources().getString(arguments!!.getInt("st_user_subscriptionSectionCircularProgress")))
        user_physicsSectionTitle.setText(getResources().getString(arguments!!.getInt("st_user_physicsSectionTitle")))
        user_physicsSectionAgeText.setText(getResources().getString(arguments!!.getInt("st_user_physicsSectionAge")))
        user_physicsSectionWeightText.setText(getResources().getString(arguments!!.getInt("st_user_physicsSectionWeight")))
        user_physicsSectionHeightText.setText(getResources().getString(arguments!!.getInt("st_user_physicsSectionHeight")))
        user_physicsSectionButton.setText(getResources().getString(arguments!!.getInt("st_user_physicsSectionButton")))
        user_physicsSectionButtonSave.setText(getResources().getString(arguments!!.getInt("st_user_physicsSectionButtonSave")))

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
                                    communicator.changeProfileImageNavHeader()
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