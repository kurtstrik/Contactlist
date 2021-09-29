package com.example.contactlist

//class Contact(var id: Int, val surname:String , val famname:String, val adress:String, val userEmail: String, val telephone:String, val birthdate:String, val edited:String, val pass:String, val uri:String){
class Contact{

    var id = 0
    var surname:String     = ""
    var famname: String    = ""
    var adress: String     = ""
    var userEmail: String  = ""
    var telephone: String  = ""
    var birthdate: String  = ""
    var edited: String     = ""
    var pass: String       = ""
    var url: String        = ""

    var image: ByteArray? = null


    constructor(surname:String){
        this.surname = surname


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
        url:String
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

    constructor( cId: Int,
                 surname: String,
                 famname: String,
                 adress: String,
                 userEmail: String,
                 telephone: String,
                 birthdate: String,
                 edited: String,
                 pass: String,
                 image:ByteArray
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

    //constructor(var id: Int, val surname:String , val famname:String, val adress:String, val userEmail: String, val telephone:String, val birthdate:String, val edited:String, val pass:String)
    //constructor(id:Int, surname:String, famname:String, adress:String, userEmail:String, telephone: String, birthdate: String, edited: String, pass: String):this(id,surname, famname, adress, userEmail, telephone, birthdate, edited, pass, "")

}