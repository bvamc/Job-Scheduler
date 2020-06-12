package com.vamc;
/**
 * Java Bean class for Building
 * @author Vamsidhar Bada
 *
 */
public class Building {
	private int buildingNumber;
	private int executedTime;
	private int totalTime;

	public Building() {
		//Default Constructor
	}

	public Building(int buildingNum, int executedTime, int totalTime) {
		this.buildingNumber = buildingNum;
		this.executedTime = executedTime;
		this.totalTime = totalTime;
	}

	public int getBuildingNumber() {
		return buildingNumber;
	}

	public void setBuildingNumber(int buildingNum) {
		this.buildingNumber = buildingNum;
	}

	public int getExecutedTime() {
		return executedTime;
	}

	public void setExecutedTime(int executedTime) {
		this.executedTime = executedTime;
	}

	public int getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}
}
