package com.abi.chirp.infra.messaging

import com.abi.chirp.domain.events.user.UserEvent
import com.abi.chirp.domain.models.ChatParticipant
import com.abi.chirp.infra.message_queue.MessageQueues
import com.abi.chirp.service.ChatParticipantService
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class ChatUserEventListener(
    private val chatParticipantService: ChatParticipantService
) {

    @RabbitListener(queues = [MessageQueues.CHAT_USER_EVENTS])
    fun handleUserEvent(event: UserEvent) {
        when (event) {
            is UserEvent.Verified -> {
                chatParticipantService.createChatParticipant(
                    chatParticipant = ChatParticipant(
                        userId = event.userId,
                        username = event.username,
                        email = event.username,
                        profilePictureUrl = null
                    )
                )
            }

            else -> Unit
        }
    }

}