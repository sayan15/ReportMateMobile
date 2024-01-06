package com.example.reportmate.model

data class CrimeData(
    val crime_type: String? = null,
    val description: String? = null,
    val generated_at: String? = null,
    val happenedAt: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val officer_id: Int? = null,
    val phone: Long? = null,
    val status: String? = null,
    val title: String? = null,
    val updated_at: String? = null
)
