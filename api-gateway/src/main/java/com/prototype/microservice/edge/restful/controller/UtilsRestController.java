package com.prototype.microservice.edge.restful.controller;

import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.UUID;

import com.prototype.microservice.edge.crypto.service.CryptoService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prototype.microservice.commons.json.ResponseJson;
import com.prototype.microservice.edge.restful.dto.PasswordEncoderResponseDTO;
import com.prototype.microservice.edge.restful.dto.PasswordMatchingResponseDTO;
import com.prototype.microservice.edge.restful.dto.RandomStringResponseDTO;
import com.prototype.microservice.edge.restful.dto.RandomUUIDResponseDTO;
import com.prototype.microservice.edge.restful.dto.UniqueCorrelationIDResponseDTO;

@RestController
@RequestMapping("/public/utils")
public class UtilsRestController extends BaseRestController {

	@Autowired
	private CryptoService cryptoSvc;

	private final static int DEFAULT_RANDOM_TXT_LEN = 10;
	private final static int CORR_ID_RANDOM_TXT_LEN = 20;
	private final static String ALPHANUMERIC = "alphanumeric";
	private final static String NUMERIC = "numeric";
	private final static String ALPHABETIC= "alphabetic";

	@Value("${eureka.instance.metadataMap.instanceId}")
	private String uniqueInstanceId;

	@Autowired
	private StringEncryptor jasyptStringEncryptor;

	/**
	 * Encrypt a string using the default encryption algorithm offered by Jasypt.
	 *
	 * @param raw
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/jasypt/encrypt", method = RequestMethod.GET)
	public ResponseJson encryptByJasypt(@RequestParam(value="raw") final String raw) throws Exception {
		if (StringUtils.isBlank(raw)) {
			throw new IllegalArgumentException("Raw string must be provided");
		}
		PasswordEncoderResponseDTO resp = new PasswordEncoderResponseDTO(getInstanceId());
		resp.setRespCode(ResponseJson.JSON_RESP_CODE_OK);
		resp.setRespMsg("Encoded by Jasypt, keep it secret!");
		resp.setRawPassword(raw);
		resp.setEncodedPassword(jasyptStringEncryptor.encrypt(raw));
		resp.setEncoder(jasyptStringEncryptor.getClass().getName());
		return resp;
	}

	/**
	 * Decrypt a string using the default encryption algorithm offered by Jasypt.
	 *
	 * @param encryptedString
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/jasypt/decrypt", method = RequestMethod.GET)
	public ResponseJson decryptByJasypt(@RequestParam(value="encrypted") final String encryptedString) throws Exception {
		if (StringUtils.isBlank(encryptedString)) {
			throw new IllegalArgumentException("Encrypted string must be provided");
		}
		PasswordEncoderResponseDTO resp = new PasswordEncoderResponseDTO(getInstanceId());
		resp.setRespCode(ResponseJson.JSON_RESP_CODE_OK);
		resp.setRespMsg("Decoded by Jasypt, keep it secret!");
		resp.setRawPassword(jasyptStringEncryptor.decrypt(encryptedString));
		resp.setEncodedPassword(encryptedString);
		resp.setEncoder(jasyptStringEncryptor.getClass().getName());
		return resp;
	}

	/**
	 * Encode a password (plain text without any encoding).
	 *
	 * @param raw
	 * @return
	 */
	@RequestMapping(value = "/password/encode", method = RequestMethod.GET)
	public ResponseJson encodePassword(@RequestParam(value="raw") String rawPassword) {
		try {
			if (StringUtils.isBlank(rawPassword)) {
				throw new IllegalArgumentException("Password must be provided");
			}
			PasswordEncoderResponseDTO resp = new PasswordEncoderResponseDTO(getInstanceId());
			resp.setRespCode(ResponseJson.JSON_RESP_CODE_OK);
			resp.setRespMsg("Keep it secret");
			resp.setRawPassword(rawPassword);
			resp.setEncodedPassword(cryptoSvc.encodePassword(rawPassword));
			resp.setEncoder(cryptoSvc.getDefaultEncoder().getClass().getName());
			return resp;
		} catch (Exception e) {
			return getErrorResponseDTO(e);
		}
	}

	/**
	 * Match a raw password (in plain text format) with an encoded password by using the predefined encoder.
	 *
	 * @param raw
	 * @param encoded
	 * @return
	 */
	@RequestMapping(value = "/password/match", method = RequestMethod.GET)
	public ResponseJson matchPassword(
			@RequestParam(value="raw") String rawPassword,
			@RequestParam(value="encoded") String encodedPassword) {
		try {
			if (StringUtils.isBlank(rawPassword)) {
				throw new IllegalArgumentException("Password must be provided");
			}
			PasswordMatchingResponseDTO resp = new PasswordMatchingResponseDTO(getInstanceId());
			resp.setRespCode(ResponseJson.JSON_RESP_CODE_OK);
			resp.setRespMsg("Does it match?");
			resp.setMatched(Boolean.valueOf(cryptoSvc.matchPassword(rawPassword, encodedPassword)));
			resp.setEncoder(cryptoSvc.getDefaultEncoder().getClass().getName());
			return resp;
		} catch (Exception e) {
			return getErrorResponseDTO(e);
		}
	}

	/**
	 * Generate a random UUID string (uniqueness is not guaranteed)
	 *
	 * @param encoded
	 * @return
	 */
	@RequestMapping(value = "/random/uuid", method = RequestMethod.GET)
	public ResponseJson randomUuid() {
		try {
			RandomUUIDResponseDTO resp = new RandomUUIDResponseDTO(getInstanceId());
			resp.setRespCode(ResponseJson.JSON_RESP_CODE_OK);
			resp.setRespMsg("Random UUID generated");
			resp.setUuid(UUID.randomUUID().toString());
			return resp;
		} catch (Exception e) {
			return getErrorResponseDTO(e);
		}
	}

	/**
	 * Generate a random alphanumeric string (uniqueness is not guaranteed)
	 *
	 * @param length
	 * @return
	 */
	@RequestMapping(value = "/random/alphanumeric", method = RequestMethod.GET)
	public ResponseJson randomAlphanumeric(@RequestParam(value="length", required=false) String length) {
		return doRandom(length, ALPHANUMERIC);
	}

	/**
	 * Generate a random numeric string (uniqueness is not guaranteed)
	 *
	 * @param length
	 * @return
	 */
	@RequestMapping(value = "/random/numeric", method = RequestMethod.GET)
	public ResponseJson randomNumeric(@RequestParam(value="length", required=false) String length) {
		return doRandom(length, NUMERIC);
	}

	/**
	 * Generate a random alphabetic string (uniqueness is not guaranteed)
	 *
	 * @param length
	 * @return
	 */
	@RequestMapping(value = "/random/alphabetic", method = RequestMethod.GET)
	public ResponseJson randomAlphabetic(@RequestParam(value="length", required=false) String length) {
		return doRandom(length, ALPHABETIC);
	}

	private ResponseJson doRandom(final String length, final String type) {
		Integer count = DEFAULT_RANDOM_TXT_LEN;
		RandomStringResponseDTO resp = new RandomStringResponseDTO(getInstanceId());
		resp.setRespCode(ResponseJson.JSON_RESP_CODE_OK);
		resp.setRespMsg("Random text generated");
		try {
			if (StringUtils.isNotBlank(length) && Integer.parseInt(length) > 0) {
				// Using default length if not specified
				count = Integer.parseInt(length);
			}
		} catch (Exception e) {
			// Just take the default length for any exception thrown, e.g.: number format
			resp.setRespSubCode(MessageFormat.format(
					"Something went wrong [{0}] The text was generated using the default length of [{1}]",
					new Object[] { e.getLocalizedMessage(), count }));
		}
		populateRandomString(type, length, count, resp);
		return resp;
	}

	private void populateRandomString(final String type, final String length, final Integer count, RandomStringResponseDTO resp) {
		resp.setType(type);
		resp.setLength(count);
		switch(type) {
		case ALPHANUMERIC: 	resp.setRandom(RandomStringUtils.randomAlphanumeric(count));
		break;
		case NUMERIC: 		resp.setRandom(RandomStringUtils.randomNumeric(count));
		break;
		case ALPHABETIC: 	resp.setRandom(RandomStringUtils.randomAlphabetic(count));
		break;
		default: 			resp.setRandom(RandomStringUtils.randomAlphanumeric(count));
		}

	}

	/**
	 * Generate an unique alphanumeric string that can be used as a Correlation ID.
	 *
	 * @return
	 */
	@RequestMapping(value = "/unique/correlationid", method = RequestMethod.GET)
	public ResponseJson uniqueCorrelationId() {
		try {
			UniqueCorrelationIDResponseDTO resp = new UniqueCorrelationIDResponseDTO(getInstanceId());
			resp.setRespCode(ResponseJson.JSON_RESP_CODE_OK);
			resp.setRespMsg("Unique Correlation ID generated");
			StringBuffer corrIdBuf = new StringBuffer(128);
			corrIdBuf.append(generateUniqueCorrelationId());
			resp.setGeneratedCorrelationId(corrIdBuf.toString());
			resp.setLength(corrIdBuf.length());
			return resp;
		} catch (Exception e) {
			return getErrorResponseDTO(e);
		}
	}

	/**
	 * Generate an unique Correlation ID string.
	 *
	 * @return
	 */
	private String generateUniqueCorrelationId() {
		return Base64.getUrlEncoder().encodeToString(getBCryptEncoder().encode(MessageFormat.format(
				"{0}-{1}",
				new Object[] {
						getUniqueInstanceId(),
						RandomStringUtils.randomAlphanumeric(CORR_ID_RANDOM_TXT_LEN) })).getBytes());
	}

	/**
	 * Returns an unique instance ID that is specific to the running instance of the Edge Server.
	 *
	 * @return
	 */
	private String getUniqueInstanceId() {
		if (StringUtils.isBlank(uniqueInstanceId)) {
			return MessageFormat.format(
					"{0}-{1}",
					new Object[] { String.valueOf(System.currentTimeMillis()), UUID.randomUUID().toString() });
		} else {
			return uniqueInstanceId;
		}
	}

	/**
	 * Returns a new BCrypt password encoder.
	 *
	 * @return
	 */
	private BCryptPasswordEncoder getBCryptEncoder() {
		return new BCryptPasswordEncoder(10, new SecureRandom(UUID.randomUUID().toString().getBytes()));
	}

}
