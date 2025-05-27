package com.example.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Gender {
	 MALE,
	 FEMALE;
	
	 @JsonCreator
	    public static Gender fromString(String key) {
	        return key == null ? null : Gender.valueOf(key.toUpperCase());
	    }
}
