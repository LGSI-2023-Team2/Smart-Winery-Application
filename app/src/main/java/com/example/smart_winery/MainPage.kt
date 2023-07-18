package com.example.smart_winery

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import android.os.Handler
import androidx.activity.result.contract.ActivityResultContracts
import android.view.View
import android.view.ViewGroup
import android.widget.Space
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.example.smart_winery.databinding.MainPageBinding
import com.example.smart_winery.databinding.ReserveBinding
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

import com.example.smart_winery.ScanPage.Companion.startScanner
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


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val mainPageBinding = MainPageBinding.inflate(layoutInflater)
        val reserveBinding = ReserveBinding.inflate(layoutInflater)
        val wineInfoBinding = WineInfoBinding.inflate(layoutInflater)
        val reserveView = reserveBinding.root
        val wineInfoView = wineInfoBinding.root
        val btnIdNumber = mainPageBinding.btn11.id
        var isInfo = true
        var isWineSelected = false
        lateinit var wineTemp:WineInfo

        val firstfloor = arrayListOf<ImageView>(
            mainPageBinding.btn11,
            mainPageBinding.btn12,
            mainPageBinding.btn13,
            mainPageBinding.btn14,
            mainPageBinding.btn15
        )
        val secondfloor = arrayListOf<ImageView>(
            mainPageBinding.btn21,
            mainPageBinding.btn22,
            mainPageBinding.btn23,
            mainPageBinding.btn24,
            mainPageBinding.btn25
        )
        val thirdfloor = arrayListOf<ImageView>(
            mainPageBinding.btn31,
            mainPageBinding.btn32,
            mainPageBinding.btn33,
            mainPageBinding.btn34,
            mainPageBinding.btn35
        )
        var floor1:JSONObject = JSONObject()
        var floor2:JSONObject = JSONObject()
        var floor3:JSONObject = JSONObject()

        fun displayWine(){

            val floor1wines:JSONArray = floor1.getJSONArray("cell_ids")
            val floor2wines:JSONArray = floor2.getJSONArray("cell_ids")
            val floor3wines:JSONArray = floor3.getJSONArray("cell_ids")
            for ((index,i) in firstfloor.withIndex()){

                for (j in 0 until floor1wines.length()){
                    val wine:JSONObject = floor1wines.getJSONObject(j)
                    if (wine.getInt("col") == index+1){
                        val floor1wine = wine.getJSONObject("wine_id")
                        var aromaList:MutableList<AromaInfo> = mutableListOf()
                        var pairingList:MutableList<PairingInfo> = mutableListOf()
                        var wineAromas:JSONArray = JSONArray()
                        try{
                            wineAromas = floor1wine.getJSONArray("aroma")
                            Log.d("aromacheck1",wineAromas.toString())
                            Log.d("aromacheck11",wineAromas.length().toString())
                        } catch (e:Exception){

                        }
                        for (k in 0 until wineAromas.length()) {
                            Log.d("aromacheck12", k.toString())
                            var wineAroma = AromaInfo("","","", mutableListOf())
                            var nameList: MutableList<String> = mutableListOf()
                            var aromaNames =
                                wineAromas.getJSONObject(k).getJSONArray("aroma_names")
                            Log.d("aromacheck5", aromaNames.toString())
                            wineAroma.Aroma_Id = wineAromas.getJSONObject(k).getString("_id")
                            Log.d("aromacheck6", wineAroma.Aroma_Id.toString())
                            wineAroma.Aroma_category =
                                wineAromas.getJSONObject(k).getString("category")
                            Log.d("aromacheck7", wineAroma.Aroma_category.toString())
                            wineAroma.Aroma_image =
                                wineAromas.getJSONObject(k).getString("imgsrc")
                            Log.d("aromacheck8", wineAroma.Aroma_image.toString())
                            Log.d("aromacheck10",aromaNames.toString())
                            Log.d("aromacheck11",aromaNames.length().toString())
                            for (j in 0 until aromaNames.length()) {
                                Log.d("aromacheck9", j.toString())
                                var aromaName: String = aromaNames.getString(j)
                                nameList.add(aromaName)
                            }
                            wineAroma.Aroma_names = nameList
                            aromaList.add(wineAroma)
                        }
                        try {
                            val winePairings = floor1wine.getJSONArray("pairing")
                            var winePairing = PairingInfo("","","", mutableListOf())
                            var nameList: MutableList<String> = mutableListOf()
                            for (k in 0 until winePairings.length()) {


                                var pairingNames =
                                    winePairings.getJSONObject(k).getJSONArray("pairing_names")

                                winePairing.Pairing_Id =
                                    winePairings.getJSONObject(k).getString("_id")
                                winePairing.Pairing_category =
                                    winePairings.getJSONObject(k).getString("category")
                                winePairing.Pairing_image =
                                    winePairings.getJSONObject(k).getString("imgsrc")

                                for (j in 0 until pairingNames.length()) {
                                    var pairingName: String = pairingNames.getString(j)
                                    nameList.add(pairingName)
                                }
                                winePairing.Pairing_names = nameList
                                pairingList.add(winePairing)
                            }
                        }catch (e:Exception){

                        }
                        try {
                            GlideApp.with(this)
                                .load(floor1wine.getString("imgsrc"))
                                .into(i)
                        }catch (e:Exception){
                        }
                        WineList1.add(WineInfo(
                            wine.getInt("col") - 1
                            ,floor1wine.getString("_id")
                            ,floor1wine.getString("eng_name")
                            ,floor1wine.getString("imgsrc")
                            ,floor1wine.getInt("price")
                            ,floor1wine.getInt("sweet")
                            ,floor1wine.getInt("acid")
                            ,floor1wine.getInt("body")
                            ,floor1wine.getInt("tannin")
                            ,aromaList
                            ,floor1wine.getString("alcohol")
                            ,floor1wine.getInt("temp")
                            ,floor1wine.getString("type")
                            ,pairingList
                        ))
                    }
                }
                i.clipToOutline = true
            }
            for ((index,i) in secondfloor.withIndex()){
                for (j in 0 until floor2wines.length()){

                    val wine:JSONObject = floor2wines.getJSONObject(j)
                    if (wine.getInt("col") == index+1){
                        val floor2wine = wine.getJSONObject("wine_id")
                        var aromaList:MutableList<AromaInfo> = mutableListOf()
                        var pairingList:MutableList<PairingInfo> = mutableListOf()

                        try{
                            val wineAromas = floor2wine.getJSONArray("aroma")
                            lateinit var wineAroma: AromaInfo
                            lateinit var nameList: MutableList<String>

                            for (k in 0 until wineAromas.length()) {

                                var aromaNames =
                                    wineAromas.getJSONObject(k).getJSONArray("aroma_names")

                                wineAroma.Aroma_Id = wineAromas.getJSONObject(k).getString("_id")
                                wineAroma.Aroma_category =
                                    wineAromas.getJSONObject(k).getString("category")
                                wineAroma.Aroma_image =
                                    wineAromas.getJSONObject(k).getString("imgsrc")

                                for (j in 0 until aromaNames.length()) {
                                    var aromaName: String = aromaNames.getString(j)
                                    nameList.add(aromaName)
                                }
                                wineAroma.Aroma_names = nameList
                                aromaList.add(wineAroma)
                            }
                        } catch (e:Exception){

                        }
                        try {
                            val winePairings = floor2wine.getJSONArray("pairing")
                            lateinit var winePairing: PairingInfo
                            lateinit var nameList: MutableList<String>
                            for (k in 0 until winePairings.length()) {


                                var pairingNames =
                                    winePairings.getJSONObject(k).getJSONArray("pairing_names")

                                winePairing.Pairing_Id =
                                    winePairings.getJSONObject(k).getString("_id")
                                winePairing.Pairing_category =
                                    winePairings.getJSONObject(k).getString("category")
                                winePairing.Pairing_image =
                                    winePairings.getJSONObject(k).getString("imgsrc")

                                for (j in 0 until pairingNames.length()) {
                                    var pairingName: String = pairingNames.getString(j)
                                    nameList.add(pairingName)
                                }
                                winePairing.Pairing_names = nameList
                                pairingList.add(winePairing)
                            }
                        }catch (e:Exception){

                        }
                        try {
                            GlideApp.with(this)
                                .load(floor2wine.getString("imgsrc"))
                                .into(i)
                        }catch (e:Exception){
                        }
                        WineList2.add(WineInfo(
                            wine.getInt("col") - 1
                            ,floor2wine.getString("_id")
                            ,floor2wine.getString("eng_name")
                            ,floor2wine.getString("imgsrc")
                            ,floor2wine.getInt("price")
                            ,floor2wine.getInt("sweet")
                            ,floor2wine.getInt("acid")
                            ,floor2wine.getInt("body")
                            ,floor2wine.getInt("tannin")
                            ,aromaList
                            ,floor2wine.getString("alcohol")
                            ,floor2wine.getInt("temp")
                            ,floor2wine.getString("type")
                            ,pairingList
                        ))
                    }
                }
                i.clipToOutline = true
            }
            for ((index,i) in thirdfloor.withIndex()){
                for (j in 0 until floor3wines.length()){

                    val wine:JSONObject = floor3wines.getJSONObject(j)
                    if (wine.getInt("col") == index+1){
                        val floor3wine = wine.getJSONObject("wine_id")
                        var aromaList:MutableList<AromaInfo> = mutableListOf()
                        var pairingList:MutableList<PairingInfo> = mutableListOf()
                        try{
                            val wineAromas = floor3wine.getJSONArray("aroma")
                            lateinit var wineAroma: AromaInfo
                            lateinit var nameList: MutableList<String>
                            for (k in 0 until wineAromas.length()) {


                                var aromaNames =
                                    wineAromas.getJSONObject(k).getJSONArray("aroma_names")

                                wineAroma.Aroma_Id = wineAromas.getJSONObject(k).getString("_id")
                                wineAroma.Aroma_category =
                                    wineAromas.getJSONObject(k).getString("category")
                                wineAroma.Aroma_image =
                                    wineAromas.getJSONObject(k).getString("imgsrc")

                                for (j in 0 until aromaNames.length()) {
                                    var aromaName: String = aromaNames.getString(j)
                                    nameList.add(aromaName)
                                }
                                wineAroma.Aroma_names = nameList
                                aromaList.add(wineAroma)
                            }
                        } catch (e:Exception){

                        }
                        try {
                            val winePairings = floor3wine.getJSONArray("pairing")
                            lateinit var winePairing: PairingInfo
                            lateinit var nameList: MutableList<String>
                            for (k in 0 until winePairings.length()) {


                                var pairingNames =
                                    winePairings.getJSONObject(k).getJSONArray("pairing_names")

                                winePairing.Pairing_Id =
                                    winePairings.getJSONObject(k).getString("_id")
                                winePairing.Pairing_category =
                                    winePairings.getJSONObject(k).getString("category")
                                winePairing.Pairing_image =
                                    winePairings.getJSONObject(k).getString("imgsrc")

                                for (j in 0 until pairingNames.length()) {
                                    var pairingName: String = pairingNames.getString(j)
                                    nameList.add(pairingName)
                                }
                                winePairing.Pairing_names = nameList
                                pairingList.add(winePairing)
                            }
                        }catch (e:Exception){

                        }
                        try {
                            GlideApp.with(this)
                                .load(floor3wine.getString("imgsrc"))
                                .into(i)
                        }catch (e:Exception){
                        }
                        WineList3.add(WineInfo(
                            wine.getInt("col") - 1
                            ,floor3wine.getString("_id")
                            ,floor3wine.getString("eng_name")
                            ,floor3wine.getString("imgsrc")
                            ,floor3wine.getInt("price")
                            ,floor3wine.getInt("sweet")
                            ,floor3wine.getInt("acid")
                            ,floor3wine.getInt("body")
                            ,floor3wine.getInt("tannin")
                            ,aromaList
                            ,floor3wine.getString("alcohol")
                            ,floor3wine.getInt("temp")
                            ,floor3wine.getString("type")
                            ,pairingList
                        ))
                    }
                }
                i.clipToOutline = true
            }
        }

//        val url = "http://10.0.2.2:3000/winecellar/status?id=64ae2b0848a3d71c485e2472"
        var url = "http://13.48.52.200:3000/winecellar/status?id=64b4f9a38b4dc227def9b5b1"
        val queue : RequestQueue = Volley.newRequestQueue(applicationContext)
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
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
                isInfo=true
                mainPageBinding.infoMove.setText("Info")

            }
        }

        fun openWineinfo(w:WineInfo){
            val wineInfoBuilder = AlertDialog.Builder(this)
                .setView(wineInfoView)
            wineInfoBinding.scanName.text = w.Wine_Name
            GlideApp.with(this)
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
            if (w.Wine_Aromas.isEmpty()){
                wineInfoBinding.aromaContainer.visibility = View.GONE
            }
            else {
                when (w.Wine_Aromas.size){
                    1 -> {
                        GlideApp.with(this)
                            .load(w.Wine_Aromas[0].Aroma_image)
                            .into(wineInfoBinding.aroma1img)
                        wineInfoBinding.aroma1txt.text = w.Wine_Aromas[0].Aroma_names.joinToString()
                        wineInfoBinding.aroma2.visibility = View.GONE
                        wineInfoBinding.aroma3.visibility = View.GONE
                        wineInfoBinding.aroma4.visibility = View.GONE
                    }
                    2 -> {
                        GlideApp.with(this)
                            .load(w.Wine_Aromas[0].Aroma_image)
                            .into(wineInfoBinding.aroma1img)
                        wineInfoBinding.aroma1txt.text = w.Wine_Aromas[0].Aroma_names.joinToString()
                        GlideApp.with(this)
                            .load(w.Wine_Aromas[1].Aroma_image)
                            .into(wineInfoBinding.aroma1img)
                        wineInfoBinding.aroma1txt.text = w.Wine_Aromas[1].Aroma_names.joinToString()
                        wineInfoBinding.aroma3.visibility = View.GONE
                        wineInfoBinding.aroma4.visibility = View.GONE
                    }
                    3 -> {
                        GlideApp.with(this)
                            .load(w.Wine_Aromas[0].Aroma_image)
                            .into(wineInfoBinding.aroma1img)
                        wineInfoBinding.aroma1txt.text = w.Wine_Aromas[0].Aroma_names.joinToString()
                        GlideApp.with(this)
                            .load(w.Wine_Aromas[1].Aroma_image)
                            .into(wineInfoBinding.aroma1img)
                        wineInfoBinding.aroma1txt.text = w.Wine_Aromas[1].Aroma_names.joinToString()
                        GlideApp.with(this)
                            .load(w.Wine_Aromas[2].Aroma_image)
                            .into(wineInfoBinding.aroma1img)
                        wineInfoBinding.aroma1txt.text = w.Wine_Aromas[2].Aroma_names.joinToString()
                        wineInfoBinding.aroma4.visibility = View.GONE
                    }
                    4 -> {
                        GlideApp.with(this)
                            .load(w.Wine_Aromas[0].Aroma_image)
                            .into(wineInfoBinding.aroma1img)
                        wineInfoBinding.aroma1txt.text = w.Wine_Aromas[0].Aroma_names.joinToString()
                        GlideApp.with(this)
                            .load(w.Wine_Aromas[1].Aroma_image)
                            .into(wineInfoBinding.aroma1img)
                        wineInfoBinding.aroma1txt.text = w.Wine_Aromas[1].Aroma_names.joinToString()
                        GlideApp.with(this)
                            .load(w.Wine_Aromas[2].Aroma_image)
                            .into(wineInfoBinding.aroma1img)
                        wineInfoBinding.aroma1txt.text = w.Wine_Aromas[2].Aroma_names.joinToString()
                        GlideApp.with(this)
                            .load(w.Wine_Aromas[4].Aroma_image)
                            .into(wineInfoBinding.aroma1img)
                        wineInfoBinding.aroma1txt.text = w.Wine_Aromas[4].Aroma_names.joinToString()
                    }
                }
            }
            if (w.Wine_Pairings.isEmpty()){
                wineInfoBinding.pairingContainer.visibility = View.GONE
            }
            else {
                when (w.Wine_Pairings.size){
                    1 -> {
                        GlideApp.with(this)
                            .load(w.Wine_Pairings[0].Pairing_image)
                            .into(wineInfoBinding.aroma1img)
                        wineInfoBinding.aroma1txt.text = w.Wine_Pairings[0].Pairing_names.joinToString()
                        wineInfoBinding.aroma2.visibility = View.GONE
                        wineInfoBinding.aroma3.visibility = View.GONE
                        wineInfoBinding.aroma4.visibility = View.GONE
                    }
                    2 -> {
                        GlideApp.with(this)
                            .load(w.Wine_Pairings[0].Pairing_image)
                            .into(wineInfoBinding.aroma1img)
                        wineInfoBinding.aroma1txt.text = w.Wine_Pairings[0].Pairing_names.joinToString()
                        GlideApp.with(this)
                            .load(w.Wine_Pairings[1].Pairing_image)
                            .into(wineInfoBinding.aroma1img)
                        wineInfoBinding.aroma1txt.text = w.Wine_Pairings[1].Pairing_names.joinToString()
                        wineInfoBinding.aroma3.visibility = View.GONE
                        wineInfoBinding.aroma4.visibility = View.GONE
                    }
                    3 -> {
                        GlideApp.with(this)
                            .load(w.Wine_Pairings[0].Pairing_image)
                            .into(wineInfoBinding.aroma1img)
                        wineInfoBinding.aroma1txt.text = w.Wine_Pairings[0].Pairing_names.joinToString()
                        GlideApp.with(this)
                            .load(w.Wine_Pairings[1].Pairing_image)
                            .into(wineInfoBinding.aroma1img)
                        wineInfoBinding.aroma1txt.text = w.Wine_Pairings[1].Pairing_names.joinToString()
                        GlideApp.with(this)
                            .load(w.Wine_Pairings[2].Pairing_image)
                            .into(wineInfoBinding.aroma1img)
                        wineInfoBinding.aroma1txt.text = w.Wine_Pairings[2].Pairing_names.joinToString()
                        wineInfoBinding.aroma4.visibility = View.GONE
                    }
                    4 -> {
                        GlideApp.with(this)
                            .load(w.Wine_Pairings[0].Pairing_image)
                            .into(wineInfoBinding.aroma1img)
                        wineInfoBinding.aroma1txt.text = w.Wine_Pairings[0].Pairing_names.joinToString()
                        GlideApp.with(this)
                            .load(w.Wine_Pairings[1].Pairing_image)
                            .into(wineInfoBinding.aroma1img)
                        wineInfoBinding.aroma1txt.text = w.Wine_Pairings[1].Pairing_names.joinToString()
                        GlideApp.with(this)
                            .load(w.Wine_Pairings[2].Pairing_image)
                            .into(wineInfoBinding.aroma1img)
                        wineInfoBinding.aroma1txt.text = w.Wine_Pairings[2].Pairing_names.joinToString()
                        GlideApp.with(this)
                            .load(w.Wine_Pairings[4].Pairing_image)
                            .into(wineInfoBinding.aroma1img)
                        wineInfoBinding.aroma1txt.text = w.Wine_Pairings[4].Pairing_names.joinToString()
                    }
                }
            }
            val wineInfoDialog = wineInfoBuilder.show()
            wineInfoBinding.takeWine.setOnClickListener(){
                wineInfoDialog.dismiss()
            }
            wineInfoBinding.reserve.setOnClickListener() {
                wineInfoDialog.dismiss()
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
                    val handler = Handler()
                    val handlerTask = object : Runnable {
                        override fun run() {
                            Toast.makeText(this@MainPage,"Your wine is ready to be served!",Toast.LENGTH_SHORT).show()
                        }
                    }
                    handler.postDelayed(handlerTask, reserveTime.toLong())
                    reserveDialog.dismiss()
                }
                reserveBinding.cancel.setOnClickListener(){
                    reserveDialog.dismiss()
                }
            }
        }


        val cellListener = object : View.OnClickListener {
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
                        //move 에서 빈칸
                        if(isWineSelected){
                            //move에서 빈칸이고 와인 선택됨
                            isWineSelected = false
                            wineTemp.Wine_Location = clickedWineIndex
                            if (clickedCellIndex < 5) {
                                WineList1.add(wineTemp)
                            }
                            else if (clickedCellIndex < 10) {
                                WineList2.add(wineTemp)
                            }
                            else {
                                WineList3.add(wineTemp)
                            }
                            displayWine()
                        }
                    }
                    else {
                        //move에서 와인칸
                        if (!isWineSelected){
                            //아직 옮길 와인 선택 안됨

                            isWineSelected = true
                            if (clickedCellIndex < 5) { // 1층
                                for ((index,w) in WineList1.withIndex()) {
                                    if (w.Wine_Location == clickedWineIndex){
                                        wineTemp = w.clone()
                                        WineList1.removeAt(index)

                                    }
                                }
                            }
                            else if (clickedCellIndex < 10) {
                                for ((index,w) in WineList2.withIndex()) {
                                    if (w.Wine_Location == clickedWineIndex){
                                        wineTemp = w.clone()
                                        WineList2.removeAt(index)
                                    }
                                }
                            }
                            else {
                                for ((index,w) in WineList3.withIndex()) {
                                    if (w.Wine_Location == clickedWineIndex){
                                        wineTemp = w.clone()
                                        WineList3.removeAt(index)
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
                                openWineinfo(w)
                            }
                        }
                    }
                    else if (clickedCellIndex < 10) {
                        for (w in WineList2) {
                            if (w.Wine_Location == clickedWineIndex){
                                openWineinfo(w)
                            }
                        }
                    }
                    else {
                        for (w in WineList3) {
                            if (w.Wine_Location == clickedWineIndex) {
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