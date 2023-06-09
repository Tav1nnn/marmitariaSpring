package com.victortavin.marmitaria.dtos.menu;

import java.io.Serializable;
import java.util.Objects;

import com.victortavin.marmitaria.entities.MenuEntity;

public class MenuDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String name;

	private float price;
	
	private float discount;
	
	private boolean active;

	private String description;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MenuDto() {
		super();
	}
	
	public MenuDto(MenuEntity menuEntity) {
		id = menuEntity.getId();
		name = menuEntity.getName();
		price = menuEntity.getPrice();
		discount = menuEntity.getDiscount();
		active = menuEntity.isActive();
		description = menuEntity.getDescription();
	}

	@Override
	public int hashCode() {
		return Objects.hash(active, description, discount, id, name, price);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MenuDto other = (MenuDto) obj;
		return active == other.active && Objects.equals(description, other.description)
				&& Float.floatToIntBits(discount) == Float.floatToIntBits(other.discount)
				&& Objects.equals(id, other.id) && Objects.equals(name, other.name)
				&& Float.floatToIntBits(price) == Float.floatToIntBits(other.price);
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}

}
