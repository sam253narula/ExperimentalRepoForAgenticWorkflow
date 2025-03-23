import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class TextEncryptor {

    private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int KEY_SIZE = 256;
    private static final int IV_SIZE = 16;

    public static String encrypt(String plainText) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(KEY_SIZE);
        SecretKey secretKey = keyGen.generateKey();

        byte[] iv = new byte[IV_SIZE];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));

        byte[] combinedIvCiphertext = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, combinedIvCiphertext, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, combinedIvCiphertext, iv.length, encryptedBytes.length);

        return Base64.getEncoder().encodeToString(combinedIvCiphertext);
    }

    public static void main(String[] args) {
        try {
            String plainText = "This is a secret message. 253";
            String encryptedText = encrypt(plainText);
            System.out.println("Encrypted text: " + encryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
