package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Category {
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
	 private String cname;

	 @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
	 private List<Product> products = new ArrayList<>();

	public Category(Long cid, String cname) {
		super();
		this.id = cid;
		this.cname = cname;
	}

	

	



}
