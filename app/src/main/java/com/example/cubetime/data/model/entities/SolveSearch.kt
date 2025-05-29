package com.example.cubetime.data.model.entities

import android.app.appsearch.SearchResult
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class SolveSearch (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var searchResult: String
)

