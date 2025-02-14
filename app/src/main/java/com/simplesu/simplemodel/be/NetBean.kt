package com.simplesu.simplemodel.be

data class NetBean(
    val fructify : Int,
    val montage : String,
    val basic : Basic? = null
){
    data class Basic(
        val use : String? = null,
        val corresponding : Boolean? = null
    ){
        override fun toString(): String {
            return "Basic(use=$use, corresponding=$corresponding)"
        }
    }

    override fun toString(): String {
        return "NetBean(fructify=$fructify, montage='$montage', basic=$basic)"
    }


}