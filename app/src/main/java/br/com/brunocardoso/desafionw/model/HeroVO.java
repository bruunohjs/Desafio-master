package br.com.brunocardoso.desafionw.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class HeroVO implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("class_id")
    @Expose
    private Integer classId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("health_points")
    @Expose
    private Double healthPoints;
    @SerializedName("defense")
    @Expose
    private Double defense;
    @SerializedName("damage")
    @Expose
    private Double damage;
    @SerializedName("attack_speed")
    @Expose
    private Double attackSpeed;
    @SerializedName("moviment_speed")
    @Expose
    private Double movimentSpeed;
    @SerializedName("class_name")
    @Expose
    private String className;
    @SerializedName("specialties")
    @Expose
    private List<Integer> specialties;
    @SerializedName("photos")
    @Expose
    private List<String> photos;

    public HeroVO() {
    }

    public HeroVO(Integer id, Integer classId, String name, Double healthPoints, Double defense, Double damage, Double attackSpeed, Double movimentSpeed, String className, List<Integer> specialties, List<String> photos) {
        this.id = id;
        this.classId = classId;
        this.name = name;
        this.healthPoints = healthPoints;
        this.defense = defense;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.movimentSpeed = movimentSpeed;
        this.className = className;
        this.specialties = specialties;
        this.photos = photos;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(Double healthPoints) {
        this.healthPoints = healthPoints;
    }

    public Double getDefense() {
        return defense;
    }

    public void setDefense(Double defense) {
        this.defense = defense;
    }

    public Double getDamage() {
        return damage;
    }

    public void setDamage(Double damage) {
        this.damage = damage;
    }

    public Double getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(Double attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public Double getMovimentSpeed() {
        return movimentSpeed;
    }

    public void setMovimentSpeed(Double movimentSpeed) {
        this.movimentSpeed = movimentSpeed;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<Integer> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<Integer> specialties) {
        this.specialties = specialties;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }
}
