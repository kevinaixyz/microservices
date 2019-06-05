package com.prototype.microservice.edge.mvc.view;

public final class ViewConstants {

    private ViewConstants() {
    }

    // View Names
    public final static String V_LOGIN = "login";
    public final static String V_LOGOUT = "logout";
    public final static String V_WELCOME = "welcome";
    public final static String V_ADMIN = "admin";
    public final static String V_USER = "user";
    public final static String V_SHARED = "shared";
    public final static String V_SHOWCASE = "showcase";

    public final static String V_ERROR_403 = "error/403";

    // Field IDs (i.e.: within a model)
    public final static String F_INSTACNE_ID = "F_INSTANCE_ID";
    public final static String F_COMPONENT_NAME = "F_COMPONENT_NAME";
    public final static String F_ZUUL_ROUTES = "F_ZUUL_ROUTES";

}
