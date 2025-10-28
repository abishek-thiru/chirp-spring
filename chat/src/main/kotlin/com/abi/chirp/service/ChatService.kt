package com.abi.chirp.service

import com.abi.chirp.domain.exception.ChatParticipantNotFoundException
import com.abi.chirp.domain.exception.InvalidChatSizeException
import com.abi.chirp.domain.models.Chat
import com.abi.chirp.domain.type.UserId
import com.abi.chirp.infra.database.entities.ChatEntity
import com.abi.chirp.infra.database.mappers.toChat
import com.abi.chirp.infra.database.repositories.ChatParticipantRepository
import com.abi.chirp.infra.database.repositories.ChatRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChatService(
    private val chatRepository: ChatRepository,
    private val chatParticipantRepository: ChatParticipantRepository
) {

    @Transactional
    fun createChat(
        creatorId: UserId,
        otherUserIds: Set<UserId>
    ): Chat {
        val otherParticipants = chatParticipantRepository.findByUserIdIn(
            userIds = otherUserIds
        )
        val allParticipants = (otherParticipants + creatorId)
        if (allParticipants.size < 2) {
            throw InvalidChatSizeException()
        }

        val creator = chatParticipantRepository.findByIdOrNull(creatorId)
            ?: throw ChatParticipantNotFoundException(creatorId)

        return chatRepository.save(
            ChatEntity(
                creator = creator,
                participants = setOf(creator) + otherParticipants
            )
        ).toChat(lastMessage = null)
    }
}