package com.leavemanagement.entity;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.search.annotations.Field;
import org.springframework.stereotype.Indexed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Indexed
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="configuration", uniqueConstraints = @UniqueConstraint(columnNames = {"key","type"}))
public class Configuration {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Field
	@Column(name="\"key\"")
	private String key;
	
	@Field
	@Column(name="\"value\"")
	private String value;
	
	@Column(name="\"type\"")
	private String type;
	
	public Configuration to() {
		return to(new Configuration());
	}
	
	public Configuration to(Configuration entiy) {
		entiy.setId(id);
		entiy.setKey(key);
		entiy.setValue(value);
		entiy.setType(type);
		return entiy;
	}
}
