package com.jx.enums;

public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"操作成功"),
    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USER_NOT_EXIT(501,"用户不存在"),
    REQUIRE_USERNAME(503, "必需填写用户名"),
    REQUIRE_PASSWORD(504,"必须填写密码"),
    LOGIN_ERROR(505,"用户名或密码错误"),
    SIGN_IN_EXIST(506,"该签到已存在"),
    SET_MENU_ERROR(509,"修改菜单'活动发布'失败，上级菜单不能选择自己"),
    HAS_CHILD_MENU(510,"存在子菜单不允许删除"),
    ROLE_EXIST(511, "角色已存在"),
    ROLE_DEPARTMENT_NOT_NULL(512,"角色对应部门不能为空"),
    ROLE_TITLE_NOT_NULL(512,"角色对应职称不能为空"),
    ROLE_KEY_NOT_NULL(513,"权限不能为空"),
    USER_IS_NOT_BIND(514,"该微信暂未绑定"),
    SIGNED_IN(515,"您已签到过"),
    USER_WX_IS_BIND(516,"该用户已绑定过微信，如有问题请联系管理员"),
    TEMPLATE_DOWNLOAD_ERROR(517,"模板下载失败"),
    NO_IMPORT_DATA(518,"excel无有效数据"),
    NOT_USE_TEMPLATE(519,"请使用数据导入模板"),
    POST_EXIT(520,"岗位已存在"),
    DEPARTMENT_EXIT(521,"部门已存在"),
    CATEGORY_NOT_EXIT(522,"该分类不存在"),
    DEPARTMENT_USED(523,"当前部门被使用，无法删除"),
    POST_USED(524,"当前岗位被使用，无法删除"),
    LOCATION_EXIT(525,"地点已存在"),
    LOCATION_USED(526,"当前地点被使用，无法删除"),
    TIMESLOT_USED(527,"当前时间段被使用，无法删除"),
    TIMESLOT_EXIT(528,"该时间段已存在"),
    NEED_TOKEN(529,"需要携带checkToken"),
    REQUEST_AGAIN(530,"请求重复"),
    FILE_TYPE_ERROR(531,"上传图片类型错误，请选择png、jpg格式图片" ),
    SIGN_IN_TIME_PASS(532,"签到时间已过"),
    SING_IN_NEED_BEFORE_SIGN_OUT(533,"请先创建签到再选择签退"),
    ADD_SCHEDULE_ERROR(534,"报班失败"),
    PARAM_NOT_NULL(535,"参数不能为空"),
    KILL_NOT_START(536,"报班还没有开始，请等待"),
    KILL_PASS(537,"报班已经结束"),
    TIME_FORMAT_ERROR(538,"时间格式错误，请不要输入中文引号"),
    TIME_INFO_ERROR(539,"时间信息有误"),
    CATEGORY_EXIT(540,"分类已存在"),
    CATEGORY_USED(541,"当前分类已被使用,无法删除"),
    JUST_ONE_POST(542,"该时间段只能选择一个岗位哦~"),
    WX_IS_BIND(543,"该微信已绑定过用户"),
    ID_NOT_NULL(544,"学号不能为空"),
    NAME_NOT_NULL(545,"姓名不能为空"),
    SEX_NOT_NULL(546,"性别不能为空"),
    EMAIL_NOT_NULL(547,"邮箱不能为空"),
    PHONE_NUMBER_NOT_NULL(548,"电话不能为空"),
    QQ_NOT_NULL(549,"qq不能为空"),
    CODE_NOT_NULL(550,"微信授权失败"),
    PEOPLE_NUM_ERROR(551,"当前报名人数大于修改后的人数"),
    SIGN_IN_TYPE_ERROR(552,"签到类型错误"),
    ACTIVITY_NOT_EXITS(553,"当前活动不存在请刷新后重试"),
    ACTIVITY_NOT_RECRUIT(554,"当前活动已停止招募"),
    ACTIVITY_ASSIGNMENT_NOT_RECRUIT(555,"当前班次已停止招募"),
    USER_PERMISSION_NOT_ENOUGH(556,"用户权限不足"),
    ACTIVITY_ASSIGNMENT_NOT_EXITS(557,"当前班次不存在请刷新后重试" ),
    THIS_MONTH_CANCEL_NUMBER_RUN_OUT(558,"本月退选次数已用完"),
    EMAIL_SEND_ERROR(559,"邮件发送失败"),
    POST_NAME_EMPTY(560,"岗位名不为空"),
    CATEGORY_EMPTY(560,"分类不为空");


    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
