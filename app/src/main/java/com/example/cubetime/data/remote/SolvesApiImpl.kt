package com.example.cubetime.data.remote

import android.util.Log
import com.example.cubetime.data.model.DTOs.SolveDTO
import com.example.cubetime.data.model.entities.Solve
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType

class SolvesApiImpl (
    private val client: HttpClient
) : SolvesAPI {
    override suspend fun getSolves(token: String): List<Solve>? {

        return try {
            client.get { url(HttpRoutes.GET_SOLVES) }.body()
        } catch (e: Exception) {
            Log.d("SolvesApiImpl", e.message.toString())
            null
        }
    }

    override suspend fun uploadSolves(solves: List<SolveDTO>): String? {
        return try {
             val token: String? = client.post {
                 url (HttpRoutes.ADD_SOLVES)
                 contentType(ContentType.Application.Json)
                 setBody(solves)
            }.body()
            HttpRoutes.GET_SOLVES + token
        } catch (e: Exception) {
            Log.d("SolvesApiImpl", e.message.toString())
            null
        }
    }
}