package com.example.googlemapsdemo

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.lifecycleScope

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.example.googlemapsdemo.databinding.ActivityMapsBinding
import com.example.googlemapsdemo.misc.TypeAndStyle
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.GoogleMap.OnPolylineClickListener
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, OnMarkerClickListener, OnMarkerDragListener, OnPolylineClickListener    // inherit onMapReadyCallback เพื่อใช้ getMapAsync (mapFragment เรียก)
{
    // shift + f6 = rename
    // control + Q = ดู doc ฟังก์ขัน
    private lateinit var map: GoogleMap                              // 1. initial map variable ก่อน เพื้่อเอาไปใช้ใน  onMapReady function
    private lateinit var binding: ActivityMapsBinding
    private val typeAndStyle by lazy { TypeAndStyle() } // ใช้ by lazy เพราะให้ lightweight เพื่อรอจน  constructor ของ class TypeAndStyle โดนเรียกแล้วค่อยทำ
    private val cameraViewport by lazy {CameraViewport() }


    private val bhiraj = LatLng(13.721437987285286, 100.52218794741914)
    private val ati = LatLng(14.084037703890884, 100.42053077551579)
    private val futurePark = LatLng(13.989291617726902, 100.61871542802871)
    private val pathumThani = LatLng(14.023965260897565, 100.52367773492774)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
                                                                     // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment        // Google Map api -> สร้างตัว mapContainer ทั้งหมด -> container ในที่นี้คือ supportMapFragment

        mapFragment.getMapAsync(this)                         //getMapAsync ใช้ call onMapReady เมิ่อ google map พร้อมทำงาน
    }

    // control + o -> เอาไว้ ovverride method
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {    // ใช้สำหรับ แสดง handle "menu" และ inflate -> menuInflater
        menuInflater.inflate(R.menu.map_types_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {   //ใช้สำหรับ handle "click" ของ menu item
        typeAndStyle.setMapType(item,map)
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {                                             //  คล้ายๆ onCreate ของ activity นั้นละ แต่ใช้กับ  map -> โดน call ตอน activity start กับ google map ready
                                                                                                // ส่วนใหญ่ใช้ add logic ที่ใช้ติดต่อกับ google map
                                                                                                // ประกาศ  map: GoogleMap ใน global ก่อน

        map = googleMap                                                                         // set google map object เป็นตัวเดียวกับ global variable ที่ประกาศด้านบน

/***  Add a marker in thaiCom and move the camera */
        val thaiCom = LatLng(14.08529689719122, 100.42137836945135)          // ประกาศ latitude, longitude -> อันนี้เป็น coordinate ของ sydney
        //map.addMarker(MarkerOptions().position(thaiCom).title("Marker in ThaiCom"))       // ใช้ googlemap object ให้ add marker โดยมี position อยู่บน sydney และก็ add title บน marker ว่าตามนั้น


        val pakkret = LatLng(13.899989038373336, 100.54210604816764)
        map.addMarker(MarkerOptions().position(pakkret).title("Marker in Pakkret").snippet("Test"))

/***  Add new position and use zIndex -> Z-Index -> สมมติ marker มันซ้อนทับกันเนื่องจาก position ใกล้กัน -> ใช้ .zIndex()
                                      ->  Snippet -> Info Window -> Allow you to display tge information to the user when they tap put a marker -> ปกติแล้ว กด 1 marker แล้วจะแสดง info 1 marker แต่พอกด marker ที่ 2 info ของตัวแรกจะปิดออโต้*/

        val paragon = LatLng(13.746357416082638, 100.53469180417855)
        map.addMarker(MarkerOptions()
            .position(paragon)
            .title("Marker in Paragon")
            .snippet("Some random text")    //Add sub title - description
            .zIndex(1f)   // ZIndex ค่าเริ้มจาก 0 -> 1(topสุด) ใช้ z index จะทำให้ marker ที่ใช้อยู่ข้างบนสุดและสังเกตง่าย (กรณี zoom ออก)
        )


/******* newLatLngZoom สำคัญ เอาไว้กำหนดขนาด zoom ของแมป  -> map.moveCamera(CameraUpdateFactory.newLatLng(losAngeles))  // set camera view ให้ตรงกับโลเคชั่นที่เราอยากได้ ในทีนี้คือ sydney */
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(thaiCom,10f))  //  รับ actual latlong, actual value (จาก 1-20) -> 15 คือ zoom เข้าไปใกล้มากๆ

/***  use camera viewport that modifies -> ทำมุมเอียง fix zoom เอง ใช้ newCameraPosition */
//      map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraViewport.thaiCom))  //  ใข้ newCameraPosition ซึ่งรับค่า camera position ที่สร้าง modify ไว้ใน cameraviewport

        // using control and gesture (uiSettings)                                             -> สำหรับควบคุมการกดแผนที่และ position ต่างๆ -> map มีสิ่งที่เรียกว่า uisetting
        map.uiSettings.apply {
            isZoomControlsEnabled = true        // ทำให้มีปุ่ม +- สำหรับ zoomเข้าออก เพิ่มขึ้นมาในหน้าแอป
            isCompassEnabled = true             // ทำให้มีเข็มทิศโผล่มุมซ้ายเวลากด rotate ถ้ากดเข็มทิศมันก็จะพากลับมาที่ default location ละก็ค่อยๆหายไป อยาก disable ก็แค่ set fault

            /*
            isMapToolbarEnabled = false         // ปกติกด marker แดงๆแล้วจะมี toolbar ที่ไว้ให้ user กดเลือกขึั้น ถ้าไม่อยากให้ชึ้นก็ปิดไว้ได้เลย
            isMyLocationButtonEnabled = true    // ทำให้มาโลเคชั่นของเครื่องที่ใช้อยู่โดยตรง -> ใช้ได้ต่อเมื่อ myLocation layer enable นะ -> เดี่ยวทำทีหลัง
            isZoomGesturesEnabled  = false      // ทำให้ไม่สามารถใช้มือกดซูมเข้าออกได้ ต้องใช้ปุ่มเท่านั้น (USE CASE -> FIX ร้านค้าให้อยู่ตามโลนี้ ไม่อยากให้ user zoom เข้าออกบรรยากาศข้างๆ)
            isScrollGesturesEnabled = false     // fix -> scroll หน้าจอ map ไปไหนไม่ได้แล้ว*/
        }


/***  Map padding -> ช่วยแก้ปัญหาเวลาเรามีการเพิ่ม view ต่างๆไม่ให้ทับกับ map ธรรมดา เข่น side bar -> สมมติไม่มี ปุ่ม +- ที่ทำไว้ก็อาจจะหายไปได้ -> ใช้ map object ในการทำนะ */
//        map.setPadding(0,0,300,0) // ทำใน onMapReady -> each integer แสดงด้านหนึ่งของ screen LEFT,TOP,RIGHT,Bottom -> add padding 300 ไปที่ด้านขวา -> ปุุ่ม +- โดนขยับมาละจากด้านขวา
         typeAndStyle.setMapStyle(map, this)

/***  set max-min zoomout level -> fix ขนาดไม่ให้ออกหรือเข้าใกล้เกินไป */
        /* normal

        map.setMinZoomPreference(15f)
        map.setMaxZoomPreference(17f)*/

/***  using coroutines -> USECASE: หลังจาก 2 วิ เราจะเพิ่มค่า zoom level -> 3 จาก default คือ 17 ที่กำหนดใน CameraViewport เอาไว้โชว์แบบเท่ๆ */
// start coroutine scope
       /* lifecycleScope.launch{
            delay(5000L)
            map.moveCamera(CameraUpdateFactory.zoomBy(3f))    // มีฟังก์ชัน zoom in,out 1 level เท่านั้น ่แต่มีฟังก์ชัน zoomTo สำหรับกำหนดขนาด specific level,
                                                                     // Zoom by สำหรับ certain amount of zoom level -> ex) ปกติ 17 ใส่ zoom by 3 = 20
        }*/

/***  Moving camera position from a position to b position -> 3 important funtion -> 1)  map.moveCamera(CameraUpdateFactory.newLatLngZoom(thaiCom,10f)), 2) */
     /*   lifecycleScope.launch{
*//***  วิแรกอยู่ไทยคม ผ่านไป 4 วิ มุมย้ายมาปากเกร็ด --> เคลื่อนย้ายมุมมองของแมป *//*
            delay(4000L)
//            map.moveCamera(CameraUpdateFactory.newLatLng(pakkret))

*//***   scrollby -> ให้เราสามารถเปลี่ยน camera position จากการกำหนดเลข pixel x,y axis
             ex) x+ -> move camera up on the ride side -> map will appear to the left
             ex) y+ -> move camera down                -> map appear will move up
             ใข้ scrollby แทน newLatLng
    ผ่านไป 4วิ อยากเลื่อนมุมกล้องไปทางขวา x = +100, ไม่อยากเลื่อนกล้องแนวตั้ง *//*
//            map.moveCamera(CameraUpdateFactory.scrollBy(100f,0f)) // scrollBy ใข้ pass ค่า x axis,y axis -> return cameraUpdate that scroll camera over the map
*//***     เลื่อน x ซ้าย 200px หลังจากนั้นลงล่าง 100px *//*
//            map.moveCamera(CameraUpdateFactory.scrollBy(-200f,100f)) //

*//****  Set boundaries (กำหนดขอบเขตแผนที่) ->ช่วยเรื่องการมองเห็นพื้นที่ที่สนใจให้มากสุดที่จะทำได้ มองกว้างขึ้นนั้นเอง -> newLatlngBounce ช่วยให้ระบุขอบเขตของพื้นที่ -> call หลังจาก layout วาดเสร็จแล้วไม่งั้น crash แต่ถ้าอยากทำก่อนแก้ด้วยการเพิ่มพารามิเตอร์ width,height *//*
//          map.moveCamera(CameraUpdateFactory.newLatLngBounds(cameraViewport.pakkretBounds, 100))  // parameter 2 คือ padding ซึ่งช่วยขยายขอบเขตรอบๆให้กว้างขึ้นอีก

*//****** อยากให้กล้องอยู่"ตรงกลาง"ในขอบเขตที่กำหนด(bounces) โดยมีระยะ zoom คงที่คือ 10 (parameter2) *//*
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraViewport.pakkretBounds.center, 10f))

*//**** Fix มุมกล้อง restriction ไม่ให้ user ขยับได้ -> ใช้ร่วมกับกำหนดขอบเขค ให้ user ขยับได้ถึงแค่มุมที่กำหนดเท่านั้น ออกไปเที่ยวโลกกว้างไม่ได้ *//*
//            map.moveCamera(CameraUpdateFactory.newLatLngBounds(cameraViewport.pakkretBounds, 100))
//            map.setLatLngBoundsForCameraTarget(cameraViewport.pakkretBounds)

*//**** Animation เวลาสไลด์ ทำให้แผนที่เคลื่อนไหวได้ -> เปลี่ยน map.moveCamera เป็น animate camera ได้ (อันข้างบนก็ได้นะ) -> มี 3 แบบ แล้วแต่เลือกเลย เราเลือกแบบที่ 3 ปรับเวลาได้
 *           ใช้กัย zoomTo,zoomBy ต่างๆก็ได้ แทนที่ newLatLngBounds เช่น อยาก zoom  ->   map.animateCamera(CameraUpdateFactory.zoomTo(15f),2000,null)
             ใช้กับ scrollby ให้มันเลื่อนไปทางทิศที่กำหนดก็ได้                         ->   map.animateCamera(CameraUpdateFactory.scrollBy(200f,0f),2000,null)
             ใช้กับจุดที่กำหนดใน CameraViewPort (มีการ set ค่าต่างๆเช่น tilt, bearing etc ซึ่งสวยมากกก) -> สมมติอยากให้ animate ไปที่ไทยคม   ->   map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraViewport.thaiCom),2000,null)
             ใช้กับ newLatLngBounds กรณีอยากให้มันเคลื่อนไหวไปที่พิกัดใหม่              ->   map.animateCamera(CameraUpdateFactory.newLatLngBounds(cameraViewport.pakkretBounds,100), 2000, null)

********* ใช้ Callback ไปที่ฟังกชันกล้องด้วย (เพื่อแสดง something ระหว่างเรียกใช้ animate)-> เอา null ตรงท้าย animate ออก
    จาก                 map.animateCamera(CameraUpdateFactory.newLatLngBounds(cameraViewport.pakkretBounds,100), 2000, null)
    เป็น                                                                                     .pakkretBounds,100), 2000, object : GoogleMap.CancelableCallback { override onFinish, onCancel} )

 *//*

 *//****       ตัวอย่าง callback ตอนเรียก newCameraPosition และใช้ตำแหน่งที่สร้างเอง คือ thaiCom
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraViewport.thaiCom),2000,object : GoogleMap.CancelableCallback {
                override fun onFinish() {
                    TODO("Not yet implemented")
                }

                override fun onCancel() {
                    TODO("Not yet implemented")
                }
            }) *//*

// ใช้จริง (animate แบบมี event callback)
////              map.animateCamera(CameraUpdateFactory.newLatLngBounds(cameraViewport.pakkretBounds,100), 2000, null)
//                map.animateCamera(CameraUpdateFactory.newLatLngBounds(cameraViewport.pakkretBounds,100), 2000, object : GoogleMap.CancelableCallback {
//                    override fun onFinish() {                   // ถูกเรียกเมื่อ animation complete
//                        Toast.makeText(this@MapsActivity, "Finish", Toast.LENGTH_SHORT).show()
//                    }
//
//                    override fun onCancel() {                     // ถูกเรียกเมื่อ animation โดน cancel -> ex) กดเลื่อนขณะกำลัง animate
//                        Toast.makeText(this@MapsActivity, "Cancel", Toast.LENGTH_SHORT).show()
//                    }
//                })

        }
// end of coroutine scope */

/*** call clicked event handler */
//        onMapClick()
        onMapLongClick()


/**** Marker */
/*** ลบ marker ออกจาก map หลังผ่านไป 4 วิ ->  คำสั่ง Marker.remove() */
//        val thaiComMarker = map.addMarker(MarkerOptions().position(thaiCom).title("Marker in thaicom"))  // สร้า่งตัวแปรเก็บ type marker object ไว้ก่อน
//        lifecycleScope.launch{
//            delay(4000)
//            thaiComMarker?.remove()
//
//        }

/** save ข้อมูล object ใน marker โดยใช้ set "tag" function และ ดึงค่าจาก object ทื่โดน save มาใข้  -> ใช้ MarkerObject.tag = "Some value" แล้ว Add GoogleMap.OnMarkerClicklistener ต่อท้าย class เพื่อเรียกใช้ onMarkerClick สำหรับใส่้ logic
      USE CASE: อยากแยกความแตกต่างของ marker แต่ละชนิด */
//       val thaiComMarker = map.addMarker(MarkerOptions()
//                              .position(thaiCom)
//                              .title("Marker in thaicom"))
//
//       thaiComMarker?.tag = "Office"
//
        map.setOnMarkerClickListener (this) // this คือ OnMarkerClickListener ที่เราเรียกมาใน class นี้่


/** Move marker to anywhere in the map -> default เลยคือ marker กดขยับไม่ได้ (กดค้างที่ marker ก็ไม่มีไรเกิดขึ้น) -> อยากให้ขยับจุดได้ ว่างั้น
  -> ใช้ addMarker(xxx).title().draggable(true)
  -> เพิ่ม dragEvent ด้วยการ implement OnMarkerDragListener ต่อ class -> ได้ overide onMarkerDrag, start, end
 */
//    val thaiComMarkerDraggable = map.addMarker(MarkerOptions()
//                                    .position(thaiCom)
//                                    .title("Marker in thaicom")
//                                    .draggable(true))

//    map.setOnMarkerDragListener(this) // this คือ MapActivity


/** Custom marker style -> ใช้ .icon(BitmapDescriptorFactory.xx)
 * -- มีข้อจำกัดด้านสี ต้อง handle เอง -> สีโดนกำหนดใน BitmapDescriptorFactory เลยต้องเอามาเรียกใข้อีกที -> เป็น defaultMarker(BitmapDescriptorFactory.XXXCOLOR)
 *                               -> BitmapDescriptorFactory มีฟังก์ชันข้างในเยอะ เช่น BitmapDescriptorFactory.fromResource(RESOURCE.ID,VECTOR.xml) เลือกได้ต่ามการใช้งาน
 *
 *                               -> แบบเพิ่มสีใหม่เอง -> defaultMarker(NUMBER OF COLOR- FLOAT) // สีมีจาก 0 ถึง 360 -> ex) 134f = green
 *                               -> เพิ่มแบบ custom -> จาก resource file เอง ex) vector-bitmap -> ใช้ตรงๆจากไฟล์ไม่ได้ เป็น .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_android_black_24dp)))
 *                                  -> CRASH !! -> ต้องแปลงเป็น bitmap ก่อน -> สร้างฟังก์ชันใหม่ไว้ handle -> fromVectorToBitmap
 *                                  -> แต่ถ้าเป็น ฺBitmap อยู่แล้วก็ใช้ได้เลยนะ -> .icon(BitmapDescriptorFactory.fromResource(R.drawable.BITMAP_RESOURCE)))
 * */
    val thaiComMarkerNewIcon= map.addMarker(MarkerOptions()
                                    .position(thaiCom)
                                    .title("Marker in thaicom")
                                    .icon(fromVectorToBitmap(
                                        R.drawable.ic_android_black_24dp,
                                        Color.parseColor("#000000")
                                        ))
                                    .alpha(0.5f) // alpha value เอาไว้ set ความเข้มชอง marker -> 0 = transparent , ใกล้ 1 คือเห็นชัด
                                    .rotation(90f) // marker ขยับไปองศาขวา
                                    .flat(true) // ปกต้แล้วเวลา marker rotate มันจะไม่ขยับตาม แต่ถ้าใส่ flat เป็น true marker ก็จะเลื่อนไปตามองศานั้นด้วย
    )
/***   CUSTOM INFO of MARKER -> สร้าง res layout file, สร้าง adapter class implement InfoWindowAdapter จะ ให้่เรา override 2 function จากนั้นสร้างฟังก์ขันไว้ render เป็นอันเสร็จ  */
    map.setInfoWindowAdapter(CustomInfoAdapter(this)) // เรียก setInfoWindowAdapter และ pass CustomInfoAdapter ที่สร้างไป มันจะขอ context คือ this (Activity นี้)

/**  POLY-LINES -> a set of lat-long ที่มาจากเส้นที่เชื่อมต่อกัน คือมาจาก location ตามลำดับที่สั่ง*/
       lifecycleScope.launch{       //ใช้ coroutine เพราะจะลองเรียก delay
           addPolyLines()
       }
/*** polyline with clickListeners -> implement class with OnPolyLineClickListener ก่อน แล้วก็มาเรียก override method -> onPolylineClick ,
                                 -> ใน onMapReady ให้ add map.setOnPolyLineClickListener(this)
                                 -> หลังจากนั้นใน onPolylineClick ที่ overide มา ก็ทำ logic ที่อยากทำได้ เช่นโชว์ toast
                                 -> ใน addPolyline.() อย่าลืมเพิ่ม clickable(true) เพราะปกติแล้ว default click มันเป็น false
 */

    map.setOnPolylineClickListener (this)
    }

    // polyline -> ปกติ จะต้องกำหนดจุดหรือตำแหน่งที่แน่นอนของพื้นที่เราก่อนในการทำการเชื่อม -> จุดสองจุดที่กำหนด ex pakkret, paragon
    private suspend fun addPolyLines(){
        val polyline = map.addPolyline(
            PolylineOptions().apply {      // CUSTOM poly lines
                add(bhiraj,ati,futurePark) // รับ lat lon object (start point to end point เรียงตามลำดับที่กำหนด)
                width(5f)
                color(Color.BLUE)
                geodesic(true) // geodesic จะช่วยให้วาดเส้น ตาม actual latlng ไม่ใช่แค่เปนเส้นตรง
                clickable(true)
            }
        )
        delay(5000)      // หลังจาก 5 วิ polyline จะเปลี่ยนเส้นพิกัด จากตรงกลางคือ ati เป็น carnegie
        val newList = listOf<LatLng>(
            bhiraj,pathumThani,futurePark
        )
        polyline.points = newList // polyline อันนี้คือ return type ของเจ้า map.addPolyline{ ทั้งหลายด้านบน
                                  // points -> Change shape of polyline -> เอาไว้กำหนดจุดหรือพิกัดใหม่ที่อยากทำการเปลี่ยนแปลง เช่นจากเดืมจุด   a->b อยากให้ a->c แทน ก็ใข้ points เอา
                                  // อันนี้เอาตัวพิกัดใหม่จาก list ที่สร้างมาใ้ช้ (newList)

    }

    override fun onPolylineClick(p0: Polyline) {
        Toast.makeText(this, "PolylineClicked", Toast.LENGTH_SHORT).show()
    }

    // CONVERT VECTOR TO BITMAP THAT USE FOR THE CUSTOM MARKER -> ใช้บ่อย
    private fun fromVectorToBitmap(id:Int, color:Int):BitmapDescriptor {
        val vectorDrawable: Drawable?  = ResourcesCompat.getDrawable(resources,id, null)// อาจเป็น null ได้
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
    }


    // จัดการพวก Drag marker
    override fun onMarkerDragStart(p0: Marker) {    // เริมกด marker -> เอา object p0 ทำอย่างอื่นได้ ex) get tag,id, visible, etc
        Log.d("Drag", "Start")
        Log.d("Drag","Start position ${p0.position}")

    }

    override fun onMarkerDrag(p0: Marker) {         // กำลังกด marker
        Log.d("Drag", "Drag")
        Log.d("Drag","Drag position ${p0.position}") // ลองเอาไว้ดู position ขณะกำลังกด

    }

    override fun onMarkerDragEnd(p0: Marker) {      // ปล่อย marker
        Log.d("Drag", "End")
    }




    override fun onMarkerClick(marker: Marker): Boolean {           // เอาไว้ให้เพิ่มอัลกอลงไปเมื่อ marker โดน click ในที่นี้คือโชว์ว่า marker ที่โดนกด มี tag อะไรเก็บไว้
        if (marker != null) {
            Log.d("Marker",marker?.tag.toString())
            map.animateCamera(CameraUpdateFactory.zoomTo(17f),2000, null) // อยากให้กด marker แล้ว เกิดการ zoomIn เข้าไป
            marker?.showInfoWindow()
        } else {
            Log.d("Marker", "Empty")
        }
        return true         // return true -> ไม่เห็นหน้า info (null), false เห็นหน้า info (Marker in Thaicom)
                            // -> แต่ถ้าอยากใช้ function ต่างๆ ห้ามใช้ false เพราะมันจะเป็น default behavior เช่่นกดแล้ว animation ไม่ทำ
                            // ถ้า return true แล้วอยากแสดง info window ใข้ marker.showInfoWindow()

    }

/**** Handle Map Clicked -> Add Single, LongClickListener -> สร้างฟังก์ชันใหม่ onMapClick ซึ่งใข้ map object  เรียก setOnMapClickListener
 *                       -> USE CASE -> ใช้กับ Marker เช่น ใส่ logic สำหรับ add new marker location เมื่อใดก็ตามที่กดไปที่ตำแหน่งนั้นๆ
 *                       -> ตัว Clicklistener มันจะให้ lat long object ที่แม่นยำสำหรับจุดที่โดนกดไป             -> ข้างในฟังก์ชัน clicklistener เราสามาารถ print lat long มาได้เลบ
 *                       -> เราสามารถเรียก "Add Marker" สำหรับเพิ่ม position ที่โดน mark เมื่อตอนกดได้ด้วย      -> map.addMarker(MarkerOptions().position(it).title("New Marker")) */
//ปกติจะทำงานเมื่อเราคลิกครั้งนึงไปที่แมป
    private fun onMapClick(){
        map.setOnMapClickListener {
            Toast.makeText( this, "Single Clicked", Toast.LENGTH_SHORT).show()
        }
    }

//ปกติจะทำงานเมื่อเรากดค้างยาวๆไปที่แมป
    private fun onMapLongClick(){
        map.setOnMapLongClickListener{
            Toast.makeText( this, "Latitude: ${it.latitude}\n Longitude: ${it.longitude}", Toast.LENGTH_SHORT).show()
        map.addMarker(MarkerOptions().position(it).title("New Marker"))

        }
    }





}