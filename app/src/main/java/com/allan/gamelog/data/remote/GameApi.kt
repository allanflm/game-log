package com.allan.gamelog.data.remote

import com.allan.gamelog.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Retrofit transforma esta interface em chamadas HTTP de verdade. Cada função é um
// endpoint do backend; "suspend" porque a rede é lenta e roda numa coroutine.
// É parecido com declarar um DAO do Room, só que para HTTP em vez de SQL.
interface GameApi {

    @GET("games/search")
    suspend fun search(@Query("q") query: String): List<CatalogGameDto>

    @GET("games/popular")
    suspend fun popular(): List<CatalogGameDto>

    companion object {
        private const val JSON_MEDIA_TYPE = "application/json"

        fun create(baseUrl: String = BuildConfig.API_BASE_URL): GameApi {
            // ignoreUnknownKeys: se o backend ganhar campos novos, o app antigo não quebra.
            val json = Json { ignoreUnknownKeys = true }
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(json.asConverterFactory(JSON_MEDIA_TYPE.toMediaType()))
                .build()
                .create(GameApi::class.java)
        }
    }
}
