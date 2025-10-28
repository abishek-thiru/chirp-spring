package com.abi.chirp.api.dto

import com.abi.chirp.domain.type.ChatId
import java.time.Instant

data class ChatDto(
    val id: ChatId,
    val participants: List<ChatParticipantDto>,
    val lastMessage: ChatMessageDto?,
    val creator: ChatParticipantDto,
    val lastActivityAt: Instant,
)