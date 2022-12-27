package com.example.contactlist

import android.content.ClipData
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


//https://developer.android.com/guide/fragments/communicate
class ItemViewModel : ViewModel() {
    private val mutableSelectedItem = MutableLiveData<ClipData.Item>()
    val selectedItem: LiveData<ClipData.Item> get() = mutableSelectedItem

    fun selectItem(item: ClipData.Item) {
        mutableSelectedItem.value = item
    }
}
