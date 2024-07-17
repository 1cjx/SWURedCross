package com.jx.domain.bean;

import com.jx.enums.AppHttpCodeEnum;
import com.jx.exception.SystemException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleContent {

    public static final String ROLE_USER = "user";

    public static final String ROLE_ASSISTANT = "assistant";
    public static final String ROLE_SYSTEM = "system";
    public static final String SYSTEM_VOLUNTEER ="volunteer";
    public static final String SYSTEM_VOLUNTEER_CONTENT ="假如你是西南大学红十字会的一名经验丰富的志愿者。" +
            "西南大学红十字会经常开展公益事业志愿活动，包括但不限于无偿献血、造血干细胞血样采集、应急救护培训、防艾知识科普、防治肺结核以及控烟活动等。" +
            "作为志愿者，你需要熟悉相关知识，并能够给予同学正确的科普与回答，需要考虑到服务的对象是西南大学的在校大学生。";
    public static final String SYSTEM_EDITOR ="editor";
    public static final String SYSTEM_EDITOR_CONTENT ="假如你是西南大学红十字会的一名宣传部的同学，你对于微信推文与qq说说的攥写非常有经验。" +
            "每当志愿活动开展前会有活动的预告、活动结束后会有活动的总结推文。你需要根据活动的名字、时间、地点、主题等攥写相关的推文或者说说。" +
            "预告要求能够吸引在校大学生来参加活动，展现活动的精彩；总结要求能够生动形象，体现西南大学红十字会与参与活动同学们的辛苦付出。" +
            "文案内容不仅限于活动，也可以多一些新颖的内容。";
    private String role;

    private String content;

    public static RoleContent createUserRoleContent(String content) {
        return new RoleContent(ROLE_USER, content);
    }

    public static RoleContent createAssistantRoleContent(String content) {
        return new RoleContent(ROLE_ASSISTANT, content);
    }
    public static RoleContent createSystemRoleContent(String role){
        if(role.equals(SYSTEM_VOLUNTEER)){
            return new RoleContent(ROLE_SYSTEM,SYSTEM_VOLUNTEER_CONTENT);
        }
        else if(role.equals(SYSTEM_EDITOR)){
            return new RoleContent(ROLE_SYSTEM,SYSTEM_EDITOR_CONTENT);
        }
        else{
            throw new SystemException(AppHttpCodeEnum.CHAT_ROLE_NOT_EXIST);
        }
    }
}
