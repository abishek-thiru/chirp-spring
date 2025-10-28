package com.abi.chirp.infra.database.mappers

import com.abi.chirp.domain.models.Chat
import com.abi.chirp.domain.models.ChatMessage
import com.abi.chirp.domain.models.ChatParticipant
import com.abi.chirp.infra.database.entities.ChatEntity
import com.abi.chirp.infra.database.entities.ChatParticipantEntity

fun ChatEntity.toChat(lastMessage: ChatMessage? = null): Chat {
    return Chat(
        id = id!!,
        participants = participants.map {
            it.toChatParticipant()
        }.toSet(),
        creator = creator.toChatParticipant(),
        lastActivityAt = lastMessage?.createdAt ?: createdAt,
        createdAt = createdAt,
        lastMessage = lastMessage
    )
}

fun ChatParticipantEntity.toChatParticipant(): ChatParticipant {
    return ChatParticipant(
        userId = userId,
        username = username,
        email = email,
        profilePictureUrl = profilePictureUrl
    )
}