import java.math.BigInteger;
public class EncDec {
    public static String Encryptor(String m, BigInteger e,BigInteger n){
        String [] message = m.split(" ");
        String ciphertext ="";
        for(String key : message){
            BigInteger base = new BigInteger((Long.parseLong(key,16))+"");
            base = base.modPow(e,n);
            ciphertext = ciphertext.concat(base.toString()+" ");
        }
        return ciphertext;
    }
    public static String Decryptor(String c, BigInteger d,BigInteger n){
        String [] cipher = c.split(" ");
        String message = "";
        for(String key: cipher){
            BigInteger base = new BigInteger(Long.parseLong(key)+"");
            base = base.modPow(d,n);
            String a = Integer.toHexString((base.intValue()));
            if(a.length()==1){
                message = message.concat("0" + a + " ");
            }else {
                message = message.concat(a + " ");
            }
        }
        return message;
    }


}
