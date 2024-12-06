package com.leavemanagement.entity;

import lombok.Data;

@Data
public class FinalResponse {

	private Boolean status;
	private String message;
	private Object data;
}
