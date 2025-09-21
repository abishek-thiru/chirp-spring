package com.abi.chirp.infra.database.mappers

import com.abi.chirp.domain.model.User
import com.abi.chirp.infra.database.entities.UserEntity

fun UserEntity.toUser(): User {
    return User(
        id = id!!,
        username = username,
        email = email,
        hasEmailVerified = hasVerifiedEmail
    )

}