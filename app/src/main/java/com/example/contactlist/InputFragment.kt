package com.example.contactlist


import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
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

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.activity_main, container, false) as ViewGroup
        press = view.findViewById<View>(R.id.searchfield) as Button
        field = view.findViewById<View>(R.id.searchtext) as EditText
        entries = view.findViewById<View>(R.id.listentries) as ListView
        del = view.findViewById<View>(R.id.delete) as Button
        //refresh =  view.findViewById<View>(R.id.refresh) as Button
        //upd =  view.findViewById<View>(R.id.update) as Button

        //https://stackoverflow.com/questions/46011223/how-to-get-the-selected-item-from-listview-in-kotlin
        //https://stackoverflow.com/questions/13122683/how-to-implement-onclick-in-a-listview
        entries.onItemClickListener =
            OnItemClickListener { adapter, arg1, position, id ->

                var imm: InputMethodManager = this.context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view?.windowToken, 0)
                val select = adapter.getItemAtPosition(position) as Contact
                (activity as MainActivity).detailsData(select)
            }

        //https://developer.android.com/guide/topics/ui/dialogs
        //arg0 is the listview, arg1 the layout enclosing the listview
        entries.setOnItemLongClickListener{ arg0, arg1, pos, id ->
            val builder = AlertDialog.Builder(mContext)

            val selectedItem = arg0.getItemAtPosition(pos) as Contact

            builder.setTitle(selectedItem.surname + " " + selectedItem.famname)
            builder.setItems(R.array.Options) { dialog, which ->
                // The 'which' argument contains the index position of the selected item
                //https://stackoverflow.com/questions/15762905/how-can-i-display-a-list-view-in-an-android-alert-dialog

                when(which){
                    0 -> { //1st element of Options array - edit
                        var imm:InputMethodManager = this.context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view?.windowToken, 0)
                        (activity as MainActivity).passData(selectedItem)
                    }
                    1 -> {//2nd element of Options array - delete
                        (activity as MainActivity).delcontact(selectedItem)
                        getList()

                    }
                }
            }

           builder.show()

           //https://workingdev.net/android,/kotlin/2018/08/01/handling-clicks-and-long-clicks.html
           true//set true, because we only want this listener to react
        }

        getList()

       // Thread {//TODO:check it out  https://developer.android.com/guide/components/processes-and-threads

       // }.start()

        //because the xml-layout onClick Method only looks on activity and not on fragment, we have to implement onClick ourselves here
       press!!.setOnClickListener { (activity as MainActivity).createcontact() }

       del!!.setOnClickListener {

           (activity as MainActivity).deltable()
           getList()
       }

       //refresh!!.setOnClickListener { actualize() }

       // upd!!.setOnClickListener { getList() }

        //https://guides.codepath.com/android/Basic-Event-Listeners#edittext-common-listeners
       field!!.addTextChangedListener(object : TextWatcher {
           override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
               // Fires right as the text is being changed (even supplies the range of text)
               if (s.toString() == "") {

                   val newlist = (activity as MainActivity).getcontacts()
                   entries.adapter = CustomAdapter(mContext, R.layout.fragment_input, newlist)

               } else {
                   val con = Contact(s.toString(), s.toString())
                   val newlist = (activity as MainActivity).search(con) // TODO: search lowercase for uppercase - error
                   entries.adapter = CustomAdapter(mContext, R.layout.fragment_input, newlist)
               }
           }

           override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
               // Fires right before text is changing
           }

           override fun afterTextChanged(s: Editable) {
           // Fires right after the text has changed
           }
       })


        return view
    }

    @Synchronized fun getList(){
        val newlist = (activity as MainActivity).getcontacts()
        entries.adapter = CustomAdapter(mContext, R.layout.fragment_input, newlist)

    }

    fun actualize(){
        val act = activity

        val newlist = (activity as MainActivity).getcontacts()
        entries.adapter = CustomAdapter(mContext, R.layout.fragment_input, newlist)

    }

    fun updatelist(list: List<Contact>){
        entries.adapter = CustomAdapter(mContext, R.layout.fragment_input, list)

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment InputFragment.
         */

        @JvmStatic
        fun newInstance() = InputFragment().apply {
                val fragment = InputFragment()
                val args = Bundle()

                fragment.arguments = args
                return fragment
            }
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(uri: Uri?)
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}




