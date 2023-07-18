package com.example.smart_winery

data class WineInfo(private val col:Int
                    ,private val id:String
                    ,private val eng_name:String
                    ,private val wine_img: String
                    ,private val price: Int
                    ,private val sweet: Int
                    ,private val acid: Int
                    ,private val body: Int
                    ,private val tannin: Int
                    ,private val aromas: List<AromaInfo>
                    ,private val alcohol: String
                    ,private val temp:Int
                    ,private val type:String
                    ,private val pairings:List<PairingInfo>
) :Cloneable {
    public override fun clone(): WineInfo = super.clone() as WineInfo
    var Wine_Location:Int? = col
    var Wine_Id:String? = id
    var Wine_Name:String? = eng_name
    var Wine_Image:String? = wine_img
    var Wine_Price:Int?=price
    var Wine_Sweet:Int?=sweet
    var Wine_Acid:Int?=acid
    var Wine_Body:Int?=body
    var Wine_Tannin:Int?=tannin
    var Wine_Aromas:List<AromaInfo>? = aromas
    var Wine_Alcohol:String? = alcohol
    var Wine_Temp:Int?=temp
    var Wine_Type:String? = type
    var Wine_Pairings:List<PairingInfo>? = pairings
}