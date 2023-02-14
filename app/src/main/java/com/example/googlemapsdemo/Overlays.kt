package com.example.googlemapsdemo

import android.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlay
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

class Overlays {

    private val bhiraj = LatLng(13.721437987285286, 100.52218794741914)
    private val ati = LatLng(14.084037703890884, 100.42053077551579)
    private val futurePark = LatLng(13.989291617726902, 100.61871542802871)
    private val pathumThani = LatLng(14.023965260897565, 100.52367773492774)


    private val bhirajBounds = LatLngBounds(
        LatLng(13.711270507788248, 100.52336975242007), // SW,
        LatLng(13.726373080880165, 100.52822991500055)  // NE
    )


    /** Handle ground overlay ภาพซ้อนทับ -> ภาพซ้อนทับจะเชื่อมโยงกับพิกัดละติจูดและลองจิจูด และ ground overlay จะถูกกำหนดให้กับ map
     *                                 -> การหมุน เอียง หรือซูมแผนทั้ จะเปลี่ยนทิศทางของภาพ
     *                                 -> usecase: ส่วนใหญ่ใช่เมื่อต้องการแก้ไขภาพเดียวในพื้นที่เดียวบนแผนที่
     *                                 -> add ground overlay : 1) สร้าง ground overlay option object
     *                                      -> กำหนด 2 properties -> 2.1 position 2.2 actual images
     *                                      -> ไปสร้าง overlay object ที่ตัว actiivity ที่จะใข้ ในที่นี้คือ MapsActivity กำหนด object overlay หัว class ก่อน ละเรียกใน -> onMapReady()
     *
     * Ground Overlay สามารถ custom เช่นลบได้ */


    fun addGroundOverlay(map:GoogleMap){
        val groundOverlay = map.addGroundOverlay( // call คล้ายๆเวลาทำพวก polyline, polygon เลย คือใช้ googlemap object เรียก
            GroundOverlayOptions().apply {// รับ 2 parameter -> position(มีแบบ position latlng ปกติ, latlngboundsที่ต้องกำหนด bound, WIDTH (meters), HEIGHT (meters) <--กำนหดได้ว่าเอา width อย่างเดียว หรือ heuight ด้วย),
                                                               //                -> actual image )
        //        position(bhiraj,10000f,10000f)    // ยิ่งค่ากว้างยาวเยอะ จนาดของรูปก็เยอะ => ใส่ parameter ความสูงแค่ตัวเดียวก็ได้ มันเก่งพอที่จะคำนวน height เอง
                  positionFromBounds(bhirajBounds)  // latlng bounds -> จะช่วยกำหนดขอบเขตที่จะใช้แสดงรูป อันนี้ก็จะกว้าง ยาวเท่า latlng ทีก่ำหนดเลย
                image(BitmapDescriptorFactory.fromResource(com.example.googlemapsdemo.R.drawable.ic_compass_gimbal_yaw))
            }
        )
    }

    /** Ground Overlay สามารถ custom เช่นลบได้
     * เพิ่ม return type
     ปกติแล้ว  map.addGroundOverlay() จะ return เป็น groundOverlay object
     เราอยากให้ addGroundOverlayCustom return same object ก็เลยเปลี่ยนจาก   val groundOverlay = map.addGroundOverl เป็น  return  map.addGroundOverlay()

     -> มี option หลายอย่างที่เรา modify ได้จากนี้ -> แค่เอา groundOverlay object ที่เราสร้างไปใช้ แล้ว . หาฟังก์ชันใน onMapReady()ได้เลย
     */

    fun addGroundOverlayCustom(map:GoogleMap): GroundOverlay? {
        return  map.addGroundOverlay( // call คล้ายๆเวลาทำพวก polyline, polygon เลย คือใช้ googlemap object เรียก
            GroundOverlayOptions().apply {// รับ 2 parameter -> position(มีแบบ position latlng ปกติ, latlngboundsที่ต้องกำหนด bound, WIDTH (meters), HEIGHT (meters) <--กำนหดได้ว่าเอา width อย่างเดียว หรือ heuight ด้วย),
                //                -> actual image )
                //        position(bhiraj,10000f,10000f)    // ยิ่งค่ากว้างยาวเยอะ จนาดของรูปก็เยอะ => ใส่ parameter ความสูงแค่ตัวเดียวก็ได้ มันเก่งพอที่จะคำนวน height เอง
                positionFromBounds(bhirajBounds)  // latlng bounds -> จะช่วยกำหนดขอบเขตที่จะใช้แสดงรูป อันนี้ก็จะกว้าง ยาวเท่า latlng ทีก่ำหนดเลย
                image(BitmapDescriptorFactory.fromResource(com.example.googlemapsdemo.R.drawable.ic_compass_gimbal_yaw))
            }
        )
    }

    fun addGroundOverlayWithTag(map:GoogleMap): GroundOverlay? {
        val groundOverlay =  map.addGroundOverlay( // call คล้ายๆเวลาทำพวก polyline, polygon เลย คือใช้ googlemap object เรียก
            GroundOverlayOptions().apply {// รับ 2 parameter -> position(มีแบบ position latlng ปกติ, latlngboundsที่ต้องกำหนด bound, WIDTH (meters), HEIGHT (meters) <--กำนหดได้ว่าเอา width อย่างเดียว หรือ heuight ด้วย),
                //                -> actual image )
                //        position(bhiraj,10000f,10000f)    // ยิ่งค่ากว้างยาวเยอะ จนาดของรูปก็เยอะ => ใส่ parameter ความสูงแค่ตัวเดียวก็ได้ มันเก่งพอที่จะคำนวน height เอง
                positionFromBounds(bhirajBounds)  // latlng bounds -> จะช่วยกำหนดขอบเขตที่จะใช้แสดงรูป อันนี้ก็จะกว้าง ยาวเท่า latlng ทีก่ำหนดเลย
                image(BitmapDescriptorFactory.fromResource(com.example.googlemapsdemo.R.drawable.ic_compass_gimbal_yaw))
            }
        )
        groundOverlay?.tag = "MY Tag"
        return groundOverlay
    }
}