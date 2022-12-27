package com.example.contactlist

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView


//https://devofandroid.blogspot.com/2018/03/custom-listview-with-item-click.html
class CustomAdapter(var mCtx: Context, var resource: Int, var items: List<Contact>)
    : ArrayAdapter<Contact>(mCtx, resource, items){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)

        var imwidth = 0
        var imheight = 0

        val view : View = layoutInflater.inflate(resource, null)
//http://www.sherif.mobi/2013/01/how-to-get-widthheight-of-view.html

        val index:TextView = view.findViewById(R.id.indexnr)
        val foto:ImageView = view.findViewById(R.id.indeximage)

        val entry: TextView = view.findViewById(R.id.indexname)

        var mitems:Contact = items[position]
        index.text = position.toString()

        if(mitems.spanStrings.length>0){//show highlighted text if it exists
            entry.text = mitems.spanStrings
        }
        else
            entry.text = mitems.surname+"\n" + mitems.famname



        if(mitems.image!=null){
            // TODO: gets called all the time even when user is not in that fragment
            var bmp = BitmapFactory.decodeByteArray(mitems.image, 0, (mitems.image)!!.size)
            bmp = Bitmap.createScaledBitmap(bmp, 100, 100, true)// TODO: resize image relative
            foto.setImageBitmap(bmp)
        }

        //foto.background
        //foto.setBackgroundColor(Color.parseColor("#008577"))
        //foto.setColorFilter(color.white,android.graphics.PorterDuff.Mode.DST_IN);
        //foto.setBackgroundColor(getColor(mCtx, color.colorAccent))

        return view
    }

}