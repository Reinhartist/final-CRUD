package com.qa.crud.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Resource {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @JsonIgnoreProperties({"projects", "skills", "resources"})
    @Relationship(type = "owns", direction = Relationship.INCOMING)
    private Person owner;

    private Resource() {}

    public Resource(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public Person getOwner() {
        return owner;
    }
}
