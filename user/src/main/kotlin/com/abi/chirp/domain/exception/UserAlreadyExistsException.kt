package com.abi.chirp.domain.exception

import java.lang.RuntimeException

class UserAlreadyExistsException: RuntimeException(
    "A user with the username or email already exists."
) {
}