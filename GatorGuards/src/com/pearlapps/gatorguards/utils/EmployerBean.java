package com.pearlapps.gatorguards.utils;

import java.io.Serializable;

public class EmployerBean implements Serializable {
	public String empName = "";
	public int empId;

	public EmployerBean() {

	}

	public EmployerBean(String empName, int empId) {
		this.empName = empName;
		this.empId = empId;
	}

	
	
}
