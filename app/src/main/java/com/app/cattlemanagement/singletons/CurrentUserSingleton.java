package com.app.cattlemanagement.singletons;

import com.app.cattlemanagement.models.User;

public class CurrentUserSingleton {
    public static User instance;

    private CurrentUserSingleton() {
    }

    public static User getInstance() {
        return instance;
    }

    public static void setInstance(User instance) {
        CurrentUserSingleton.instance = instance;
    }
}
