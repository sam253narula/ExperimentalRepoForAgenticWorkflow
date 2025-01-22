import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PIIPseudonymizer {

    public static String pseudonymizeText(String text) {
        // Define regex patterns for common PII
        String emailPattern = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b";
        String phonePattern = "\\b\\d{3}[-.]?\\d{3}[-.]?\\d{4}\\b";
        String ssnPattern = "\\b\\d{3}-\\d{2}-\\d{4}\\b";

        // Pseudonymize email addresses
        text = pseudonymizePattern(text, emailPattern, "EMAIL");

        // Pseudonymize phone numbers
        text = pseudonymizePattern(text, phonePattern, "PHONE");

        // Pseudonymize SSNs
        text = pseudonymizePattern(text, ssnPattern, "SSN");

        return text;
    }

    private static String pseudonymizePattern(String text, String pattern, String type) {
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(text);
        StringBuffer sb = new StringBuffer();

        while (m.find()) {
            String pii = m.group();
            String pseudonym = type + "_" + hashPII(pii);
            m.appendReplacement(sb, pseudonym);
        }
        m.appendTail(sb);

        return sb.toString();
    }

    private static String hashPII(String pii) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(pii.getBytes());
            return Base64.getEncoder().encodeToString(hash).substring(0, 8);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "HASH_ERROR";
        }
    }

    public static void main(String[] args) {
        String text = "Bull dog email is bulldog@example.com and his phone number is 123-456-7890. " +
                      "His SSN is 123-45-6789.";
        String pseudonymizedText = pseudonymizeText(text);
        System.out.println("Original text: " + text);
        System.out.println("Pseudonymized text: " + pseudonymizedText);
    }
}
