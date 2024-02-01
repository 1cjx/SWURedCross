import java.math.BigDecimal;

public class TestDemo {
    public static void main(String[] args) {
        Long b = 1706452776000L -  1706444132000L;
        b = b/1000/60;
        Long remainder = b%60L;
        Long temp = remainder < 20L ? 0L : (remainder < 40L ? 30L : 60L);
        Long ans = b -remainder + temp;
        System.out.println((double)ans/60);
    }
}
