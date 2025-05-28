package com.example.dto;

import com.example.enums.Gender;

public class PlayerUpdateRequest {
	
	private Integer playerid;
    private String name;
    private Gender gender;
    private int level;
    private int experience;
    private int gold;
    private int diamond;
    private String avatarFace;
	
	public PlayerUpdateRequest() {
		super();
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
	
	
}
