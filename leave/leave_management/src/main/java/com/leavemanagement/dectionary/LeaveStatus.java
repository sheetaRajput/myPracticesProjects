package com.leavemanagement.dectionary;

public enum LeaveStatus {

	PENDING("Pending"),
	APPROVED("Approved"),
	REJECTED("Rejected");
	
	private String name;
	
	public String getName() {
		return name;
	}

	LeaveStatus(String name){
		this.name=name;
	}
}
