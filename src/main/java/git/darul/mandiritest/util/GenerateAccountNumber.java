package git.darul.mandiritest.util;

import java.util.Random;

public class GenerateAccountNumber {
    public static Integer generateAccountNumber() {
        Random random = new Random();
        int number = 10000000 + random.nextInt(90000000);
        return number;

    }
}
