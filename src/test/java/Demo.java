import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: gengwei
 * @Date: 2019-12-22 10:14
 * @Description:
 */
public class Demo {
    @Test
    public void test(){
        int x = 1024;
        int b = 0x00000400;
        Integer y = 1;
        Integer y2 = 2;
        Integer y3 = 4;
        Integer y4 = 8;
        Integer y5 = 16;
        Integer y6 = 32;
        Integer y7 = 64;
        Integer y8 = 128;
        Integer y9 = 256;
        Integer y10 = 512;
        Integer y11 = 2048;
        System.out.println(x & b);
        System.out.println(y & b);
        System.out.println(y2 & b);
        System.out.println(y3 & b);
        System.out.println(y4 & b);
        System.out.println(y5 & b);
        System.out.println(y6 & b);
        System.out.println(y7 & b);
        System.out.println(y8 & b);
        System.out.println(y9 & b);
        System.out.println(y10 & b);
        System.out.println(y11 & b);
    }

    public static void main2(String[] args) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("", "");
    }

    public static void main3(String[] args) {
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
        map.put("asd","abc");
    }

    public static void main(String[] args) {
        int a = 1;
        if (a == 2) {
            System.out.println(a);
        } else if (1 == 1) {
            System.out.println(1);
        } else if (a == 1) {
            System.out.println("a = " + a);
        } else {
            System.out.println("else");
        }
    }
}
