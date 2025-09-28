package com.abi.chirp.infra.database.mappers

import com.abi.chirp.domain.model.EmailVerificationToken
import com.abi.chirp.infra.database.entities.EmailVerificationTokenEntity

fun EmailVerificationTokenEntity.toEmailVerificationToken(): EmailVerificationToken {
    return EmailVerificationToken(
        id = id,
        token = token,
        user = user.toUser()
    )
}