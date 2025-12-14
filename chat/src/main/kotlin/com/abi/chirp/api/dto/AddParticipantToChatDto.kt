package com.abi.chirp.api.dto

import com.abi.chirp.domain.type.UserId
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Size

data class AddParticipantToChatDto(
    @field:Size(min = 1)
    @JsonProperty("userIds")
    val userIds: List<UserId>
)
