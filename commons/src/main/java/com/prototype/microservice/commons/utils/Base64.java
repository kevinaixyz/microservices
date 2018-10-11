package com.prototype.microservice.commons.utils;

public final class Base64 {
    private Base64() {}

    /**
     * Decode a Base64 URL Safe encoded string into a byte array.
     *
     * @param base64UrlSafeString
     * @return
     * @throws Exception
     */
    public static byte[] UriBase64ToBytes(final String base64UrlSafeString) {
        return java.util.Base64.getUrlDecoder().decode(base64UrlSafeString);
    }

    /**
     * Encode a byte array into a Base64 URL Safe encoded string.
     *
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String bytesToUriBase64(final byte[] bytes) {
        return java.util.Base64.getUrlEncoder().encodeToString(bytes);
    }

}
