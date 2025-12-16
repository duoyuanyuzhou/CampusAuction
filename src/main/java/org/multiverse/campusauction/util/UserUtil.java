package org.multiverse.campusauction.util;

import org.multiverse.campusauction.entity.domain.User;

public class UserUtil {

    public static User getSafeUser(User user) {
        user.setPassword(null);
        user.setCreateTime(null);
        user.setUpdateTime(null);
        user.setDelFlag(null);

        return user;
    }
}
