
import java.util.LinkedList;

public class Test {

    public static void main(String[] args) {
        LinkedList<Integer> queue = new LinkedList<Integer>();
        queue.add(1);
        queue.add(2);
        queue.add(3);
        queue.add(4);
        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());
        if (queue.poll() == null) {
            System.out.println("as");
        }
        System.out.println(queue.poll());
        queue.add(1);
    }
}


//osinda eger jol jok bolsa, bar jagina karay kashu kerek, 
//bombosi bolmasa kasu kerek
//13 11 1 100
//.............
//.!.!.!.!.!;!;
//............;
//;!.!.!.!.!;!;
//;...........;
//.!;!.!.!.!.!.
//......;.....;
//.!;!.!.!.!;!.
//;.....;.....;
//;!;!.!.!;!.!.
//;..;..;......
//4
//m 0 8 3 0 0
//m 0 8 4 0 0
//p 1 10 0 0 0
//b 1 10 0 5 2


//monster korse orninga kalip jatr, kashu kerek //ili turu kerek?
//13 11 1 184
//.............
//.!.!.!.!.!.!.
//.............
//;!.!.!.!.!.!.
//;............
//.!;!.!.!.!.!.
//......;......
//.!;!.!.!.!.!.
//;.....;.....;
//;!;!.!.!;!.!.
//;..;..;......
//3
//p 1 10 6 0 0
//b 1 10 6 5 2
//m 0 10 7 0 0