package com.simplesu.simplemodel.be

data class ScheduleBean(
    val time : String,
    val principal : Int,
    val interest : Int,
    val total : Int,
    val remain : Int
)
