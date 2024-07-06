package com.example.contactlist

//import android.support.v4.app.Fragment
//import android.R
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.*
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2




//https://stackoverflow.com/questions/14678593/the-application-may-be-doing-too-much-work-on-its-main-thread
//https://developer.android.com/guide/fragments/communicate
private const val NUM_PAGES = 3 // The number of pages to show

//TODO: https://developer.android.com/studio/publish/preparing
class MainActivity : AppCompatActivity(), InputFragment.OnListFragmentInteractionListener,
    BlankFragment.OnFragmentInteractionListener, DataPassListener {

    private var softInputMode:Int? = null

    /*
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private lateinit var mPager: ViewPager2
    //TODO: https://developer.android.com/training/animation/vp2-migration
    //val getContent = registerForActivityResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI),100) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_slide)

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = findViewById<View>(R.id.pager) as ViewPager2
        mPager.offscreenPageLimit=2
        val pagerAdapter = ScreenSlideViewPager2(this)

        mPager.adapter = pagerAdapter
        mPager.setUserInputEnabled(false)//disable manual swiping

        //1st fragment(InputFragment) is already loaded with view in the adapter..
        //..but the 2nd fragment(BlankFragment) not, so we have to go through the fragments once and then back to the start
        gotoform()
        gofromform()
        todetails()
        gofromdetail()

        softInputMode = window?.attributes?.softInputMode
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        pagechange_register()

        //https://stackoverflow.com/questions/56808828/how-do-i-access-sqlite-db-in-an-activity-from-inside-a-fragment-using-kotlin
        try {
            DBHelper.getInstance(this)
        }
        catch (e: Exception) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Error Exception")
            builder.setMessage("Could not create a database connection")
        }
    }

    override fun onDestroy() {
        DBHelper.close()
        super.onDestroy()
    }


    private fun pagechange_register() {

        mPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                //in the contactlist screen, we don't want the inputwindow to squeeze the rest of the layout -> hence the adjust_nothing mode
                if(position==0){
                    softInputMode?.let {window?.setSoftInputMode(it) }
                    window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
                }
                else if(position==1){//we are in the addContacts and because we have a ScrollView there, the inputwindow resizing the rest of the layout has no great effect
                    softInputMode?.let {window?.setSoftInputMode(it) }
                    window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                }

                else{
                    softInputMode?.let {window?.setSoftInputMode(it) }
                    window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

                }
            }
        })

    }

//https://www.androiddesignpatterns.com/2013/04/retaining-objects-across-config-changes.html

    fun backbutton(){
        if(mPager.currentItem!=0)
            mPager.currentItem=0//0 = InputFragment

        if(mPager.currentItem==0){
            val toupdate = (mPager.adapter as ScreenSlideViewPager2).createFragment(0) as InputFragment
            toupdate.actualize()
            //super.onBackPressed()
        }
        val checker=  mPager.adapter as ScreenSlideViewPager2
        var checker2 = checker.createFragment(1) as BlankFragment
        checker2.isonUpdate(false)
    }

    fun actualizing(){//TODO:should immediately be visible
        val toupdate = (mPager.adapter as ScreenSlideViewPager2).createFragment(0) as InputFragment
        toupdate.actualize()

    }

    fun createcontact(){
       val checker=  mPager.adapter as ScreenSlideViewPager2
       var checker2 = checker.createFragment(1) as BlankFragment

        gotoform()

        if(checker2.lifecycle.currentState== Lifecycle.State.STARTED)//when it is not the 1st create call
            checker2.creating()
    }

    fun gotoform(){
        if(mPager.currentItem!=1)
            mPager.currentItem=1
    }

    fun gofromform(){
        if(mPager.currentItem!=0)
            mPager.currentItem=0

        val checker=  mPager.adapter as ScreenSlideViewPager2
        var checker2 = checker.createFragment(1) as BlankFragment
        checker2.isonUpdate(false)

    }

    fun todetails(){//TODO: transition animation happens here - remove
         if(mPager.currentItem!=2)
            mPager.currentItem=2
    }

    fun gofromdetail(){
        if(mPager.currentItem!=0)
            mPager.currentItem=0
    }

    fun getcontacts():List<Contact>{
      return DBHelper.getInstance(this).viewContacts()
    }

    fun search(contact: Contact):List<Contact>{
        return DBHelper.getInstance(this).searchContact(contact)
    }


    fun addcontact(con: Contact):Long{
       return DBHelper.getInstance(this).addContact(con)
    }

    fun updatecontact(con: Contact):Int{
        return DBHelper.getInstance(this).updateContact(con)
    }


    fun delcontact(con: Contact){
        DBHelper.getInstance(this).deleteContact(con)
    }

    fun deltable(){
       return DBHelper.getInstance(this).deleteTable()
    }

    fun detailsData(con: Contact) {
        val detailfrag = mPager.adapter as ScreenSlideViewPager2
        val frag = detailfrag.createFragment(2) as DetailsFragment

        todetails()

        frag.receiveData(con)//TODO:bug
    }

    //https://stackoverflow.com/questions/8094715/how-to-catch-event-with-hardware-back-button-on-android/8094821
    override fun onBackPressed() {
        if (mPager.currentItem == 0)
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
         else{
            val checker = mPager.adapter as ScreenSlideViewPager2

                var checker2 = checker.createFragment(1) as BlankFragment
                checker2.isonUpdate(false)
                checker2.reset()

            mPager.currentItem = 0
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
     * source: https://developer.android.com/training/animation/screen-slide-2
     *
     */
    private inner class ScreenSlideViewPager2(fa: FragmentActivity):FragmentStateAdapter(fa){
        private val input:Fragment = InputFragment()
        private val blank:Fragment = BlankFragment()
        private val detail:Fragment = DetailsFragment()

        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment{
            if(position == 0)
                return input//Input Fragment() would destroy reference we need for further methods/operations
            else {
                if (position == 1)
                    return blank
                else
                    return detail
            }
        }
    }


    /** used to pass Contact data from one fragment to another
     *
     * @param Contact object containing data
     * */
    override fun passData(data: Contact) {
        gotoform()
        val checker=  mPager.adapter as ScreenSlideViewPager2
        var checker2 = checker.createFragment(1) as BlankFragment

        return checker2.updating(data)
    }

    fun createbutton(){
        if(mPager.currentItem==1)//1 = BlankFragment
            mPager.currentItem=0//0 = InputFragment

        if(mPager.currentItem==0){
            val toupdate = (mPager.adapter as ScreenSlideViewPager2).createFragment(0) as InputFragment
            toupdate.actualize()
        }

        val checker=  mPager.adapter as ScreenSlideViewPager2
        var checker2 = checker.createFragment(1) as BlankFragment
        checker2.isonUpdate(false)
    }

    fun openimagegallery(activityResult: ActivityResultLauncher<Intent>, context: Context){
        val intent = Intent(context, this.javaClass)
        activityResult.launch(intent)

    }

}


