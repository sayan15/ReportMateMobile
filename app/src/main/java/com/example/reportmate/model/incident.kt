package com.example.reportmate.model

import java.time.LocalDateTime

data class incident(val description: String,
                    val generated_at: String,
                    val latitude: Double, val longitude: Double,
                    val officer_id: Int,
                    val status: String,
                    val title: String,
                    val updated_at: String)
