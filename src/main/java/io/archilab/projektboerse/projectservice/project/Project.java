package io.archilab.projektboerse.projectservice.project;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.archilab.projektboerse.projectservice.core.AbstractEntity;

import io.archilab.projektboerse.projectservice.module.Module;

import javax.persistence.CascadeType;
import javax.persistence.Column;

import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.persistence.ManyToMany;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Transient;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends AbstractEntity {

    @Setter
    @JsonUnwrapped
    private ProjectName name;

    @Setter
    @JsonUnwrapped
    private ProjectDescription description;

    @Setter
    private ProjectStatus status;

    @NotNull
    @Setter
    @JsonUnwrapped
    private CreatorID creatorID;

    @NotNull
    @Setter
    @JsonUnwrapped
    private CreatorName creatorName;

    @NotNull
    @Setter
    @JsonUnwrapped
    private SupervisorName supervisorName;

    @ManyToMany
    private List<Module> modules = new ArrayList<>();

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    @CreationTimestamp
    private java.util.Date created;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private java.util.Date modified;


    public Project(ProjectName name, ProjectDescription description, ProjectStatus status, @NotNull CreatorID creatorID, @NotNull CreatorName creatorName, @NotNull SupervisorName supervisorName, List<Module> modules) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.creatorID = creatorID;
        this.creatorName = creatorName;
        this.supervisorName = supervisorName;
        this.modules = modules;
    }
}
