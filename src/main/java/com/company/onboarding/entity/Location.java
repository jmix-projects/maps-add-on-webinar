package com.company.onboarding.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import java.util.UUID;

@JmixEntity
@Table(name = "LOCATION")
@Entity
public class Location {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @InstanceName
    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "CITY")
    private String city;

    @Column(name = "TYPE_")
    private Integer type;

    @NotNull
    @Column(name = "BUILDING", nullable = false)
    private Point building;

    @Column(name = "BUILDING_AREA")
    private Polygon buildingArea;

    public Polygon getBuildingArea() {
        return buildingArea;
    }

    public void setBuildingArea(Polygon buildingArea) {
        this.buildingArea = buildingArea;
    }

    public Point getBuilding() {
        return building;
    }

    public void setBuilding(Point building) {
        this.building = building;
    }

    public LocationType getType() {
        return type == null ? null : LocationType.fromId(type);
    }

    public void setType(LocationType type) {
        this.type = type == null ? null : type.getId();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}