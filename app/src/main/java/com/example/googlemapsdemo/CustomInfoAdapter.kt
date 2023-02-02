package com.example.googlemapsdemo

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker

class CustomInfoAdapter(context: Context): InfoWindowAdapter {

    private val contentView = (context as Activity).layoutInflater.inflate(R.layout.custom_info_window, null)

    override fun getInfoWindow(marker: Marker): View? {
        renderViews(marker,contentView)
        return contentView
    }

    override fun getInfoContents(marker: Marker): View? {       //get info window retun null -> getInfoContents โดนเรียก -> getInfoWindow return null อีก -> default info จะโดนเรียก
        renderViews(marker,contentView)
        return contentView                                      // อยากเปลี่ยน image ก็ทำที่นี้ได้นะ
    }

    // create funtion to set title and description of info window -> use in 2 function
    private fun renderViews(marker: Marker?, contentView: View) { // marker เป็น null ได้ เนื่องจาก marker ของ  getInfoWindo, getInfoContents อาจจะเป็น null คู่เลย, paramter 2 เป็น view ที่จะใช้
        val title = marker?.title
        val description = marker?.snippet
        val test2 = marker?.tag

        val titleTextView = contentView.findViewById<TextView>(R.id.title_textView)
        titleTextView.text = title

        val descriptionTextView = contentView.findViewById<TextView>(R.id.description_textView)
        descriptionTextView.text = description

        val titleTextViewTest2 = contentView.findViewById<TextView>(R.id.textviewtest)
        titleTextViewTest2.text = test2.toString()
    }

}