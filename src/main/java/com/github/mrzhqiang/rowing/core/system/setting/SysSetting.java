package com.github.mrzhqiang.rowing.core.system.setting;

import com.github.mrzhqiang.rowing.core.domain.AuditableEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class SysSetting extends AuditableEntity {

    private String name;
    private String value;

    @ManyToOne
    private SysSetting parent;

    @OneToMany(mappedBy = "parent")
    @ToString.Exclude
    private List<SysSetting> children;
}
