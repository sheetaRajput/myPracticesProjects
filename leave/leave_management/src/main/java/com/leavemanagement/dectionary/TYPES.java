package com.leavemanagement.dectionary;

import com.leavemanagement.config.ConfigurationEnum;

public enum TYPES implements ConfigurationEnum {

	TYPES("types");

	private final String key;

	TYPES(String key) {
		this.key = key;
	}

	@Override
	public String getKey() {
		return this.key;
	}

	@Override
	public String getType() {
		return "types";
	}

}
