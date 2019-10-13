package com.itbcafrica.cmsecommercecar.models.data;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {

	Product findBySlug(String slug);

	Product findBySlugAndIdNot(String slug, Long id);

	Page<Product> findAll(Pageable pagable);

	List<Product> findAllByCategoryId(String categoryId, Pageable pagable);

	long countByCategoryId(String categoryId);

}
