import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DESCBC {
    public static byte[] encrypt (byte[] plaintext, SecretKey key, byte[] IV ) throws Exception
    {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "DES");

        IvParameterSpec ivSpec = new IvParameterSpec(IV);

        //Initialize Cipher for ENCRYPT_MODE
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        byte[] cipherText = cipher.doFinal(plaintext);

        //System.out.println("" + Base64.getEncoder().encodeToString(cipherText) );

        return cipherText;

    }

    public static String decrypt (byte[] cipherText, SecretKey key,byte[] IV) throws Exception
    {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "DES");

        //Create IvParameterSpec
        IvParameterSpec ivSpec = new IvParameterSpec(IV);

        //Initialize Cipher for DECRYPT_MODE
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        byte[] decryptedText = cipher.doFinal(cipherText);

        //System.out.println("DeCrypted Text : "+decryptedText);

        return new String(decryptedText);
    }
}
