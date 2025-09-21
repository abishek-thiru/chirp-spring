package com.abi.chirp.api.mappers

import com.abi.chirp.api.dto.AuthenticatedUserDto
import com.abi.chirp.api.dto.UserDto
import com.abi.chirp.domain.model.AuthenticatedUser
import com.abi.chirp.domain.model.User

fun AuthenticatedUser.toAuthenticatedUserDto(): AuthenticatedUserDto {
    return AuthenticatedUserDto(
    this.user.toUserDto(),
    this.accessToken,
    this.refreshToken
    )
}

fun User.toUserDto(): UserDto {
    return UserDto(
        id = id,
        email = email,
        username = username,
        hasVerifiedEmail = hasEmailVerified
    )
}