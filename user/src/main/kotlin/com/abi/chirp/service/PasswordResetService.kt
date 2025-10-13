package com.abi.chirp.service

import com.abi.chirp.domain.exception.InvalidCredentialsException
import com.abi.chirp.domain.exception.InvalidTokenException
import com.abi.chirp.domain.exception.SamePasswordException
import com.abi.chirp.domain.exception.UserNotFoundException
import com.abi.chirp.domain.type.UserId
import com.abi.chirp.infra.database.entities.PasswordResetTokenEntity
import com.abi.chirp.infra.database.repositories.PasswordResetTokenRepository
import com.abi.chirp.infra.database.repositories.RefreshTokenRepository
import com.abi.chirp.infra.database.repositories.UserRepository
import com.abi.chirp.infra.security.PasswordEncoder
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class PasswordResetService(
    private val userRepository: UserRepository,
    private val passwordResetTokenRepository: PasswordResetTokenRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    @param:Value("\${chirp.email.reset-password.expiry-minutes}")
    private val expiryMinutes: Long
) {

    @Transactional
    fun requestPasswordReset(email: String) {
        val user = userRepository.findByEmail(email) ?: return

        passwordResetTokenRepository.invalidateActiveTokensForUser(user)

        val token = PasswordResetTokenEntity(
            user = user,
            expiresAt = Instant.now().plus(expiryMinutes, ChronoUnit.MINUTES)
        )

        passwordResetTokenRepository.save(token)

        // TODO: Inform notification service about reset password triggered to send email
    }

    @Transactional
    fun resetPassword(token: String, newPassword: String) {
        val resetToken = passwordResetTokenRepository.findByToken(token)
            ?: throw InvalidTokenException("Invalid password reset token")

        if (resetToken.isUsed) {
            throw InvalidTokenException("Email verification token is already used")
        }

        if (resetToken.isExpired) {
            throw InvalidTokenException("Email verification token is expired")
        }

        val user = resetToken.user

        if (passwordEncoder.matches(newPassword, user.hashedPassword)) {
            throw SamePasswordException()
        }
        val hashedNewPassword = passwordEncoder.encode(newPassword)

        userRepository.save(
            user.apply {
                this.hashedPassword = hashedNewPassword
            }
        )

        passwordResetTokenRepository.save(
            resetToken.apply {
                this.usedAt = Instant.now()
            }
        )

        refreshTokenRepository.deleteByUserId(user.id!!)
    }

    @Transactional
    fun changePassword(
        userId: UserId,
        oldPassword: String,
        newPassword: String
    ) {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException()

        if (!passwordEncoder.matches(oldPassword, user.hashedPassword)) {
            throw InvalidCredentialsException()
        }

        if (oldPassword == newPassword) {
            throw SamePasswordException()
        }

        refreshTokenRepository.deleteByUserId(user.id!!)

        val newHashedPassword = passwordEncoder.encode(newPassword)
        userRepository.save(
            user.apply {
                this.hashedPassword = newHashedPassword
            }
        )
    }


    @Scheduled(cron = "0 0 3 * * *")
    fun cleanUpExpiredTokens() {
        passwordResetTokenRepository.deleteByExpiresAtLessThan(Instant.now())
    }

}