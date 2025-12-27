package com.abi.chirp.domain.event

import com.abi.chirp.domain.type.ChatId
import com.abi.chirp.domain.type.UserId

data class ChatParticipantsJoinedEvent(
    val chatId: ChatId,
    val userIds: Set<UserId>
)
