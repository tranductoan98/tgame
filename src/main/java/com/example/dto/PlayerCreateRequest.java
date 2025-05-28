package com.example.dto;

import com.example.enums.Gender;

public class PlayerCreateRequest {
	
	private Integer userId;
    private String characterName;
    private Gender gender;
	
	public PlayerCreateRequest() {
		super();
	}
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getCharacterName() {
		return characterName;
	}

	public void setCharacterName(String characterName) {
		this.characterName = characterName;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}
}
