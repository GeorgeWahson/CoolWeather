package com.example.baselib.entity
// 实体基类一般是根据后台返回的内容格式进行创建，泛型里面的内容才是我们最终需要的数据
data class BaseResult<T>(
    var status:String,
    var code: String,
    var updateTime: String,
    var fxLink: String,
    var location: T?,
    var now: T?,
    var daily:T?,
    var hourly:T?
)

