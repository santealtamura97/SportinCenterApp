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
import com.example.sportincenterapp.fragments.AddActivityFragment
import com.example.sportincenterapp.fragments.*
import com.example.sportincenterapp.interfaces.Communicator
import com.example.sportincenterapp.utils.ApplicationContextProvider
import com.example.sportincenterapp.utils.SessionManager
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, Communicator {

    // DECLARATIONS
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var sessionManager: SessionManager

    //User information
    private lateinit var userName: TextView
    private lateinit var userEmail: TextView
    //Fragments
    private val fragmentHome : Fragment = HomeFragment()
    private val fragmentUser : Fragment = UserPage()
    private val fragmentAdvertisement : Fragment = Advertisment()

    private val fragmentSettings : Fragment = Settings()
    private val fragmentFaq : Fragment = Faq()
    private val fragmentContacts : Fragment = Contacts()
    private val fragmentCalendarAdmin: Fragment = CalendarAdminFragment()
    private val fragmentAddActivity: Fragment = AddActivityFragment()
    private val fragmentEventParticipants: Fragment = EventPartecipantsFragment()
    //Bundles
    private val bundleHome : Bundle = Bundle()
    private val bundleUser : Bundle = Bundle()
    private val bundleAdvertisement: Bundle = Bundle()
    private val bundleFaq: Bundle = Bundle()

    private val bundleContacts: Bundle = Bundle()
    private val bundleEventParticipants: Bundle = Bundle()

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

        bundleContacts.putInt("color", R.color.orange)

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

                bundleHome.putInt("cl_home_background", R.color.background_primary_color)
                bundleHome.putInt("cl_home_text", R.color.black)
                bundleUser.putInt("cl_user_background", R.color.background_primary_color)
                bundleUser.putInt("cl_user_Layoutbackground", R.drawable.circular_white_bordersolid)
                bundleUser.putInt("cl_user_text", R.color.black)
                bundleAdvertisement.putInt("cl_advertisment_background", R.color.background_primary_color)
                bundleFaq.putInt("cl_faq_background", R.color.background_primary_color)
                bundleFaq.putInt("cl_faq_text", R.color.black)

                bundleContacts.putInt("color", R.color.orange)
    
            }
            1 -> {
                header.setBackgroundResource(R.color.orange)
                tools.setBackgroundResource(R.color.orange)
                homeBtn.setBackgroundResource(R.color.orange)
                bundleHome.putInt("cl_home_background", R.color.background_primary_color_2)
                bundleHome.putInt("cl_home_text", R.color.white)
                bundleUser.putInt("cl_user_background", R.color.background_primary_color_2)
                bundleUser.putInt("cl_user_Layoutbackground", R.drawable.circular_dark_bordersolid)
                bundleUser.putInt("cl_user_text", R.color.white)
                bundleAdvertisement.putInt("cl_advertisment_background", R.color.background_primary_color_2)
                bundleFaq.putInt("cl_faq_background", R.color.background_primary_color_2)
                bundleFaq.putInt("cl_faq_text", R.color.white)

                bundleContacts.putInt("color", R.color.orange)
    
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

            bundleContacts.putInt("string_contact_1", R.string.contacts_address)
            bundleContacts.putInt("string_contact_2", R.string.contacts_telephone)
            bundleContacts.putInt("string_contact_3", R.string.contacts_hours)
            bundleContacts.putInt("string_contact_4", R.string.contacts_email)
            bundleContacts.putInt("string_contact_5", R.string.contacts_title)
            bundleContacts.putInt("string_contact_6", R.string.contacts_hours_string)
            bundleContacts.putInt("string_contact_7", R.string.contacts_hours_string_2)
            bundleContacts.putInt("string_contact_8", R.string.contacts_address_string)
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



            bundleContacts.putInt("string_contact_1", R.string.contacts_address_en)
            bundleContacts.putInt("string_contact_2", R.string.contacts_telephone_en)
            bundleContacts.putInt("string_contact_3", R.string.contacts_hours_en)
            bundleContacts.putInt("string_contact_4", R.string.contacts_email)
            bundleContacts.putInt("string_contact_5", R.string.contacts_title_en)
            bundleContacts.putInt("string_contact_6", R.string.contacts_hours_string_en)
            bundleContacts.putInt("string_contact_7", R.string.contacts_hours_string_2_en)
            bundleContacts.putInt("string_contact_8", R.string.contacts_address_string_en)

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
        fragmentEventParticipants.arguments = bundleEventParticipants
        supportFragmentManager.beginTransaction().replace(R.id.Fragment_container, fragmentEventParticipants, "EventPartecipantsFragment").commit()
    }
}