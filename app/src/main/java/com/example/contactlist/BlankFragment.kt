package com.example.contactlist

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import java.io.ByteArrayOutputStream
import java.util.*

/**
 * hier werden die Daten fuer die Kontakte erstellen/bearbeiten vom User eingegeben
 *
 * contains necessary input data from user for creating/updating a contact entry
 */
class BlankFragment : Fragment(){

    lateinit var cancel:Button
    lateinit var enter:Button

    var date_clicked:Boolean = false


    var c_id:Int = 0
    lateinit var surname:EditText
    lateinit var familyname:EditText
    lateinit var adress:EditText
    lateinit var email:EditText
    lateinit var telephone:EditText
    lateinit var birth:EditText
    lateinit var birthdate:DatePicker
    lateinit var linear:LinearLayout
    lateinit var img:ImageButton
    var imgheight = 150
    var imgwidth = 150


    lateinit var edited:TextView
    lateinit var status:TextView
    lateinit var filename:Uri

    lateinit var delete_surname:ImageView
    lateinit var delete_famname:ImageView
    lateinit var delete_adress:ImageView
    lateinit var delete_email:ImageView
    lateinit var delete_tel:ImageView
    lateinit var delete_date:ImageView


    var updated:Boolean = false

    //https://stackoverflow.com/questions/61455381/how-to-replace-startactivityforresult-with-activity-result-apis


    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val gallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            resultLauncher.launch(gallery)
            // PERMISSION GRANTED
        } else {
            // PERMISSION NOT GRANTED

        }
    }

    private fun startRedPermissionRequest() {
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->


        //val permission = one?.let { checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE) }


        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data

           if(data?.data!=null) {

               val bitmapfile = context?.contentResolver?.openInputStream(data?.data)
               filename = data?.data

               val draw = BitmapDrawable(this.resources, bitmapfile)

               if (draw != null) {
                    /*
                   val dm:DisplayMetrics = DisplayMetrics()
                   getWindowManager().getDefaultDisplay().getMetrics(metrics)
                   val usableHeight: Int = metrics.heightPixels
*/
                    //TODO:readme: https://developer.android.com/develop/ui/views/graphics
                    Glide.with(this).load(draw).override(imgwidth, imgheight).optionalCircleCrop().into(
                        img
                    )

                   //img.setImageDrawable(BitmapDrawable(resources,decodeSampledBitmapFromResource(draw, R.id.imageButton, 100, 100)))
               }
               //img.setImageDrawable(draw)
           }
        }
    }




    fun openSomeActivityForResult() {
        startRedPermissionRequest()
    }


    private var mListener: OnFragmentInteractionListener? = null

    private var softInputMode:Int? = null

    lateinit var mContext:Context


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
            inflater.inflate(R.layout.fragment_blank, container, false) as ViewGroup

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: set width & height properly
        val imageLayer = view.findViewById(R.id.detailstopLayer) as ConstraintLayout


        imgheight = imageLayer.height/2 //0
        imgwidth = imageLayer.height/2

        status = view.findViewById(R.id.editing_status) as TextView

        linear = view.findViewById(R.id.detailslinear) as LinearLayout

        delete_surname = view.findViewById(R.id.delete_surname) as ImageView
        delete_famname = view.findViewById(R.id.delete_famname) as ImageView
        delete_adress  = view.findViewById(R.id.delete_address) as ImageView
        delete_email   = view.findViewById(R.id.delete_email) as ImageView
        delete_tel     = view.findViewById(R.id.delete_tel) as ImageView
        delete_date    = view.findViewById(R.id.delete_date) as ImageView

        delete_surname.visibility=View.INVISIBLE
        delete_famname.visibility=View.INVISIBLE
         delete_adress.visibility=View.INVISIBLE
          delete_email.visibility=View.INVISIBLE
            delete_tel.visibility=View.INVISIBLE
           delete_date.visibility=View.INVISIBLE


        img = view.findViewById(R.id.imageButton)

        //because the xml-layout onClick Method only looks on activity and not on fragment, we have to implement onClick ourselves here
        img.setOnClickListener{


            if(img.drawable.equals(R.mipmap.ic_contact_round)){//TODO:never called - how to compare two drawables

            //if(img.drawable==null){

            //https://www.tutorialspoint.com/how-to-pick-an-image-from-an-image-gallery-on-android-using-kotlin

                openSomeActivityForResult()
            }
            else{
                val builder = AlertDialog.Builder(mContext)

                builder.setItems(R.array.PicOptions) { dialog, which ->
                    // The 'which' argument contains the index position of the selected item
                    //https://stackoverflow.com/questions/15762905/how-can-i-display-a-list-view-in-an-android-alert-dialog

                    when(which){
                        0 -> { //1st element of PicOptions array - add photo
                            openSomeActivityForResult()
                        }
                        1 -> {//2nd element of PicOptions array - remove
                            img.setImageResource(R.mipmap.ic_contact_round)

                        }
                    }
                }

                builder.show()

            }

        }

        birth = view.findViewById(R.id.birth) as EditText

        initbirthlistener()

        //https://stackoverflow.com/questions/5803193/android-disable-soft-keyboard-at-all-edittexts
        // window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        //window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        //birth.inputType = InputType.TYPE_NULL

        birth.addTextChangedListener(object : TextWatcher {

            // Fires right as the text is being changed (even supplies the range of text)
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            // Fires right before text is changing
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            // Fires right after the text has changed
            override fun afterTextChanged(s: Editable) {
                if (s.isBlank()) {
                    delete_date.visibility = View.INVISIBLE
                } else {
                    delete_date.visibility = View.VISIBLE
                }
            }
        })

        delete_date.setOnClickListener{
            if(delete_date.visibility==View.VISIBLE)
                birth.setText(null)}


        surname = view.findViewById(R.id.surname) as EditText

        surname.addTextChangedListener(object : TextWatcher {

            // Fires right as the text is being changed (even supplies the range of text)
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            // Fires right before text is changing
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            // Fires right after the text has changed
            override fun afterTextChanged(s: Editable) {
                if (s.isBlank()) {
                    delete_surname.visibility = View.INVISIBLE
                } else {
                    delete_surname.visibility = View.VISIBLE
                }
            }
        })

        delete_surname.setOnClickListener{
            if(delete_surname.visibility==View.VISIBLE)
                surname.setText(null)
        }

        familyname = view.findViewById(R.id.familyname) as EditText

        familyname.addTextChangedListener(object : TextWatcher {

            // Fires right as the text is being changed (even supplies the range of text)
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            // Fires right before text is changing
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            // Fires right after the text has changed
            override fun afterTextChanged(s: Editable) {
                if (s.isBlank()) {
                    delete_famname.visibility = View.INVISIBLE

                } else {
                    delete_famname.visibility = View.VISIBLE
                }
            }
        })

        delete_famname.setOnClickListener{
            if(delete_famname.visibility==View.VISIBLE)
                familyname.setText(null)
        }

        adress = view.findViewById(R.id.adress) as EditText

        adress.addTextChangedListener(object : TextWatcher {
            // Fires right as the text is being changed (even supplies the range of text)
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            // Fires right before text is changing
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            // Fires right after the text has changed
            override fun afterTextChanged(s: Editable) {
                if (s.isBlank()) {
                    delete_adress.visibility = View.INVISIBLE

                } else {
                    delete_adress.visibility = View.VISIBLE
                }
            }
        })

        delete_adress.setOnClickListener{
            if(delete_adress.visibility==View.VISIBLE)
                adress.setText(null)
        }

        email = view.findViewById(R.id.email) as EditText

        email.addTextChangedListener(object : TextWatcher {
            // Fires right as the text is being changed (even supplies the range of text)
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            // Fires right before text is changing
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            // Fires right after the text has changed
            override fun afterTextChanged(s: Editable) {
                if (s.isBlank()) {
                    delete_email.visibility = View.INVISIBLE

                } else {
                    delete_email.visibility = View.VISIBLE
                }
            }
        })

        delete_email.setOnClickListener{
            if(delete_email.visibility==View.VISIBLE)
                email.setText(null)
        }

        telephone = view.findViewById(R.id.tel) as EditText

        telephone.addTextChangedListener(object : TextWatcher {
            // Fires right as the text is being changed (even supplies the range of text)
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            // Fires right before text is changing
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            // Fires right after the text has changed
            override fun afterTextChanged(s: Editable) {
                if (s.isBlank()) {
                    delete_tel.visibility = View.INVISIBLE

                } else {
                    delete_tel.visibility = View.VISIBLE
                }
            }
        })

        delete_tel.setOnClickListener{
            if(delete_tel.visibility==View.VISIBLE)
                telephone.setText(null)
        }

        enter = view.findViewById(R.id.enter) as Button
        enter!!.setOnClickListener {
            surname = view.findViewById(R.id.surname) as EditText
            familyname = view.findViewById(R.id.familyname) as EditText
            adress = view.findViewById(R.id.adress) as EditText
            email = view.findViewById(R.id.email) as EditText
            telephone = view.findViewById(R.id.tel) as EditText
            birth = view.findViewById(R.id.birth) as EditText
            edited = view.findViewById(R.id.textViewEdit) as TextView

            val tempview:View = layoutInflater.inflate(R.layout.datedialog, null)

            birthdate = tempview.findViewById(R.id.picker) as DatePicker
            var dateString = ""
            /*
            if(date_clicked)
                dateString = "" + birthdate.dayOfMonth + "." + (birthdate.month) + "." + birthdate.year//date was changed

            else*/
                dateString = birth.text.toString()//date was unchanged or not given

            lateinit var contactprop:Contact

            val c:Calendar = Calendar.getInstance()

            val editdate:String = ""+c.get(Calendar.DATE)+"."+ (c.get(Calendar.MONTH)+1)+"."+c.get(
                Calendar.YEAR
            )


            //val editdate = LocalDate.now() not usable because we have API23 Timestamp(Calendar.getInstance().time.time)//https://alvinalexander.com/java/java-timestamp-example-current-time-now/

            if(img!=null){

                //https://stackoverflow.com/questions/6341977/convert-drawable-to-blob-datatype
                val hold:BitmapDrawable? = img.drawable as? BitmapDrawable
                val bitmap:Bitmap? = hold?.bitmap

                val stream = ByteArrayOutputStream()

                /* if we take JPEG the background transparency is set to black by default
                   WEBP is used over PNG because of better compression
                 */
                bitmap?.compress(Bitmap.CompressFormat.WEBP, 50, stream)

                val imgInByte:ByteArray = stream.toByteArray()

                contactprop = Contact(
                    c_id, surname.text.toString(),
                    familyname.text.toString(),
                    adress.text.toString(),
                    email.text.toString(),
                    telephone.text.toString(),
                    dateString,
                    editdate,
                    imgInByte
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
                    editdate
                )
            }
            if(updated) {
                val result = (activity as MainActivity).updatecontact(contactprop)

                if (result == 1) {


                    (activity as MainActivity).actualizing()
                    updated = false
                    reset()
                    //(activity as MainActivity).backbutton()
                    Toast.makeText(mContext, R.string.updated, Toast.LENGTH_SHORT).show()

                }else
                    Toast.makeText(mContext, R.string.not_updated, Toast.LENGTH_SHORT).show()
            }
            else {

                val result = (activity as MainActivity).addcontact(contactprop)

                if (result == -1L) {
                    Toast.makeText(mContext, R.string.not_added, Toast.LENGTH_SHORT).show()
                }
                else {

                    Toast.makeText(mContext, R.string.added, Toast.LENGTH_SHORT).show()
                    //(activity as MainActivity).backbutton()
                    reset()
                }
            }


        }
        cancel = view.findViewById(R.id.cancel) as Button
        cancel!!.setOnClickListener {

            reset()

        }

    }



    private fun initbirthlistener() {


        birth.setOnClickListener{

            //DatePickerDialog not a compatible option because of API versions
            //https://developer.android.com/develop/ui/views/components/dialogs

            var dialog = Dialog(mContext)
            dialog.setContentView(R.layout.datedialog)

            val okay = dialog.findViewById(R.id.button5) as Button

            okay.setOnClickListener{
                val dateTime:Calendar = getDateValue(dialog)
                birth.setText(
                    "" + dateTime.get(Calendar.DAY_OF_MONTH) + "." + (dateTime.get(Calendar.MONTH) + 1) + "." + dateTime.get(
                        Calendar.YEAR
                    )
                )
                dialog.dismiss()
                date_clicked = true
            }

            val no = dialog.findViewById(R.id.button4) as Button

            no.setOnClickListener{
                dialog.dismiss()
            }

            dialog.show()
        }
    }


    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onDestroy() {
        super.onDestroy()
      //  softInputMode?.let { activity?.window?.setSoftInputMode(it) }
    }

    public fun reset(){

        surname = view?.findViewById(R.id.surname) as EditText
        surname.setText(null) //clear the value of the text
        surname.setHint(R.string.insert)//then refill the hint with initial value

        familyname = view?.findViewById(R.id.familyname) as EditText
        familyname.setText(null)
        familyname.setHint(R.string.insert)

        adress = view?.findViewById(R.id.adress) as EditText
        adress.setText(null)
        adress.setHint(R.string.insert)

        email = view?.findViewById(R.id.email) as EditText
        email.setText(null)
        email.setHint(R.string.insert)

        telephone = view?.findViewById(R.id.tel) as EditText
        telephone.setText(null)
        telephone.setHint(R.string.insert)

        birth = view?.findViewById(R.id.birth) as EditText
        birth.setText(null)
        birth.setHint(R.string.insert)


        edited = view?.findViewById(R.id.textViewEdit) as TextView
        edited.setText(null)
        edited.setHint("")

        img.setImageResource(R.mipmap.ic_contact_round)

        date_clicked = false



        var imm:InputMethodManager = this.context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
        (activity as MainActivity).backbutton()

    }

    fun creating(){

        updated = false

       status = view?.findViewById(R.id.editing_status) as TextView
       status.setText(R.string.status_new)

    }

    fun updating(con: Contact){

        updated = true

        status = view?.findViewById(R.id.editing_status) as TextView
        status.setText(R.string.status_edit)

        c_id = con.id//ID handling?

        val surnamecurrent = view?.findViewById(R.id.surname) as EditText
        surnamecurrent?.setText(con.surname)


        if(surnamecurrent?.text.isNotBlank()){
           delete_surname.visibility = View.VISIBLE
        }

        val famnamecurrent = view?.findViewById(R.id.familyname) as EditText
        famnamecurrent?.setText(con.famname)

        if(famnamecurrent?.text.isNotBlank()){
            delete_famname.visibility = View.VISIBLE
        }

        val adresscurrent = view?.findViewById(R.id.adress) as EditText
        adresscurrent?.setText(con.adress)

        if(adresscurrent?.text.isNotBlank()){
            delete_adress.visibility = View.VISIBLE
        }

        val emailcurrent = view?.findViewById(R.id.email) as EditText
        emailcurrent?.setText(con.userEmail)

        if(emailcurrent?.text.isNotBlank()){
            delete_email.visibility = View.VISIBLE
        }

        val telcurrent = view?.findViewById(R.id.tel) as EditText
        telcurrent?.setText(con.telephone)

        if(telcurrent?.text.isNotBlank()){
            delete_tel.visibility = View.VISIBLE
        }

        val birthcurrent = view?.findViewById(R.id.birth) as EditText
        birthcurrent.setText(con.birthdate)

        if(birthcurrent?.text.isNotBlank()){
            delete_date.visibility = View.VISIBLE
        }

        val editcurrent =  view?.findViewById(R.id.textViewEdit) as TextView
        editcurrent?.setText(con.edited)

        val imgcurrent = view?.findViewById(R.id.imageButton) as ImageButton

        if(con.image!=null) {
            val options: BitmapFactory.Options = BitmapFactory.Options()

            val hold: BitmapDrawable? = img.drawable as? BitmapDrawable
            val temp = con.image!!.size
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(con.image, 0, temp, options)
            imgcurrent.setImageBitmap(bitmap)
        }
    }

    private fun getDateValue(dialog: Dialog):Calendar{
           val dat=  dialog.findViewById(R.id.picker) as DatePicker

           var value = Calendar.getInstance()
            value.set(dat.year, dat.month, dat.dayOfMonth)

           return value
    }

    public fun isonUpdate(bool: Boolean){
        updated = bool
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment InputFragment.
         */
        @JvmStatic
        fun newInstance() = BlankFragment().apply {
            val fragment = BlankFragment()
            val args = Bundle()

            fragment.arguments = args
            return fragment
        }
    }


    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri?)
    }


}