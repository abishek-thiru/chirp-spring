package com.abi.chirp.api.mappers

import com.abi.chirp.api.dto.ChatDto
import com.abi.chirp.api.dto.ChatMessageDto
import com.abi.chirp.api.dto.ChatParticipantDto
import com.abi.chirp.domain.models.Chat
import com.abi.chirp.domain.models.ChatMessage
import com.abi.chirp.domain.models.ChatParticipant

fun Chat.toChatDto(): ChatDto {
    return ChatDto(
        id = id,
        participants = participants.map {
            it.toChatParticipantDto()
        },
        lastMessage = lastMessage?.toChatMessageDto(),
        creator = creator.toChatParticipantDto(),
        lastActivityAt = lastActivityAt
    )
}

fun ChatMessage.toChatMessageDto(): ChatMessageDto {
    return ChatMessageDto(
        id = id,
        chatId = chatId,
        content = content,
        senderId = sender.userId,
        createdAt = createdAt
    )
}

fun ChatParticipant.toChatParticipantDto(): ChatParticipantDto {
    return ChatParticipantDto(
        userId = userId,
        username = username,
        email = email,
        profilePictureUrl = profilePictureUrl
    )
}