package Clases;

public class Hash{
    public static String hash12(String texto){
        int a = 0;
        int b = 1;
        int f;
        String hash="";
        for(int i = 0; i<12;i++){
            f = a + b;
            a = f-a;
            b = f;
            int x = 0;
            for(int j=0;j<texto.length();j++){
                x += texto.charAt(j)*f+hash.length();
            }
            hash+= ""+ (int)(x%10);
        }
        return hash;
    }
    public static String hash24(String texto){
        int a = 0;
        int b = 1;
        int f;
        String hash="";
        for(int i = 0; i<12;i++){
            f = a + b;
            a = f-a;
            b = f;
            int x = 0;
            for(int j=0;j<texto.length();j++){
                x += texto.charAt(j)*f+hash.length();
            }
            if((int)(x%122)<48){
            hash+=(char)((int)(x%122)+48) +""+(int)(x%10);
            }else{
                hash+=(char)((int)(x%122))+""+(int)(x%10);
            }
        }
        return hash;
    }

}
