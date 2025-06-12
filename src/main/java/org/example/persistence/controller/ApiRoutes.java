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

    public static class GameSessionApiRoutes {
        public static final String BASE = "/session";
        public static final String CREATE = "/create";
        public static final String BY_ID = "/{id}";
        public static final String RUNNING = "/running";
        public static final String JOIN = "/{id}/join";
        public static final String LEAVE = "/{id}/leave";

        public static final String RANKING = "/{id}/ranking";
        public static final String STOP = "/{id}/stop";


    }
}
