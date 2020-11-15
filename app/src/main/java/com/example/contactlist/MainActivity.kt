package com.example.contactlist

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager


/**
 * The number of pages (wizard steps) to show in this demo.
 */
private const val NUM_PAGES = 2

class MainActivity : AppCompatActivity(), InputFragment.OnListFragmentInteractionListener,
    BlankFragment.OnFragmentInteractionListener {

    private var softInputMode:Int? = null
   // var database:DBHelper? = null

    //val database:DBHelper? = null


    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        InputFragment fragment = new InputFragment();
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
*/

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private lateinit var mPager: ViewPager


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
           // database = DBHelper(this)
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
        if(mPager.currentItem==1)//1 = InputFragment
            mPager.currentItem=0//0 = BlankFragment

    }

    fun gotoform(){
        if(mPager.currentItem==0)
            mPager.currentItem=1

    }

    fun getcontacts():List<Contact>{
      return DBHelper.getInstance(this).viewContacts()
    }

    fun addcontact(con:Contact):Long{

       return DBHelper.getInstance(this).addContact(con)
    }

    fun delcontact(con:Contact){
        DBHelper.getInstance(this).deleteContact(con)
    }

    fun deltable(){
       return DBHelper.getInstance(this).deleteTable()

    }


    override fun onBackPressed() {
        if (mPager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            mPager.currentItem = mPager.currentItem - 1
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
     */
    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int = NUM_PAGES

        override fun getItem(position: Int): Fragment{
            if(position == 0){

               return InputFragment()
           }
            else{
                return BlankFragment()
            }

        }
    }


}


