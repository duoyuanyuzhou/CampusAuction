package org.multiverse.campusauction.util;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class Argon2Util {

    private static final Argon2 argon2 = Argon2Factory.create(
            Argon2Factory.Argon2Types.ARGON2id
    );

    // 加密（存入数据库）
    public static String hash(String password) {
        return argon2.hash(3, 65536, 1, password);
    }

    // 校验（登录时验证）
    public static boolean verify(String hashedPassword, String rawPassword) {
        return argon2.verify(hashedPassword, rawPassword);
    }
}
