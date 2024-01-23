package br.com.askcode.askcode.Config.Constants;

public class WebSecurityConfigConstants {
    // URLS
    public static final String URL_LOCAL = "http://localhoost:4200";

    // PATHS
    public static final String URI_SAVE_USER = "/api/save-user";
    public static final String URI_LOGIN_USER = "/api/login";
    public static final String URI_API = "/api/**";

    // SWAGGER
    public static final String URI_SWAGGER_UI = "/swagger-ui/***";
    public static final String URI_SWAGGER_API_DOCS = "/v2/api-docs";
    public static final String URI_SWAGGER_RESOURCES = "/swagger-resources/**";
    public static final String URI_SWAGGER_CONFIGURATION = "/configuration/**";
    public static final String URI_SWAGGER_WEBJARS = "/webjars/**";

    // METHODS
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_UPDATE = "UPDATE";
    public static final String METHOD_DELETE = "DELETE";
}
