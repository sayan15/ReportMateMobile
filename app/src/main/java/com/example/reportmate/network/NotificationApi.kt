package com.example.reportmate.network

import com.example.reportmate.model.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApi {
    companion object{
        const val CONTENT_TYPE = "application/json"
        const val API_KEY = "AAAATD0tkxs:APA91bGDBwX6lnhVRXLHqENmrJPeumbEFDEQCdoyZHOwAaDRssAE-47TsVTuiDkBb2q261JieAQxoh3Mdj_MZqUxVMb_vfWtjiP8DIjm_CiO_QIoONNrVI7dndvc9stZlpKh6rNy7Rhn"
    }

    @Headers("Authorization: key=${API_KEY}", "Content-Type: $CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}