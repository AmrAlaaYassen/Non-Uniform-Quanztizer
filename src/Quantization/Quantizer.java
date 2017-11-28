package Quantization ;

import javax.xml.bind.NotIdentifiableEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Quantizer {
    private ArrayList <Integer> averages = new ArrayList<Integer>() ;
    ArrayList <Integer> pixelsList = new ArrayList<Integer>() ;
    int numberOfBits = 0 ;

    public void compress () {
        int sum = 0;
        for (int i = 0; i < pixelsList.size(); i++) {
            sum += pixelsList.get(i);
        }

        Node root = new Node();
        int numOfLevels = (int) Math.pow(2, this.numberOfBits);
        root.pixelsList = this.pixelsList;

        setTree(root, numOfLevels);

        preorder(root);

        List<Integer> Last = averages.subList(averages.size() - numOfLevels, averages.size());

        ArrayList<Integer> Ranges = new ArrayList<Integer>();
        Ranges.add(0);
        for (int i = 0; i < Last.size() - 1; i++) {
            int temp = (Last.get(i) + Last.get(i + 1)) / 2;
            Ranges.add(temp);
        }
        Ranges.add(127);
        ArrayList <Integer> Qs = new ArrayList<Integer>() ;

        for (int i=0 ; i<this.pixelsList.size() ; i++ ) {
            for (int j=0 ; j<Ranges.size() ; j ++ ) {
                if (pixelsList.get(i) < Ranges.get(j)){
                    Qs.add(j-1) ;
                    break;
                }
            }
        }

        try {
            OutputStream os = new FileOutputStream("C:\\Users\\amral\\Desktop\\Ranges.txt");
            for(int i = 0; i < Last.size() ; i++) {
             os.write(Last.get(i));
            }
            os.close();
         }catch (Exception e) {
            e.printStackTrace();
            }

        try {
            OutputStream os = new FileOutputStream("C:\\Users\\amral\\Desktop\\Qs.txt");
            for(int i = 0; i < Qs.size() ; i++) {
                os.write(Qs.get(i));
            }
            os.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("input : " + pixelsList);
        System.out.println("Q : " +Qs );
    }


    public void decompress () {
        ArrayList <Integer> Last = new ArrayList<Integer>()   ;
        ArrayList <Integer> QS = new ArrayList<Integer>() ;
        try {
            InputStream is = null;
            is = new FileInputStream("C:\\Users\\amral\\Desktop\\Ranges.txt");
            int size = is.available();

            for (int i = 0; i < size; i++) {
                Last.add((int)is.read()) ;
            }
            is = null;
            is = new FileInputStream("C:\\Users\\amral\\Desktop\\Qs.txt");
            size = is.available();
            for (int i = 0; i < size; i++) {
                QS.add((int)is.read()) ;
            }
            is.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList <Integer> output = new ArrayList <Integer>() ;
        for (int i = 0 ; i< QS.size() ; i++ ) {
            for (int j=0 ; j<Last.size() ; j++ ) {
                if (QS.get(i) == j ) {
                    output.add (Last.get(j)) ;
                    break;
                }
            }
        }

        System.out.println("Q-1 : " + output);
    }

    public void setTree (Node node , int NumOfLevels) {
        if (NumOfLevels == 0)
        {
            return;
        }else {

        int a = node.avg - 1 ;
        int b = node.avg + 1 ;
        node.left = new Node () ; node.right = new Node () ;

        for (int i = 0 ; i< node.pixelsList.size() ; i++ ) {
            int temp1 , temp2 ;
            temp1 = Math.abs(a-node.pixelsList.get(i)) ;
            temp2 = Math.abs(b- node.pixelsList.get(i)) ;

            if (temp2 > temp1) {
                node.left.pixelsList.add(node.pixelsList.get(i)) ;
            }else {
                node.right.pixelsList.add(node.pixelsList.get(i)) ;
            }
        }

        if (node.left.pixelsList.size() == 0 ) {
            node.left.avg = node.avg ;
        }else {
            int sumLeft = 0 ;
            for (int i = 0 ; i < node.left.pixelsList.size() ; i++ ) {
                sumLeft+= node.left.pixelsList.get(i) ;
            }
            node.left.avg = sumLeft/node.left.pixelsList.size() ;
        }
        if (node.right.pixelsList.size() == 0 ) {
            node.right.avg = node.avg ;
        }else {
            int sumLeft = 0 ;
            for (int i = 0 ; i < node.right.pixelsList.size() ; i++ ) {
                sumLeft+= node.right.pixelsList.get(i) ;
            }
            node.right.avg = sumLeft/node.right.pixelsList.size() ;
        }
        NumOfLevels -- ;
        setTree(node.left , NumOfLevels);
        setTree(node.right , NumOfLevels);
        }
    }


    private void preorder(Node root) {
        if ((root.left.left == null && root.left.right == null) || (root.right.left == null && root.right.right == null)) {
            return;
        }
        averages.add(root.left.avg);
        averages.add(root.right.avg);
        preorder(root.left);
        preorder(root.right);
    }
}


class Node {
    Node left , right  ;
    ArrayList<Integer> pixelsList ;
    int avg = 0 ;
    Node () {
        left = right = null ;
        pixelsList = new ArrayList<Integer>() ;
    }
}
