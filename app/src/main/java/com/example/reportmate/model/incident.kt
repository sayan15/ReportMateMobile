package com.example.reportmate.model

import java.time.LocalDateTime

data class incident(
                    val title: String,
                    val latitude: Double?, val longitude: Double?,
                    val description: String,
                    val happenedAt:String,
                    val generated_at: String,
                    val crime_type: String,
                    val officer_id: Int,
                    val phone: Long,
                    val status: String,
                    val updated_at: String,
                    val secret:String)
