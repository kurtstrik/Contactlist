package com.example.contactlist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream

/**
 * hier werden die Daten fuer die Kontakte erstellen/bearbeiten vom User eingegeben
 *
 * contains necessary input data from user for creating/updating a contact entry
 */
class BlankFragment : Fragment() {

    lateinit var cancel:Button
    lateinit var enter:Button


    var c_id:Int = 0
    lateinit var surname:EditText
    lateinit var familyname:EditText
    lateinit var adress:EditText
    lateinit var email:EditText
    lateinit var telephone:EditText

    lateinit var birthdate:DatePicker
    lateinit var linear:LinearLayout
    lateinit var img:ImageButton
    var imgheight = 100
    var imgwidth = 100


    lateinit var edited:TextView
    lateinit var password:EditText
    lateinit var filename:Uri

    var updated:Boolean = false


    //https://stackoverflow.com/questions/61455381/how-to-replace-startactivityforresult-with-activity-result-apis



    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data



           if(data?.data!=null) {

               val bitmapfile = context?.contentResolver?.openInputStream(data?.data)
               filename = data?.data

               val draw = BitmapDrawable(this.resources, bitmapfile)



               if (draw != null) {

                    Glide.with(this).load(draw).override(150,150).circleCrop().into(img)

                   //img.setImageDrawable(BitmapDrawable(resources,decodeSampledBitmapFromResource(draw, R.id.imageButton, 100, 100)))
               }
               //img.setImageDrawable(draw)

           }

        }
    }

    fun openSomeActivityForResult() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        resultLauncher.launch(gallery)
    }


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
        linear = view.findViewById(R.id.blanklinear) as LinearLayout

       img = view.findViewById(R.id.imageButton)

        img.setOnClickListener{
        //https://www.tutorialspoint.com/how-to-pick-an-image-from-an-image-gallery-on-android-using-kotlin

            openSomeActivityForResult()
        }

        enter = view.findViewById(R.id.enter) as Button
        enter!!.setOnClickListener {
          surname = view.findViewById(R.id.surname) as EditText
          familyname = view.findViewById(R.id.familyname) as EditText
          adress = view.findViewById(R.id.adress) as EditText
          email = view.findViewById(R.id.email) as EditText
          telephone = view.findViewById(R.id.tel) as EditText
          birthdate = view.findViewById(R.id.datepick) as DatePicker
          edited = view.findViewById(R.id.textViewEdit) as TextView
          password = view.findViewById(R.id.password) as EditText




           val dateString =""+ birthdate.dayOfMonth+"."+(birthdate.month+1)+"."+birthdate.year

            lateinit var contactprop:Contact

            if(img!=null){

                //https://stackoverflow.com/questions/6341977/convert-drawable-to-blob-datatype
                val hold:BitmapDrawable? = img.drawable as? BitmapDrawable
                val bitmap:Bitmap? = hold?.bitmap
                val stream = ByteArrayOutputStream()
                bitmap?.compress(Bitmap.CompressFormat.JPEG,100,stream)

                val imgInByte:ByteArray = stream.toByteArray()

                contactprop = Contact(
                    c_id, surname.text.toString(),
                    familyname.text.toString(),
                    adress.text.toString(),
                    email.text.toString(),
                    telephone.text.toString(),
                    dateString,
                    edited.text.toString(),
                    password.text.toString(),
                    imgInByte,
                )

            }
            else {
                contactprop = Contact(
                    c_id, surname.text.toString(),
                    familyname.text.toString(),
                    adress.text.toString(),
                    email.text.toString(),
                    telephone.text.toString(),
                    dateString,
                    edited.text.toString(),
                    password.text.toString()
                )
            }
           if(updated) {
               val result = (activity as MainActivity).updatecontact(contactprop)

               updated = false//!!!was tun wenn wiederholte Eingabe erfolgt? update nur false wenn screen verlassen wird

               if (result == 1)
                   Toast.makeText(mContext, R.string.updated, Toast.LENGTH_SHORT).show()
               else
                   Toast.makeText(mContext, R.string.not_updated, Toast.LENGTH_SHORT).show()

               //(activity as MainActivity).backbutton()

           }
            else {

               val result = (activity as MainActivity).addcontact(contactprop)

               if (result != 0L) {
                   Toast.makeText(mContext, R.string.added, Toast.LENGTH_SHORT).show()
                   (activity as MainActivity).backbutton()
               }
               else
                   Toast.makeText(mContext, R.string.not_added, Toast.LENGTH_SHORT).show()
           }
        }
        cancel = view.findViewById(R.id.cancel) as Button
        cancel!!.setOnClickListener {

            surname = view.findViewById(R.id.surname) as EditText
            surname.setText(null) //clear the value of the text
            surname.setHint(R.string.insert)//then refill the hint with initial value

            familyname = view.findViewById(R.id.familyname) as EditText
            familyname.setText(null)
            familyname.setHint(R.string.insert)

            adress = view.findViewById(R.id.adress) as EditText
            adress.setText(null)
            adress.setHint(R.string.insert)

            email = view.findViewById(R.id.email) as EditText
            email.setText(null)
            email.setHint(R.string.insert)

            telephone = view.findViewById(R.id.tel) as EditText
            telephone.setText(null)
            telephone.setHint(R.string.insert)


            edited = view.findViewById(R.id.textViewEdit) as TextView
            edited.setText(null)
            edited.setHint("")

            password = view.findViewById(R.id.password) as EditText
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

    public fun cancel(){
        val surname = view?.findViewById(R.id.surname) as EditText
        surname.setText(null) //clear the value of the text
        surname.setHint(R.string.insert)//then refill the hint with initial value

        val familyname = view?.findViewById(R.id.familyname) as EditText
        familyname.setText(null)
        familyname.setHint(R.string.insert)

        val adress = view?.findViewById(R.id.adress) as EditText
        adress.setText(null)
        adress.setHint(R.string.insert)

        val email = view?.findViewById(R.id.email) as EditText
        email.setText(null)
        email.setHint(R.string.insert)

        val telephone = view?.findViewById(R.id.tel) as EditText
        telephone.setText(null)
        telephone.setHint(R.string.insert)


        val edited = view?.findViewById(R.id.textViewEdit) as TextView
        edited.setText(null)
        edited.setHint("")

        val password = view?.findViewById(R.id.password) as EditText
        password.setText(null)
        password.setHint(R.string.insert)



    }

    public fun updating(con:Contact){

        updated = true

        c_id = con.id//ID handling?

        val surnamecurrent = view?.findViewById(R.id.surname) as EditText
        surnamecurrent?.setText(con.surname)

        val famnamecurrent = view?.findViewById(R.id.familyname) as EditText
        famnamecurrent?.setText(con.famname)

        val adresscurrent = view?.findViewById(R.id.adress) as EditText
        adresscurrent?.setText(con.adress)

        val emailcurrent = view?.findViewById(R.id.email) as EditText
        emailcurrent?.setText(con.userEmail)

        val telcurrent = view?.findViewById(R.id.tel) as EditText
        telcurrent?.setText(con.telephone)

      //

    //   birthcurrent.dayOfMonth = con.birthdate.
    //       birthcurrent.month =
    //  birthcurrent.year =

        /*val strs = con.birthdate.split(".").toTypedArray() Problem!
        var birthcurrent = view?.findViewById<DatePicker>(R.id.datepick)
        birthcurrent?.updateDate(strs[2].toInt(), strs[1].toInt(), strs[0].toInt())


        val editcurrent = view?.findViewById<TextView>(R.id.textViewEdit) Problem!
        editcurrent?.setText(con.edited)

        password?.setText(con.pass) Problem!
*/
    }

    public fun isonUpdate(bool:Boolean){
        updated = bool
    }


    interface OnFragmentInteractionListener {

        fun onFragmentInteraction(uri: Uri?)
    }

    //https://developer.android.com/topic/performance/graphics/load-bitmap#kotlin
    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    fun decodeSampledBitmapFromResource(
        res: Resources,
        resId: Int,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeResource(res, resId, this)

            // Calculate inSampleSize
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)

            // Decode bitmap with inSampleSize set
            inJustDecodeBounds = false

            BitmapFactory.decodeResource(res, resId, this)
        }
    }


}