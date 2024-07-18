package com.example.contactlist

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.contactlist.BlankFragment.OnFragmentInteractionListener

/**
 * hier werden die Daten fuer die Kontakte erstellen/bearbeiten vom User eingegeben
 *
 * contains necessary input data from user for creating/updating a contact entry
 */
class DetailsFragment : Fragment() {


    var c_id:Int = 0
    lateinit var surname:TextView
    lateinit var familyname:TextView
    lateinit var adress:TextView
    lateinit var email:TextView
    lateinit var telephone:TextView

    lateinit var linear:LinearLayout
    lateinit var back:ImageView
    lateinit var edit:ImageView

    lateinit var contact:Contact

    var imgheight = 100
    var imgwidth = 100

    //lateinit var tablayout:TabLayout

    lateinit var edited:TextView

    lateinit var filename:Uri

    var updated:Boolean = false

    private var mListener: OnFragmentInteractionListener? = null

    private var softInputMode:Int? = null

    lateinit var mContext:Context

    /*
    fun newInstance(): DetailsFragment? {
        val fragment = DetailsFragment()
        val args = Bundle()
        fragment.arguments = args
        return fragment
    }*/

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is BlankFragment.OnFragmentInteractionListener) {
            context


        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnFragmentInteractionListener"
            )
        }

        mContext=context
    }

    //https://guides.codepath.com/android/Creating-and-Using-Fragments

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? { // Inflate the layout for this fragment
        val view =
            inflater.inflate(R.layout.fragment_details, container, false) as ViewGroup

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: set width & height properly
        //val imageLayer = view.findViewById(R.id.imageLayer) as ConstraintLayout

        // imgwidth = imageLayer.width //0
        //  imgheight = imageLayer.height //0

        linear = view.findViewById(R.id.detailslinear) as LinearLayout

        back = view.findViewById(R.id.left)
        back.setOnClickListener(){

            (activity as MainActivity).gofromdetail()
        }

        edit = view.findViewById(R.id.right)
        edit.setOnClickListener(){

            (activity as MainActivity).passData(contact)//TODO: pass contact object

        }

        /*
        tablayout = view.findViewById(R.id.tablayout) as TabLayout
        tablayout.addTab(tablayout.newTab().setText("Tab 1"))
        tablayout.addTab(tablayout.newTab().setText("Tab 2"))
        tablayout.addTab(tablayout.newTab().setText("Tab 3"))
*/
    }


    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onDestroy() {
        super.onDestroy()
        //  softInputMode?.let { activity?.window?.setSoftInputMode(it) }
    }

    public fun cancel(){
        val surname = view?.findViewById(R.id.surname) as TextView
        surname.setText(null) //clear the value of the text
        surname.setHint(R.string.insert)//then refill the hint with initial value

        val familyname = view?.findViewById(R.id.familyname) as TextView
        familyname.setText(null)
        familyname.setHint(R.string.insert)

        val adress = view?.findViewById(R.id.adress) as TextView
        adress.setText(null)
        adress.setHint(R.string.insert)

        val email = view?.findViewById(R.id.email) as TextView
        email.setText(null)
        email.setHint(R.string.insert)

        val telephone = view?.findViewById(R.id.tel) as TextView
        telephone.setText(null)
        telephone.setHint(R.string.insert)


        val edited = view?.findViewById(R.id.textViewEdit) as TextView
        edited.setText(null)
        edited.setHint("")

    }

    fun receiveData(con:Contact){

        val surnamecurrent = view?.findViewById(R.id.surname) as TextView
        surnamecurrent?.setText(con.surname)

        val famnamecurrent = view?.findViewById(R.id.familyname) as TextView
        famnamecurrent?.setText(con.famname)

        val adresscurrent = view?.findViewById(R.id.adress) as TextView
        adresscurrent?.setText(con.adress)

        val emailcurrent = view?.findViewById(R.id.email) as TextView
        emailcurrent?.setText(con.userEmail)

        val telcurrent = view?.findViewById(R.id.tel) as TextView
        telcurrent?.setText(con.telephone)

        val birthdate = view?.findViewById(R.id.birth) as TextView
        birthdate?.setText(con.birthdate)

        val editcurrent =  view?.findViewById(R.id.textViewEdit) as TextView
        editcurrent?.setText(con.edited)

        val imgcurrent = view?.findViewById(R.id.imageView2) as ImageView

        if(con.image !=null) {
            val bitmap:Bitmap = BitmapFactory.decodeByteArray(con.image, 0, con.image!!.size)

            imgcurrent?.setImageBitmap(bitmap)
        }

        contact = con
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
        fun newInstance() = DetailsFragment().apply {
            val fragment = DetailsFragment()
            val args = Bundle()

            fragment.arguments = args
            return fragment
        }
    }

}