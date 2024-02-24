package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryDTO {
	
	private Long id;
    private String cname;
	public CategoryDTO(Long id, String cname) {
		super();
		this.id = id;
		this.cname = cname;
	}
    
    
	
    
	
    
    

}
