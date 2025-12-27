package com.abi.chirp.domain.event

import com.abi.chirp.domain.type.ChatId
import com.abi.chirp.domain.type.ChatMessageId

data class MessageDeletedEvent(
    val chatId: ChatId,
    val messageId: ChatMessageId
)
