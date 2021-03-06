package com.example.sportincenterapp.data

import com.example.sportincenterapp.data.models.Activity
import com.example.sportincenterapp.data.models.Event
import com.example.sportincenterapp.data.models.User
import com.example.sportincenterapp.data.requests.LoginRequest
import com.example.sportincenterapp.data.requests.SignUpRequest
import com.example.sportincenterapp.data.responses.*
import com.example.sportincenterapp.fragments.AddActivityFragment
import com.example.sportincenterapp.utils.Constant
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*

/**
 * Interface for defining REST request functions
 */
interface ApiService {

    @POST(Constant.LOGIN_URL)
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST(Constant.SIGN_UP_URL)
    fun signUp(@Body request: SignUpRequest) : Call<SignUpResponse>

    @POST(Constant.VALIDATE_USER_CODE_URL)
    fun validateUserCode(@Body request: String) : Call<UserCodeResponse>

    @GET(Constant.USER_INFO_URL + "/{userId}")
    fun getMyUserInfo(@Path(value = "userId") userId: String) : Call<User>

    @GET(Constant.CALENDAR_SERVICE + "/all/getusers/{eventId}")
    fun getUsersForEvent(@Path(value = "eventId") eventId: Long) : Call<List<User>>

    @GET(Constant.SUBSCRIPTION_SERVICE + "/user/getSubfromid/{idAbbonamento}")
    fun getSubfromid(@Path(value = "idAbbonamento") idAbbonamento: String) : Call<SubscriptionResponse>

    @GET(Constant.ACTIVITY_SERVICE + "/all/activities")
    fun getSportActivities(): Call <List<Activity>>

    @PUT(Constant.CALENDAR_SERVICE + "/user/user_date_events/{subId}/{userId}/{date}")
    fun getEventsForUserInDate(@Path(value = "subId") subId: String, @Path(value = "userId") userId : String, @Path(value = "date") date: String): Call <List<Event>>

    @PUT(Constant.CALENDAR_SERVICE + "/user/book_event/{userId}/{eventId}")
    fun bookEvent(@Path(value = "userId") userId: String, @Path(value = "eventId") eventId: String) : Call<Event>

    @POST(Constant.CALENDAR_SERVICE + "/admin/add_events")
    fun addEvents(@Body eventList: List<AddActivityFragment.EventObject>) : Call<ResponseBody>

    @GET(Constant.CALENDAR_SERVICE + "/user/user_bookings/{userId}")
    fun getBookingsForUser(@Path(value = "userId") userId: String) : Call<List<Event>>

    @PUT(Constant.CALENDAR_SERVICE + "/user/delete_booking/{userId}/{eventId}")
    fun deleteBooking(@Path(value = "userId") userId: String, @Path(value = "eventId") eventId: String) : Call<ResponseBody>

    @GET(Constant.ACTIVITY_SERVICE + "/all/get_activity/{activityId}")
    fun getActivityFromId(@Path(value = "activityId") activityId: String) : Call<Activity>

    @GET("/all/get_activity/{activityId}")
    fun getActivityFromIdNoAuth(@Path(value = "activityId") activityId: String) : Call<Activity>

    @GET("/all/date_events/{date}")
    fun getEventsInDate(@Path(value = "date") date: String): Call <List<Event>>

    @POST(Constant.CALENDAR_SERVICE + "/admin/date_events")
    fun getEventsInDateForAdmin(@Body date: String) : Call<List<Event>>

    @POST(Constant.CALENDAR_SERVICE + "/admin/delete_events")
    fun deleteEvents(@Body events: List<Event>) : Call<ResponseBody>

    @Multipart
    @POST(Constant.UPLOAD_PROFILE_IMAGE_URL + "/{userId}")
    fun uploadImageProfile(@Part img: MultipartBody.Part, @Path(value = "userId") userId: String): Call<ApiResponse>

    @GET(Constant.GET_PROFILE_IMAGE_URL + "/{userId}")
    fun getImageProfile(@Path(value = "userId") userId: String): Call<ResponseBody>

    @POST(Constant.SET_ENTRIES)
    fun setEntries(@Body userIds: List<String>): Call<ApiResponse>

    @PUT(Constant.CALENDAR_SERVICE + "/admin/remove/{eventId}/bookings")
    fun removeBookings(@Body userIds: List<String>, @Path(value = "eventId") eventId: String): Call<ResponseBody>

    @POST(Constant.SET_PHONE_NUMBER_URL + "/{userId}")
    fun setPhoneNumber(@Body phoneNumber: String, @Path(value = "userId") userId: String) : Call<ResponseBody>


}