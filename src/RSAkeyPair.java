
public class RSAkeyPair {
    private int p = 29;
    private int q = 43;

    private int n = p*q; //1247
    private int z = (p-1)*(q-1); //1176

    private int e = 65;

    private int d = calculateD(); //977

    private int calculateD(){

        for(int i = 2 ; i < 2000 ; i++){

            if(((e*i-1)%1176)==0){

                return i;
            }
        }
        return 0;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getE() {
        return e;
    }

    public void setE(int e) {
        this.e = e;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }
}
