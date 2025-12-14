package com.abi.chirp.api.dto

import com.abi.chirp.domain.type.UserId
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Size

data class CreateChatRequest(
    @field:Size(
        min = 1,
        message = "Chats must have at least 2 participants"
    )
    @JsonProperty("otherUserIds")
    val otherUserIds: List<UserId>
)
