package com.example.kadaituti

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class WeatherData(@PrimaryKey
                       var id: Long = 0,
                       var dt: String = "",
                       var weather: String = "",
                       var temp: String = ""
): RealmObject()