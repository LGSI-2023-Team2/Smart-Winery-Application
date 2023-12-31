package com.example.smart_winery

import android.app.ActivityManager
import android.app.ActivityManager.RunningTaskInfo
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.example.smart_winery.ScanPage.Companion.startScanner
import com.example.smart_winery.databinding.MainPageBinding
import com.example.smart_winery.databinding.ReserveBinding
import org.json.JSONArray
import org.json.JSONObject
import com.example.smart_winery.databinding.SettingPageBinding
import com.example.smart_winery.databinding.WineInfoBinding
import com.google.mlkit.vision.barcode.common.Barcode

@GlideModule
class MyGlide : AppGlideModule()


class MainPage : AppCompatActivity() {

    private val cameraPermission = android.Manifest.permission.CAMERA
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            startScanner()
        }
    }
    val WineList1: MutableList<WineInfo> = mutableListOf()
    val WineList2: MutableList<WineInfo> = mutableListOf()
    val WineList3: MutableList<WineInfo> = mutableListOf()

    var floor1type = 1
    var floor2type = 3
    var floor3type = 2

    var floor1smart = true
    var floor2smart = true
    var floor3smart = true

    companion object{
        var instance:MainPage? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        instance = this
        val mainPageBinding = MainPageBinding.inflate(layoutInflater)
        val reserveBinding = ReserveBinding.inflate(layoutInflater)
        val wineInfoBinding = WineInfoBinding.inflate(layoutInflater)
        val settingPageBinding = SettingPageBinding.inflate(layoutInflater)
        val reserveView = reserveBinding.root
        val wineInfoView = wineInfoBinding.root
        val btnIdNumber = mainPageBinding.btn11.id
        var isInfo = true
        var isWineSelected = false
        var wineBefore:WineInfo = WineInfo(0,0,"","","",0,0,0,0,0, mutableListOf(),"",0,"",
            mutableListOf()
        )
        var wineAfter:WineInfo = WineInfo(0,0,"","","",0,0,0,0,0, mutableListOf(),"",0,"",
            mutableListOf()
        )
        var wineInfoTemp:WineInfo = WineInfo(0,0,"","","",0,0,0,0,0, mutableListOf(),"",0,"",
            mutableListOf()
        )


        val firstfloor = arrayListOf<ImageView>(
            mainPageBinding.btn11,
            mainPageBinding.btn12,
            mainPageBinding.btn13,
            mainPageBinding.btn14,
            mainPageBinding.btn15
        )
        val firstlayout = arrayListOf<LinearLayout>(
            mainPageBinding.layout11,
            mainPageBinding.layout12,
            mainPageBinding.layout13,
            mainPageBinding.layout14,
            mainPageBinding.layout15
        )
        val secondfloor = arrayListOf<ImageView>(
            mainPageBinding.btn21,
            mainPageBinding.btn22,
            mainPageBinding.btn23,
            mainPageBinding.btn24,
            mainPageBinding.btn25
        )
        val secondlayout = arrayListOf<LinearLayout>(
            mainPageBinding.layout21,
            mainPageBinding.layout22,
            mainPageBinding.layout23,
            mainPageBinding.layout24,
            mainPageBinding.layout25
        )
        val thirdfloor = arrayListOf<ImageView>(
            mainPageBinding.btn31,
            mainPageBinding.btn32,
            mainPageBinding.btn33,
            mainPageBinding.btn34,
            mainPageBinding.btn35
        )
        val thirdlayout = arrayListOf<LinearLayout>(
            mainPageBinding.layout31,
            mainPageBinding.layout32,
            mainPageBinding.layout33,
            mainPageBinding.layout34,
            mainPageBinding.layout35
        )
        var floor1:JSONObject = JSONObject()
        var floor2:JSONObject = JSONObject()
        var floor3:JSONObject = JSONObject()

        fun clearMain(){
            WineList1.clear()
            WineList2.clear()
            WineList3.clear()
        }

        fun displayWine(){
            clearMain()
            val floor1wines:JSONArray = floor1.getJSONArray("cell_ids")
            val floor2wines:JSONArray = floor2.getJSONArray("cell_ids")
            val floor3wines:JSONArray = floor3.getJSONArray("cell_ids")
            floor1type = floor1.getInt("type")
            when(floor1type){
                1 -> {
                    mainPageBinding.floor1Type.setText("Red")
                    mainPageBinding.floor1Type.setBackgroundResource(R.drawable.red_back)
                    for (i in firstlayout) {
                        i.setBackgroundResource(R.drawable.red_border)
                    }
                }
                2 -> {
                    mainPageBinding.floor1Type.setText("White")
                    mainPageBinding.floor1Type.setBackgroundResource(R.drawable.white_back)
                    for (i in firstlayout){
                        i.setBackgroundResource(R.drawable.white_border)
                    }

                }
                3 -> {
                    mainPageBinding.floor1Type.setText("Sparkling")
                    mainPageBinding.floor1Type.setBackgroundResource(R.drawable.sparkling_back)
                    for (i in firstlayout){
                        i.setBackgroundResource(R.drawable.sparkling_border)
                    }
                }
            }
            floor2type = floor2.getInt("type")
            when(floor2type){
                1 -> {
                    mainPageBinding.floor2Type.setText("Red")
                    mainPageBinding.floor2Type.setBackgroundResource(R.drawable.red_back)
                    for (i in secondlayout){
                        i.setBackgroundResource(R.drawable.red_border)
                    }
                }
                2 -> {
                    mainPageBinding.floor2Type.setText("White")
                    mainPageBinding.floor2Type.setBackgroundResource(R.drawable.white_back)
                    for (i in secondlayout){
                        i.setBackgroundResource(R.drawable.white_border)
                    }
                }
                3 -> {
                    mainPageBinding.floor2Type.setText("Sparkling")
                    mainPageBinding.floor2Type.setBackgroundResource(R.drawable.sparkling_back)
                    for (i in secondlayout){
                        i.setBackgroundResource(R.drawable.sparkling_border)
                    }
                }
            }
            floor3type = floor3.getInt("type")
            when(floor3type){
                1 -> {
                    mainPageBinding.floor3Type.setText("Red")
                    mainPageBinding.floor3Type.setBackgroundResource(R.drawable.red_back)
                    for (i in thirdlayout){
                        i.setBackgroundResource(R.drawable.red_border)
                    }
                }
                2 -> {
                    mainPageBinding.floor3Type.setText("White")
                    mainPageBinding.floor3Type.setBackgroundResource(R.drawable.white_back)
                    for (i in thirdlayout){
                        i.setBackgroundResource(R.drawable.white_border)
                    }
                }
                3 -> {
                    mainPageBinding.floor3Type.setText("Sparkling")
                    mainPageBinding.floor3Type.setBackgroundResource(R.drawable.sparkling_back)
                    for (i in thirdlayout){
                        i.setBackgroundResource(R.drawable.sparkling_border)
                    }
                }
            }
            floor1smart = floor1.getBoolean("is_smart_mode")
            when(floor1smart){
                true -> {}
                false -> {
                    mainPageBinding.floor1Type.setText("User")
                    mainPageBinding.floor1Type.setBackgroundResource(R.drawable.user_back)
                    for (i in firstlayout) {
                        i.setBackgroundResource(R.drawable.user_border)
                    }
                }
            }
            floor2smart = floor2.getBoolean("is_smart_mode")
            when(floor2smart){
                true -> {}
                false -> {
                    mainPageBinding.floor2Type.setText("User")
                    mainPageBinding.floor2Type.setBackgroundResource(R.drawable.user_back)
                    for (i in secondlayout) {
                        i.setBackgroundResource(R.drawable.user_border)
                    }
                }
            }
            floor3smart = floor3.getBoolean("is_smart_mode")
            when(floor3smart){
                true -> {}
                false -> {
                    mainPageBinding.floor3Type.setText("User")
                    mainPageBinding.floor3Type.setBackgroundResource(R.drawable.user_back)
                    for (i in thirdlayout) {
                        i.setBackgroundResource(R.drawable.user_border)
                    }
                }
            }
            mainPageBinding.floor1Now.setText("Now: ${floor1.getInt("temperature_now")}℃")
            mainPageBinding.floor2Now.setText("Now: ${floor2.getInt("temperature_now")}℃")
            mainPageBinding.floor3Now.setText("Now: ${floor3.getInt("temperature_now")}℃")
            mainPageBinding.floor1Target.setText("Target: ${floor1.getInt("temperature_target")}℃")
            mainPageBinding.floor2Target.setText("Target: ${floor2.getInt("temperature_target")}℃")
            mainPageBinding.floor3Target.setText("Target: ${floor3.getInt("temperature_target")}℃")
            for ((index,i) in firstfloor.withIndex()){
                for (j in 0 until floor1wines.length()){
                    val wine:JSONObject = floor1wines.getJSONObject(j)
                    if (wine.getInt("col") == index+1){
                        try {
                            val floor1wine = wine.getJSONObject("wine_id")
                            var aromaList: MutableList<AromaInfo> = mutableListOf()
                            var pairingList: MutableList<PairingInfo> = mutableListOf()
                            var wineAromas: JSONArray = JSONArray()
                            var winePairings: JSONArray = JSONArray()
                            try {
                                wineAromas = floor1wine.getJSONArray("aroma")
                            } catch (e: Exception) { }
                            try{
                                winePairings = floor1wine.getJSONArray("pairing")
                            } catch (e:Exception){ }
                            when(floor1wine.getString("type")){
                                "Red"-> firstlayout[index].setBackgroundResource(R.drawable.red_border)
                                "White"-> firstlayout[index].setBackgroundResource(R.drawable.white_border)
                                "Sparkling" -> firstlayout[index].setBackgroundResource(R.drawable.sparkling_border)
                            }
                            for (k in 0 until wineAromas.length()) {
                                var wineAroma = AromaInfo("", "", "", mutableListOf())
                                var nameList: MutableList<String> = mutableListOf()
                                var aromaNames = wineAromas.getJSONObject(k).getJSONArray("aroma_names")
                                wineAroma.Aroma_Id = wineAromas.getJSONObject(k).getString("_id")
                                wineAroma.Aroma_category =
                                    wineAromas.getJSONObject(k).getString("category")
                                wineAroma.Aroma_image =
                                    wineAromas.getJSONObject(k).getString("imgsrc")
                                for (l in 0 until aromaNames.length()) {
                                    var aromaName: String = aromaNames.getString(l)
                                    nameList.add(aromaName)
                                }
                                wineAroma.Aroma_names = nameList
                                aromaList.add(wineAroma)
                            }
                            for (k in 0 until winePairings.length()) {
                                var winePairing = PairingInfo("", "", "", mutableListOf())
                                var nameList: MutableList<String> = mutableListOf()
                                var pairingNames = winePairings.getJSONObject(k).getJSONArray("pairing_names")
                                winePairing.Pairing_Id = winePairings.getJSONObject(k).getString("_id")
                                winePairing.Pairing_category = winePairings.getJSONObject(k).getString("category")
                                winePairing.Pairing_image = winePairings.getJSONObject(k).getString("imgsrc")
                                for (l in 0 until pairingNames.length()) {
                                    var pairingName: String = pairingNames.getString(l)
                                    nameList.add(pairingName)
                                }
                                winePairing.Pairing_names = nameList
                                pairingList.add(winePairing)
                            }
                            try {
                                GlideApp.with(this).asBitmap()
                                    .load(floor1wine.getString("imgsrc"))
                                    .into(i)
                            } catch (e: Exception) { }
                            WineList1.add(
                                WineInfo(wine.getInt("row"),
                                    wine.getInt("col") - 1,
                                    floor1wine.getString("_id"),
                                    floor1wine.getString("eng_name"),
                                    floor1wine.getString("imgsrc"),
                                    floor1wine.getInt("price"),
                                    floor1wine.getInt("sweet"),
                                    floor1wine.getInt("acid"),
                                    floor1wine.getInt("body"),
                                    floor1wine.getInt("tannin"),
                                    aromaList,
                                    floor1wine.getString("alcohol"),
                                    floor1wine.getInt("temp"),
                                    floor1wine.getString("type"),
                                    pairingList
                                )
                            )
                        }catch (e:Exception){ }
                    }
                }
                i.clipToOutline = true
            }
            for ((index,i) in secondfloor.withIndex()){
                for (j in 0 until floor2wines.length()){
                    val wine:JSONObject = floor2wines.getJSONObject(j)
                    if (wine.getInt("col") == index+1){
                        try {
                            val floor2wine = wine.getJSONObject("wine_id")
                            var aromaList: MutableList<AromaInfo> = mutableListOf()
                            var pairingList: MutableList<PairingInfo> = mutableListOf()
                            var wineAromas: JSONArray = JSONArray()
                            var winePairings: JSONArray = JSONArray()
                            try {
                                wineAromas = floor2wine.getJSONArray("aroma")
                            } catch (e: Exception) { }
                            try{
                                winePairings = floor2wine.getJSONArray("pairing")
                            } catch (e:Exception){ }
                            when(floor2wine.getString("type")){
                                "Red"-> secondlayout[index].setBackgroundResource(R.drawable.red_border)
                                "White"-> secondlayout[index].setBackgroundResource(R.drawable.white_border)
                                "Sparkling" -> secondlayout[index].setBackgroundResource(R.drawable.sparkling_border)
                            }
                            for (k in 0 until wineAromas.length()) {
                                var wineAroma = AromaInfo("", "", "", mutableListOf())
                                var nameList: MutableList<String> = mutableListOf()
                                var aromaNames = wineAromas.getJSONObject(k).getJSONArray("aroma_names")
                                wineAroma.Aroma_Id = wineAromas.getJSONObject(k).getString("_id")
                                wineAroma.Aroma_category =
                                    wineAromas.getJSONObject(k).getString("category")
                                wineAroma.Aroma_image =
                                    wineAromas.getJSONObject(k).getString("imgsrc")
                                for (l in 0 until aromaNames.length()) {
                                    var aromaName: String = aromaNames.getString(l)
                                    nameList.add(aromaName)
                                }
                                wineAroma.Aroma_names = nameList
                                aromaList.add(wineAroma)
                            }
                            for (k in 0 until winePairings.length()) {
                                var winePairing = PairingInfo("", "", "", mutableListOf())
                                var nameList: MutableList<String> = mutableListOf()
                                var pairingNames = winePairings.getJSONObject(k).getJSONArray("pairing_names")
                                winePairing.Pairing_Id = winePairings.getJSONObject(k).getString("_id")
                                winePairing.Pairing_category = winePairings.getJSONObject(k).getString("category")
                                winePairing.Pairing_image = winePairings.getJSONObject(k).getString("imgsrc")
                                for (l in 0 until pairingNames.length()) {
                                    var pairingName: String = pairingNames.getString(l)
                                    nameList.add(pairingName)
                                }
                                winePairing.Pairing_names = nameList
                                pairingList.add(winePairing)
                            }
                            try {
                                GlideApp.with(this).asBitmap()
                                    .load(floor2wine.getString("imgsrc"))
                                    .into(i)
                            } catch (e: Exception) { }
                            WineList2.add(
                                WineInfo(wine.getInt("row"),
                                    wine.getInt("col") - 1,
                                    floor2wine.getString("_id"),
                                    floor2wine.getString("eng_name"),
                                    floor2wine.getString("imgsrc"),
                                    floor2wine.getInt("price"),
                                    floor2wine.getInt("sweet"),
                                    floor2wine.getInt("acid"),
                                    floor2wine.getInt("body"),
                                    floor2wine.getInt("tannin"),
                                    aromaList,
                                    floor2wine.getString("alcohol"),
                                    floor2wine.getInt("temp"),
                                    floor2wine.getString("type"),
                                    pairingList
                                )
                            )
                        }catch (e:Exception){ }
                    }
                }
                i.clipToOutline = true
            }
            for ((index,i) in thirdfloor.withIndex()){
                for (j in 0 until floor3wines.length()){
                    val wine:JSONObject = floor3wines.getJSONObject(j)
                    if (wine.getInt("col") == index+1){
                        try {
                            val floor3wine = wine.getJSONObject("wine_id")
                            var aromaList: MutableList<AromaInfo> = mutableListOf()
                            var pairingList: MutableList<PairingInfo> = mutableListOf()
                            var wineAromas: JSONArray = JSONArray()
                            var winePairings: JSONArray = JSONArray()
                            try {
                                wineAromas = floor3wine.getJSONArray("aroma")
                            } catch (e: Exception) { }
                            try{
                                winePairings = floor3wine.getJSONArray("pairing")
                            } catch (e:Exception){ }
                            when(floor3wine.getString("type")){
                                "Red"-> thirdlayout[index].setBackgroundResource(R.drawable.red_border)
                                "White"-> thirdlayout[index].setBackgroundResource(R.drawable.white_border)
                                "Sparkling" -> thirdlayout[index].setBackgroundResource(R.drawable.sparkling_border)
                            }
                            for (k in 0 until wineAromas.length()) {
                                var wineAroma = AromaInfo("", "", "", mutableListOf())
                                var nameList: MutableList<String> = mutableListOf()
                                var aromaNames = wineAromas.getJSONObject(k).getJSONArray("aroma_names")
                                wineAroma.Aroma_Id = wineAromas.getJSONObject(k).getString("_id")
                                wineAroma.Aroma_category =
                                    wineAromas.getJSONObject(k).getString("category")
                                wineAroma.Aroma_image =
                                    wineAromas.getJSONObject(k).getString("imgsrc")
                                for (l in 0 until aromaNames.length()) {
                                    var aromaName: String = aromaNames.getString(l)
                                    nameList.add(aromaName)
                                }
                                wineAroma.Aroma_names = nameList
                                aromaList.add(wineAroma)
                            }
                            for (k in 0 until winePairings.length()) {
                                var winePairing = PairingInfo("", "", "", mutableListOf())
                                var nameList: MutableList<String> = mutableListOf()
                                var pairingNames = winePairings.getJSONObject(k).getJSONArray("pairing_names")
                                winePairing.Pairing_Id = winePairings.getJSONObject(k).getString("_id")
                                winePairing.Pairing_category = winePairings.getJSONObject(k).getString("category")
                                winePairing.Pairing_image = winePairings.getJSONObject(k).getString("imgsrc")
                                for (l in 0 until pairingNames.length()) {
                                    var pairingName: String = pairingNames.getString(l)
                                    nameList.add(pairingName)
                                }
                                winePairing.Pairing_names = nameList
                                pairingList.add(winePairing)
                            }
                            try {
                                GlideApp.with(this).asBitmap()
                                    .load(floor3wine.getString("imgsrc"))
                                    .into(i)
                            } catch (e: Exception) { }
                            WineList3.add(
                                WineInfo(wine.getInt("row"),
                                    wine.getInt("col") - 1,
                                    floor3wine.getString("_id"),
                                    floor3wine.getString("eng_name"),
                                    floor3wine.getString("imgsrc"),
                                    floor3wine.getInt("price"),
                                    floor3wine.getInt("sweet"),
                                    floor3wine.getInt("acid"),
                                    floor3wine.getInt("body"),
                                    floor3wine.getInt("tannin"),
                                    aromaList,
                                    floor3wine.getString("alcohol"),
                                    floor3wine.getInt("temp"),
                                    floor3wine.getString("type"),
                                    pairingList
                                )
                            )
                        }catch (e:Exception){ }
                    }
                }
                i.clipToOutline = true
            }
        }

//        val url = "http://10.0.2.2:3000/winecellar/status?id=64ae2b0848a3d71c485e2472"
        var url = "http://13.48.52.200:3000/winecellar/status?id=64b4f9a38b4dc227def9b5b1"
        val queue : RequestQueue = Volley.newRequestQueue(applicationContext)

        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            Log.d("responseebal",response.toString())
            floor1 = response.getJSONObject("floor1")
            floor2 = response.getJSONObject("floor2")
            floor3 = response.getJSONObject("floor3")
            displayWine()
            }, { error ->
            Log.e("TAGa", "RESPONSE IS $error")
            // in this case we are simply displaying a toast message.
            Toast.makeText(this@MainPage, "Fail to get response", Toast.LENGTH_SHORT)
                .show()
            })
        queue.add(request)

        setContentView(mainPageBinding.root)
        mainPageBinding.addWine.setOnClickListener() {
            requestCameraAndStartScanner()
        }
        mainPageBinding.settings.setOnClickListener(){
            val intent = Intent(this,SettingPage::class.java)
            startActivity(intent)
        }
        mainPageBinding.mainSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // On 할 때
                isInfo=false
                mainPageBinding.infoMove.setText("Move")
            } else {
                if(wineBefore.id == ""){
                    isInfo=true
                    mainPageBinding.infoMove.setText("Info")
                }
            }
        }
        mainPageBinding.refresh.setOnClickListener(){
            finish()
            overridePendingTransition(0, 0) //인텐트 효과 없애기
            val intent = intent //인텐트
            startActivity(intent) //액티비티 열기
            overridePendingTransition(0, 0) //인텐트 효과 없애기
        }

        fun openWineinfo(w:WineInfo){
            val wineInfoBuilder = AlertDialog.Builder(this)
                .setView(wineInfoView)
            wineInfoBinding.scanName.text = w.Wine_Name
            GlideApp.with(this).asBitmap()
                .load(w.Wine_Image)
                .into(wineInfoBinding.scanImage)
            when (w.Wine_Sweet) {
                1 -> wineInfoBinding.sweetLevel.background = getDrawable(R.drawable.level1)
                2 -> wineInfoBinding.sweetLevel.background = getDrawable(R.drawable.level2)
                3 -> wineInfoBinding.sweetLevel.background = getDrawable(R.drawable.level3)
                4 -> wineInfoBinding.sweetLevel.background = getDrawable(R.drawable.level4)
                5 -> wineInfoBinding.sweetLevel.background = getDrawable(R.drawable.level5)
            }
            when (w.Wine_Acid) {
                1 -> wineInfoBinding.sourLevel.background = getDrawable(R.drawable.level1)
                2 -> wineInfoBinding.sourLevel.background = getDrawable(R.drawable.level2)
                3 -> wineInfoBinding.sourLevel.background = getDrawable(R.drawable.level3)
                4 -> wineInfoBinding.sourLevel.background = getDrawable(R.drawable.level4)
                5 -> wineInfoBinding.sourLevel.background = getDrawable(R.drawable.level5)
            }
            when (w.Wine_Body) {
                1 -> wineInfoBinding.bodyLevel.background = getDrawable(R.drawable.level1)
                2 -> wineInfoBinding.bodyLevel.background = getDrawable(R.drawable.level2)
                3 -> wineInfoBinding.bodyLevel.background = getDrawable(R.drawable.level3)
                4 -> wineInfoBinding.bodyLevel.background = getDrawable(R.drawable.level4)
                5 -> wineInfoBinding.bodyLevel.background = getDrawable(R.drawable.level5)
            }
            when (w.Wine_Tannin) {
                1 -> wineInfoBinding.tanninLevel.background = getDrawable(R.drawable.level1)
                2 -> wineInfoBinding.tanninLevel.background = getDrawable(R.drawable.level2)
                3 -> wineInfoBinding.tanninLevel.background = getDrawable(R.drawable.level3)
                4 -> wineInfoBinding.tanninLevel.background = getDrawable(R.drawable.level4)
                5 -> wineInfoBinding.tanninLevel.background = getDrawable(R.drawable.level5)
            }
            if (w.Wine_Price != 0){
                wineInfoBinding.price.text = "PRICE : ₹"+w.Wine_Price.toString()
                wineInfoBinding.price.visibility = View.VISIBLE
            }
            else{
                wineInfoBinding.price.visibility = View.GONE
            }
            if (w.Wine_Alcohol != "null"){
                wineInfoBinding.alcohol.text = "ALC. : "+w.Wine_Alcohol
                wineInfoBinding.alcohol.visibility = View.VISIBLE
            }
            else{
                wineInfoBinding.alcohol.visibility = View.GONE
            }
            wineInfoBinding.temp.text = "TEMP. : " + w.Wine_Temp.toString() + "°C"
            if (w.Wine_Aromas?.isEmpty() == true){
                wineInfoBinding.aromaContainer.visibility = View.GONE
            }
            else {
                wineInfoBinding.aromaContainer.visibility = View.VISIBLE
                when (w.Wine_Aromas?.size){
                    1 -> {
                        GlideApp.with(this).asBitmap()
                            .load(w.Wine_Aromas!![0].Aroma_image)
                            .into(wineInfoBinding.aroma1img)
                        wineInfoBinding.aroma1txt.text = w.Wine_Aromas!![0].Aroma_names?.joinToString()
                        wineInfoBinding.aroma1.visibility = View.VISIBLE
                        wineInfoBinding.aroma2.visibility = View.GONE
                        wineInfoBinding.aroma3.visibility = View.GONE
                        wineInfoBinding.aroma4.visibility = View.GONE
                    }
                    2 -> {
                        GlideApp.with(this).asBitmap()
                            .load(w.Wine_Aromas!![0].Aroma_image)
                            .into(wineInfoBinding.aroma1img)
                        wineInfoBinding.aroma1txt.text = w.Wine_Aromas!![0].Aroma_names?.joinToString()
                        GlideApp.with(this).asBitmap()
                            .load(w.Wine_Aromas!![1].Aroma_image)
                            .into(wineInfoBinding.aroma2img)
                        wineInfoBinding.aroma2txt.text = w.Wine_Aromas!![1].Aroma_names?.joinToString()
                        wineInfoBinding.aroma1.visibility = View.VISIBLE
                        wineInfoBinding.aroma2.visibility = View.VISIBLE
                        wineInfoBinding.aroma3.visibility = View.GONE
                        wineInfoBinding.aroma4.visibility = View.GONE
                    }
                    3 -> {
                        GlideApp.with(this).asBitmap()
                            .load(w.Wine_Aromas!![0].Aroma_image)
                            .into(wineInfoBinding.aroma1img)
                        wineInfoBinding.aroma1txt.text = w.Wine_Aromas!![0].Aroma_names?.joinToString()
                        GlideApp.with(this).asBitmap()
                            .load(w.Wine_Aromas!![1].Aroma_image)
                            .into(wineInfoBinding.aroma2img)
                        wineInfoBinding.aroma2txt.text = w.Wine_Aromas!![1].Aroma_names?.joinToString()
                        GlideApp.with(this).asBitmap()
                            .load(w.Wine_Aromas!![2].Aroma_image)
                            .into(wineInfoBinding.aroma3img)
                        wineInfoBinding.aroma3txt.text = w.Wine_Aromas!![2].Aroma_names?.joinToString()
                        wineInfoBinding.aroma1.visibility = View.VISIBLE
                        wineInfoBinding.aroma2.visibility = View.VISIBLE
                        wineInfoBinding.aroma3.visibility = View.VISIBLE
                        wineInfoBinding.aroma4.visibility = View.GONE
                    }
                    4 -> {
                        GlideApp.with(this).asBitmap()
                            .load(w.Wine_Aromas!![0].Aroma_image)
                            .into(wineInfoBinding.aroma1img)
                        wineInfoBinding.aroma1txt.text = w.Wine_Aromas!![0].Aroma_names?.joinToString()
                        GlideApp.with(this).asBitmap()
                            .load(w.Wine_Aromas!![1].Aroma_image)
                            .into(wineInfoBinding.aroma2img)
                        wineInfoBinding.aroma2txt.text = w.Wine_Aromas!![1].Aroma_names?.joinToString()
                        GlideApp.with(this).asBitmap()
                            .load(w.Wine_Aromas!![2].Aroma_image)
                            .into(wineInfoBinding.aroma3img)
                        wineInfoBinding.aroma3txt.text = w.Wine_Aromas!![2].Aroma_names?.joinToString()
                        GlideApp.with(this).asBitmap()
                            .load(w.Wine_Aromas!![3].Aroma_image)
                            .into(wineInfoBinding.aroma4img)
                        wineInfoBinding.aroma4txt.text = w.Wine_Aromas!![3].Aroma_names?.joinToString()
                        wineInfoBinding.aroma1.visibility = View.VISIBLE
                        wineInfoBinding.aroma2.visibility = View.VISIBLE
                        wineInfoBinding.aroma3.visibility = View.VISIBLE
                        wineInfoBinding.aroma4.visibility = View.VISIBLE
                    }
                }
            }
            if (w.Wine_Pairings?.isEmpty() == true){
                wineInfoBinding.pairingContainer.visibility = View.GONE
            }
            else {
                wineInfoBinding.pairingContainer.visibility = View.VISIBLE
                when (w.Wine_Pairings?.size){
                    1 -> {
                        GlideApp.with(this).asBitmap()
                            .load(w.Wine_Pairings!![0].Pairing_image)
                            .into(wineInfoBinding.pair1img)
                        wineInfoBinding.pair1txt.text = w.Wine_Pairings!![0].Pairing_names?.joinToString()
                        wineInfoBinding.pair1.visibility = View.VISIBLE
                        wineInfoBinding.pair2.visibility = View.GONE
                        wineInfoBinding.pair3.visibility = View.GONE
                        wineInfoBinding.pair4.visibility = View.GONE
                    }
                    2 -> {
                        GlideApp.with(this).asBitmap()
                            .load(w.Wine_Pairings!![0].Pairing_image)
                            .into(wineInfoBinding.pair1img)
                        wineInfoBinding.pair1txt.text = w.Wine_Pairings!![0].Pairing_names?.joinToString()
                        GlideApp.with(this).asBitmap()
                            .load(w.Wine_Pairings!![1].Pairing_image)
                            .into(wineInfoBinding.pair2img)
                        wineInfoBinding.pair2txt.text = w.Wine_Pairings!![1].Pairing_names?.joinToString()
                        wineInfoBinding.pair1.visibility = View.VISIBLE
                        wineInfoBinding.pair2.visibility = View.VISIBLE
                        wineInfoBinding.pair3.visibility = View.GONE
                        wineInfoBinding.pair4.visibility = View.GONE
                    }
                    3 -> {
                        GlideApp.with(this)
                            .asBitmap()
                            .load(w.Wine_Pairings!![0].Pairing_image)
                            .into(wineInfoBinding.pair1img)
                        wineInfoBinding.pair1txt.text = w.Wine_Pairings!![0].Pairing_names?.joinToString()
                        GlideApp.with(this)
                            .asBitmap()
                            .load(w.Wine_Pairings!![1].Pairing_image)
                            .into(wineInfoBinding.pair2img)
                        wineInfoBinding.pair2txt.text = w.Wine_Pairings!![1].Pairing_names?.joinToString()
                        GlideApp.with(this)
                            .asBitmap()
                            .load(w.Wine_Pairings!![2].Pairing_image)
                            .into(wineInfoBinding.pair3img)
                        wineInfoBinding.pair3txt.text = w.Wine_Pairings!![2].Pairing_names?.joinToString()
                        wineInfoBinding.pair1.visibility = View.VISIBLE
                        wineInfoBinding.pair2.visibility = View.VISIBLE
                        wineInfoBinding.pair3.visibility = View.VISIBLE
                        wineInfoBinding.pair4.visibility = View.GONE
                    }
                    4 -> {
                        GlideApp.with(this).asBitmap()
                            .load(w.Wine_Pairings!![0].Pairing_image)
                            .into(wineInfoBinding.pair1img)
                        wineInfoBinding.pair1txt.text = w.Wine_Pairings!![0].Pairing_names?.joinToString()
                        GlideApp.with(this).asBitmap()
                            .load(w.Wine_Pairings!![1].Pairing_image)
                            .into(wineInfoBinding.pair2img)
                        wineInfoBinding.pair2txt.text = w.Wine_Pairings!![1].Pairing_names?.joinToString()
                        GlideApp.with(this).asBitmap()
                            .load(w.Wine_Pairings!![2].Pairing_image)
                            .into(wineInfoBinding.pair3img)
                        wineInfoBinding.pair3txt.text = w.Wine_Pairings!![2].Pairing_names?.joinToString()
                        GlideApp.with(this).asBitmap()
                            .load(w.Wine_Pairings!![3].Pairing_image)
                            .into(wineInfoBinding.pair4img)
                        wineInfoBinding.pair4txt.text = w.Wine_Pairings!![3].Pairing_names?.joinToString()
                        wineInfoBinding.pair1.visibility = View.VISIBLE
                        wineInfoBinding.pair2.visibility = View.VISIBLE
                        wineInfoBinding.pair3.visibility = View.VISIBLE
                        wineInfoBinding.pair4.visibility = View.VISIBLE
                    }
                }
            }
            if(wineInfoView.getParent() !=null){
                (wineInfoView.getParent() as ViewGroup).removeView(wineInfoView)
            }
            val wineInfoDialog = wineInfoBuilder.show()
            wineInfoBinding.takeWine.setOnClickListener(){
                val getqueue : RequestQueue = Volley.newRequestQueue(this@MainPage)
                val postqueue : RequestQueue = Volley.newRequestQueue(this@MainPage)
                var postURL = "http://13.48.52.200:3000/winecellar"
                val postDataReq = """
                                {
                                    "cellarid": "64b4f9a38b4dc227def9b5b1",
                                    "row": ${w.Wine_Floor},
                                    "col": ${w.Wine_Location + 1}
                                }
                                """.trimIndent()
                var post_data1:JSONObject = JSONObject(postDataReq)
                val moveRequest = JsonObjectRequest(Request.Method.POST, postURL, post_data1, { response ->
                    Toast.makeText(this@MainPage, "Wine is taken out.", Toast.LENGTH_SHORT)
                        .show()
                }, { error ->
                    Log.e("TAGa", "RESPONSE IS $error")
                    // in this case we are simply displaying a toast message.
                    Toast.makeText(this@MainPage, "Fail to get response", Toast.LENGTH_SHORT)
                        .show()
                })
                postqueue.add(moveRequest)
                Toast.makeText(this@MainPage, "Wine Move Complete.", Toast.LENGTH_SHORT)
                    .show()
                val request1 = JsonObjectRequest(Request.Method.GET, url, null, { response ->
                    floor1 = response.getJSONObject("floor1")
                    floor2 = response.getJSONObject("floor2")
                    floor3 = response.getJSONObject("floor3")
                    displayWine()
                    wineInfoDialog.dismiss()
                    finish()
                    overridePendingTransition(0, 0) //인텐트 효과 없애기
                    val intent = intent //인텐트
                    startActivity(intent) //액티비티 열기
                    overridePendingTransition(0, 0) //인텐트 효과 없애기
                }, { error ->
                    Log.e("TAGa", "RESPONSE IS $error")
                    // in this case we are simply displaying a toast message.
                    Toast.makeText(this@MainPage, "Fail to get response", Toast.LENGTH_SHORT)
                        .show()
                })
                getqueue.add(request1)
            }
            wineInfoBinding.reserve.setOnClickListener() {
                wineInfoDialog.dismiss()
//                finish()
//                overridePendingTransition(0, 0) //인텐트 효과 없애기
//                val intent = intent //인텐트
//                startActivity(intent) //액티비티 열기
//                overridePendingTransition(0, 0) //인텐트 효과 없애기
                reserveBinding.minuteET.setText("00")
                reserveBinding.hourET.setText("00")
                val reserveBuilder = AlertDialog.Builder(this)
                    .setView(reserveView)
                if(reserveView.getParent() !=null){
                    (reserveView.getParent() as ViewGroup).removeView(reserveView)
                }
                val reserveDialog = reserveBuilder.show()
                reserveBinding.proceed.setOnClickListener() {

                    var hour = Integer.parseInt(reserveBinding.hourET.getText().toString())
                    var minute = Integer.parseInt(reserveBinding.minuteET.getText().toString())

                    var reserveTime = (hour * 60 + minute)*1000
                    val postReserveQueue : RequestQueue = Volley.newRequestQueue(this@MainPage)
                    val postReserveURL = "http://13.48.52.200:3000/winecellar/reserve"
                    val postReserveReq = """
                                                {
                                                    "cellar_id": "64b4f9a38b4dc227def9b5b1",
                                                    "row": ${wineInfoTemp.Wine_Floor},
                                                    "col": ${wineInfoTemp.Wine_Location + 1}
                                                }
                                                """.trimIndent()
                    var post_reserve_data:JSONObject = JSONObject(postReserveReq)
                    val reserveRequest = JsonObjectRequest(Request.Method.POST, postReserveURL, post_reserve_data, { response ->
                    }, { error ->
                        Log.e("TAGa", "RESPONSE IS $error")
                        // in this case we are simply displaying a toast message.
                        Toast.makeText(this@MainPage, "Fail to get response", Toast.LENGTH_SHORT)
                            .show()
                    })
                    postReserveQueue.add(reserveRequest)
                    Toast.makeText(this@MainPage, "Your reserved wine will be ready in ${hour} : ${minute}", Toast.LENGTH_SHORT)
                        .show()
                    val handler = Handler()
                    val handlerTask = object : Runnable {
                        override fun run() {
                            Toast.makeText(this@MainPage,"Your wine is ready to be served!",Toast.LENGTH_SHORT).show()
                        }
                    }
                    handler.postDelayed(handlerTask, reserveTime.toLong())
                    reserveDialog.dismiss()
                    finish()
                    overridePendingTransition(0, 0) //인텐트 효과 없애기
                    val intent = intent //인텐트
                    startActivity(intent) //액티비티 열기
                    overridePendingTransition(0, 0) //인텐트 효과 없애기
                }
                reserveBinding.cancel.setOnClickListener(){
                    reserveDialog.dismiss()
                }
            }
        }


        val cellListener  = object : View.OnClickListener  {
            override fun onClick (v:View?){
                var clickedCellIndex = v?.id.toString().toInt() - btnIdNumber
                var clickedWineIndex = clickedCellIndex % 5//move 상황
                if (!isInfo) {
                    var spaceVacant = true
                    if (clickedCellIndex<5){
                        for (w in WineList1){
                            if (w.Wine_Location == clickedWineIndex){
                                spaceVacant = false
                                Log.d("cellllspacevacant", spaceVacant.toString())
                                break
                            }
                        }
                    }
                    else if (clickedCellIndex<10){
                        Log.d("cellllspacevacant1", spaceVacant.toString())
                        Log.d("cellllspacevacant2", WineList2.size.toString())
                        for (w in WineList2){
                            if (w.Wine_Location == clickedWineIndex){
                                spaceVacant = false
                                break
                            }
                        }
                    }
                    else{
                        for (w in WineList3){
                            if (w.Wine_Location == clickedWineIndex){
                                spaceVacant = false
                                break
                            }
                        }
                    }
                    if (spaceVacant) {
                        var clickedFloor:Int = clickedCellIndex / 5
                        var checkFloorWine = false
                        if(isWineSelected){
                            if (clickedFloor == 0){
                                if (!floor1smart || floor1type == wineBefore.Wine_Floor_Type){
                                    checkFloorWine = true
                                }
                            }
                            else if (clickedFloor == 1) {
                                if (!floor2smart || floor2type == wineBefore.Wine_Floor_Type){
                                    checkFloorWine = true
                                }
                            }
                            else {
                                if (!floor3smart || floor3type == wineBefore.Wine_Floor_Type){
                                    checkFloorWine = true
                                }
                            }
                            if(checkFloorWine) {
                                isWineSelected = false
                                wineAfter = wineBefore.clone()
                                wineAfter.Wine_Location = clickedWineIndex
                                if (clickedCellIndex < 5) {
                                    wineAfter.Wine_Floor = 1
                                    WineList1.add(wineAfter)
                                }
                                else if (clickedCellIndex < 10) {
                                    wineAfter.Wine_Floor = 2
                                    WineList2.add(wineAfter)
                                }
                                else {
                                    wineAfter.Wine_Floor = 3
                                    WineList3.add(wineAfter)
                                }
                                val postqueue : RequestQueue = Volley.newRequestQueue(this@MainPage)
                                val getqueue : RequestQueue = Volley.newRequestQueue(this@MainPage)
                                var postURL = "http://13.48.52.200:3000/winecellar/move"
                                val postDataReq = """
                                                {
                                                    "cellarid": "64b4f9a38b4dc227def9b5b1",
                                                    "wine_id": ${wineBefore.Wine_Id.toString()},
                                                    "wine1_row": ${wineBefore.Wine_Floor},
                                                    "wine1_col": ${wineBefore.Wine_Location + 1},
                                                    "wine2_row": ${wineAfter.Wine_Floor},
                                                    "wine2_col": ${wineAfter.Wine_Location + 1}
                                                }
                                                """.trimIndent()
                                var post_data:JSONObject = JSONObject(postDataReq)
                                val moveRequest = JsonObjectRequest(Request.Method.POST, postURL, post_data, { response ->
                                }, { error ->
                                    Log.e("TAGa", "RESPONSE IS $error")
                                    // in this case we are simply displaying a toast message.
                                    Toast.makeText(this@MainPage, "Fail to get response", Toast.LENGTH_SHORT)
                                        .show()
                                })
                                postqueue.add(moveRequest)
                                Toast.makeText(this@MainPage, "Wine Move Complete.", Toast.LENGTH_SHORT)
                                    .show()
                                val request1 = JsonObjectRequest(Request.Method.GET, url, null, { response ->
                                    floor1 = response.getJSONObject("floor1")
                                    floor2 = response.getJSONObject("floor2")
                                    floor3 = response.getJSONObject("floor3")
//                                    displayWine()
                                    mainPageBinding.mainSwitch.isEnabled = true
                                    finish()
                                    overridePendingTransition(0, 0) //인텐트 효과 없애기
                                    val intent = intent //인텐트
                                    startActivity(intent) //액티비티 열기
                                    overridePendingTransition(0, 0) //인텐트 효과 없애기
                                }, { error ->
                                    Log.e("TAGa", "RESPONSE IS $error")
                                    // in this case we are simply displaying a toast message.
                                    Toast.makeText(this@MainPage, "Fail to get response", Toast.LENGTH_SHORT)
                                        .show()
                                })
                                getqueue.add(request1)
                            }
                            else{
                                Toast.makeText(this@MainPage, "Wine-Floor type mismatch!\nTry again.", Toast.LENGTH_SHORT)
                                    .show()
                                mainPageBinding.mainSwitch.isEnabled = true
                                isWineSelected = false
                                when(wineBefore.Wine_Floor){
                                    1 -> WineList1.add(wineBefore)
                                    2 -> WineList2.add(wineBefore)
                                    3 -> WineList3.add(wineBefore)
                                }
                                when(wineBefore.Wine_Floor){
                                    1 -> {
                                        firstlayout[wineBefore.Wine_Location].elevation = 0F
                                        when(wineBefore.Wine_Type){
                                            "Red"-> firstlayout[wineBefore.Wine_Location].setBackgroundResource(R.drawable.red_border)
                                            "White"-> firstlayout[wineBefore.Wine_Location].setBackgroundResource(R.drawable.white_border)
                                            "Sparkling" -> firstlayout[wineBefore.Wine_Location].setBackgroundResource(R.drawable.sparkling_border)
                                        }
                                    }
                                    2 -> {
                                        secondlayout[wineBefore.Wine_Location].elevation = 0F
                                        when(wineBefore.Wine_Type){
                                            "Red"-> secondlayout[wineBefore.Wine_Location].setBackgroundResource(R.drawable.red_border)
                                            "White"-> secondlayout[wineBefore.Wine_Location].setBackgroundResource(R.drawable.white_border)
                                            "Sparkling" -> secondlayout[wineBefore.Wine_Location].setBackgroundResource(R.drawable.sparkling_border)
                                        }
                                    }
                                    3 -> {
                                        thirdlayout[wineBefore.Wine_Location].elevation = 0F
                                        when(wineBefore.Wine_Type){
                                            "Red"-> thirdlayout[wineBefore.Wine_Location].setBackgroundResource(R.drawable.red_border)
                                            "White"-> thirdlayout[wineBefore.Wine_Location].setBackgroundResource(R.drawable.white_border)
                                            "Sparkling" -> thirdlayout[wineBefore.Wine_Location].setBackgroundResource(R.drawable.sparkling_border)
                                        }
                                    }
                                }
                            }
                        }
                        else{
                            Toast.makeText(this@MainPage, "Nothing selected.", Toast.LENGTH_SHORT)
                                .show()

                        }
                    }
                    else {
                        //move에서 와인칸
                        if (!isWineSelected){
                            //아직 옮길 와인 선택 안됨
                            mainPageBinding.mainSwitch.isEnabled = false
                            isWineSelected = true
                            if (clickedCellIndex < 5) { // 1층
                                for ((index,w) in WineList1.withIndex()) {
                                    if (w.Wine_Location == clickedWineIndex){
                                        wineBefore = w.clone()
                                        firstlayout[w.Wine_Location].setBackgroundResource(R.drawable.select_border)
                                        firstlayout[w.Wine_Location].elevation = 70F
                                        WineList1.removeAt(index)
                                        Toast.makeText(this@MainPage, "Cell 1, Index:${(clickedCellIndex%5)+1} wine selected.", Toast.LENGTH_SHORT)
                                            .show()
                                        break
                                    }
                                }
                            }
                            else if (clickedCellIndex < 10) {
                                for ((index,w) in WineList2.withIndex()) {
                                    if (w.Wine_Location == clickedWineIndex){
                                        wineBefore = w.clone()
                                        secondlayout[w.Wine_Location].setBackgroundResource(R.drawable.select_border)
                                        secondlayout[w.Wine_Location].elevation = 70F
                                        WineList2.removeAt(index)
                                        Toast.makeText(this@MainPage, "Cell 2, Index:${(clickedCellIndex%5)+1} wine selected.", Toast.LENGTH_SHORT)
                                            .show()
                                        break
                                    }
                                }
                            }
                            else {
                                for ((index,w) in WineList3.withIndex()) {
                                    if (w.Wine_Location == clickedWineIndex){
                                        wineBefore = w.clone()
                                        thirdlayout[w.Wine_Location].setBackgroundResource(R.drawable.select_border)
                                        thirdlayout[w.Wine_Location].elevation = 70F
                                        WineList3.removeAt(index)
                                        Toast.makeText(this@MainPage, "Cell 3, Index:${(clickedCellIndex%5)+1} wine selected.", Toast.LENGTH_SHORT)
                                            .show()
                                        break
                                    }
                                }
                            }
                        }
                        else{
                            mainPageBinding.mainSwitch.isEnabled = true
                            Toast.makeText(this@MainPage, "This space is not vacant!", Toast.LENGTH_SHORT)
                                .show()
                            isWineSelected = false
                            when(wineBefore.Wine_Floor){
                                1 -> WineList1.add(wineBefore)
                                2 -> WineList2.add(wineBefore)
                                3 -> WineList3.add(wineBefore)
                            }
                            when(wineBefore.Wine_Floor){
                                1 -> {
                                    firstlayout[wineBefore.Wine_Location].elevation = 0F
                                    when(wineBefore.Wine_Type){
                                        "Red"-> firstlayout[wineBefore.Wine_Location].setBackgroundResource(R.drawable.red_border)
                                        "White"-> firstlayout[wineBefore.Wine_Location].setBackgroundResource(R.drawable.white_border)
                                        "Sparkling" -> firstlayout[wineBefore.Wine_Location].setBackgroundResource(R.drawable.sparkling_border)
                                    }
                                }
                                2 -> {
                                    secondlayout[wineBefore.Wine_Location].elevation = 0F
                                    when(wineBefore.Wine_Type){
                                        "Red"-> secondlayout[wineBefore.Wine_Location].setBackgroundResource(R.drawable.red_border)
                                        "White"-> secondlayout[wineBefore.Wine_Location].setBackgroundResource(R.drawable.white_border)
                                        "Sparkling" -> secondlayout[wineBefore.Wine_Location].setBackgroundResource(R.drawable.sparkling_border)
                                    }
                                }
                                3 -> {
                                    thirdlayout[wineBefore.Wine_Location].elevation = 0F
                                    when(wineBefore.Wine_Type){
                                        "Red"-> thirdlayout[wineBefore.Wine_Location].setBackgroundResource(R.drawable.red_border)
                                        "White"-> thirdlayout[wineBefore.Wine_Location].setBackgroundResource(R.drawable.white_border)
                                        "Sparkling" -> thirdlayout[wineBefore.Wine_Location].setBackgroundResource(R.drawable.sparkling_border)
                                    }
                                }
                            }
                        }
                    }
                }
                else {
                    if (clickedCellIndex < 5) { // 1층
                        for (w in WineList1) {
                            if (w.Wine_Location == clickedWineIndex){
                                wineInfoTemp = w.clone()
                                openWineinfo(w)
                            }
                        }
                    }
                    else if (clickedCellIndex < 10) {
                        for (w in WineList2) {
                            if (w.Wine_Location == clickedWineIndex){
                                wineInfoTemp = w.clone()
                                openWineinfo(w)
                            }
                        }
                    }
                    else {
                        for (w in WineList3) {
                            if (w.Wine_Location == clickedWineIndex) {
                                wineInfoTemp = w.clone()
                                openWineinfo(w)
                            }
                        }
                    }
                }
            }
        }
        mainPageBinding.btn11.setOnClickListener(cellListener)
        mainPageBinding.btn12.setOnClickListener(cellListener)
        mainPageBinding.btn13.setOnClickListener(cellListener)
        mainPageBinding.btn14.setOnClickListener(cellListener)
        mainPageBinding.btn15.setOnClickListener(cellListener)
        mainPageBinding.btn21.setOnClickListener(cellListener)
        mainPageBinding.btn22.setOnClickListener(cellListener)
        mainPageBinding.btn23.setOnClickListener(cellListener)
        mainPageBinding.btn24.setOnClickListener(cellListener)
        mainPageBinding.btn25.setOnClickListener(cellListener)
        mainPageBinding.btn31.setOnClickListener(cellListener)
        mainPageBinding.btn32.setOnClickListener(cellListener)
        mainPageBinding.btn33.setOnClickListener(cellListener)
        mainPageBinding.btn34.setOnClickListener(cellListener)
        mainPageBinding.btn35.setOnClickListener(cellListener)
    }


    private fun requestCameraAndStartScanner() {
        if (isPermissionGranted(cameraPermission)) {
            startScanner()
        } else {
            requestCameraPermission()
        }
    }

    private fun startScanner() {
        startScanner(this) { barcodes ->
            barcodes.forEach { barcode ->
                when (barcode.valueType) {
                    Barcode.TYPE_URL -> {
                        val dialog = ScanPopup(this@MainPage,"URL",barcode.url.toString())
                        dialog.show()
                    }
                    Barcode.TYPE_CONTACT_INFO -> {
                        val dialog = ScanPopup(this@MainPage,"CONTACT",barcode.contactInfo.toString())
                        dialog.show()
                    }
                    else -> {
                        val dialog = ScanPopup(this@MainPage,"Other",barcode.rawValue.toString())
                        dialog.show()
                    }
                }
            }
        }
    }

    private fun requestCameraPermission() {
        when {
            shouldShowRequestPermissionRationale(cameraPermission) -> {
                cameraPermissionRequest(
                    positive = { openPermissionSetting() }
                )
            }

            else -> {
                requestPermissionLauncher.launch(cameraPermission)
            }
        }
    }
}