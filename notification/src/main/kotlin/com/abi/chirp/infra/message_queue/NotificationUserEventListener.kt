package com.abi.chirp.infra.message_queue

import com.abi.chirp.domain.events.user.UserEvent
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class NotificationUserEventListener {

    @RabbitListener(queues = [MessageQueues.NOTIFICATION_USER_EVENTS])
    @Transactional
    fun handleUserEvent(event: UserEvent) {
        when (event) {
            is UserEvent.Created -> {
                println("User created")
            }
            is UserEvent.RequestResendVerification -> {
                println("Requested Resend Verification")
            }
            is UserEvent.RequestResetPassword -> {
                println("Requested Resend Password")
            }
            else -> Unit
        }
    }
}