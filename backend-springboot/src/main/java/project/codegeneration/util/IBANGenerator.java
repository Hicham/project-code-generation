package project.codegeneration.util;

import java.math.BigInteger;
import java.util.UUID;

public class IBANGenerator {
    private static final String COUNTRY_CODE = "NL";
    private static final String BANK_CODE = "INHO";

    public static String generateUniqueIBAN() {
        String accountNumber = generateAccountNumber();
        String partialIBAN = COUNTRY_CODE + "00" + BANK_CODE + accountNumber;
        String checkDigits = calculateCheckDigits(partialIBAN);
        return COUNTRY_CODE + checkDigits + BANK_CODE + accountNumber;
    }

    private static String generateAccountNumber() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String numericUUID = new BigInteger(uuid, 16).toString();
        return numericUUID.substring(0, 10);
    }

    private static String calculateCheckDigits(String partialIBAN) {
        String reformattedIBAN = partialIBAN.substring(4) + partialIBAN.substring(0, 4);
        String numericIBAN = convertToNumeric(reformattedIBAN);
        BigInteger ibanNumber = new BigInteger(numericIBAN);
        int checkDigits = 98 - ibanNumber.mod(BigInteger.valueOf(97)).intValue();
        return String.format("%02d", checkDigits);
    }

    private static String convertToNumeric(String input) {
        StringBuilder numericString = new StringBuilder();
        for (char ch : input.toCharArray()) {
            if (Character.isDigit(ch)) {
                numericString.append(ch);
            } else {
                numericString.append(Character.getNumericValue(ch));
            }
        }
        return numericString.toString();
    }
}
