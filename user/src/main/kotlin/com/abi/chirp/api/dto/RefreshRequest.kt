package com.abi.chirp.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class RefreshRequest(
    @JsonProperty("refreshToken")
    val refreshToken: String
)
