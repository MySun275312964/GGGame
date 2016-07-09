package com.gg.common;

/**
 * @author guofeng.qin
 */
public class StringUtils {

    public static String concat(Object... strs) {
        StringBuilder sb = threadLocalStringBuilderHolder.get().resetAndGetStringBuilder();
        for (Object str : strs) {
            sb.append(str);
        }
        return sb.toString();
    }

    public static String join(String split, Object... objs) {
        StringBuilder sb = threadLocalStringBuilderHolder.get().resetAndGetStringBuilder();
        int len = objs.length;
        for (int index = 0; index < len; index++) {
            if (index > 0) {
                sb.append(split);
            }
            sb.append(objs[index]);
        }
        return sb.toString();
    }

    public static StringBuilder getStringBuilder() {
        return threadLocalStringBuilderHolder.get().resetAndGetStringBuilder();
    }

    /**
     * 参考 https://github.com/springside/springside4/blob/master/modules/utils/src/
     * main/java/org/springside/modules/utils/StringBuilderHolder.java, 可重用的StringBuilder,
     * 节约StringBuilder内部的char[]
     */
    private static final ThreadLocal<StringBuilderHolder> threadLocalStringBuilderHolder =
            new ThreadLocal<StringBuilderHolder>() {
                protected StringBuilderHolder initialValue() {
                    return new StringBuilderHolder(256);
                }
            };

    private static class StringBuilderHolder {
        private final StringBuilder sb;

        public StringBuilderHolder(int capacity) {
            sb = new StringBuilder(capacity);
        }

        /**
         * 重置StringBuilder内部的writerIndex, 而char[]保留不动.
         */
        public StringBuilder resetAndGetStringBuilder() {
            sb.setLength(0);
            return sb;
        }
    }
}
