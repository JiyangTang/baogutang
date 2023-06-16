package top.baogutang.common.utils;

/**
 * @description:
 * @author: nikooh
 * @date: 2023/06/15 : 12:03
 */
public class UserThreadLocal {
    private static final ThreadLocal<Long> userThread = new ThreadLocal<>();

    public static void set(Long userId) {
        userThread.set(userId);
    }

    public static Long get() {
        return userThread.get();
    }

    public static void remove() {
        userThread.remove();
    }
}
