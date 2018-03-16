package model;

import java.math.BigDecimal;

public class Room {
	public Room(){};
	
	public Room(String name,String location,BigDecimal lat,BigDecimal lng,String tags,String username){
		this.name = name;
		this.location = location;
		this.lat = lat;
		this.lng = lng;
		this.tags = tags;
		this.username = username;
	}

	private int id;
	private String name;
	private String location;
	private BigDecimal lat;
	private BigDecimal lng;
	private String tags;
	private String username;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public BigDecimal getLat() {
		return lat;
	}
	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}
	public BigDecimal getLng() {
		return lng;
	}
	public void setLng(BigDecimal lng) {
		this.lng = lng;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
}
