package com.itbcafrica.cmsecommercecar.models.data;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

/**
 * PageRepository
 */

public interface PageRepository extends CrudRepository<Page, Long> {

	Page findBySlug(String slug);

	/*
	 * @Query("SELECT p FROM  Page p WHERE p.id !=:id and p.slug =:slug") Page
	 * findBySlug(int id, String slug);
	 */
	Page findBySlugAndIdNot(String slug, Long id);

	List<Page> findAllByOrderBySortingAsc();

}