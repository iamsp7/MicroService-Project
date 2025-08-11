package com.application.signin.dto;



import com.fasterxml.jackson.annotation.JsonPropertyOrder;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonPropertyOrder({ "username", "email", "password", "firstName", "lastName", "role" })
public class SignupDTO {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String role;
    private String lastName;

//    // Constructors
//    public SignupDTO() {}
//
//  
//	
//
//	public SignupDTO(String username, String email, String password, String role ,String firstName, String lastName) {
//		super();
//		this.username = username;
//		this.email = email;
//		this.password = password;
//		this.firstName = firstName;
//		this.lastName = lastName;
//		this.role = role;
//		
//	}
//
//
//
//
//	public String getUsername() {
//		return username;
//	}
//
//	public void setUsername(String username) {
//		this.username = username;
//	}
//
//	public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(String email) {
//		this.email = email;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}
//	
//	public String getRole() {
//		return role;
//	}
//
//
//
//
//	public void setRole(String role) {
//		this.role = role;
//	}
//
//
//	public String getFirstName() {
//		return firstName;
//	}
//
//	public void setFirstName(String firstName) {
//		this.firstName = firstName;
//	}
//
//	public String getLastName() {
//		return lastName;
//	}
//
//	public void setLastName(String lastName) {
//		this.lastName = lastName;
//	}
//
//
//


   
}
