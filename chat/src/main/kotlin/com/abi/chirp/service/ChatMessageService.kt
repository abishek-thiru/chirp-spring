package com.abi.chirp.service

import com.abi.chirp.domain.exception.ChatNotFoundException
import com.abi.chirp.domain.exception.ChatParticipantNotFoundException
import com.abi.chirp.domain.exception.ForbiddenException
import com.abi.chirp.domain.exception.MessageNotFoundException
import com.abi.chirp.domain.models.ChatMessage
import com.abi.chirp.domain.type.ChatId
import com.abi.chirp.domain.type.ChatMessageId
import com.abi.chirp.domain.type.UserId
import com.abi.chirp.infra.database.entities.ChatMessageEntity
import com.abi.chirp.infra.database.mappers.toChatMessage
import com.abi.chirp.infra.database.repositories.ChatMessageRepository
import com.abi.chirp.infra.database.repositories.ChatParticipantRepository
import com.abi.chirp.infra.database.repositories.ChatRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ChatMessageService(
    private val chatRepository: ChatRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val chatParticipantRepository: ChatParticipantRepository
) {

    @Transactional
    fun sendMessage(
        chatId: ChatId,
        senderId: UserId,
        content: String,
        messageId: ChatMessageId? = null
    ): ChatMessage {
        val chat = chatRepository.findChatById(chatId, senderId)
            ?: throw ChatNotFoundException()
        val sender = chatParticipantRepository.findByIdOrNull(senderId)
            ?: throw ChatParticipantNotFoundException(senderId)

        val savedMessage = chatMessageRepository.save(
            ChatMessageEntity(
                id = messageId,
                content = content,
                chatId = chatId,
                chat = chat,
                sender = sender
            )
        )
        return savedMessage.toChatMessage()
    }

    @Transactional
    fun deleteMessage(
        messageId: ChatMessageId,
        requestUserId: UserId
    ) {
        val message = chatMessageRepository.findByIdOrNull(messageId)
            ?: throw MessageNotFoundException(messageId)

        if (message.sender.userId != requestUserId) {
            throw ForbiddenException()
        }
        chatMessageRepository.delete(message)
    }
}