package com.abi.chirp.api.dto

import com.abi.chirp.domain.type.ChatId
import com.abi.chirp.domain.type.ChatMessageId
import com.abi.chirp.domain.type.UserId
import java.time.Instant

data class ChatMessageDto(
    val id: ChatMessageId,
    val chatId: ChatId,
    val content: String,
    val senderId: UserId,
    val createdAt: Instant
)
