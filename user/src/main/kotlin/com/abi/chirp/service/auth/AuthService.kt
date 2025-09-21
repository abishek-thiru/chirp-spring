package com.abi.chirp.service.auth

import com.abi.chirp.domain.exception.UserAlreadyExistsException
import com.abi.chirp.domain.model.User
import com.abi.chirp.infra.database.entities.UserEntity
import com.abi.chirp.infra.database.mappers.toUser
import com.abi.chirp.infra.database.repositories.UserRepository
import com.abi.chirp.infra.security.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun register(email: String, username: String, password: String): User {
        val user = userRepository.findByEmailOrUsername(
            email = email.trim(),
            username = username.trim()
        )
        if(user != null) {
            throw UserAlreadyExistsException()
        }

        val savedUser = userRepository.save(
            UserEntity(
                email = email.trim(),
                username = username.trim(),
                hashedPassword = passwordEncoder.encode(password)
            )
        ).toUser()

        return savedUser
    }
}