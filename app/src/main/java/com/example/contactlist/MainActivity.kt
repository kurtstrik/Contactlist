package com.example.contactlist

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
//import android.support.v4.app.Fragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager

//https://stackoverflow.com/questions/14678593/the-application-may-be-doing-too-much-work-on-its-main-thread

/**
 * The number of pages (wizard steps) to show in this demo.
 */
private const val NUM_PAGES = 2

class MainActivity : AppCompatActivity(), InputFragment.OnListFragmentInteractionListener,
    BlankFragment.OnFragmentInteractionListener, DataPassListener {

    private var softInputMode:Int? = null

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private lateinit var mPager: ViewPager

    //val getContent = registerForActivityResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI),100) {}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_slide)

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = findViewById<View>(R.id.pager) as ViewPager
        // Instantiate a ViewPager and a PagerAdapter.
          val pagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager)


        mPager.adapter = pagerAdapter


        softInputMode = window?.attributes?.softInputMode
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        mPager.addOnPageChangeListener(object:ViewPager.OnPageChangeListener{

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
            override fun onPageSelected(position: Int) {

                //in the contactlist screen, we don't want the inputwindow to squeeze the rest of the layout -> hence the adjust_nothing mode
                if(position==0){
                    softInputMode?.let {window?.setSoftInputMode(it) }
                    window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
                }
                else if(position==1){//we are in the addContacts and because we have a ScrollView there, the inputwindow resizing the rest of the layout has no great effect
                    softInputMode?.let {window?.setSoftInputMode(it) }
                    window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                }
            }
        })


        //https://stackoverflow.com/questions/56808828/how-do-i-access-sqlite-db-in-an-activity-from-inside-a-fragment-using-kotlin
        try {

            DBHelper.getInstance(this)

           // database = this.openOrCreateDatabase("CreditsDB", Context.MODE_PRIVATE, null)
        }
        catch(e: Exception)
        {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Error Exception")
            builder.setMessage("Could not create a database connection")
        }

    }


    fun backbutton(){
        if(mPager.currentItem==1)//1 = BlankFragment
            mPager.currentItem=0//0 = InputFragment

        if(mPager.currentItem==0){
            val toupdate = (mPager.adapter as ScreenSlidePagerAdapter).getItem(0) as InputFragment
            toupdate.actualize()
        }

        val checker=  mPager.adapter as ScreenSlidePagerAdapter
        var checker2 = checker.getItem(1) as BlankFragment
        checker2.isonUpdate(false)



    }

    fun createbutton(){
        if(mPager.currentItem==1)//1 = BlankFragment
            mPager.currentItem=0//0 = InputFragment

        if(mPager.currentItem==0){
            val toupdate = (mPager.adapter as ScreenSlidePagerAdapter).getItem(0) as InputFragment
            toupdate.actualize()
        }

        val checker=  mPager.adapter as ScreenSlidePagerAdapter
        var checker2 = checker.getItem(1) as BlankFragment
        checker2.isonUpdate(false)

    }


    fun gotoform(){
        if(mPager.currentItem==0)
            mPager.currentItem=1


    }

    fun gotodetails(){


    }


    fun passtoform(con:Contact){
        if(mPager.currentItem==0) {
            mPager.currentItem = 1

        mPager.adapter

        }

    }

    fun getcontacts():List<Contact>{
      return DBHelper.getInstance(this).viewContacts()
    }

    fun search(contact:Contact):List<Contact>{

        return DBHelper.getInstance(this).searchContact(contact)
    }


    fun addcontact(con:Contact):Long{

       return DBHelper.getInstance(this).addContact(con)
    }

    fun updatecontact(con:Contact):Int{
        return DBHelper.getInstance(this).updateContact(con)
    }


    fun delcontact(con:Contact){
        DBHelper.getInstance(this).deleteContact(con)
    }

    fun deltable(){
       return DBHelper.getInstance(this).deleteTable()

    }

    fun details(con:Contact){


    }

    //https://stackoverflow.com/questions/8094715/how-to-catch-event-with-hardware-back-button-on-android/8094821
    override fun onBackPressed() {
        if (mPager.currentItem == 0)
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
         else{
            // Otherwise, select the previous step.
            mPager.currentItem = mPager.currentItem - 1

            val checker=  mPager.adapter as ScreenSlidePagerAdapter
            var checker2 = checker.getItem(1) as BlankFragment
            checker2.isonUpdate(false)
            checker2.cancel()

        }

    }

    override fun onListFragmentInteraction(uri: Uri?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFragmentInteraction(uri: Uri?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    /**
     * A simple pager adapter that represents 2 ScreenSlidePageFragment objects, in
     * sequence.
     *
     * source: https://developer.android.com/training/animation/screen-slide
     *
     */
    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        private val input:Fragment = InputFragment()
        private val blank:Fragment = BlankFragment()

        override fun getCount(): Int = NUM_PAGES

        override fun getItem(position: Int): Fragment{
            if(position == 0)
               return input

            else
                return blank

        }
    }

    override fun passData(data: Contact) {
        gotoform()
        val checker=  mPager.adapter as ScreenSlidePagerAdapter
        var checker2 = checker.getItem(1) as BlankFragment

        return checker2.updating(data)
    }

    fun openimagegallery(activityResult: ActivityResultLauncher<Intent>, context: Context){
        val intent = Intent(context, this.javaClass)
        activityResult.launch(intent)

    }


}


