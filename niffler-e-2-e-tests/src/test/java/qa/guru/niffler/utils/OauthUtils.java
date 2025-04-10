package qa.guru.niffler.utils;

import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class OauthUtils {

    public static String generateCodeVerifier() {
        byte[] code = new byte[32];
        new SecureRandom().nextBytes(code);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(code);
    }

    public static String generateCodeChallenge(String codeVerifier) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(codeVerifier.getBytes());
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }


    public static String getCodeFromRedirectUrl(String redirectUrl) {
        URI uri = URI.create(redirectUrl);
        String queryParam = uri.getQuery();

        String[] params = queryParam.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue[0].equals("code")) {
                return keyValue[1];
            }
        }
        throw new IllegalArgumentException("Invalid redirect URL: " + redirectUrl);
    }
}
