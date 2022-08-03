package com.github.mrzhqiang.rowing.api.system.init;

import com.github.mrzhqiang.rowing.api.domain.BaseEntity;
import com.github.mrzhqiang.rowing.api.domain.TaskStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class SysInitStep extends BaseEntity {

    private String name;
    private String path;
    private String action;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    private String tips;
    private Integer ordered;
}
