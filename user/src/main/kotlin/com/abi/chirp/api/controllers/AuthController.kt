package com.abi.chirp.api.controllers

import com.abi.chirp.api.dto.*
import com.abi.chirp.api.mappers.toAuthenticatedUserDto
import com.abi.chirp.api.mappers.toUserDto
import com.abi.chirp.service.AuthService
import com.abi.chirp.service.EmailVerificationService
import com.abi.chirp.service.PasswordResetService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
    private val emailVerificationService: EmailVerificationService,
    private val passwordResetService: PasswordResetService
) {

    @PostMapping("/register")
    fun register(
        @Valid @RequestBody body: RegisterRequest
    ): UserDto {
        return authService.register(
            email = body.email,
            username = body.username,
            password = body.password
        ).toUserDto()
    }

    @PostMapping("/login")
    fun login(
        @RequestBody body: LoginRequest
    ): AuthenticatedUserDto {
        return authService.login(
            email = body.email,
            password = body.password
        ).toAuthenticatedUserDto()
    }

    @PostMapping("/refresh")
    fun refresh(
        @RequestBody body: RefreshRequest
    ): AuthenticatedUserDto {
        return authService.refresh(body.refreshToken)
            .toAuthenticatedUserDto()
    }

    @GetMapping("/verify")
    fun verifyEmail(
        @RequestParam token: String
    ) {
        emailVerificationService.verifyEmail(token)
    }

    @PostMapping("/forgot-password")
    fun forgotPassword(
        @Valid @RequestBody body: EmailRequest
    ) {
        passwordResetService.requestPasswordReset(body.email)
    }

    @PostMapping("/reset-password")
    fun resetPassword(
        @Valid @RequestBody body: ResetPasswordRequest
    ) {
       passwordResetService.resetPassword(body.token, body.newPassword)
    }

    @PostMapping("/change-password")
    fun changePassword(
        @Valid @RequestBody body: ChangePasswordRequest
    ) {
       // TODO: User ID to be extracted
    }

}