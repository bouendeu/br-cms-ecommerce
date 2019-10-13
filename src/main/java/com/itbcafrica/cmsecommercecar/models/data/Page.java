package com.itbcafrica.cmsecommercecar.models.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * Page
 */

@Entity
@Table(name = "pages")
public class Page {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(min = 2, message = "Title must be at least 2 characters long")
	private String title;

	private String slug;

	@Size(min = 5, message = "Content must be at least 2 characters long")
	private String content;
	private int sorting;

	public Page() {

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getSorting() {
		return sorting;
	}

	public void setSorting(int sorting) {
		this.sorting = sorting;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}