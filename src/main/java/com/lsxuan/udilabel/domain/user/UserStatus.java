package com.lsxuan.udilabel.domain.user;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum UserStatus {

    INACTIVE(0), ACTIVE(1);

    @EnumValue
    private final int code;

    UserStatus(int code) {
        this.code = code;
    }
}
