package com.khanh.expensemanagement.util.db;

import java.util.Objects;

public class SqlParamsUtil {

    public static final char WILD_CARD = '%';

    /**
     * 前方一致
     * @param str 文字列
     * @return エスケープ処理済みの文字列、ブランクのときはnull
     */
    public static String forward(String str) {

        if (Objects.equals(str, "")) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        for (char c : str.toCharArray()) {
            switch (c) {
                case '\\':
                    sb.append("\\\\");
                    break;
                case '_':
                    sb.append("\\_");
                    break;
                case '%':
                    sb.append("\\%");
                    break;
                default:
                    sb.append(c);
            }
        }

        sb.append(WILD_CARD);
        return sb.toString();
    }
}
