package com.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "map_image_data")
public class MapImageData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "map_id", nullable = false)
    private Integer mapId;

    @Column(name = "image_id", nullable = false)
    private Integer imageId;

    // Vị trí cắt trong atlas
    @Column(nullable = false)
    private Integer x;

    @Column(nullable = false)
    private Integer y;

    @Column(nullable = false)
    private Integer w;

    @Column(nullable = false)
    private Integer h;

    // Tọa độ hiển thị trên bản đồ (theo tile)
    @Column(name = "render_x", nullable = false)
    private Integer renderX;

    @Column(name = "render_y", nullable = false)
    private Integer renderY;

    @Column(name = "z_order", nullable = false)
    private Integer zOrder = 0;

    @Column(name = "is_walkable", nullable = false)
    private Boolean isWalkable = true;

    @Column(length = 32)
    private String type;

    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMapId() {
        return mapId;
    }

    public void setMapId(Integer mapId) {
        this.mapId = mapId;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getW() {
        return w;
    }

    public void setW(Integer w) {
        this.w = w;
    }

    public Integer getH() {
        return h;
    }

    public void setH(Integer h) {
        this.h = h;
    }

    public Integer getRenderX() {
        return renderX;
    }

    public void setRenderX(Integer renderX) {
        this.renderX = renderX;
    }

    public Integer getRenderY() {
        return renderY;
    }

    public void setRenderY(Integer renderY) {
        this.renderY = renderY;
    }

    public Integer getZOrder() {
        return zOrder;
    }

    public void setZOrder(Integer zOrder) {
        this.zOrder = zOrder;
    }

    public Boolean getIsWalkable() {
        return isWalkable;
    }

    public void setIsWalkable(Boolean isWalkable) {
        this.isWalkable = isWalkable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
