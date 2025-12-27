package com.abi.chirp.service

import com.abi.chirp.domain.event.MessageDeletedEvent
import com.abi.chirp.domain.events.chat.ChatEvent
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
import com.abi.chirp.infra.message_queue.EventPublisher
import jakarta.transaction.Transactional
import org.springframework.cache.annotation.CacheEvict
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ChatMessageService(
    private val chatRepository: ChatRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val chatParticipantRepository: ChatParticipantRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val eventPublisher: EventPublisher
) {

    @Transactional
    @CacheEvict(
        value = ["messages"],
        key = "#chatId"
    )
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

        val savedMessage = chatMessageRepository.saveAndFlush(
            ChatMessageEntity(
                id = messageId,
                content = content,
                chatId = chatId,
                chat = chat,
                sender = sender
            )
        )

        eventPublisher.publish(
            event = ChatEvent.NewMessage(
                senderId = senderId,
                senderUsername = sender.username,
                recipientIds = chat.participants.map { it.userId }.toSet(),
                chatId = chatId,
                message = savedMessage.content
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

        applicationEventPublisher.publishEvent(
            MessageDeletedEvent(
                chatId = message.chatId,
                messageId = messageId
            )
        )

        evictMessagesCache(message.chatId)
    }

    @CacheEvict(
        value = ["messages"],
        key = "#chatId"
    )
    fun evictMessagesCache(chatId: ChatId) {
        // Let spring handle cache evict. We don't have to do anything.
    }
}