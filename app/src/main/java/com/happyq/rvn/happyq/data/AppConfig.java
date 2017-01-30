package com.happyq.rvn.happyq.data;

public class AppConfig {

    // flag for display ads
    public static final boolean ENABLE_ADSENSE = false;

    // flag for save image offline
    public static final boolean IMAGE_CACHE = true;

    // if you place data more than 200 items please set TRUE
    public static final boolean LAZY_LOAD = false;

    // flag for tracking analytics
    public static final boolean ENABLE_ANALYTICS = true;

    // clear image cache when receive push notifications
    public static final boolean REFRESH_IMG_NOTIF = true;

    //server user register URL
    public static String URL_REGISTER = "https://happyq.txtlinkapp.com/happyq_app/register.php";

    //server user login URL
    public static String URL_LOGIN = "https://happyq.txtlinkapp.com/happyq_app/loginb.php";

    //server user login URL
    public static String URL_QUEUE = "https://happyq.txtlinkapp.com/happyq_app/queue_query.php";

    public static String URL_GMAIL = "https://happyq.txtlinkapp.com/happyq_app/user_gmail.php";

    public static String URL_FACEBOOK = "https://happyq.txtlinkapp.com/happyq_app/user_facebook.php";

}
