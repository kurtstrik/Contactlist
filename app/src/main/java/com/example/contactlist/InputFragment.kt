package com.example.contactlist


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*


import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment



/**
 * Startfragment, wo eingetragene Kontakte aufgelistet, gesucht, oder bearbeitet werden.
 * Jegliche weitere Interaktion wird dann zu -> Blankfragment weitergeleitet
 *
 *
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
class InputFragment : Fragment() {
   lateinit var field: EditText
   lateinit var press: Button
   lateinit var del: Button
   lateinit var refresh: Button
   lateinit var upd:Button
  // lateinit var img: ImageView
   lateinit var entries:ListView
   lateinit var mContext:Context

  //  https://androidexample.com/How_To_Create_A_Custom_Listview_-_Android_Example/index.php?view=article_discription&aid=67&aaid=92


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //https://medium.com/@xabaras/setting-windowsoftinputmode-for-a-single-fragment-c0b386463986
        //softInputMode = activity?.window?.attributes?.softInputMode
        //activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.activity_main, container, false) as ViewGroup
        press = view.findViewById<View>(R.id.searchfield) as Button
        field = view.findViewById<View>(R.id.searchtext) as EditText
        entries = view.findViewById<View>(R.id.listentries) as ListView
        del = view.findViewById<View>(R.id.delete) as Button
        refresh =  view.findViewById<View>(R.id.refresh) as Button
        upd =  view.findViewById<View>(R.id.update) as Button

        //https://developer.android.com/guide/topics/ui/dialogs
        //arg0 is the listview, arg1 the layout enclosing the listview
        entries.setOnItemLongClickListener{ arg0, arg1, pos, id ->
            val builder = AlertDialog.Builder(mContext)

            val selectedItem = arg0.getItemAtPosition(pos) as Contact

            builder.setTitle(selectedItem.surname+" "+selectedItem.famname)
            builder.setItems(R.array.Options) { dialog, which ->
                // The 'which' argument contains the index position of the selected item
                //https://stackoverflow.com/questions/15762905/how-can-i-display-a-list-view-in-an-android-alert-dialog

                when(which){
                    0->{ //1st element of Options array
                        //Toast.makeText(mContext, "edit selected" , Toast.LENGTH_SHORT).show()
                        (activity as MainActivity).passData(selectedItem)
                         }
                    1->{//2nd element of Options array
                        //Toast.makeText(mContext, "delete selected" , Toast.LENGTH_SHORT).show()
                        (activity as MainActivity).delcontact(selectedItem)
                        val newlist =  (activity as MainActivity).getcontacts()
                        entries.adapter = CustomAdapter(mContext,R.layout.fragment_input,newlist)

                    }

                }
            }

           builder.show()
          // builder.create()

           //https://workingdev.net/android,/kotlin/2018/08/01/handling-clicks-and-long-clicks.html
           true//set true, because we only want this listener to react
        }

        /*entries.setOnItemClickListener{ parent, view, position, id ->
            val selectedItem =  parent.getItemAtPosition(position) as Contact


            (activity as MainActivity).details(selectedItem)
        }*/

        var list = mutableListOf<Contact>()


        val lists = (activity as MainActivity).getcontacts()

        entries.adapter = CustomAdapter(mContext,R.layout.fragment_input,lists)

        //because the xml-layout onClick Method only looks on activity and not on fragment, we have to implement onClick ourselves here
        press!!.setOnClickListener {
          //field!!.hint = "OK "
          (activity as MainActivity).gotoform()
       }

       del!!.setOnClickListener {

           (activity as MainActivity).deltable()
           val newlist =  (activity as MainActivity).getcontacts()

           entries.adapter = CustomAdapter(mContext,R.layout.fragment_input,newlist)
       }

       refresh!!.setOnClickListener {

         actualize()

       }

        upd!!.setOnClickListener {
            val newlist =  (activity as MainActivity).getcontacts()
            entries.adapter = CustomAdapter(mContext,R.layout.fragment_input,newlist)
        }

        //https://guides.codepath.com/android/Basic-Event-Listeners#edittext-common-listeners
       field!!.addTextChangedListener(object : TextWatcher {
           override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
               // Fires right as the text is being changed (even supplies the range of text)
               if(s.toString()==""){
                   val newlist =  (activity as MainActivity).getcontacts()
                   entries.adapter = CustomAdapter(mContext,R.layout.fragment_input,newlist)

               }
               else{
                   val con = Contact(s.toString(),s.toString())
                   val newlist = (activity as MainActivity).search(con)



                   entries.adapter = CustomAdapter(mContext,R.layout.fragment_input,newlist)
               }

           }

           override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
               // Fires right before text is changing
           }

           override fun afterTextChanged(s: Editable) {
               // Fires right after the text has changed
               //tvDisplay.setText(s.toString())
           }
       })


        return view
    }


    fun actualize(){
        val newlist =  (activity as MainActivity).getcontacts()
        //  entries = view.findViewById<View>(R.id.listentries) as ListView
        entries.adapter = CustomAdapter(mContext,R.layout.fragment_input,newlist)

    }

    fun updatelist(list:List<Contact>){
        entries.adapter = CustomAdapter(mContext,R.layout.fragment_input,list)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment InputFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = InputFragment().apply {
                val fragment = InputFragment()
                val args = Bundle()

                fragment.arguments = args
                return fragment
            }
    }

    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(uri: Uri?)
    }

    override fun onDestroy() {
        super.onDestroy()
     //   softInputMode?.let { activity?.window?.setSoftInputMode(it) }
    }


}
