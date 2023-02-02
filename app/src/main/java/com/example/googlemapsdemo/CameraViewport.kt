package com.example.googlemapsdemo

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds


class CameraViewport {

    //modify properties -> pass 2 thing -> actual target (lat,long) and Zoom level
    val thaiCom: CameraPosition = CameraPosition.Builder()
        .target(LatLng(14.08529689719122, 100.42137836945135))
        .zoom(17f)
        .bearing(100f) // ปรับมุมหมุน
        .tilt(45f) //ex 0 มุมบน 45, side view
        .build()


    // Set boundaries (กำหนดขอบเขตแผนที่) ->ช่วยเรื่องการมองเห็นพื้นที่ที่สนใจให้มากสุดที่จะทำได้ -> newLatlngBounce ช่วยให้ระบุขอบเขตของพื้นที่ -> call หลังจาก layout วาดเสร็จแล้วไม่งั้น crash แก้ด้วยการเพิ่มพารามิเตอร์ width,height
    // หา boundary จาก google map -> เลือกเมืองและเลือกของเขตมาสองจุด -> southwest, nothe east
    val pakkretBounds = LatLngBounds(
        LatLng(13.854741472592906, 100.52056199535701) ,// south west
        LatLng(13.930229355529553, 100.5959213400457) // north east

    )

}


