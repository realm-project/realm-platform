package net.realmproject.platform.util;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class RealmAuthentication {

    private static final int SALT_BYTE_SIZE = 64;
    private static final String HASH_ALGORITHM = "SHA-512";

    protected static Log log = LogFactory.getLog(RealmAuthentication.class);

    public static byte[] generateRandomBytestring(int bytesize) {
        byte[] salt = new byte[bytesize];
        Random rand = getRandom();
        rand.nextBytes(salt);
        return salt;
    }

    public static String generateTemporaryPassword() {
        return generateTemporaryPassword(10).toLowerCase();
    }

    public static String generateTemporaryPassword(int length) {
        Base32 b32 = new Base32();
        return b32.encodeAsString(generateRandomBytestring(5));
    }

    public static String generateSalt() {
        return Hex.encodeHexString(generateRandomBytestring(SALT_BYTE_SIZE));
    }

    public static Random getRandom() {
        return new SecureRandom();
    }

    public static String hash(String password, String salt) {
        try {
            password = password + salt;
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            digest.update(password.getBytes("UTF-8"));
            byte[] bytedigest = digest.digest();
            return Hex.encodeHexString(bytedigest);
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e); // is a runtime exception
                                                   // preferred?
        }
    }

    public static boolean authenticate(String password, String currentHash, String currentSalt)
            throws InterruptedException {

        // compute hash of provided password with current salt
        String loginHash = hash(password, currentSalt);

        if (currentHash == null) {
            log.debug("Hash is null, not authenticating");
            return false;
        }

        log.debug("currentHash = " + currentHash);
        log.debug("loginHash = " + loginHash);

        // compare current hash with hash of provided password
        if (currentHash.equals(loginHash)) return true;
        return false;

    }

    public static String generateToken() {
        return generateToken(4);
    }

    public static String generateToken(int blocks) {

        int bits = blocks * 20;
        int bytes = (int) Math.ceil(bits / 8f);

        byte[] bytestring = RealmAuthentication.generateRandomBytestring(bytes);
        Base32 b32 = new Base32();
        String b32string = b32.encodeAsString(bytestring).toLowerCase();

        String token = b32string.substring(0, blocks * 4);
        return formatToken(token);
    }

    public static String formatToken(String inputToken) {
        inputToken = inputToken.replace("-", "");
        inputToken = inputToken.replace(" ", "");
        inputToken = inputToken.replace(":", "");
        inputToken = inputToken.replace("_", "");
        inputToken = inputToken.replace(".", "");

        inputToken = inputToken.trim();

        String pass = "";
        String block = "";
        while (inputToken.length() > 0) {
            if (inputToken.length() < 4) {
                if (pass.length() > 0) {
                    pass += "-";
                }
                pass += inputToken;
                break;
            }
            block = inputToken.substring(0, 4);
            inputToken = inputToken.substring(4);
            if (pass.length() > 0) {
                pass += "-";
            }
            pass += block;
        }

        return pass;

    }

    public static void randomDelay() throws InterruptedException {
        randomDelay(100, 100);
    }

    public static void randomDelay(int base, int range) throws InterruptedException {
        int delay = base + RealmAuthentication.getRandom().nextInt(range);
        Thread.sleep(delay);
    }

}
