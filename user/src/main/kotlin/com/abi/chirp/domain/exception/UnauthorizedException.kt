package com.abi.chirp.domain.exception

class UnauthorizedException: RuntimeException(
    "Missing authorization details"
) {
}