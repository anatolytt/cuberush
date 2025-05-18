package com.example.cubetime.data.remote

import com.example.cubetime.data.model.DTOs.SolveDTO
import com.example.cubetime.data.model.entities.Solve
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

interface SolvesAPI {
    suspend fun getSolves(token: String): List<Solve>?


    suspend fun uploadSolves(solves: List<Solve>) : String?

    companion object {
        fun create() : SolvesAPI {
            return SolvesApiImpl(
                client = HttpClient(Android) {
                    install(HttpTimeout) {
                        this.requestTimeoutMillis = 20000
                    }
                    install(ContentNegotiation) {
                        json()
                    }
                }
            )
        }
    }
}

