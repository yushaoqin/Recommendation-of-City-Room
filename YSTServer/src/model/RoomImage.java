package model;

public class RoomImage {
	public RoomImage(){};
	public RoomImage(int roomId,String username,String imgPath){
		this.roomId = roomId;
		this.username = username;
		this.imgPath = imgPath;
	}
	private int id;
	private int roomId;
	private String username;
	private String imgPath;
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	
}
