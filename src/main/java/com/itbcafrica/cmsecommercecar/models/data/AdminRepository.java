package com.itbcafrica.cmsecommercecar.models.data;

import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<Admin, Long> {
	Admin findByUsername(String username);
}
