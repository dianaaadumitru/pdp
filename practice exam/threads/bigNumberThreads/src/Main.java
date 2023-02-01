import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        BigInteger x = new BigInteger("12343546535423352432313425");
        BigInteger y = new BigInteger("567890");

        System.out.println("x= " + x);
        System.out.println("y= " + y);

        Karatsuba karatsuba = new Karatsuba();
        System.out.println(karatsuba.productParallelized(x, y));
    }
}
