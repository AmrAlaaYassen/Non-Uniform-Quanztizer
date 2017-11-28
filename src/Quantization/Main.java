package Quantization ;

import java.util.ArrayList;

public class Main {
    public static void  main (String [] args){
        Quantizer x = new Quantizer() ;
        ArrayList <Integer> arr = new ArrayList<>() ;
        arr.add(6) ; arr.add(15) ; arr.add(17) ; arr.add(60) ; arr.add(100) ; arr.add(90) ; arr.add(66) ; arr.add(59) ;arr.add(18) ;
        arr.add(3) ; arr.add(5) ; arr.add(16) ; arr.add(14) ; arr.add(67) ; arr.add(63) ; arr.add(2) ; arr.add(98) ; arr.add(92) ;
        x.pixelsList = arr ;
        x.numberOfBits = 2 ;
        x.compress();
        x.decompress();
    }
}
