package com.depromeet.clippingserver.post.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	
	@Query("select max(c.orderNo) from Category c where c.user.id = :userId")
	public Optional<Integer> findMaxOrderNoByUserId(@Param("userId") Long userId);
}
