package com.itbcafrica.cmsecommercecar.models.data;

import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {

	Category findByName(String name);

	Category findBySlugAndIdNot(String slug, Long id);

	Category findBySlug(String slug);

	// List<Category> findAllByOrderBySortingAsc();

}
