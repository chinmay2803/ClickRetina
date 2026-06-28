package com.example.skillforge.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    // The endpoint is a static JSON file on raw.githubusercontent.com,
    // so we pass the full URL at call-time rather than baking a fixed path.
    @GET
    suspend fun getData(@Url url: String): SkillforgeResponse
}

object SkillforgeApi {
    private const val DATA_URL =
        "https://raw.githubusercontent.com/android-assesment/notes/refs/heads/main/data.json"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    // baseUrl is unused functionally since we pass the full @Url, but
    // Retrofit requires a valid one to build.
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://raw.githubusercontent.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(ApiService::class.java)

    suspend fun fetchData(): SkillforgeResponse = service.getData(DATA_URL)
}
