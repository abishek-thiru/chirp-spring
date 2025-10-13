package com.abi.chirp.domain.model

import com.abi.chirp.domain.type.UserId

data class User(
    val id: UserId,
    val username: String,
    val email: String,
    val hasEmailVerified: Boolean
)


