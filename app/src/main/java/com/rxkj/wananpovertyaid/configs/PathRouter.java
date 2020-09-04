package com.rxkj.wananpovertyaid.configs;

import com.alibaba.android.arouter.launcher.ARouter;

public class PathRouter {

    /**
     * =============================================================================================
     * ====================================== 路径名定义 =============================================
     * =============================================================================================
     */
    //主页相关各模块路径
    private static final String PATH_GROUP_MAIN = "/PATH_GROUP_MAIN";
    public static final String PATH_GROUP_MAIN_MAINPAGE = PATH_GROUP_MAIN + "/MAIN";
    public static final String PATH_GROUP_MAIN_LOGIN = PATH_GROUP_MAIN + "/LOGIN";


    /**
     * =============================================================================================
     * ======================================= 跳转方法定义 ==========================================
     * =============================================================================================
     */

    /**
     * 主页
     */
    public static void route2Mainpage() {
        ARouter.getInstance().build(PATH_GROUP_MAIN_MAINPAGE)
                .navigation();
    }

    /**
     * 登录页
     */
    public static void route2Login() {
        ARouter.getInstance().build(PATH_GROUP_MAIN_LOGIN)
                .navigation();
    }




}
