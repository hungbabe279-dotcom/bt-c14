package com.example;

import java.util.*;

public class UserDB {
    private static final Map<Long, User> database = new HashMap<>();

    public static void insert(User user) {
        database.put(user.getUserID(), user);
    }

    public static void update(User user) {
        if (database.containsKey(user.getUserID())) {
            database.put(user.getUserID(), user);
        }
    }

    public static void delete(User user) {
        database.remove(user.getUserID());
    }

    public static User getUser(Long userID) {
        return database.get(userID);
    }

    public static List<User> getUsers() {
        return new ArrayList<>(database.values());
    }
}
