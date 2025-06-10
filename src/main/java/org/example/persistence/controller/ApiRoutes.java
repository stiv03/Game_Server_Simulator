package org.example.persistence.controller;

public class ApiRoutes {
    public static class UserApiRoutes {
        public static final String BASE = "/user";
        public static final String BY_USERNAME = "/{username}";
        public static final String BY_ID = "/id/{id}";
    }

    public static class AuthApiRoutes {
        public static final String REGISTER = "/register";
    }
}
