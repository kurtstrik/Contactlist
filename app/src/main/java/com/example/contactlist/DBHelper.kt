package com.example.contactlist

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import java.io.ByteArrayOutputStream


private const val DATABASE_NAME = "MyDBName.db"
private const val CONTACTS_TABLE_NAME = "contacts"
private const val CONTACTS_COLUMN_ID = "id"
private const val CONTACTS_COLUMN_SURNAME = "surname"
private const val CONTACTS_COLUMN_FAMILYNAME = "familyname"
private const val CONTACTS_COLUMN_ADRESS = "adress"
private const val CONTACTS_COLUMN_EMAIL = "email"
private const val CONTACTS_COLUMN_TEL = "telephone"
private const val CONTACTS_COLUMN_BIRTHDATE = "birthdate"
private const val CONTACTS_COLUMN_EDITDATE = "edited"
private const val CONTACTS_COLUMN_PASSWORD = "password"
private const val CONTACTS_COLUMN_IMGPATH = "imgpath"
private const val CONTACTS_COLUMN_IMGDATA = "imgdata"

private const val SQL_CREATE_ENTRIES = "CREATE TABLE "+ CONTACTS_TABLE_NAME+ " (" +
        CONTACTS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        CONTACTS_COLUMN_SURNAME + " VARCHAR(256), " +
        CONTACTS_COLUMN_FAMILYNAME +" VARCHAR(256), " +
        CONTACTS_COLUMN_ADRESS +" VARCHAR(256), " +
        CONTACTS_COLUMN_EMAIL +" VARCHAR(256), " +
        CONTACTS_COLUMN_TEL+" VARCHAR(256), " +
        CONTACTS_COLUMN_BIRTHDATE +" VARCHAR(256), " +
        CONTACTS_COLUMN_EDITDATE +" VARCHAR(256), " +
        CONTACTS_COLUMN_PASSWORD +" VARCHAR(256)) "
        //CONTACTS_COLUMN_IMGPATH +" VARCHAR(256), "+
        //CONTACTS_COLUMN_IMGDATA+" BLOB)"

        //"image "+" BLOB)"



private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "+ CONTACTS_TABLE_NAME



//https://www.tutorialspoint.com/how-to-use-a-simple-sqlite-database-in-kotlin-android
//https://www.javatpoint.com/kotlin-android-sqlite-tutorial
//https://abhiandroid.com/database/add-retrieve-image-sqlite-database-example-android-studio.html
class DBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, 1){

    companion object // https://stackoverflow.com/questions/35634803/kotlin-best-way-to-convert-singleton-databasecontroller-in-android
    {
        private var instance: DBHelper? = null
        fun getInstance(context: Context): DBHelper
        {
            if(instance == null)
            {
                instance = DBHelper(context)
            }

            return instance!!
        }
    }

    override fun onCreate(p0: SQLiteDatabase?) {

        p0?.execSQL(SQL_DELETE_ENTRIES)
        p0?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

        p0?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(p0)
    }

    fun addContact(contact: Contact):Long{
        val db = this.writableDatabase


        val contentValues = ContentValues()
        //contentValues.put(CONTACTS_COLUMN_ID,         contact.id)
        contentValues.put(CONTACTS_COLUMN_SURNAME, contact.surname)
        contentValues.put(CONTACTS_COLUMN_FAMILYNAME, contact.famname)
        contentValues.put(CONTACTS_COLUMN_ADRESS, contact.adress)
        contentValues.put(CONTACTS_COLUMN_EMAIL, contact.userEmail)
        contentValues.put(CONTACTS_COLUMN_TEL, contact.telephone)
        contentValues.put(CONTACTS_COLUMN_BIRTHDATE, contact.birthdate)
        contentValues.put(CONTACTS_COLUMN_EDITDATE, contact.edited)
        contentValues.put(CONTACTS_COLUMN_PASSWORD, contact.pass)

      //  if(contact.url!="")
      //      contentValues.put(CONTACTS_COLUMN_IMGPATH, contact.url)
      //  else if(contact.image?.firstOrNull()!=null)
      //      contentValues.put(CONTACTS_COLUMN_IMGDATA , contact.image)

        // Inserting Row
        val success = db.insert(CONTACTS_TABLE_NAME, null, contentValues) //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return success
    }

    fun viewContacts():MutableList<Contact>{
        val conList:MutableList<Contact> = ArrayList()
        val selectQuery = "Select * from $CONTACTS_TABLE_NAME"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
           // db.execSQL(selectQuery) database is empty/null
            return ArrayList()
        }

        if (cursor.moveToFirst()) {
            do {
                    val isnull = cursor.isNull(cursor.getColumnIndex("id"))
                    if(!isnull) {

                        val Id = cursor.getInt(cursor.getColumnIndex("id"))
                        val Surname = cursor.getString(cursor.getColumnIndex("surname"))
                        val Familyname = cursor.getString(cursor.getColumnIndex("familyname"))
                        val Adress = cursor.getString(cursor.getColumnIndex("adress"))
                        val Email = cursor.getString(cursor.getColumnIndex("email"))
                        val tel = cursor.getString(cursor.getColumnIndex("telephone"))
                        val BirthDate = cursor.getString(cursor.getColumnIndex("birthdate"))
                        val EditDate = cursor.getString(cursor.getColumnIndex("edited"))
                        val Password = cursor.getString(cursor.getColumnIndex("password"))
                      //  val ImgPath = cursor.getString(cursor.getColumnIndex(CONTACTS_COLUMN_IMGPATH))

                     //   val ImgData = cursor.getBlob(cursor.getColumnIndex(CONTACTS_COLUMN_IMGDATA))

                        val con = Contact(
                            Id,
                            Surname,
                            Familyname,
                            Adress,
                            Email,
                            tel,
                            BirthDate,
                            EditDate,
                            Password

                        )
                        conList.add(con)
                    }
            } while (cursor.moveToNext())
        }
        cursor.close()

        db.close()
        return conList
    }

    fun searchContact(contact: Contact):List<Contact>{
        val conList:MutableList<Contact> = ArrayList()
        val temp = Array(1){contact.surname+"%"}
       // val temp = Array(2){contact.surname+"%"; "%"+contact.famname+"%"} falls Familienname auch beachtet wird

        //val selectQuery = "Select * from $CONTACTS_TABLE_NAME where $CONTACTS_COLUMN_SURNAME like "+temp
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(
                "Select * from $CONTACTS_TABLE_NAME where $CONTACTS_COLUMN_SURNAME like ?",
                temp
            )
            //cursor = db.rawQuery("Select * from $CONTACTS_TABLE_NAME where $CONTACTS_COLUMN_SURNAME like ? or $CONTACTS_COLUMN_FAMILYNAME like ? ", temp) falls Familienname auch beachtet wird


        }catch (e: SQLiteException) {
            // db.execSQL(selectQuery) database is empty/null
            return ArrayList()
        }


        if (cursor.moveToFirst()) {
            do {
                val isnull = cursor.isNull(cursor.getColumnIndex("id"))
                if(!isnull) {

                    val Id = cursor.getInt(cursor.getColumnIndex("id"))
                    val Surname = cursor.getString(cursor.getColumnIndex("surname"))
                    val Familyname = cursor.getString(cursor.getColumnIndex("familyname"))
                    val Adress = cursor.getString(cursor.getColumnIndex("adress"))
                    val Email = cursor.getString(cursor.getColumnIndex("email"))
                    val tel = cursor.getString(cursor.getColumnIndex("telephone"))
                    val BirthDate = cursor.getString(cursor.getColumnIndex("birthdate"))
                    val EditDate = cursor.getString(cursor.getColumnIndex("edited"))
                    val Password = cursor.getString(cursor.getColumnIndex("password"))


                    val con = Contact(
                        Id,
                        Surname,
                        Familyname,
                        Adress,
                        Email,
                        tel,
                        BirthDate,
                        EditDate,
                        Password
                    )
                    conList.add(con)
                }
            } while (cursor.moveToNext())
        }
        cursor.close()

        db.close()
        return conList

    }

    //method to update data
    fun updateContact(contact: Contact):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(CONTACTS_COLUMN_SURNAME, contact.surname)
        contentValues.put(CONTACTS_COLUMN_FAMILYNAME, contact.famname)
        contentValues.put(CONTACTS_COLUMN_ADRESS, contact.adress)
        contentValues.put(CONTACTS_COLUMN_EMAIL, contact.userEmail)
        contentValues.put(CONTACTS_COLUMN_TEL, contact.telephone)
        contentValues.put(CONTACTS_COLUMN_BIRTHDATE, contact.birthdate)
        contentValues.put(CONTACTS_COLUMN_EDITDATE, contact.edited)
        contentValues.put(CONTACTS_COLUMN_PASSWORD, contact.pass)

       // val upd = "UPDATE "+ CONTACTS_TABLE_NAME+" SET "//!!!!!!!
        // Updating Row
        val success = db.update(CONTACTS_TABLE_NAME, contentValues, "id=" + contact.id, null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }

    fun getBytes(bitmap: Bitmap): ByteArray? {
        val stream = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.PNG, 0, stream)

        return stream.toByteArray()
    }



    /*
    @Throws(SQLiteException::class)
    fun addEntry(name: String?, image: ByteArray?) {
        val database = this.writableDatabase
        val cv = ContentValues()
        cv.put(KEY_IMAGE, image)
        database.insert(DB_TABLE, null, cv)
    }*/


    fun deleteTable(){
        val db = this.writableDatabase

        db.execSQL(SQL_DELETE_ENTRIES)
        db.execSQL(SQL_CREATE_ENTRIES)
        db.close()

    }

    //method to delete data
    fun deleteContact(contact: Contact):Int{
        val db = this.writableDatabase

        // Deleting Row
        val success = db.delete(CONTACTS_TABLE_NAME, "id=" + contact.id, null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }

}