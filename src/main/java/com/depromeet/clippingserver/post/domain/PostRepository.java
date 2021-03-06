package com.depromeet.clippingserver.post.domain;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PostRepository extends JpaRepository<Post, Long> {
	Page<Post> findByUserIdAndDeletedFalseOrderByUpdatedDateDesc(Long userId, Pageable pageable);
	
	Page<Post> findByDeletedFalseAndUserIdAndTitleContainingIgnoreCaseAndPersonalTitleContainingIgnoreCase(Long userId, String title, String personalTitle, Pageable pageable);

	Page<Post> findByDeletedFalseAndUserIdAndTitleContainingIgnoreCaseOrPersonalTitleContainingIgnoreCase(Long userId, String title, String personalTitle, Pageable pageable);

	@Query("SELECT p FROM Post p WHERE p.deleted = false and p.userId = :userId and (p.title Like %:title% or p.personalTitle Like %:personalTitle%)")
	Page<Post> searchOr(@Param("userId") Long userId,@Param("title") String title,@Param("personalTitle") String personalTitle, Pageable pageable);

	@Query("SELECT p FROM Post p WHERE p.deleted = false and p.userId = :userId and p.title Like %:title% and p.personalTitle Like %:personalTitle%")
	Page<Post> searchAnd(@Param("userId") Long userId,@Param("title") String title,@Param("personalTitle") String personalTitle,  Pageable pageable);

	Page<Post> findByUserIdAndDeletedFalseAndIsBookmarkTrueOrderByUpdatedDateDesc(Long userId, Pageable pageable);

	List<Post> findByUserIdAndPersonalTitleContainingAndDeletedFalse(Long userId, String personalTitle);

	@Query("update Post p set p.category.id = :categoryId where p.id = :postId")
	@Modifying
	@Transactional
	void updateCategoryId(@Param("postId") Long postId, @Param("categoryId") Long categoryId);
	
	@Query("update Post p set p.deleted = true where p.id = :postId")
	@Modifying
	@Transactional
	void updateDeletedTrue(@Param("postId") Long postId);
	
	@Query("update Post p set p.isBookmark = case p.isBookmark when true then false else true end where p.id = :postId")
	@Modifying
	@Transactional
	void updateBookmark(@Param("postId") Long postId);

}
