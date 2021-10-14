package com.example.contactlist

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan


//class Contact(var id: Int, val surname:String , val famname:String, val adress:String, val userEmail: String, val telephone:String, val birthdate:String, val edited:String, val pass:String, val uri:String){
class Contact{

    var id = 0
    var surname: String = ""
    var famname: String    = ""
    var adress: String     = ""
    var userEmail: String  = ""
    var telephone: String  = ""
    var birthdate: String  = ""
    var edited: String     = ""
    var pass: String       = ""
    var url: String        = ""

    //https://codinginflow.com/tutorials/android/spannablestring-text-color
    var spanStrings: SpannableStringBuilder = SpannableStringBuilder("")//stores the string with highlighted search term

    var image: ByteArray? = null




    constructor(surname: String){
        this.surname = surname


    }

    constructor(surname: String, famname: String){
        this.surname = surname
        this.famname = famname

    }
    constructor(
        cId: Int,
        surname: String,
        famname: String,
        adress: String,
        userEmail: String,
        telephone: String,
        birthdate: String,
        edited: String,
        pass: String

    ){

        this.id = cId
        this.surname = surname
        this.famname = famname
        this.adress = adress
        this.userEmail = userEmail
        this.telephone = telephone
        this.birthdate = birthdate
        this.edited = edited
        this.pass = pass

    }



    constructor(
        cId: Int,
        surname: String,
        famname: String,
        adress: String,
        userEmail: String,
        telephone: String,
        birthdate: String,
        edited: String,
        pass: String,
        url: String
    ){

        this.id = cId
        this.surname = surname
        this.famname = famname
        this.adress = adress
        this.userEmail = userEmail
        this.telephone = telephone
        this.birthdate = birthdate
        this.edited = edited
        this.pass = pass
        this.url = url
    }

    constructor(
        cId: Int,
        surname: String,
        famname: String,
        adress: String,
        userEmail: String,
        telephone: String,
        birthdate: String,
        edited: String,
        pass: String,
        image: ByteArray
    ){

        this.id = cId
        this.surname = surname
        this.famname = famname
        this.adress = adress
        this.userEmail = userEmail
        this.telephone = telephone
        this.birthdate = birthdate
        this.edited = edited
        this.pass = pass
        this.image = image

    }

    fun initspanString(arg: Contact, input:String){

        spanStrings = SpannableStringBuilder(input)

        val markup = ForegroundColorSpan(Color.MAGENTA)

        val search1 = arg.surname
        val search2 = arg.famname

        if(search1.length>0 && surname.contains(search1,true)){
            val start = spanStrings.indexOf(search1)


            spanStrings.setSpan(markup, start, (start+search1.length), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        else if(search2.length>0 && famname.contains(search2,true)){
            val start = spanStrings.indexOf(search2)
            spanStrings.setSpan(markup, start, (start+search2.length), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }


    }

    //constructor(var id: Int, val surname:String , val famname:String, val adress:String, val userEmail: String, val telephone:String, val birthdate:String, val edited:String, val pass:String)
    //constructor(id:Int, surname:String, famname:String, adress:String, userEmail:String, telephone: String, birthdate: String, edited: String, pass: String):this(id,surname, famname, adress, userEmail, telephone, birthdate, edited, pass, "")

}