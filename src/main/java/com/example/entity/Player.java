package com.example.entity;

import java.time.LocalDateTime;

import com.example.enums.Gender;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "player")
public class Player {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer playerid;
	
	@ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    private User user;
	
	@Column(nullable = false, unique = true)
    private String name;
	
	@Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;
	
	@Column(nullable = false)
    private int level = 1;
	
	@Column(nullable = false)
	private int experience = 0;
	
	@Column(nullable = false)
    private int gold = 0;

    @Column(nullable = false)
    private int diamond = 0;
    
    @Column(name = "avatar_face", length = 100)
    private String avatarFace;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "status")
    private String status;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    public int getExperienceToNextLevel() {
        return level * 100;
    }
    
    public void addExperience(int amount) {
        this.experience += amount;
        while (this.experience >= getExperienceToNextLevel()) {
            this.experience -= getExperienceToNextLevel();
            this.level++;
        }
    }
	
	public Player() {
		super();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getPlayerid() {
		return playerid;
	}

	public void setPlayerid(Integer playerid) {
		this.playerid = playerid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getDiamond() {
		return diamond;
	}

	public void setDiamond(int diamond) {
		this.diamond = diamond;
	}

	public String getAvatarFace() {
		return avatarFace;
	}

	public void setAvatarFace(String avatarFace) {
		this.avatarFace = avatarFace;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	
}
