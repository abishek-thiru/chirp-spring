package com.abi.chirp.domain.exception

import com.abi.chirp.domain.type.ChatMessageId

class MessageNotFoundException(
    id: ChatMessageId
) : RuntimeException("Message with ID $id not found")