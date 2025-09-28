package com.abi.chirp.domain.exception

class SamePasswordException: RuntimeException(
    "The new password can't be same as the old one."
) {
}