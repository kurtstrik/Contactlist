package com.example.contactlist

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager


class BlankFragment : Fragment() {

    var cancel: Button? = null
    var enter: Button? = null


    /*

    private var surname: EditText? = null
    private var familyname: EditText? = null
    private var adress: EditText? = null
    private var email: EditText? = null
    private var telephone: EditText? = null
    private var birthdate: DatePicker? = null
    private var edited: TextView? = null
    private var password: EditText? = null
*/
    private var mListener: OnFragmentInteractionListener? = null

    private var softInputMode:Int? = null

    lateinit var mContext:Context

    fun newInstance(): BlankFragment? {
        val fragment = BlankFragment()
        val args = Bundle()
        fragment.arguments = args
        return fragment
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //https://medium.com/@xabaras/setting-windowsoftinputmode-for-a-single-fragment-c0b386463986
    //    softInputMode = activity?.window?.attributes?.softInputMode
    //    activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        val view =
            inflater.inflate(R.layout.fragment_blank, container, false) as ViewGroup
/*
        surname = view.findViewById<View>(R.id.surname) as EditText
        familyname = view.findViewById<View>(R.id.familyname) as EditText
        adress = view.findViewById<View>(R.id.adress) as EditText
        email = view.findViewById<View>(R.id.email) as EditText
        telephone = view.findViewById<View>(R.id.tel) as EditText
        birthdate = view.findViewById<View>(R.id.datepick) as DatePicker
        edited = view.findViewById<View>(R.id.textViewEdit) as TextView
        password = view.findViewById<View>(R.id.password) as EditText
*/




        enter = view.findViewById<View>(R.id.enter) as Button
        enter!!.setOnClickListener {
          val surname = view.findViewById<View>(R.id.surname) as EditText
          val familyname = view.findViewById<View>(R.id.familyname) as EditText
          val adress = view.findViewById<View>(R.id.adress) as EditText
          val email = view.findViewById<View>(R.id.email) as EditText
          val telephone = view.findViewById<View>(R.id.tel) as EditText
          val birthdate = view.findViewById<View>(R.id.datepick) as DatePicker
          val edited = view.findViewById<View>(R.id.textViewEdit) as TextView
          val password = view.findViewById<View>(R.id.password) as EditText


           val dateString =""+ birthdate.dayOfMonth+"."+birthdate.month+"."+birthdate.year

           // val mPager = view.findViewById<View>(R.id.pager) as ViewPager
           // val page = mPager.adapter.

            val result = (activity as MainActivity).addcontact(Contact(0,surname.text.toString(),familyname.text.toString(), adress.text.toString(), email.text.toString(),telephone.text.toString(),dateString, edited.text.toString(), password.text.toString()))

            if(result==1L)
                Toast.makeText(mContext, R.string.added , Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(mContext, R.string.added , Toast.LENGTH_SHORT).show()

        }
        cancel = view.findViewById<View>(R.id.cancel) as Button
        cancel!!.setOnClickListener {

            val surname = view.findViewById<View>(R.id.surname) as EditText
            surname.setText(null) //clear the value of the text
            surname.setHint(R.string.insert)//then refill the hint with initial value

            val familyname = view.findViewById<View>(R.id.familyname) as EditText
            familyname.setText(null)
            familyname.setHint(R.string.insert)

            val adress = view.findViewById<View>(R.id.adress) as EditText
            adress.setText(null)
            adress.setHint(R.string.insert)

            val email = view.findViewById<View>(R.id.email) as EditText
            email.setText(null)
            email.setHint(R.string.insert)

            val telephone = view.findViewById<View>(R.id.tel) as EditText
            telephone.setText(null)
            telephone.setHint(R.string.insert)


            val edited = view.findViewById<View>(R.id.textViewEdit) as TextView
            edited.setText(null)
            edited.setHint("")

            val password = view.findViewById<View>(R.id.password) as EditText
            password.setText(null)
            password.setHint(R.string.insert)

            (activity as MainActivity).backbutton()

        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is OnFragmentInteractionListener) {
            context


        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnFragmentInteractionListener"
            )
        }

        mContext=context
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onDestroy() {
        super.onDestroy()
      //  softInputMode?.let { activity?.window?.setSoftInputMode(it) }
    }


    interface OnFragmentInteractionListener {

        fun onFragmentInteraction(uri: Uri?)
    }
}