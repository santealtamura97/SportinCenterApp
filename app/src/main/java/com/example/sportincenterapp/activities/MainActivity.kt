package com.example.sportincenterapp.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.sportincenterapp.R
import com.example.sportincenterapp.data.ApiClient
import com.example.sportincenterapp.fragments.*
import com.example.sportincenterapp.interfaces.Communicator
import com.example.sportincenterapp.utils.ApplicationContextProvider
import com.example.sportincenterapp.utils.SessionManager
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, Communicator {

    // DECLARATIONS
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var sessionManager: SessionManager

    //User information
    private lateinit var userName: TextView
    private lateinit var userEmail: TextView
    private lateinit var profileImage: CircleImageView


    //Fragments
    private val fragmentUser : Fragment = UserPage()
    private val fragmentSettings : Fragment = Settings()
    private val fragmentHome : Fragment = HomeFragment()
    private val fragmentAdvertisement : Fragment = Advertisment()
    private val fragmentFaq : Fragment = Faq()
    private val fragmentContacts : Fragment = Contacts()
    private val fragmentCalendarAdmin: Fragment = CalendarAdminFragment()
    private val fragmentAddActivity: Fragment = AddActivityFragment()
    private val fragmentEventParticipants: Fragment = EventPartecipantsFragment()
    //Bundles
    private val bundleUser : Bundle = Bundle()
    private val bundleHome : Bundle = Bundle()
    private val bundleAdvertisement: Bundle = Bundle()
    private val bundleFaq: Bundle = Bundle()
    private val bundleContacts: Bundle = Bundle()
    private val bundleEventParticipants: Bundle = Bundle()

    //API
    private lateinit var apiClient: ApiClient

    /* ON CREATE CLASS */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Session manager
        sessionManager = SessionManager(ApplicationContextProvider.getContext())
        //Main activity
        setContentView(R.layout.activity_main)
        //Drawer
        drawerLayout = findViewById(R.id.nav_view)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        val header = navigationView.getHeaderView(0)

        profileImage = header.findViewById<CircleImageView>(R.id.profileimage_container)
        userName = header.findViewById<TextView>(R.id.nome_utente_nav_header)
        userEmail = header.findViewById<TextView>(R.id.email_nav_header)

        //Login button
        val loginButton = header.findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener {
            intent = Intent(ApplicationContextProvider.getContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        //Elements of the menu
        val menu = navigationView.menu
        val userPageItem = menu.getItem(0)
        val newsItem = menu.getItem(1)
        val calendarItem = menu.getItem(2)
        val calendarAdminItem = menu.getItem(3)
        val faqItem = menu.getItem(5).subMenu.getItem(1)
        val contactsItem = menu.getItem(5).subMenu.getItem(2)
        val logoutItem = menu.getItem(5).subMenu.getItem(3)

        if (!sessionManager.fetchUserName().isNullOrEmpty()) {
            userName.text = sessionManager.fetchUserName()
            userEmail.text = sessionManager.fetchUserEmail()
            userName.visibility = View.VISIBLE
            loginButton.visibility = View.GONE
            getProfileImage()//get from API profile image

            if (sessionManager.fetchUserName() == "Admin" ) { //Admin section
                userPageItem.isVisible = false
                newsItem.isVisible = false
                calendarItem.isVisible = false
                faqItem.isVisible = false
                contactsItem.isVisible = false
                calendarAdminItem.isVisible = true
            }

        } else { //if we don't have done the logout
            userPageItem.isVisible = false
            logoutItem.isVisible = false
            calendarItem.isVisible = false
        }

        navigationView.setNavigationItemSelectedListener(this)

        //INITIALIZATIONS
        supportFragmentManager.beginTransaction().replace(R.id.Fragment_container, fragmentHome, "HomeFragment")
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_draw_open, R.string.navigation_draw_close)

        //Home button management (on the toolbar)
        val homeButton : ImageButton = findViewById(R.id.home_button)
        homeButton.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.Fragment_container, fragmentHome, "HomeFragment").commit()
        }
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.Fragment_container, fragmentHome, "HomeFragment").commit()
            navigationView.setCheckedItem(R.id.home)
        }
        initializeFragments()
    }

    /* BACK BUTTON PRESSED MANAGEMENT */
    override fun onBackPressed() {
        //if the current fragment is the add activity return to the calendar admin fragment
        //else back to home fragment
        val addActivityFragment = supportFragmentManager.findFragmentByTag("AddActivityFragment")

        if (addActivityFragment != null && addActivityFragment.isVisible) {
            supportFragmentManager.beginTransaction().replace(R.id.Fragment_container, fragmentCalendarAdmin).commit()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.Fragment_container, fragmentHome).commit()
        }
    }

    /* NAVIGATION TAB MANAGEMENT */
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        //When i press the correspondent id we load the fragment
        when (menuItem.itemId) {
            R.id.calendar -> supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_container, CalendarCollectionFragment(), "CalendarFragment").commit() //Calendar fragment
            R.id.admin_calendar -> supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_container, CalendarAdminFragment(), "AdminCalendarFragment").commit()

            R.id.profile -> supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_container, fragmentUser, "UserFragment").commit()

            R.id.news -> supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_container, fragmentAdvertisement, "NewsFragment").commit()

            R.id.settings -> supportFragmentManager.beginTransaction()
                    .replace(R.id.Fragment_container, fragmentSettings, "SettingsFragment").commit()

            R.id.help -> supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_container, fragmentFaq, "FaqFragment").commit()

            R.id.contacts -> supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_container, fragmentContacts, "ContactsFragment").commit()

            R.id.logout -> logout()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    /* LOGOUT */
    private fun logout() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.logout_confirm).setMessage(R.string.logout_message)

        builder.setPositiveButton(R.string.logout_yes) {
            dialog, _ -> sessionManager.logout()
            finish()
            startActivity(intent)
            dialog.dismiss()
        }

        builder.setNegativeButton(R.string.logout_no) {
                dialog, _ -> dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }

    private fun getProfileImage() {
        //var bitmap: Bitmap? = null
        apiClient = ApiClient()
        sessionManager = SessionManager(ApplicationContextProvider.getContext())
        sessionManager.fetchUserId()?.let {
            apiClient.getApiServiceAuth(this).getImageProfile(it)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            val body = response.body()?.byteStream()
                            val bitmap = BitmapFactory.decodeStream(body)
                            profileImage.setImageBitmap(bitmap)
                            if (bitmap != null) {
                                sessionManager.saveImage(encodeTobase64(bitmap))
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(ApplicationContextProvider.getContext(), "ERRORE DI CARICAMENTO!", Toast.LENGTH_LONG).show()
                    }
                })
        }
    }

    // method for bitmap to base64
    private fun encodeTobase64(image: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        val imageEncoded: String = Base64.encodeToString(b, Base64.DEFAULT)
        return imageEncoded
    }


    /* INITIALIZE THE FRAGMENTS */
    private fun initializeFragments() {
        //Strings
        bundleUser.putString("username", userName.text.toString())
        bundleUser.putString("email", userEmail.text.toString())
        bundleUser.putString("um1", "Kg")
        bundleUser.putString("um2", "Cm")
        //Color
        bundleUser.putInt("color", R.color.orange)
        bundleHome.putInt("color", R.color.orange)
        bundleAdvertisement.putInt("color", R.color.orange)
        bundleFaq.putInt("color", R.color.orange)
        bundleContacts.putInt("color", R.color.orange)
        //Language
        bundleHome.putInt("string_home_1", R.string.first_title_home)
        bundleHome.putInt("string_home_2", R.string.first_text_home)
        bundleHome.putInt("string_home_3", R.string.second_title_home)
        bundleHome.putInt("string_home_4", R.string.second_text_home)
        bundleAdvertisement.putInt("string_title", R.string.advertisment_title)
        bundleAdvertisement.putInt("string_girl", R.string.advertisment_girlpower)
        bundleAdvertisement.putInt("string_easter", R.string.advertisment_easter)
        bundleAdvertisement.putInt("string_anniversary", R.string.advertisment_anniversary)
        bundleAdvertisement.putInt("string_halloween", R.string.advertisment_halloween)
        bundleAdvertisement.putInt("string_newyear", R.string.advertisment_newyear)
        bundleFaq.putInt("string_faq_1", R.string.faq_question_1)
        bundleFaq.putInt("string_faq_1a", R.string.faq_respose_1)
        bundleFaq.putInt("string_faq_2", R.string.faq_question_2)
        bundleFaq.putInt("string_faq_2a", R.string.faq_respose_2)
        bundleFaq.putInt("string_faq_3", R.string.faq_question_3)
        bundleFaq.putInt("string_faq_3a", R.string.faq_respose_3)
        bundleUser.putInt("string_user_1", R.string.user_information_title)
        bundleUser.putInt("string_user_2", R.string.user_physics_title)
        bundleUser.putInt("string_user_3", R.string.age)
        bundleUser.putInt("string_user_4", R.string.weight)
        bundleUser.putInt("string_user_5", R.string.height)
        bundleContacts.putInt("string_contact_1", R.string.contacts_address)
        bundleContacts.putInt("string_contact_2", R.string.contacts_telephone)
        bundleContacts.putInt("string_contact_3", R.string.contacts_hours)
        bundleContacts.putInt("string_contact_4", R.string.contacts_email)
        bundleContacts.putInt("string_contact_5", R.string.contacts_title)
        bundleContacts.putInt("string_contact_6", R.string.contacts_hours_string)
        bundleContacts.putInt("string_contact_7", R.string.contacts_hours_string_2)
        bundleContacts.putInt("string_contact_8", R.string.contacts_address_string)

        //Assignments
        fragmentUser.arguments = bundleUser
        fragmentHome.arguments = bundleHome
        fragmentAdvertisement.arguments = bundleAdvertisement
        fragmentFaq.arguments = bundleFaq
        fragmentContacts.arguments = bundleContacts
    }

    override fun um_update(um_1: String, um_2: String) {
        bundleUser.putString("um1", um_1)
        bundleUser.putString("um2", um_2)
        fragmentUser.arguments = bundleUser
    }

    override fun theme(index : Int) {
        val homeBtn = findViewById<ImageButton>(R.id.home_button)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        val header = navigationView.getHeaderView(0)
        val tools: Toolbar = findViewById(R.id.toolbar)

        when (index) {
            0 -> {
                header.setBackgroundResource(R.color.orange)
                tools.setBackgroundResource(R.color.orange)
                homeBtn.setBackgroundResource(R.color.orange)
                bundleUser.putInt("color", R.color.orange)
                bundleHome.putInt("color", R.color.orange)
                bundleAdvertisement.putInt("color", R.color.orange)
                bundleFaq.putInt("color", R.color.orange)
                bundleContacts.putInt("color", R.color.orange)
    
            }
            1 -> {
                header.setBackgroundResource(R.color.orange)
                tools.setBackgroundResource(R.color.orange)
                homeBtn.setBackgroundResource(R.color.orange)
                bundleUser.putInt("color", R.color.orange)
                bundleHome.putInt("color", R.color.orange)
                bundleAdvertisement.putInt("color", R.color.orange)
                bundleFaq.putInt("color", R.color.orange)
                bundleContacts.putInt("color", R.color.orange)
    
            }
        }
    }

    override fun language(index: Int) {
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        val menu = navigationView.menu

        if (index == 0) {
            bundleHome.putInt("string_home_1", R.string.first_title_home)

            bundleHome.putInt("string_home_2", R.string.first_text_home)
            bundleHome.putInt("string_home_3", R.string.second_title_home)
            bundleHome.putInt("string_home_4", R.string.second_text_home)
            bundleAdvertisement.putInt("string_title", R.string.advertisment_title)
            bundleAdvertisement.putInt("string_girl", R.string.advertisment_girlpower)
            bundleAdvertisement.putInt("string_easter", R.string.advertisment_easter)
            bundleAdvertisement.putInt("string_anniversary", R.string.advertisment_anniversary)
            bundleAdvertisement.putInt("string_halloween", R.string.advertisment_halloween)
            bundleAdvertisement.putInt("string_newyear", R.string.advertisment_newyear)
            bundleFaq.putInt("string_faq_1", R.string.faq_question_1)
            bundleFaq.putInt("string_faq_1a", R.string.faq_respose_1)
            bundleFaq.putInt("string_faq_2", R.string.faq_question_2)
            bundleFaq.putInt("string_faq_2a", R.string.faq_respose_2)
            bundleFaq.putInt("string_faq_3", R.string.faq_question_3)
            bundleFaq.putInt("string_faq_3a", R.string.faq_respose_3)
            bundleContacts.putInt("string_contact_1", R.string.contacts_address)
            bundleContacts.putInt("string_contact_2", R.string.contacts_telephone)
            bundleContacts.putInt("string_contact_3", R.string.contacts_hours)
            bundleContacts.putInt("string_contact_4", R.string.contacts_email)
            bundleContacts.putInt("string_contact_5", R.string.contacts_title)
            bundleContacts.putInt("string_contact_6", R.string.contacts_hours_string)
            bundleContacts.putInt("string_contact_7", R.string.contacts_hours_string_2)
            bundleContacts.putInt("string_contact_8", R.string.contacts_address_string)
            menu.getItem(0).setTitle(getResources().getString(R.string.profile))
            menu.getItem(1).setTitle(getResources().getString(R.string.advertisment_title))
            menu.getItem(2).setTitle(getResources().getString(R.string.calendar))
            //menu.getItem(3).setTitle(getResources().getString(R.string.activities))
            menu.getItem(5).subMenu.getItem(0).setTitle(getResources().getString(R.string.settings))
            menu.getItem(5).subMenu.getItem(1).setTitle(getResources().getString(R.string.FAQ))
            menu.getItem(5).subMenu.getItem(2).setTitle(getResources().getString(R.string.contacts))
        } else if (index == 1) {
            bundleHome.putInt("string_home_1", R.string.first_title_home_en)
            bundleHome.putInt("string_home_2", R.string.first_text_home_en)
            bundleHome.putInt("string_home_3", R.string.second_title_home_en)
            bundleHome.putInt("string_home_4", R.string.second_text_home_en)
            bundleAdvertisement.putInt("string_title", R.string.advertisment_title_en)
            bundleAdvertisement.putInt("string_girl", R.string.advertisment_girlpower_en)
            bundleAdvertisement.putInt("string_easter", R.string.advertisment_easter_en)
            bundleAdvertisement.putInt("string_anniversary", R.string.advertisment_anniversary_en)
            bundleAdvertisement.putInt("string_halloween", R.string.advertisment_halloween_en)
            bundleAdvertisement.putInt("string_newyear", R.string.advertisment_newyear_en)
            bundleFaq.putInt("string_faq_1", R.string.faq_question_1_en)
            bundleFaq.putInt("string_faq_1a", R.string.faq_respose_1_en)
            bundleFaq.putInt("string_faq_2", R.string.faq_question_2_en)
            bundleFaq.putInt("string_faq_2a", R.string.faq_respose_2_en)
            bundleFaq.putInt("string_faq_3", R.string.faq_question_3_en)
            bundleFaq.putInt("string_faq_3a", R.string.faq_respose_3_en)
            bundleUser.putInt("string_user_1", R.string.user_information_title_en)
            bundleUser.putInt("string_user_2", R.string.user_physics_title_en)
            bundleUser.putInt("string_user_3", R.string.age_en)
            bundleUser.putInt("string_user_4", R.string.weight_en)
            bundleUser.putInt("string_user_5", R.string.height_en)
            bundleContacts.putInt("string_contact_1", R.string.contacts_address_en)
            bundleContacts.putInt("string_contact_2", R.string.contacts_telephone_en)
            bundleContacts.putInt("string_contact_3", R.string.contacts_hours_en)
            bundleContacts.putInt("string_contact_4", R.string.contacts_email)
            bundleContacts.putInt("string_contact_5", R.string.contacts_title_en)
            bundleContacts.putInt("string_contact_6", R.string.contacts_hours_string_en)
            bundleContacts.putInt("string_contact_7", R.string.contacts_hours_string_2_en)
            bundleContacts.putInt("string_contact_8", R.string.contacts_address_string_en)

            menu.getItem(0).setTitle(getResources().getString(R.string.profile_en))
            menu.getItem(1).setTitle(getResources().getString(R.string.advertisment_title_en))
            menu.getItem(2).setTitle(getResources().getString(R.string.calendar_en))
            //menu.getItem(3).setTitle(getResources().getString(R.string.activities_en))
            menu.getItem(5).subMenu.getItem(0).setTitle(getResources().getString(R.string.settings_en))
            menu.getItem(5).subMenu.getItem(1).setTitle(getResources().getString(R.string.FAQ_en))
            menu.getItem(5).subMenu.getItem(2).setTitle(getResources().getString(R.string.contacts_en))
            menu.getItem(5).subMenu.getItem(3).setTitle(getResources().getString(R.string.logout_en))
        }
    }

    override fun openAddActivity() {
        supportFragmentManager.beginTransaction().replace(R.id.Fragment_container, fragmentAddActivity, "AddActivityFragment").commit()
    }

    override fun closeAddActivity() {
        supportFragmentManager.beginTransaction().replace(R.id.Fragment_container, fragmentCalendarAdmin, "AddActivityFragment").commit()
    }

    override fun openPartecipantsForEvent(eventId: String) {
        bundleEventParticipants.putString("eventId", eventId)
        fragmentEventParticipants.arguments = bundleEventParticipants
        supportFragmentManager.beginTransaction().replace(R.id.Fragment_container, fragmentEventParticipants, "EventPartecipantsFragment").commit()
    }

    override fun changeProfileImageNavHeader(dataImage: Uri) {
        profileImage.setImageURI(dataImage)
    }
}