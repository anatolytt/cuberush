package com.example.cubetime.data.remote

object HttpRoutes {
    private const val BASE_URL = "http://192.168.0.103:8080"
    const val ADD_SOLVES = "$BASE_URL/addsolves"
    const val GET_SOLVES = "$BASE_URL/solves/"
}