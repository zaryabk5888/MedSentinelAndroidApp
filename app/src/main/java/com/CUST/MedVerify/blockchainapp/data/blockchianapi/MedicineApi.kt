package com.CUST.MedVerify.blockchainapp.data.blockchianapi

import com.CUST.MedVerify.blockchainapp.data.dto.Medicine
import com.CUST.MedVerify.blockchainapp.data.dto.MedicineId
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

const val address = "http://54.164.127.224:8080/"

interface MedicineApi {
    @GET("medicines")
    suspend fun medicineList(): List<Medicine>

    @POST("history")
    suspend fun medicineHistoryList(@Body data: MedicineId): List<Medicine>

    @POST("get")
    suspend fun getMedicine(@Body data: MedicineId): Medicine

    @POST("create")
    suspend fun createMedicine(@Body data: Medicine): Response<Any>

    @POST("delete")
    suspend fun deleteMedicine(@Body data: MedicineId): Medicine

    @POST("update")
    suspend fun updateMedicine(@Body data: Medicine)

    @POST("journey")
    suspend fun updateMedicineJourney(@Body data: MedicineId): List<Medicine>
}
