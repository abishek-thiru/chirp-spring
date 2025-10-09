package com.abi.chirp.api.util

import com.abi.chirp.domain.exception.UnauthorizedException
import com.abi.chirp.domain.type.UserId
import org.springframework.security.core.context.SecurityContextHolder

val requestUserId: UserId
    get() = SecurityContextHolder.getContext().authentication?.principal as? UserId
        ?: throw UnauthorizedException()