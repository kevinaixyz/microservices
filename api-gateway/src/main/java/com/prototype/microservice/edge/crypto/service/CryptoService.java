package com.prototype.microservice.edge.crypto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CryptoService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Returns the default password encoder.
	 *
	 * @return
	 */
	public PasswordEncoder getDefaultEncoder() {
		return passwordEncoder;
	}

	/**
	 * Encode the password based on the predefined hashing algorithm.
	 *
	 * @param rawPassword
	 * @return
	 * @throws Exception
	 */
	public String encodePassword(final String rawPassword) throws Exception {
		return passwordEncoder.encode(rawPassword);

	}

	/**
	 * Matches the raw password with the encoded password based on the predefined
	 * hashing algorithm.
	 *
	 * @param rawPassword
	 * @param encodedPassword
	 * @return
	 * @throws Exception
	 */
	public boolean matchPassword(final String rawPassword, final String encodedPassword) throws Exception {
		try {
			return passwordEncoder.matches(rawPassword, encodedPassword);
		} catch (Exception e) {
			return false;
		}
	}

}
