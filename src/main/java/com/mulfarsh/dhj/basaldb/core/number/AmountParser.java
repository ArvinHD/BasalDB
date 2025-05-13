package com.mulfarsh.dhj.basaldb.core.number;

import cn.hutool.core.util.NumberUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class AmountParser {

    // 定义常见的金额格式
    private static final Pattern CURRENCY_SYMBOL_PATTERN = Pattern.compile("^[\\p{Sc}]?(?:\\d{1,3}(,\\d{3})*|\\d{1,7})(\\.\\d+)?$");

    // 定义最大金额
    private static final DecimalFormat FORMAT = new DecimalFormat("#,##0.##");

    private static final Integer ROUND = 2;
    public static BigDecimal parseAmount(String amountStr) {
        return parseAmount(amountStr, CURRENCY_SYMBOL_PATTERN);
    }

    public static BigDecimal parseAmount(String amountStr, Integer max) {
        String regex = "^[\\p{Sc}]?(?:\\d{1,3}(,\\d{3})*|\\d{1," + max + "})(\\.\\d+)?$";
        Pattern pattern = Pattern.compile(regex);
        return parseAmount(amountStr, pattern);
    }

    public static BigDecimal parseAmount(String amountStr, Pattern pattern) {
        if (amountStr == null || amountStr.trim().isEmpty()) {
            return null;
        }

        // 去除前后空格
        amountStr = amountStr.trim();

        // 将中文标点符号转换为英文标点符号
        amountStr = convertChinesePunctuationToEnglish(amountStr);
        amountStr = amountStr.replaceAll("\\s+", "")
                .replaceAll(",", "");

        if (!pattern.matcher(amountStr).matches()) {
            return null;
        }

        amountStr = removeCurrencySymbol(amountStr);

        try {
            BigDecimal decimal = NumberUtil.toBigDecimal(amountStr);
            return decimal;
        } catch (Exception e) {
            return null;
        }
    }

    public static String removeCurrencySymbol(String input) {
        if (input == null || input.isEmpty()) {
            return input; // 若字符串为空，直接返回
        }

        // 常见货币符号
        String currencySymbols = "¥￥$€£₩₹₽฿₫₭₦₲₪₵៛₺₴₨";

        // 如果第一位是货币符号，则去掉
        if (currencySymbols.indexOf(input.charAt(0)) != -1) {
            return input.substring(1).trim(); // 去掉第一位货币符号并移除多余空格
        }
        return input; // 否则返回原字符串
    }


    private static String convertChinesePunctuationToEnglish(String amountStr) {
        if (amountStr == null) {
            return null;
        }
        return amountStr.replace('，', ',').replace('。', '.');
    }


    public static String formatAmount(BigDecimal amount) {
        if (amount == null) {
            return null;
        }
        return FORMAT.format(amount);
    }

    public static String formatAmount(BigDecimal amount, String formatStr) {
        try {
            final DecimalFormat decimalFormat = new DecimalFormat(formatStr);
            return formatAmount(amount, formatStr);
        } catch (Exception e) {
            return null;
        }
    }

    public static String formatAmount(BigDecimal amount, DecimalFormat format) {
        if (amount == null) {
            return null;
        }
        return format.format(amount);
    }

    public static Boolean isZero(String amountStr) {
        BigDecimal amont = parseAmount(amountStr);
        if (amont == null) {
            return false;
        }
        return BigDecimal.ZERO.compareTo(amont) == 0;
    }


    public static void main(String[] args) {
        String[] testAmounts = {
                "¥1,234.56", "￥1,234.56", "$1,234.56", "€1,234.56",
                "£1,234.56", "₩1,234.56", "₹1,234.56", "₽1,234.56",
                "1234.56", "฿1,234", "₫12345", "₭98,765.43",
                "$1,234.56", "€1.234,56", "¥1,234.56", "1,234.56",
                "1234,56", "1234.56", "1234", "1,234", "1234.567", "abc",
                "1,234.56.78", "1,234,567.89", "233232b12s", "10000000.00",
                "123,456，7。00", "1 234 567.89", "1 234,567.89", "123, 213.453",
                "123, 213.455", "-1", "-123.234234"      };

        for (String amountStr : testAmounts) {
            BigDecimal amount = parseAmount(amountStr);
            String decimalStr = formatAmount(amount);
            System.out.println("Input: " + amountStr + " -> BigDecimal: " + decimalStr);
        }
    }
}
