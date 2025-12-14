package com.abi.chirp.api.controllers

import com.abi.chirp.api.util.requestUserId
import com.abi.chirp.domain.type.ChatMessageId
import com.abi.chirp.service.ChatMessageService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/messages")
class ChatMessageController(private val chatMessageService: ChatMessageService) {

    @DeleteMapping("/{messageId}")
    fun deleteMessage(
        @PathVariable messageId: ChatMessageId
    ) {
        chatMessageService.deleteMessage(messageId, requestUserId)
    }

}