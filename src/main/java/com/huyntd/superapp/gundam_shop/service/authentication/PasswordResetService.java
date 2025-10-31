package com.huyntd.superapp.gundam_shop.service.authentication;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.huyntd.superapp.gundam_shop.exception.AppException;
import com.huyntd.superapp.gundam_shop.exception.ErrorCode;
import com.huyntd.superapp.gundam_shop.model.User;
import com.huyntd.superapp.gundam_shop.repository.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class PasswordResetService {

	private final UserRepository userRepository;
	private final JavaMailSender mailSender;
	private final PasswordEncoder passwordEncoder;
	private final long expirationSeconds;
	private final ResetNonceStore resetNonceStore;
	private final String senderEmail;
	private final String senderName;

	public PasswordResetService(UserRepository userRepository,
								JavaMailSender mailSender,
								PasswordEncoder passwordEncoder,
								ResetNonceStore resetNonceStore,
								@Value("${app.reset.expirationSeconds:900}") long expirationSeconds,
								@Value("${spring.mail.username:}") String senderEmail,
								@Value("${app.mail.fromName:Gundam Shop}") String senderName) {
		this.userRepository = userRepository;
		this.mailSender = mailSender;
		this.passwordEncoder = passwordEncoder;
		this.resetNonceStore = resetNonceStore;
		this.expirationSeconds = expirationSeconds;
		this.senderEmail = senderEmail;
		this.senderName = senderName;
	}

	public void requestReset(String email) {
		if (email == null || email.trim().isEmpty()) return;

		Optional<User> maybe = userRepository.findByEmail(email);
		if (maybe.isEmpty()) {
			// Do not reveal existence; but for explicit behavior we throw USER_NOT_EXISTED
			throw new AppException(ErrorCode.USER_NOT_EXISTED);
		}

		User user = maybe.get();

		int codeNum = new java.util.Random().nextInt(1_000_000);
		String code = String.format("%06d", codeNum);
		resetNonceStore.put(user.getEmail(), code, expirationSeconds);

		long minutes = Math.max(1, expirationSeconds / 60);
		StringBuilder body = new StringBuilder();
		body.append("Your password reset OTP: ").append(code).append("\n\n");
		body.append("This OTP is valid for ").append(minutes).append(" minutes.\n\n");
		body.append("Use this OTP on the reset page (paste it into the form) to set a new password. If you didn't request this, ignore this email.");

		try {
			MimeMessage mime = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mime, false, "UTF-8");
			helper.setTo(user.getEmail());
			helper.setSubject("Gundam Shop - Password reset OTP");
			helper.setText(body.toString(), false);
			if (senderEmail != null && !senderEmail.isBlank()) {
				helper.setFrom(senderEmail, senderName);
			} else {
				helper.setFrom(senderName + " <" + user.getEmail() + ">");
			}
			mailSender.send(mime);
		} catch (MessagingException | org.springframework.mail.MailException | UnsupportedEncodingException mex) {
			System.out.println("[PasswordReset] failed to send email to " + user.getEmail() + ", code=" + code);
			throw new AppException(ErrorCode.MAIL_SEND_FAILED);
		}
	}

	public void resetPassword(String email, String code, String newPlainPassword) {
		if (code == null || code.trim().isEmpty()) throw new AppException(ErrorCode.UNAUTHENTICATED);
		if (newPlainPassword == null || newPlainPassword.trim().isEmpty()) throw new AppException(ErrorCode.BLANK_PASSWORD);

		Optional<User> maybe = userRepository.findByEmail(email);
		if (maybe.isEmpty()) throw new AppException(ErrorCode.USER_NOT_EXISTED);

		User user = maybe.get();
		String stored = resetNonceStore.get(user.getEmail());
		if (stored == null || !stored.equals(code)) {
			// Treat an incorrect or missing code as an invalid reset code
			throw new AppException(ErrorCode.INVALID_CODE);
		}

		user.setPasswordHash(passwordEncoder.encode(newPlainPassword));
		resetNonceStore.remove(user.getEmail());
		userRepository.save(user);
	}

	/**
	 * Verify a reset code without changing the password. Useful for a separate "verify code" step.
	 * Throws INVALID_CODE when code missing/incorrect or UNAUTHENTICATED when code blank.
	 */
	public void verifyCode(String email, String code) {
		if (code == null || code.trim().isEmpty()) throw new AppException(ErrorCode.UNAUTHENTICATED);

		Optional<User> maybe = userRepository.findByEmail(email);
		if (maybe.isEmpty()) throw new AppException(ErrorCode.USER_NOT_EXISTED);

		User user = maybe.get();
		String stored = resetNonceStore.get(user.getEmail());
		if (stored == null || !stored.equals(code)) {
			throw new AppException(ErrorCode.INVALID_CODE);
		}
		// keep the nonce until reset-password is called; do not remove here
	}

}
