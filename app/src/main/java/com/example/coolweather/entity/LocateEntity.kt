package com.example.coolweather.entity

import com.example.coolweather.data.City

data class LocateEntity(var city: City,
                        var select:Boolean,
                        var open:Boolean,
                        var now:NowEntity?,
                        var oneDay:DailyEntity?)