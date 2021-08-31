package com.example.sportincenterapp.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
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
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.sportincenterapp.R
import com.example.sportincenterapp.data.ApiClient
import com.example.sportincenterapp.fragments.*
import com.example.sportincenterapp.interfaces.Communicator
import com.example.sportincenterapp.utils.ApplicationContextProvider
import com.example.sportincenterapp.utils.SessionManager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.resources.TextAppearance
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
    private val fragmentHome : Fragment = HomeFragment()
    private val fragmentUser : Fragment = UserPage()
    private val fragmentAdvertisement : Fragment = Advertisment()

    private val fragmentSettings : Fragment = Settings()
    private val fragmentFaq : Fragment = Faq()
    private val fragmentContacts : Fragment = Contacts()
    private val fragmentCalendarAdmin: Fragment = CalendarAdminFragment()
    private val fragmentCalendar: Fragment = CalendarCollectionFragment()
    private val fragmentAddActivity: Fragment = AddActivityFragment()
    private val fragmentEventParticipants: Fragment = EventPartecipantsFragment()
    //Bundles
    private val bundleHome : Bundle = Bundle()
    private val bundleUser : Bundle = Bundle()
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
        val settingsItem = menu.getItem(5).subMenu.getItem(0)
        val faqItem = menu.getItem(5).subMenu.getItem(1)
        val contactsItem = menu.getItem(5).subMenu.getItem(2)
        val logoutItem = menu.getItem(5).subMenu.getItem(3)
        navigationView.setItemIconTintList(null)

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
        if (sessionManager.fetchUserName().equals("Admin")) {
            /*
                GESTIONE DEI FRAGMENT ADMIN
             */
            val addActivityFragment = supportFragmentManager.findFragmentByTag("AddActivityFragment")

            if (addActivityFragment != null && addActivityFragment.isVisible) {
                supportFragmentManager.beginTransaction().replace(R.id.Fragment_container, fragmentCalendarAdmin).commit()
            }else{
                supportFragmentManager.beginTransaction().replace(R.id.Fragment_container, fragmentHome).commit()
            }
        }else{
            /*
                GESTIONE DEI FRAGMENT USER
             */

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
        bundleHome.putInt("cl_home_background", R.color.background_primary_color)
        bundleHome.putInt("cl_home_text", R.color.black)
        bundleUser.putInt("cl_user_background", R.color.background_primary_color)
        bundleUser.putInt("cl_user_Layoutbackground", R.drawable.circular_white_bordersolid)
        bundleUser.putInt("cl_user_text", R.color.black)
        bundleAdvertisement.putInt("cl_advertisment_background", R.color.background_primary_color)
        bundleFaq.putInt("cl_faq_background", R.color.background_primary_color)
        bundleFaq.putInt("cl_faq_text", R.color.black)
        bundleContacts.putInt("cl_contacts_background", R.color.background_primary_color)
        bundleContacts.putInt("cl_contacts_text", R.color.black)

        //Language
        bundleHome.putInt("st_home_firstQuestion", R.string.fr_home_firstQuestion_it)
        bundleHome.putInt("st_home_firstAnswer", R.string.fr_home_firstAnswer_it)
        bundleHome.putInt("st_home_secondQuestion", R.string.fr_home_secondQuestion_it)
        bundleHome.putInt("st_home_secondAnswer", R.string.fr_home_secondAnswer_it)
        bundleUser.putInt("st_user_pageTitle", R.string.fr_user_pageTitle_it)
        bundleUser.putInt("st_user_informationSectionTitle", R.string.fr_user_informationSectionTitle_it)
        bundleUser.putInt("st_user_informationSectionButton", R.string.fr_user_informationSectionButton_it)
        bundleUser.putInt("st_user_informationSectionButtonSave", R.string.fr_user_informationSectionButtonSave_it)
        bundleUser.putInt("st_user_subscriptionSectionTitle", R.string.fr_user_subscriptionSectionTitle_it)
        bundleUser.putInt("st_user_subscriptionSectionTipology", R.string.fr_user_subscriptionSectionTipologyText_it)
        bundleUser.putInt("st_user_subscriptionSectionExpired", R.string.fr_user_subscriptionSectionExpiredText_it)
        bundleUser.putInt("st_user_subscriptionSectionCircularProgress", R.string.fr_user_subscriptionSectionCircularProgressText_it)
        bundleUser.putInt("st_user_physicsSectionTitle", R.string.fr_user_physicalSectionTitle_it)
        bundleUser.putInt("st_user_physicsSectionAge", R.string.fr_user_physicalSectionAge_it)
        bundleUser.putInt("st_user_physicsSectionWeight", R.string.fr_user_physicalSectionWeight_it)
        bundleUser.putInt("st_user_physicsSectionHeight", R.string.fr_user_physicalSectionHeight_it)
        bundleUser.putInt("st_user_physicsSectionButtonSave", R.string.fr_user_informationSectionButtonSave_it)
        bundleUser.putInt("st_user_physicsSectionButton", R.string.fr_user_informationSectionButton_it)
        bundleAdvertisement.putInt("st_advertisment_title", R.string.fr_advertisment_title_it)
        bundleAdvertisement.putInt("st_advertisment_event1", R.string.fr_advertisment_event1_it)
        bundleAdvertisement.putInt("st_advertisment_event2", R.string.fr_advertisment_event2_it)
        bundleAdvertisement.putInt("st_advertisment_event3", R.string.fr_advertisment_event3_it)
        bundleAdvertisement.putInt("st_advertisment_event4", R.string.fr_advertisment_event4_it)
        bundleAdvertisement.putInt("st_advertisment_event5", R.string.fr_advertisment_event5_it)
        bundleFaq.putInt("st_faq_question1", R.string.fr_faq_question1_it)
        bundleFaq.putInt("st_faq_question2", R.string.fr_faq_question2_it)
        bundleFaq.putInt("st_faq_question3", R.string.fr_faq_question3_it)
        bundleFaq.putInt("st_faq_answer1", R.string.fr_faq_answer1_it)
        bundleFaq.putInt("st_faq_answer2", R.string.fr_faq_answer2_it)
        bundleFaq.putInt("st_faq_answer3", R.string.fr_faq_answer3_it)
        bundleContacts.putInt("st_contacts_title", R.string.fr_contacts_title_it)
        bundleContacts.putInt("st_contacts_timetableText", R.string.fr_contacts_timetable_it)
        bundleContacts.putInt("st_contacts_timetableValue1", R.string.fr_contacts_openingTimetable1_it)
        bundleContacts.putInt("st_contacts_timetableValue2", R.string.fr_contacts_openingTimetable2_it)
        bundleContacts.putInt("st_contacts_addressText", R.string.fr_contacts_addressText_it)
        bundleContacts.putInt("st_contacts_addressValue", R.string.fr_contacts_addressValue_it)
        bundleContacts.putInt("st_contacts_TelephoneText", R.string.fr_contacts_telephoneText_it)

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

        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        val menu = navigationView.menu
        val userPageItem = menu.getItem(0)
        val newsItem = menu.getItem(1)
        val calendarItem = menu.getItem(2)
        val calendarAdminItem = menu.getItem(3)
        val systemItem = menu.getItem(5)
        val settingsItem = menu.getItem(5).subMenu.getItem(0)
        val faqItem = menu.getItem(5).subMenu.getItem(1)
        val contactsItem = menu.getItem(5).subMenu.getItem(2)
        val logoutItem = menu.getItem(5).subMenu.getItem(3)
        val spannable_user = SpannableString(userPageItem.title)
        val spannable_news = SpannableString(newsItem.title)
        val spannable_calendarItem = SpannableString(calendarItem.title)
        val spannable_calendarAdminItem = SpannableString(calendarAdminItem.title)
        val spannable_systemItem = SpannableString(systemItem.title)
        val spannable_settings = SpannableString(settingsItem.title)
        val spannable_faqItem = SpannableString(faqItem.title)
        val spannable_contactsItem = SpannableString(contactsItem.title)
        val spannable_logoutItem = SpannableString(logoutItem.title)

        when (index) {
            0 -> {

                navigationView.setBackgroundResource(R.color.background_primary_color_2)
                userPageItem.setIcon(R.drawable.ic_baseline_account_circle_white_24)

                bundleHome.putInt("cl_home_background", R.color.background_primary_color)
                bundleHome.putInt("cl_home_text", R.color.black)
                bundleUser.putInt("cl_user_background", R.color.background_primary_color)
                bundleUser.putInt("cl_user_Layoutbackground", R.drawable.circular_white_bordersolid)
                bundleUser.putInt("cl_user_text", R.color.black)
                bundleAdvertisement.putInt("cl_advertisment_background", R.color.background_primary_color)
                bundleFaq.putInt("cl_faq_background", R.color.background_primary_color)
                bundleFaq.putInt("cl_faq_text", R.color.black)
                bundleContacts.putInt("cl_contacts_background", R.color.background_primary_color)
                bundleContacts.putInt("cl_contacts_text", R.color.black)


                navigationView.setBackgroundResource(R.color.background_primary_color)
                spannable_user.setSpan(TextAppearanceSpan(this,R.style.TextAppareance),0,spannable_user.length,0)
                userPageItem.setTitle(spannable_user)
                userPageItem.setIcon(R.drawable.ic_baseline_account_circle_24)

                spannable_news.setSpan(TextAppearanceSpan(this,R.style.TextAppareance),0,spannable_news.length,0)
                newsItem.setTitle(spannable_news)
                newsItem.setIcon(R.drawable.ic_baseline_new_releases_24)

                spannable_calendarItem.setSpan(TextAppearanceSpan(this,R.style.TextAppareance),0,spannable_calendarItem.length,0)
                calendarItem.setTitle(spannable_calendarItem)
                calendarItem.setIcon(R.drawable.ic_baseline_calendar_today_24)

                spannable_calendarAdminItem.setSpan(TextAppearanceSpan(this,R.style.TextAppareance),0,spannable_calendarAdminItem.length,0)
                calendarAdminItem.setTitle(spannable_calendarAdminItem)
                calendarAdminItem.setIcon(R.drawable.ic_baseline_calendar_today_24)

                spannable_systemItem.setSpan(TextAppearanceSpan(this,R.style.TextAppareance3),0,spannable_systemItem.length,0)
                systemItem.setTitle(spannable_systemItem)

                spannable_settings.setSpan(TextAppearanceSpan(this,R.style.TextAppareance),0,spannable_settings.length,0)
                settingsItem.setTitle(spannable_settings)
                settingsItem.setIcon(R.drawable.ic_baseline_settings_24)

                spannable_faqItem.setSpan(TextAppearanceSpan(this,R.style.TextAppareance),0,spannable_faqItem.length,0)
                faqItem.setTitle(spannable_faqItem)
                faqItem.setIcon(R.drawable.ic_baseline_help_outline_24)

                spannable_contactsItem.setSpan(TextAppearanceSpan(this,R.style.TextAppareance),0,spannable_contactsItem.length,0)
                contactsItem.setTitle(spannable_contactsItem)
                contactsItem.setIcon(R.drawable.ic_baseline_send_24)

                spannable_logoutItem.setSpan(TextAppearanceSpan(this,R.style.TextAppareance),0,spannable_logoutItem.length,0)
                logoutItem.setTitle(spannable_logoutItem)
                logoutItem.setIcon(R.drawable.ic_logout)
            }

            1 -> {

                navigationView.setBackgroundResource(R.color.background_primary_color_2)

                bundleHome.putInt("cl_home_background", R.color.background_primary_color_2)
                bundleHome.putInt("cl_home_text", R.color.white)
                bundleUser.putInt("cl_user_background", R.color.background_primary_color_2)
                bundleUser.putInt("cl_user_Layoutbackground", R.drawable.circular_dark_bordersolid)
                bundleUser.putInt("cl_user_text", R.color.white)
                bundleAdvertisement.putInt("cl_advertisment_background", R.color.background_primary_color_2)
                bundleFaq.putInt("cl_faq_background", R.color.background_primary_color_2)
                bundleFaq.putInt("cl_faq_text", R.color.white)
                bundleContacts.putInt("cl_contacts_background", R.color.background_primary_color_2)
                bundleContacts.putInt("cl_contacts_text", R.color.white)

                navigationView.setBackgroundResource(R.color.background_primary_color_2)

                spannable_user.setSpan(TextAppearanceSpan(this,R.style.TextAppareance2),0,spannable_user.length,0)
                userPageItem.setTitle(spannable_user)
                userPageItem.setIcon(R.drawable.ic_baseline_account_circle_white_24)

                spannable_news.setSpan(TextAppearanceSpan(this,R.style.TextAppareance2),0,spannable_news.length,0)
                newsItem.setTitle(spannable_news)
                newsItem.setIcon(R.drawable.ic_baseline_new_releases_white_24)

                spannable_calendarItem.setSpan(TextAppearanceSpan(this,R.style.TextAppareance2),0,spannable_calendarItem.length,0)
                calendarItem.setTitle(spannable_calendarItem)
                calendarItem.setIcon(R.drawable.ic_baseline_calendar_today_white_24)

                spannable_calendarAdminItem.setSpan(TextAppearanceSpan(this,R.style.TextAppareance2),0,spannable_calendarAdminItem.length,0)
                calendarAdminItem.setTitle(spannable_calendarAdminItem)
                calendarAdminItem.setIcon(R.drawable.ic_baseline_calendar_today_white_24)

                spannable_systemItem.setSpan(TextAppearanceSpan(this,R.style.TextAppareance4),0,spannable_systemItem.length,0)
                systemItem.setTitle(spannable_systemItem)

                spannable_settings.setSpan(TextAppearanceSpan(this,R.style.TextAppareance2),0,spannable_settings.length,0)
                settingsItem.setTitle(spannable_settings)
                settingsItem.setIcon(R.drawable.ic_baseline_settings_white_24)

                spannable_faqItem.setSpan(TextAppearanceSpan(this,R.style.TextAppareance2),0,spannable_faqItem.length,0)
                faqItem.setTitle(spannable_faqItem)
                faqItem.setIcon(R.drawable.ic_baseline_help_outline_white_24)

                spannable_contactsItem.setSpan(TextAppearanceSpan(this,R.style.TextAppareance2),0,spannable_contactsItem.length,0)
                contactsItem.setTitle(spannable_contactsItem)
                contactsItem.setIcon(R.drawable.ic_baseline_send_white_24)

                spannable_logoutItem.setSpan(TextAppearanceSpan(this,R.style.TextAppareance2),0,spannable_logoutItem.length,0)
                logoutItem.setTitle(spannable_logoutItem)
                logoutItem.setIcon(R.drawable.ic_logout_white)
            }
        }
    }

    override fun language(index: Int) {
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        val menu = navigationView.menu

        if (index == 0) {

            bundleHome.putInt("st_home_firstQuestion", R.string.fr_home_firstQuestion_it)
            bundleHome.putInt("st_home_firstAnswer", R.string.fr_home_firstAnswer_it)
            bundleHome.putInt("st_home_secondQuestion", R.string.fr_home_secondQuestion_it)
            bundleHome.putInt("st_home_secondAnswer", R.string.fr_home_secondAnswer_it)
            bundleUser.putInt("st_user_pageTitle", R.string.fr_user_pageTitle_it)
            bundleUser.putInt("st_user_informationSectionTitle", R.string.fr_user_informationSectionTitle_it)
            bundleUser.putInt("st_user_informationSectionButton", R.string.fr_user_informationSectionButton_it)
            bundleUser.putInt("st_user_informationSectionButtonSave", R.string.fr_user_informationSectionButtonSave_it)
            bundleUser.putInt("st_user_subscriptionSectionTitle", R.string.fr_user_subscriptionSectionTitle_it)
            bundleUser.putInt("st_user_subscriptionSectionTipology", R.string.fr_user_subscriptionSectionTipologyText_it)
            bundleUser.putInt("st_user_subscriptionSectionExpired", R.string.fr_user_subscriptionSectionExpiredText_it)
            bundleUser.putInt("st_user_subscriptionSectionCircularProgress", R.string.fr_user_subscriptionSectionCircularProgressText_it)
            bundleUser.putInt("st_user_physicsSectionTitle", R.string.fr_user_physicalSectionTitle_it)
            bundleUser.putInt("st_user_physicsSectionAge", R.string.fr_user_physicalSectionAge_it)
            bundleUser.putInt("st_user_physicsSectionWeight", R.string.fr_user_physicalSectionWeight_it)
            bundleUser.putInt("st_user_physicsSectionHeight", R.string.fr_user_physicalSectionHeight_it)
            bundleUser.putInt("st_user_physicsSectionButtonSave", R.string.fr_user_informationSectionButtonSave_it)
            bundleUser.putInt("st_user_physicsSectionButton", R.string.fr_user_informationSectionButton_it)
            bundleAdvertisement.putInt("st_advertisment_title", R.string.fr_advertisment_title_it)
            bundleAdvertisement.putInt("st_advertisment_event1", R.string.fr_advertisment_event1_it)
            bundleAdvertisement.putInt("st_advertisment_event2", R.string.fr_advertisment_event2_it)
            bundleAdvertisement.putInt("st_advertisment_event3", R.string.fr_advertisment_event3_it)
            bundleAdvertisement.putInt("st_advertisment_event4", R.string.fr_advertisment_event4_it)
            bundleAdvertisement.putInt("st_advertisment_event5", R.string.fr_advertisment_event5_it)
            bundleFaq.putInt("st_faq_question1", R.string.fr_faq_question1_it)
            bundleFaq.putInt("st_faq_question2", R.string.fr_faq_question2_it)
            bundleFaq.putInt("st_faq_question3", R.string.fr_faq_question3_it)
            bundleFaq.putInt("st_faq_answer1", R.string.fr_faq_answer1_it)
            bundleFaq.putInt("st_faq_answer2", R.string.fr_faq_answer2_it)
            bundleFaq.putInt("st_faq_answer3", R.string.fr_faq_answer3_it)
            bundleContacts.putInt("st_contacts_title", R.string.fr_contacts_title_it)
            bundleContacts.putInt("st_contacts_timetableText", R.string.fr_contacts_timetable_it)
            bundleContacts.putInt("st_contacts_timetableValue1", R.string.fr_contacts_openingTimetable1_it)
            bundleContacts.putInt("st_contacts_timetableValue2", R.string.fr_contacts_openingTimetable2_it)
            bundleContacts.putInt("st_contacts_addressText", R.string.fr_contacts_addressText_it)
            bundleContacts.putInt("st_contacts_addressValue", R.string.fr_contacts_addressValue_it)
            bundleContacts.putInt("st_contacts_TelephoneText", R.string.fr_contacts_telephoneText_it)

            menu.getItem(0).setTitle(getResources().getString(R.string.fr_user_pageTitle_it))
            menu.getItem(1).setTitle(getResources().getString(R.string.fr_advertisment_title_it))
            menu.getItem(2).setTitle(getResources().getString(R.string.calendar))
            //menu.getItem(3).setTitle(getResources().getString(R.string.activities))
            menu.getItem(5).subMenu.getItem(0).setTitle(getResources().getString(R.string.settings))
            menu.getItem(5).subMenu.getItem(1).setTitle(getResources().getString(R.string.FAQ))
            menu.getItem(5).subMenu.getItem(2).setTitle(getResources().getString(R.string.contacts))

        } else if (index == 1) {

            bundleHome.putInt("st_home_firstQuestion", R.string.fr_home_firstQuestion_en)
            bundleHome.putInt("st_home_firstAnswer", R.string.fr_home_firstAnswer_en)
            bundleHome.putInt("st_home_secondQuestion", R.string.fr_home_secondQuestion_en)
            bundleHome.putInt("st_home_secondAnswer", R.string.fr_home_secondAnswer_en)
            bundleUser.putInt("st_user_pageTitle", R.string.fr_user_pageTitle_en)
            bundleUser.putInt("st_user_informationSectionTitle", R.string.fr_user_informationSectionTitle_en)
            bundleUser.putInt("st_user_informationSectionButton", R.string.fr_user_informationSectionButton_en)
            bundleUser.putInt("st_user_informationSectionButtonSave", R.string.fr_user_informationSectionButtonSave_en)
            bundleUser.putInt("st_user_subscriptionSectionTitle", R.string.fr_user_subscriptionSectionTitle_en)
            bundleUser.putInt("st_user_subscriptionSectionTipology", R.string.fr_user_subscriptionSectionTipologyText_en)
            bundleUser.putInt("st_user_subscriptionSectionExpired", R.string.fr_user_subscriptionSectionExpiredText_en)
            bundleUser.putInt("st_user_subscriptionSectionCircularProgress", R.string.fr_user_subscriptionSectionCircularProgressText_en)
            bundleUser.putInt("st_user_physicsSectionTitle", R.string.fr_user_physicalSectionTitle_en)
            bundleUser.putInt("st_user_physicsSectionAge", R.string.fr_user_physicalSectionAge_en)
            bundleUser.putInt("st_user_physicsSectionWeight", R.string.fr_user_physicalSectionWeight_en)
            bundleUser.putInt("st_user_physicsSectionHeight", R.string.fr_user_physicalSectionHeight_en)
            bundleUser.putInt("st_user_physicsSectionButtonSave", R.string.fr_user_informationSectionButtonSave_en)
            bundleUser.putInt("st_user_physicsSectionButton", R.string.fr_user_informationSectionButton_en)
            bundleAdvertisement.putInt("st_advertisment_title", R.string.fr_advertisment_title_en)
            bundleAdvertisement.putInt("st_advertisment_event1", R.string.fr_advertisment_event1_en)
            bundleAdvertisement.putInt("st_advertisment_event2", R.string.fr_advertisment_event2_en)
            bundleAdvertisement.putInt("st_advertisment_event3", R.string.fr_advertisment_event3_en)
            bundleAdvertisement.putInt("st_advertisment_event4", R.string.fr_advertisment_event4_en)
            bundleAdvertisement.putInt("st_advertisment_event5", R.string.fr_advertisment_event5_en)
            bundleFaq.putInt("st_faq_question1", R.string.fr_faq_question1_en)
            bundleFaq.putInt("st_faq_question2", R.string.fr_faq_question2_en)
            bundleFaq.putInt("st_faq_question3", R.string.fr_faq_question3_en)
            bundleFaq.putInt("st_faq_answer1", R.string.fr_faq_answer1_en)
            bundleFaq.putInt("st_faq_answer2", R.string.fr_faq_answer2_en)
            bundleFaq.putInt("st_faq_answer3", R.string.fr_faq_answer3_en)
            bundleContacts.putInt("st_contacts_title", R.string.fr_contacts_title_en)
            bundleContacts.putInt("st_contacts_timetableText", R.string.fr_contacts_timetable_en)
            bundleContacts.putInt("st_contacts_timetableValue1", R.string.fr_contacts_openingTimetable1_en)
            bundleContacts.putInt("st_contacts_timetableValue2", R.string.fr_contacts_openingTimetable2_en)
            bundleContacts.putInt("st_contacts_addressText", R.string.fr_contacts_addressText_en)
            bundleContacts.putInt("st_contacts_addressValue", R.string.fr_contacts_addressValue_en)
            bundleContacts.putInt("st_contacts_TelephoneText", R.string.fr_contacts_telephoneText_en)

            menu.getItem(0).setTitle(getResources().getString(R.string.fr_user_pageTitle_en))
            menu.getItem(1).setTitle(getResources().getString(R.string.fr_advertisment_title_en))
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
        /*fragmentEventParticipants.arguments = bundleEventParticipants
        supportFragmentManager.beginTransaction().replace(R.id.Fragment_container, fragmentEventParticipants, "EventPartecipantsFragment").commit()*/
        val eventPartecipantsFragment = EventPartecipantsFragment()
        eventPartecipantsFragment.arguments = bundleEventParticipants
        eventPartecipantsFragment.show(supportFragmentManager, "TAG")
    }

    override fun changeProfileImageNavHeader() {
        getProfileImage()
    }
}