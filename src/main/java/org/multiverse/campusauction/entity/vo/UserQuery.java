package org.multiverse.campusauction.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.Date;

@Data
public class UserQuery {
    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 登录名
     */
    @TableField(value = "username")
    private String username;



    /**
     * 昵称
     */
    @TableField(value = "nickname")
    private String nickname;

    /**
     * 学号（可选）
     */
    @TableField(value = "school_id")
    private String schoolId;

    /**
     * 1=学生 2=管理员
     */
    @TableField(value = "role")
    private Integer role;



    /**
     * 0=正常 1=禁用
     */
    @TableField(value = "status")
    private Integer status;

}
