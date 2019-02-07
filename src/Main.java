import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Mac;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    private static PublicKey publicK; //This key objects declared as global.
    private static PrivateKey privateK;
    private static String K1 = "8a091f6c3d17cdf2e64a806defa2f9c9"; //128 bit symmetric key
    private static String K2 = "e8ba9957f2113f9c720a3581dde120ec284d1492cd4eaa81a90fcf0e115ff0f4"; // 256 bit symmetric key
    public static void main(String[] args) throws Exception{
        int questionNumber;
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("Choose question of 1-2-3-4-5: ");
            System.out.println("To exit press 0: ");
            questionNumber = scanner.nextInt();

            if (questionNumber == 1) {
                //Key Pair generated with KeyPairGenerator class
                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
                generator.initialize(2048); // keys generated with 2048 bit prime numbers.
                KeyPair keypair = generator.genKeyPair();
                publicK = keypair.getPublic();
                privateK = keypair.getPrivate();
                System.out.println("Public key: " + publicK.toString() +
                        "\nPrivate key: \t" + bytesToHex(privateK.getEncoded()) +
                        "\nPublic key: \t" + bytesToHex(publicK.getEncoded())); //Key values printed on the screen
            } else if (questionNumber == 2) {
                //Both K1 and K2 encrypted with public key and decrypted with private key.
                System.out.println("For 128 bit K1: ");
                byte[] encryptedK1 = encrypt(publicK, K1);
                System.out.println("Symmetric key before encryption: " + K1);
                System.out.println("Symmetric key after encryption: " + bytesToHex(encryptedK1)); //hex values of the chars
                byte[] decryptedK1 = decrypt(privateK, encryptedK1);
                System.out.println("Symmetric key after decryption: " + new String(DatatypeConverter.parseHexBinary(bytesToHex(decryptedK1))));

                System.out.println("For 256 bit K2: ");
                byte[] encryptedK2 = encrypt(publicK, K2);
                System.out.println("Symmetric key before encryption: " + K2);
                System.out.println("Symmetric key after encryption: " + bytesToHex(encryptedK2)); //hex values of the chars
                byte[] decryptedK2 = decrypt(privateK, encryptedK2);
                System.out.println("Symmetric key after decryption: " + new String(DatatypeConverter.parseHexBinary(bytesToHex(decryptedK2))));

            } else if (questionNumber == 3) {
                String longtextm = "We can only take the best player who is available. Rounding first, Hinske pumped his fist when it landed in the seats, " +
                        "almost as if he knew a two-run lead would be enough with Halladay cruising and the Yankees scuffling.";
                System.out.println(longtextm);
                //Instance of SHA256 algorithm for obtaining the message digest is created.
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] encodedhash = digest.digest(longtextm.getBytes(StandardCharsets.UTF_8));
                System.out.println("Message digest H(m): " + bytesToHex(encodedhash));
                byte[] signature = sign(privateK, bytesToHex(encodedhash));
                System.out.println("Signed message: " + bytesToHex(signature)); //message signed using private key.
                byte[] verify = verify(publicK, signature); //message verified with public key.
                //hex values of the characters parsed to chars and printed as string
                System.out.println("Verified message: " + new String(DatatypeConverter.parseHexBinary(bytesToHex(verify))));

            } else if (questionNumber == 4) {
                //Secret key initialized using the byte values of K1 key.
                Mac sha256_mac = Mac.getInstance("HmacSHA256");
                SecretKeySpec skey = new SecretKeySpec(K1.getBytes(), "HmacSHA256");
                sha256_mac.init(skey); //HMAC instance gets the secret key object.
                String fox = "The quick brown fox jumps over the lazy dog.";
                //hash code ( hmac) created
                String hmac = Base64.getEncoder().encodeToString(sha256_mac.doFinal(fox.getBytes()));
                System.out.println("Message is: " + fox); //printing the original message.
                System.out.println("HMAC is : " + hmac); //printing hmac value.
            } else if (questionNumber == 5) {
                System.out.println("enter input file directory: ");
                Scanner file = new Scanner(System.in);
                String fileName = file.next();

                System.out.println("enter output file directory (without txt file name): ");
                Scanner file1 = new Scanner(System.in);
                String outputFileDir = file1.next();

                //Question5
                // String plainText = "This is a plain text which need to be encrypted by AES Algorithm with CBC Mode";

                //generate key for AES 128-256 bit
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
                keyGenerator.init(128);
                SecretKey key = keyGenerator.generateKey();
                keyGenerator.init(256);
                SecretKey key2 = keyGenerator.generateKey();

                //generate key for DES 56 bit
                KeyGenerator keyGenerator1 = KeyGenerator.getInstance("DES");
                keyGenerator1.init(56);
                SecretKey key3 = keyGenerator1.generateKey();


                SecureRandom random = new SecureRandom();
                // Generating first IV.
                byte[] IV1 = new byte[16];
                random.nextBytes(IV1);

                //Generating second IV.
                byte[] IV2 = new byte[16];
                random.nextBytes(IV2);

                //Generating 8bit IV for DES
                byte[] IV3 = new byte[8];
                random.nextBytes(IV3);

                String plainText = new String(Files.readAllBytes(Paths.get(fileName)));

                //encrypt with AES 128 bit IV1
                Files.write(Paths.get(outputFileDir + "ENC128IV1AES.txt"), AESCBC.encrypt(plainText.getBytes(), key, IV1));
                System.out.print("time in miliseconds - AES 128 bit IV1 = ");
                System.out.println(System.currentTimeMillis());

                //encrypt with AES 128 bit IV2
                Files.write(Paths.get(outputFileDir + "ENC128IV2AES.txt"), AESCBC.encrypt(plainText.getBytes(), key, IV2));
                System.out.print("time in miliseconds - AES 128 bit IV2 = ");
                System.out.println(System.currentTimeMillis());

                //encrypt with AES 256 bit IV1
                Files.write(Paths.get(outputFileDir + "ENC256IV1AES.txt"), AESCBC.encrypt(plainText.getBytes(), key2, IV1));
                System.out.print("time in miliseconds - AES 256 bit IV1 = ");
                System.out.println(System.currentTimeMillis());

                //encrypt with DES 56 bit IV3
                Files.write(Paths.get(outputFileDir + "ENC56IV3DES.txt"), DESCBC.encrypt(plainText.getBytes(), key3, IV3));
                System.out.print("time in miliseconds - DES 56 bit IV3 = ");
                System.out.println(System.currentTimeMillis());

                Files.write(Paths.get(outputFileDir+ "DEC128IV1AES.txt"), AESCBC.decrypt((AESCBC.encrypt(plainText.getBytes(), key, IV1)), key, IV1).getBytes());
                Files.write(Paths.get(outputFileDir+ "DEC128IV2AES.txt"), AESCBC.decrypt((AESCBC.encrypt(plainText.getBytes(), key, IV2)), key, IV2).getBytes());
                Files.write(Paths.get(outputFileDir+ "DEC256IV1AES.txt"), AESCBC.decrypt((AESCBC.encrypt(plainText.getBytes(), key2, IV1)), key2, IV1).getBytes());
                Files.write(Paths.get(outputFileDir+ "DEC56IV1DES.txt"), DESCBC.decrypt((DESCBC.encrypt(plainText.getBytes(), key3, IV3)), key3, IV3).getBytes());
            } else if (questionNumber == 0) {
                System.out.println();
                System.exit(0);
            } else {
                System.out.println("wrong number try again");
            }
        }
    }
        private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
            for (byte aHash : hash) {
                String hex = Integer.toHexString(0xff & aHash);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
        return hexString.toString();
    }
        private static byte[] encrypt (PublicKey key, String plaintext) throws Exception{
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE,key);
            return cipher.doFinal(plaintext.getBytes());
        }
        private static byte [] decrypt (PrivateKey key , byte [] ciphertext) throws Exception{
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE,key);
            return cipher.doFinal(ciphertext);
        }
        private static byte[] verify (PublicKey key, byte [] ciphertext) throws Exception{
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE,key);
            return cipher.doFinal(ciphertext);
        }
        private static byte [] sign (PrivateKey key , String plaintext ) throws Exception{
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE,key);
            return cipher.doFinal(plaintext.getBytes());
        }
    }

