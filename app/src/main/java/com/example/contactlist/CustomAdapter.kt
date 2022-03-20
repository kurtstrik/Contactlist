package com.example.contactlist

import android.R.attr.data
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.graphics.toColor
import com.example.contactlist.R.color


var counter = 1
//https://devofandroid.blogspot.com/2018/03/custom-listview-with-item-click.html
class CustomAdapter(var mCtx: Context, var resource: Int, var items: List<Contact>)
    : ArrayAdapter<Contact>(mCtx, resource, items){




    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)

        val view : View = layoutInflater.inflate(resource, null)
        val index:TextView = view.findViewById(R.id.indexnr)
        val foto:ImageView = view.findViewById(R.id.indeximage)


        val entry: TextView = view.findViewById(R.id.indexname)

        var mitems:Contact = items[position]
        //index.text = mitems.id.toString()
        index.text = position.toString()
        //index.text = counter.toString()
       // counter++

        if(mitems.spanStrings.length>0){
            entry.text = mitems.spanStrings
        }
        else
            entry.text = mitems.surname+"\n" + mitems.famname

        // TODO: resize image to fit uniformingly
        if(mitems.image!=null){
            // TODO: gets called all the time even when user is not in that fragment
            val bmp = BitmapFactory.decodeByteArray(mitems.image, 0, (mitems.image)!!.size);

            foto.setImageBitmap(bmp);

        }

        //foto.background
       // foto.setBackgroundColor(Color.parseColor("#008577"))
        //foto.setColorFilter(color.white,android.graphics.PorterDuff.Mode.DST_IN);
        //foto.setBackgroundColor(getColor(mCtx, color.colorAccent))
        return view
    }



}