package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Entity
@Data
@Table(name = "admins")
public class AdminSignup {
	@Id
	@NotBlank
	@Email
	@Column(name = "email", nullable = false)
	private String email;
	
	@NotBlank
	@Column(name = "last_name", nullable = false)
	private String lastName;
	
	@NotBlank
	@Column(name = "first_name", nullable = false)
	private String firstName;

	@NotBlank
	@Size(min = 4, message = "パスワードは4文字以上で入力してください")
	@Column(name = "password", nullable = false)
	private String password;
}