import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
public class HashFunction {

    private static String [] HashValues = {"6a09e667", "bb67ae85", "3c6ef372", "a54ff53a" , "510e527f", "9b05688c","1f83d9ab", "5be0cd19"};

    private static String [] K = { "428a2f98","71374491","b5c0fbcf","e9b5dba5","3956c25b","59f111f1","923f82a4","ab1c5ed5",
            "d807aa98","12835b01","243185be","550c7dc3","72be5d74","80deb1fe","9bdc06a7","c19bf174",
            "e49b69c1","efbe4786","0fc19dc6","240ca1cc","2de92c6f","4a7484aa","5cb0a9dc","76f988da",
            "983e5152","a831c66d","b00327c8","bf597fc7","c6e00bf3","d5a79147","06ca6351","14292967",
            "27b70a85","2e1b2138","4d2c6dfc","53380d13","650a7354","766a0abb","81c2c92e","92722c85",
            "a2bfe8a1","a81a664b","c24b8b70","c76c51a3","d192e819","d6990624","f40e3585","106aa070",
            "19a4c116","1e376c08","2748774c","34b0bcb5","391c0cb3","4ed8aa4a","5b9cca4f","682e6ff3",
            "748f82ee","78a5636f","84c87814","8cc70208","90befffa","a4506ceb","bef9a3f7","c67178f2"};


    public static String Padding(String m){
        int messageLength = m.length();
        m = m.concat("1");
        int a = m.length();
        if ((a + 64) % 512 == 0) {
            return m;
        }
        else{
            for(int i = 0; i < (512 - ((a+64) % 512) ) ; i++){
                m = m.concat("0");
            }
        }
        // write length of message in Big Endian Format
        String length = Integer.toString(messageLength,2);
        System.out.println("length binary: " + length);
        byte[] lengthBytes = length.getBytes();
        byte[] lengthReversed = new byte[lengthBytes.length];
        for(int i = 0 ; i< lengthBytes.length ; i++){
            lengthReversed[i] = lengthBytes[lengthBytes.length - i - 1];
        }
        System.out.println("reversed: " + new String(lengthReversed));
        m = m.concat(new String(lengthReversed));

        for(int i = 0 ; i < 64 - lengthBytes.length ; i++){
            m = m.concat("0");
        }
        return m;
    }
    public static BigInteger [][] chunks(String a){
        int totalChunks = a.length() / 512;
        String [] newChunks = new String[totalChunks];
        for(int i = 0; i < totalChunks; i++){
                newChunks[i] = a.substring( i*512 ,(i*512+512));

            System.out.println(newChunks[i].length());
        }
        BigInteger[][] intChunks = new BigInteger[totalChunks][16]; //put the chars in 32 bit integers.
        for(int i = 0; i < totalChunks ; i++){
            for(int j = 0 ; j < 16 ; j++){
                intChunks[i][j] = new BigInteger(newChunks[i].substring(j*32,32*j+32),2);
              //  System.out.println(intChunks[i][j].toString(2));
            }

        }
        return intChunks;
    }
    public static String hashComputation (BigInteger [][] x) throws NoSuchAlgorithmException {
        //message schedule
            BigInteger base = new BigInteger("2"); //2ˆ32
            BigInteger maxInt = base.pow(32);
            BigInteger [] H = new BigInteger[8]; // H iterations
            BigInteger a,b,c,d,e,f,g,h; //working variables
            //using the initial hash values
            H[0] = new BigInteger(HashValues[0],16);
            H[1] = new BigInteger(HashValues[1],16);
            H[2] = new BigInteger(HashValues[2],16);
            H[3] = new BigInteger(HashValues[3],16);
            H[4] = new BigInteger(HashValues[4],16);
            H[5] = new BigInteger(HashValues[5],16);
            H[6] = new BigInteger(HashValues[6],16);
            H[7] = new BigInteger(HashValues[7],16);
            //for each chunk
            for(int n = 0; n < x.length ; n++){
                BigInteger [] W = new BigInteger[64]; //message schedule is declared again in every block
                //Step 1,Prepare the message schedule, {Wt} , for block n
                for(int i = 0; i < 64 ; i++){
                    if(i<16){
                        W[i] = x[n][i];
                    }else{
                        W[i] = o1(W[i-2]).add(W[i-7]).add(o0(W[i-15]).add(W[i-16]).mod(maxInt));
                    }
                }
                //step 2, Initialize the eight working variables, a, b, c, d, e, f, g, and h, with the (i-1)st hash value:
                    a = H[0];
                    b = H[1];
                    c = H[2];
                    d = H[3];
                    e = H[4];
                    f = H[5];
                    g = H[6];
                    h = H[7];
                //step 3, calculate new working variables
                for(int t = 0; t<64 ; t++){
                    BigInteger T1,T2;
                    T1 = h.add(E1(e)).add(Ch(e,f,g)).add(new BigInteger(K[t],16)).add(W[t]).mod(maxInt);
                    T2 = E0(a).add(Maj(a,b,c)).mod(maxInt);
                    h = g;
                    g = f;
                    f = e;
                    e = d.add(T1).mod(maxInt);
                    d = c;
                    c = b;
                    b = a;
                    a = T1.add(T2).mod(maxInt);
                }
                //Step 4, compute new hash values
                H[0] = H[0].add(a).mod(maxInt);
                H[1] = H[1].add(b).mod(maxInt);
                H[2] = H[2].add(c).mod(maxInt);
                H[3] = H[3].add(d).mod(maxInt);
                H[4] = H[4].add(e).mod(maxInt);
                H[5] = H[5].add(f).mod(maxInt);
                H[6] = H[6].add(g).mod(maxInt);
                H[7] = H[7].add(h).mod(maxInt);
            }
            String hashcode = "";
            for (BigInteger hash : H){
                int m = hash.toString(2).length();
                if(m < 32){
                    for(int i = 0; i < 32-m ; i++){
                       hashcode =  hashcode.concat("0");
                    }
                }
                hashcode = hashcode.concat(hash.toString(2));
            }
        return hashcode;
    }
    private static BigInteger ROTR (int n, BigInteger x){
        return  x.shiftRight(n).or(x.shiftLeft(32-n));
    }
    private static BigInteger SHR(int n, BigInteger x){
        return x.shiftRight(n);
    }
    private static BigInteger E0(BigInteger x){ //Σ0
       return ROTR(2,x).xor(ROTR(13,x).xor(ROTR(22,x)));
    }
    private static BigInteger E1(BigInteger x){ //Σ1
        return ROTR(6,x).xor(ROTR(11,x).xor(ROTR(25,x)));
    }
    private static BigInteger Ch(BigInteger x,BigInteger y,BigInteger z){
        return x.and(y).xor(z.and(x.not()));
    }
    private static BigInteger Maj(BigInteger x,BigInteger y,BigInteger z){
        return x.and(y).xor(x.and(z)).xor(y.and(z));
    }
    private static BigInteger o0(BigInteger x){ //σ0
        return ROTR(7,x).xor(ROTR(18,x)).xor(x.shiftRight(3));
    }
    private static BigInteger o1(BigInteger x){ //σ1
        return ROTR(17, x).xor(ROTR(19,x)).xor(x.shiftRight(10));
    }

}