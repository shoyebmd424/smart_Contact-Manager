package com.springboot.contact.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name="USER")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
				+ ", enabled=" + enabled + ", imageulr=" + imageulr + ", about=" + about + ", contact=" + contact + "]";
	}
	@NotBlank(message="name field is requeired")
	@Size(min=2,max=10,message="min 2 and max 10 character")
	private String name;
	@NotBlank(message=" field is requeired")
	@Column(unique = true)
	private String email;
	@NotBlank(message=" field is requeired")
	private String password;
	private String role;
	private boolean enabled;
	private String imageulr;
	@NotBlank(message=" field is requeired")
	@Column(length = 500)
	private String about;
	@OneToMany(cascade=CascadeType.ALL,orphanRemoval = true, fetch=FetchType.LAZY,mappedBy = "user" )
	private List<Contact> contact =new ArrayList<>();
	
	public List<Contact> getContact() {
		return contact;
	}
	public void setContact(List<Contact> contact) {
		this.contact = contact;
	}
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getImageulr() {
		return imageulr;
	}
	public void setImageulr(String imageulr) {
		this.imageulr = imageulr;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
}
