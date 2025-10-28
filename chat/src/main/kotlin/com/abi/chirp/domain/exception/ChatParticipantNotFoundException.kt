package com.abi.chirp.domain.exception

import com.abi.chirp.domain.type.UserId

class ChatParticipantNotFoundException(
    private val id: UserId
): RuntimeException(
    "Chat participant with ID $id was not found"
)