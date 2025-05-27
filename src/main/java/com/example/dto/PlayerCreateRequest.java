package com.example.dto;

import com.example.enums.Gender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PlayerCreateRequest {
	
	@NotNull(message = "User ID cannot be null")
	private Integer userId;
	
	@NotBlank(message = "Character name cannot be blank")
    private String characterName;
	
	@NotNull(message = "Gender cannot be null")
    private Gender gender;
    
	public PlayerCreateRequest(Integer userId, String characterName, Gender gender) {
		super();
		this.userId = userId;
		this.characterName = characterName;
		this.gender = gender;
	}
	
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
