package com.example.contactlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

var counter = 1
//https://devofandroid.blogspot.com/2018/03/custom-listview-with-item-click.html
class CustomAdapter(var mCtx:Context , var resource:Int,var items:List<Contact>)
    : ArrayAdapter<Contact>( mCtx , resource , items ){




    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)

        val view : View = layoutInflater.inflate(resource , null )
        val index:TextView = view.findViewById(R.id.indexnr)
        //val foto:ImageView = view.findViewById(R.id.indeximage)
        val entry: TextView = view.findViewById(R.id.indexname)

        var mitems:Contact = items[position]
        //index.text = mitems.id.toString()
        index.text = position.toString()
        //index.text = counter.toString()
       // counter++

        entry.text = mitems.surname+"\n" + mitems.famname
        return view
    }



}