package com.example.entity;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userid;
	
    @Column(unique = true)
    private String username;

    @Column(nullable = false)
    private String password;
   
    private String email;
    private LocalDateTime createdate;
    private LocalDateTime lastlogin;
    
	public User(Integer userid, String username, String password, String email, LocalDateTime createdate,
			LocalDateTime lastlogin) {
		super();
		this.userid = userid;
		this.username = username;
		this.password = password;
		this.email = email;
		this.createdate = createdate;
		this.lastlogin = lastlogin;
	}

	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public User(Integer userid) {
		super();
		this.userid = userid;
	}

	public User(String username) {
		super();
		this.username = username;
	}
	
	public User() {	}

	@Override
	public int hashCode() {
		return Objects.hash(email, username);
	}

	public Integer getId() {
		return userid;
	}

	public void setId(Integer userid) {
		this.userid = userid;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getCreatedate() {
		return createdate;
	}

	public void setCreatedate(LocalDateTime createdate) {
		this.createdate = createdate;
	}

	public LocalDateTime getLastlogin() {
		return lastlogin;
	}

	public void setLastlogin(LocalDateTime lastlogin) {
		this.lastlogin = lastlogin;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(email, other.email) && Objects.equals(username, other.username);
	}
   
}
