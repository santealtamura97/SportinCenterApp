package com.example.sportincenterapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.sportincenterapp.R
import com.example.sportincenterapp.fragments.*
import com.example.sportincenterapp.interfaces.Communicator
import com.example.sportincenterapp.utils.ApplicationContextProvider
import com.example.sportincenterapp.utils.SessionManager
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, Communicator {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var sessionManager: SessionManager

    //User informations
    private lateinit var userName: TextView
    private lateinit var userEmail: TextView
    //Fragments
    private val fragmentUser : Fragment = UserPage()
    private val fragmentSettings : Fragment = Settings()
    private val fragmentHome : Fragment = HomeFragment()
    private val fragmentAdvertisment : Fragment = Advertisment()
    private val fragmentFaq : Fragment = Faq()
    private val fragmentContacts : Fragment = Contacts()
    //private val fragmentActivities: Fragment = ActivitiesFragment()
    private val fragmentCalendarAdmin: Fragment = CalendarAdminFragment()
    //Bundles
    private val bundleUser : Bundle = Bundle()
    private val bundleHome : Bundle = Bundle()
    private val bundleAdvertisment: Bundle = Bundle()
    private val bundleFaq: Bundle = Bundle()
    private val bundleContacts: Bundle = Bundle()
    private val bundleCalendarAdmin: Bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(ApplicationContextProvider.getContext()) //initialize session manager in this class
        //Main navigation setting
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById(R.id.nav_view)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        val header = navigationView.getHeaderView(0)

        userName = header.findViewById<TextView>(R.id.nome_utente_nav_header)
        userEmail = header.findViewById<TextView>(R.id.email_nav_header)

        val loginButton = header.findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener { intent = Intent(ApplicationContextProvider.getContext(), LoginActivity::class.java)
            startActivity(intent) }

        val menu = navigationView.menu
        val userPageItem = menu.getItem(0)
        val newsItem = menu.getItem(1)
        val calendarItem = menu.getItem(2)
        //val activitiesItem = menu.getItem(3)
        val calendarAdminItem = menu.getItem(4)
        val faqItem = menu.getItem(5).subMenu.getItem(1)
        val contactsItem = menu.getItem(5).subMenu.getItem(2)
        val logoutItem = menu.getItem(5).subMenu.getItem(3)

        if (!sessionManager.fetchUserName().isNullOrEmpty()) {
            userName?.text = sessionManager.fetchUserName()
            userEmail.text = sessionManager.fetchUserEmail()
            if (sessionManager.fetchUserName() == "Admin" ) {
                userPageItem.isVisible = false
                newsItem.isVisible = false
                calendarItem.isVisible = false
                //activitiesItem.isVisible = false
                calendarAdminItem.isVisible = true
                faqItem.isVisible = false
                contactsItem.isVisible = false
            }
            userName?.visibility = View.VISIBLE
            loginButton.visibility = View.GONE
        } else{
            //calendarItem.isVisible = false
            userPageItem.isVisible = false
            newsItem.isVisible = true
            logoutItem.isVisible = false
           //activitiesItem.isVisible = false
        }

        navigationView.setNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction().replace(R.id.Fragment_container, fragmentHome)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_draw_open, R.string.navigation_draw_close)

        //Home button settings
        val homeButton : ImageButton = findViewById(R.id.home_button)
        homeButton.setOnClickListener {supportFragmentManager.beginTransaction()
            .replace(R.id.Fragment_container, fragmentHome).commit()}
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_container, fragmentHome).commit()
            navigationView.setCheckedItem(R.id.home)
        }
        initializeFragments()
    }

    override fun onBackPressed() {
        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout!!.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {

        when (menuItem.itemId) {

            R.id.calendar -> supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_container, CalendarCollectionFragment()).commit()

            R.id.admin_calendar -> supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_container, CalendarAdminFragment()).commit()

            R.id.profile -> supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_container, fragmentUser).commit()

            R.id.news -> supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_container, fragmentAdvertisment).commit()

            /*R.id.activities -> supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_container, fragmentActivities).commit()*/

            R.id.settings -> supportFragmentManager.beginTransaction()
                    .replace(R.id.Fragment_container, fragmentSettings).commit()

            R.id.help -> supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_container, fragmentFaq).commit()

            R.id.contacts -> supportFragmentManager.beginTransaction()
                .replace(R.id.Fragment_container, fragmentContacts).commit()

            R.id.logout -> logout()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun logout() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.logout_confirm);
        builder.setMessage(R.string.logout_message);

        builder.setPositiveButton(R.string.logout_yes) {
            dialog, which -> // Do nothing but close the dialog
            sessionManager.logout()
            finish();
            startActivity(intent)
            dialog.dismiss()
        }

        builder.setNegativeButton(R.string.logout_no) {
            dialog, which -> // Do nothing but close the dialog
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }


    private fun initializeFragments() {
        //Strings
        bundleUser.putString("username", userName.text.toString())
        bundleUser.putString("email", userEmail.text.toString())
        bundleUser.putString("um1", "Kg")
        bundleUser.putString("um2", "Cm")
        //Color
        bundleUser.putInt("color", R.color.primary_color)
        bundleHome.putInt("color", R.color.primary_color)
        bundleAdvertisment.putInt("color", R.color.primary_color)
        bundleFaq.putInt("color", R.color.primary_color)
        bundleContacts.putInt("color", R.color.primary_color)
        //Language
        bundleHome.putInt("string_home_1", R.string.first_title_home)
        bundleHome.putInt("string_home_2", R.string.first_text_home)
        bundleHome.putInt("string_home_3", R.string.second_title_home)
        bundleHome.putInt("string_home_4", R.string.second_text_home)
        bundleAdvertisment.putInt("string_title", R.string.advertisment_title)
        bundleAdvertisment.putInt("string_girl", R.string.advertisment_girlpower)
        bundleAdvertisment.putInt("string_easter", R.string.advertisment_easter)
        bundleAdvertisment.putInt("string_anniversary", R.string.advertisment_anniversary)
        bundleAdvertisment.putInt("string_halloween", R.string.advertisment_halloween)
        bundleAdvertisment.putInt("string_newyear", R.string.advertisment_newyear)
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
        fragmentAdvertisment.arguments = bundleAdvertisment
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
        val tlbar: Toolbar = findViewById(R.id.toolbar)

        if (index == 0) {
            header.setBackgroundResource(R.color.primary_color)
            tlbar.setBackgroundResource(R.color.primary_color)
            homeBtn.setBackgroundResource(R.color.primary_color)
            bundleUser.putInt("color", R.color.primary_color)
            bundleHome.putInt("color", R.color.primary_color)
            bundleAdvertisment.putInt("color", R.color.primary_color)
            bundleFaq.putInt("color", R.color.primary_color)
            bundleContacts.putInt("color", R.color.primary_color)

        } else if (index == 1) {
            header.setBackgroundResource(R.color.primary_color_2)
            tlbar.setBackgroundResource(R.color.primary_color_2)
            homeBtn.setBackgroundResource(R.color.primary_color_2)
            bundleUser.putInt("color", R.color.primary_color_2)
            bundleHome.putInt("color", R.color.primary_color_2)
            bundleAdvertisment.putInt("color", R.color.primary_color_2)
            bundleFaq.putInt("color", R.color.primary_color_2)
            bundleContacts.putInt("color", R.color.primary_color_2)

        } else if (index == 2) {
            header.setBackgroundResource(R.color.primary_color_3)
            tlbar.setBackgroundResource(R.color.primary_color_3)
            homeBtn.setBackgroundResource(R.color.primary_color_3)
            bundleUser.putInt("color", R.color.primary_color_3)
            bundleHome.putInt("color", R.color.primary_color_3)
            bundleAdvertisment.putInt("color", R.color.primary_color_3)
            bundleFaq.putInt("color", R.color.primary_color_3)
            bundleContacts.putInt("color", R.color.primary_color_3)
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
            bundleAdvertisment.putInt("string_title", R.string.advertisment_title)
            bundleAdvertisment.putInt("string_girl", R.string.advertisment_girlpower)
            bundleAdvertisment.putInt("string_easter", R.string.advertisment_easter)
            bundleAdvertisment.putInt("string_anniversary", R.string.advertisment_anniversary)
            bundleAdvertisment.putInt("string_halloween", R.string.advertisment_halloween)
            bundleAdvertisment.putInt("string_newyear", R.string.advertisment_newyear)
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
            bundleAdvertisment.putInt("string_title", R.string.advertisment_title_en)
            bundleAdvertisment.putInt("string_girl", R.string.advertisment_girlpower_en)
            bundleAdvertisment.putInt("string_easter", R.string.advertisment_easter_en)
            bundleAdvertisment.putInt("string_anniversary", R.string.advertisment_anniversary_en)
            bundleAdvertisment.putInt("string_halloween", R.string.advertisment_halloween_en)
            bundleAdvertisment.putInt("string_newyear", R.string.advertisment_newyear_en)
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

    override fun createActivity() {
        val builder = android.app.AlertDialog.Builder(this,taskId)
        val customlayout = layoutInflater.inflate(R.layout.add_activity_dialog, null)

        builder.setView(customlayout)
        builder.setPositiveButton("Conferma") {
                dialog, which ->
            /*val edittitle = customlayout.findViewById<EditText>(R.id.edit_activity_text)
            val editbooking= customlayout.findViewById<EditText>(R.id.edit_booking_text)
            val editinitial = customlayout.findViewById<EditText>(R.id.edit_initial_text)
            val editfinal= customlayout.findViewById<EditText>(R.id.edit_final_text)

            bundleCalendarAdmin.putString("title", edittitle.text.toString())
            bundleCalendarAdmin.putString("booking", editbooking.text.toString())
            bundleCalendarAdmin.putString("initial", editinitial.text.toString())
            bundleCalendarAdmin.putString("final", editfinal.text.toString())*/

            fragmentCalendarAdmin.arguments = bundleCalendarAdmin

            dialog.dismiss()
        }

        builder.setNegativeButton("Annulla") {
                dialog, which -> dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }



}