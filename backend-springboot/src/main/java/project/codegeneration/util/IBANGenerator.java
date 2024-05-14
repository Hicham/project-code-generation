package project.codegeneration.util;

import java.util.Random;

public class IBANGenerator {
    public static String generateIBAN(){
        String randomTwoNumbers = String.format("%02d", new Random().nextInt(100));

        String randomTenNumbers = String.format("%010d", new Random().nextInt(1000000000));

        return "NL" + randomTwoNumbers + "INHO" + randomTenNumbers;
    }
}
