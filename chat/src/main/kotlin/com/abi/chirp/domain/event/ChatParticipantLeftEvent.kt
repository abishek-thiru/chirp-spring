package com.abi.chirp.domain.event

import com.abi.chirp.domain.type.ChatId
import com.abi.chirp.domain.type.UserId

data class ChatParticipantLeftEvent(
    val chatId: ChatId,
    val userId: UserId
)
