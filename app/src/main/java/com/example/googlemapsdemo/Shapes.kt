package com.example.googlemapsdemo

import android.R
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.ButtCap
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.CustomCap
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.RoundCap
import com.google.android.gms.maps.model.SquareCap
import kotlinx.coroutines.delay


class Shapes: AppCompatActivity(),GoogleMap.OnPolygonClickListener {
    private val bhiraj = LatLng(13.721437987285286, 100.52218794741914)
    private val ati = LatLng(14.084037703890884, 100.42053077551579)
    private val futurePark = LatLng(13.989291617726902, 100.61871542802871)
    private val pathumThani = LatLng(14.023965260897565, 100.52367773492774)

    // polygon -> ถ้าตำแหน่งสุดท้ายกับตำแหน่งแรกไม่ตรงกัน -> polygon จะ auto add เพิ่มอีก 1 พิกัด ที่ตัวสุดท้ายของลำดับ ในที่นี้่คือ เจอ p3 แล้ว p3 ไม่ตรงเหลี่ยม จะทำการเพิ่ม p0 อีกรอบให้วนกลับมาเอง -> พิกัดสุดท้ายจะเป็นจุดเดียวกับจุดแรกเริ่มของเรา
    // คล้ายๆเริ่ม p0 ถ้าจุดสุดท้าย เช่น p3 ทำให้รูปมันไม่เหลี่ยม polygon ก็จะทำการ add จุด p0 อีกรอบนึงในพิกัดตัวสุดท้าย เพื่อให้เส้นมันวนมาจุดเดิม
    private val p0   = LatLng(14.0723155267548, 100.61601263388812)
    private val p1      = LatLng(14.052500007981644, 100.71660619982075)
    private val p2      = LatLng(14.028519271560226, 100.69806677172924)
    private val p3 = LatLng(14.02935197812403, 100.61429602052633)

    // small polugon
    private val p00   = LatLng(14.063157306831538, 100.62820059190038)
    private val p01      = LatLng(14.045505885698901, 100.70184332266804)
    private val p02      = LatLng(14.03717927135291, 100.68776708974342)
    private val p03 = LatLng(14.041509148569947, 100.62940222153671)



    // polyline -> ปกติ จะต้องกำหนดจุดหรือตำแหน่งที่แน่นอนของพื้นที่เราก่อนในการทำการเชื่อม -> จุดสองจุดที่กำหนด ex pakkret, paragon -> ถ้าไม่ได้เรียก map object ก็ให้ pass เข้าไปด้วย
     suspend fun addPolyLines(map:GoogleMap){

        // val pattern = listOf(Dot(),Gap(50f)) // custom poly line

        val polyline = map.addPolyline(
            PolylineOptions().apply {      // CUSTOM poly lines
                add(bhiraj,ati,futurePark) // รับ lat lon object (start point to end point เรียงตามลำดับที่กำหนด)
                width(50f)
                color(Color.BLUE)
                geodesic(true) // geodesic จะช่วยให้วาดเส้น ตาม actual latlng ไม่ใช่แค่เปนเส้นตรง
                clickable(true)
        //        pattern(pattern)   // กำหนด pattern เส้น รายละเอียดดูล่าง ใช้กับ polyline, polygon, circle ได้
                jointType(JointType.ROUND)
                startCap(RoundCap())
                endCap(CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.ic_menu_compass), 20f)) // custom cap ต้อง pass 2 ค่า 1.bitmap object(res), 2. float value (stroke size - pixel) ยิ่งค่ามาก size icon ยิ่งน้อย
            }
        )
        delay(5000)      // หลังจาก 5 วิ polyline จะเปลี่ยนเส้นพิกัด จากตรงกลางคือ ati เป็น pathumThani
        val newList = listOf<LatLng>(
            bhiraj,pathumThani,futurePark
        )
        polyline.points = newList // polyline อันนี้คือ return type ของเจ้า map.addPolyline{ ทั้งหลายด้านบน
        // points -> Change shape of polyline -> เอาไว้กำหนดจุดหรือพิกัดใหม่ที่อยากทำการเปลี่ยนแปลง เช่นจากเดืมจุด   a->b อยากให้ a->c แทน ก็ใข้ points เอา
        // อันนี้เอาตัวพิกัดใหม่จาก list ที่สร้างมาใ้ช้ (newList)

    }

    // polygon -> คล้ายๆ polyline ที่รับ google map object (แนวๆอยากสร้างพวกสามเหลี่ยม) -> อธิบายด้านบนไปละ
     fun addPolygon(map:GoogleMap){
        val polygon = map.addPolygon(
            PolygonOptions().apply {
                add(p0,p1,p2,p3)
                fillColor(R.color.black)      // custom ัpolygon background color -> ทำสีข้างใน แบบปกติ ก็อาจจะ Color.BLUE , อยากให้โปร่งแสง Transparent ใช้ R.color.tealXX หรือสีใน color.xml ซึ่งเรา custom เองได้
                strokeColor(R.color.black)    // สีขอบของ polygon
//                addHole(listOf(p00, p01, p02, p03))  // สร้าง small polygon ภายใต้ polygon ใหญ่ (ใช้พิกัดใหม ที่ห้ามออกไปจากพิกัดแรกนะ่)
                zIndex(1f)
                clickable(true)
            }
        )

        /*** Add Hole polygon -> รูปหลายเหลี่ยมสามารถใข้รวมกันเพื่อวาดรูปร่างที่ซับซ้อนได้ เช่นภาพวาดสนาม อย่างโดนัท
                          -> รูปร่างที่ซับซ้อนอาจเกิดจากเส้นทางง่ายๆหลายๆเส้นทางเสมอ
                          -> เคล็ดลับ  "การกำหนดรูปหลายเหลี่ยม สองรูป ในพื้นที่เดียวกัน โดยรูปหลายเหลี่ยมอัน่ใหญ่อาจจะใช้เป็นตัวกำหนดขนาด field area ส่วนรูปหลายเหลี่นมอันเล็ก อาจจะเป็นพื้นที่ช้างใน transparent"
                             -> สร้าง new polygon shape -> กำหนด lat long พื้นที่ใหม่ ใน shape latlong เดิม
                             -> เรียกใช้ addHole แล้วยัดค่า listOf(พิกัดใหม่) ไป -> addHole สามารถสร้างรูปร่างที่ซับซ้อนได้
                             -> output จะเห็นเป็นสี่เหลี่ยมใหญ่คือพิกัดแรกเป็นขอบ และสีเหลี่ยมเล็กจะเป็นพิกัดใน hole
                             -> NOTE: ระวังเรื่องพิกัดช้างใน ห้ามเลยไปข้างนอก field เพราะจะทำให้ addHole ไม่ทำงาน (transparent สวยๆหาย)

        แล้วถ้าอยาก add polygon ใหม่่ "ภายใน"รูปหลายเหลี่ยมแรก แทนการใช้ addHole ละ ทำไง ก็สร้างไปเลยย ->
                                   -> สังเกตได้ว่ารูปจะโดนทับกันอยู่ ทีนี้่ปัญหาจะเกิดเมื่อเราเรัยกใช้ onClickListener สำหรับหนึ่งใน polygon ที่ทับกันอยู่นี้่
                                   -> ต้องระวัง ให้แก้ด้วย zIndex() สำหรับหนุึ่งใน polygon ที่เราต้องการสร้าง clickListener
                                        -> สมมติอยากสร้าง clickListener ที่ polyGon อันแรก -> ก็ add zIndex(1) ต่อท้ายไป -> zIndex default คือ 0  ดังนั้นอันไหนอยากให้เป็น ontop ข้างบน shape อื่นๆก็ใส่ 1 (ยกเว้น marker นะ เพราะทำไม่ได -> marker always top of everythong้)
                                        -> เท่านี polyGon แรกก็ addClickListener ได้ละ (polygon ขอบนะ)
         */
        val polygon2 = map.addPolygon(
            PolygonOptions().apply {
                add(p00, p01, p02, p03)
                fillColor(R.color.black)   // custom ัpolygon background color -> ทำสีข้างใน แบบปกติ ก็อาจจะ Color.BLUE , อยากให้โปร่งแสง Transparent ใช้ R.color.tealXX หรือสีใน color.xml ซึ่งเรา custom เองได้
                strokeColor(R.color.black)    // สีขอบของ polygon
//                addHole(listOf(p00, p01, p02, p03))
            }
        )
    }

    override fun onPolygonClick(p0: Polygon) {
        Log.i("Came Here","polyline version Clicked")
    }


    /*** Circle shape -> 2 สิ่งที่ต้องกำหนดในการสร้าง
     *                     -> 1 location point ที่จะใช้เป็นจุดศูนย์กลางของวงกลม
     *                     -> 2 radius คือรัศมีที่แสดงในเมทริคของแผนที่
     *                     ดังนั้นวงกลมที่ปรากฎบนแผนที่ก็เกือบจะเป็น"วงกลมที่สมบูรณ์แบบ"เมื่ออย"ู่ใกล้กับเส้นศุนย์สูตร (equator)
     *                     และจะปรากฎเป็นรูปไม่กลมเมื่อเคลื่อนออกจากเส้นศูนย์สูตร
     *                    */

    suspend fun addCircle(map: GoogleMap){ //  อยากใช้ delay function ก็เลยกำหนด suspend เอา
        val circle = map.addCircle(  // สร้าง object circle ได้ละ ก็เอาไปใช้เล่นได้เลย เย้
            CircleOptions().apply {
                center(bhiraj)
                radius(50000.0)
                fillColor(com.example.googlemapsdemo.R.color.purple) // custom สีได้
                strokeColor(com.example.googlemapsdemo.R.color.purple)
                clickable(true) // default circle จะ click ไม่ได้ ถ้าอยาให้ click ก็ใข้ clickable() แบบเดียวกับพวก polygon -> implement OnCircleClick ใน class ที่เรียกไปด้วย อันนี้คือ MapsActivity ละก็ overide method สำหรับจัดการ click listener ได้เลย

            }
        ) // รับ  1 parameter ซ๎่งคือ circle option -> add 2 property คือ center point(lat long) กับ radius(double value หน่วยเมตร)
          //  Circle จะอยู่ ontop ของตำแหน่งที่เราสร้างกำหนดไป

        // ลองเล่น circle object ซึ่งเป็น return type ของ .addCircle() เอาไปทำไรก็ได้ละ-> ผ่านไป 4 วิ เปลี่ยนสี
        delay(4000)
        circle.fillColor = R.color.black

    }

    /** CUSTOM SHAPE
     *
     * -> STROKE PATTERN -> POLYLINE,POLYGON,CIRCLE
     *                   -> DEFAULT เป็น เส้นทึบใน polyline,และเส้นขอบใน polygon, circle
     *                   -> ระบุเป็น list ของ ช่องว่าง,จุด, เส้นประ เพื่อให้เราสามารถระบุได้
     *                   -> ยกตัวอย่างใช้ฟังก๋ชัน add polyline เดืม เราจะสร้าง new variable ชื่อ pattern อยู่ตำแหน่ง on top โดยกำหนดเป็น list เพื่อระบุจุดประในช่องว่าง
     *                      -> ใน list จะใส่ค่า Dot() จุด,Gap(float value) ช่องว่าง, Dash(flaot value) เส้นประ
     *                          -> ex)         val pattern = listOf(Dot(), Gap(30f),Dash(50f))
     *                          -> output: เริ่มต้นด้วย dot ใหญ่ๆกลมๆ(dot) และตามด้วยช่องว่างระหว่างสองเส้นนี้(gap 30) หลังจากนั้นจะเป็นเส้นประ(dash)และหลังจากนั้นจะไม่มีช่องว่างเลย ทำให่รูปเรียงต่อกัน ดังนั้นเลยควรมี gap หน่อย
     *                                     เราสามารถเพิ่มช่องว่าง(gap)ได้ เป็น   val pattern = listOf(Dot(), Gap(30f),Dash(50f), Gap(30f))
     *                                     หรือจะเอาแค่ gap, dash -> dot ไม่เอาก็ได้ -> val pattern = listOf(Gap(30f),Dash(50f), Gap(30f)) เหลือแค่เส้นประเหลี่ยมๆ จุดหายไปละ
                                           หรือแค่ dot ก็ได้ ->    val pattern = listOf(Dot(),Gap())


        -> JOINT TYPE  -> POLYLINE, OUTLINE OF POLYGON -> สามารถกำหนด รอบๆประเภทข้อต่อ (joint type) ได้และ joint type ที่จะส่างผลต่อความโค้งงอภายใน (จุดข้อต่อตอนเส้นกำลังหักมุมโค้ง)
                       -> จะช่วยระบุว่าตัวช้อต่อของเส้นเชื่อมระหว่างพิกัดนึงไปยังอีกพิกัดนึงนั้น(มุมโค้ง) จะมีการกำหนดยังไง
                       -> สมมติมีการใช้ stroke pattern เป็นแบบมีเส้นประ (dash) จากนั้นมีการเรียกใช้งาน เมื่อมันเจอกับข้อต่อ (joint) joint type จะไม่กำหนดกับ dots เพราะว่ามันจะเป็นวงกลมอยู่แล้ว
                       -> ยกตัวอย่าง  ใช้ฟังก๋ชัน add polyline ( jointType(JointType.XX) ) -> เลือกได้ว่าเอา BEVEL(ขอบมุมเป็นเส้นตรงเหลี่นมๆ) หรือ ROUND (โค้งมน อันนี้สวย), DEFAULT
                       -> NOTE: ใช้กับ dot ไม่เห็นผลนะ เพราะมันกลมอยู่แล้ว

        -> LINE CAPS   -> สามารถระบุ gap style(ช่องว่าง) ในแต่ละจปลายเส้นของไลน์์
                       -> ต้องกำหนด 3 อย่าง -> 1. Start caps, 2. end caps, 3. OPTIONAL (3.1 BUTT caps (default) คล้ายๆ square แหละ 3.2 SQUARE caps, 3.3 rounds caps)
                            -> ex)  map.addPolyline(
                                        PolylineOptions().apply {
                                                startCap(RoundCap()) <-- จุดเริ่มของเส้นจะเป็นครึ่งวงกลมๆ (เดิมเป็นเหลี่ยม) > จุดท้ายยังคงเป็นเส้นตรง
                                                endCap(XXCap]())  <-- อยากให้เหลี่ยม ใส่ end caps ด้วย
                          -> สามารถ custom bitmap (cap )มาใช้ได้ด้วย
                                ex) startCap(CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.xx), XX.X)  // อยากใช้ bitmap object ใน cap ก็ pass เข้าไป
                                     ** note ใช้ VECTOR ในนีไม่ได้นะ ได้แค่ PNG -> ถ้าอยากใช้ ให้ใช้ฟังก์ขัน vector to bitmap ที่เราสร้างได้

     * */





}

// note: PolyLine with clickListener
/*** polyline with clickListeners -> implement class with OnPolyLineClickListener ก่อน แล้วก็มาเรียก override method -> onPolylineClick ,
-> ใน onMapReady ให้ add map.setOnPolyLineClickListener(this)
-> หลังจากนั้นใน onPolylineClick ที่ overide มา ก็ทำ logic ที่อยากทำได้ เช่นโชว์ toast
-> ใน addPolyLines.() อย่าลืมเพิ่ม clickable(true) เพราะปกติแล้ว default click มันเป็น false
 */