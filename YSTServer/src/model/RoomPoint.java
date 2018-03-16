package model;

public class RoomPoint {
	public RoomPoint(){}
	public RoomPoint(int userId,int roomId,double value){
		this.userId = userId;
		this.roomId = roomId;
		this.value = value;
	}
	private int id;
	private int roomId;
	private int userId;
	private double value;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
}
