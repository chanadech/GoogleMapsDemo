package com.example.googlemapsdemo

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import androidx.appcompat.app.AppCompatActivity

 class Markers: AppCompatActivity() {


/***   // CONVERT VECTOR TO BITMAP THAT USE FOR THE CUSTOM MARKER -> ใช้บ่อย
     fun fromVectorToBitmap(id:Int, color:Int): BitmapDescriptor {
        val vectorDrawable: Drawable?  = ResourcesCompat.getDrawable(resources,id,null)// อาจเป็น null ได้
        if (vectorDrawable == null){
            Log.d("MapsActivity", "Resources not found")
            return BitmapDescriptorFactory.defaultMarker()
        }
        val bitmap = Bitmap.createBitmap(  // สร้าง bitmap object มี actual width and height ละก็สี
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)     // วาด bitmap ลง canvas ละกำหนดค่าต่างๆ
        vectorDrawable.setBounds(0,0,canvas.width,canvas.height) // 0,0 ผ่านทางซ้าย และต้องการผ่านศูนย์กลาง
        DrawableCompat.setTint(vectorDrawable,color)// ตั้งค่าสีให้ bitmap
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap) // return เอากลับไปใช้ในการทำ marker
    }*/
}