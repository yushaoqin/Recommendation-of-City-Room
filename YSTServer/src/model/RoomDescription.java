package model;

public class RoomDescription {
	public RoomDescription(){}
	public RoomDescription(String username,String description,int roomId){
		this.username = username;
		this.description = description;
		this.roomId = roomId;
	}
	
	private int id;
	private String username;
	private String description;
	private int roomId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	
}
