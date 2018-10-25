package com.depromeet.clippingserver.category.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.depromeet.clippingserver.exception.CategoryNotFoundException;
import com.depromeet.clippingserver.post.domain.GetAllPostsResponse;
import com.depromeet.clippingserver.post.domain.Post;
import com.depromeet.clippingserver.user.domain.User;

@Service
@Transactional
public class CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;

	public List<CategoryDto> findValidAndOrderedCategory(Long userId) {
		return categoryRepository.findByUserIdAndDeletedFalseOrderByOrderNoAsc(userId).stream()
				.map(CategoryDto::fromEntity).collect(Collectors.toList());
	}

	public CategoryDto saveNewCategory(CategoryDto dto, Long userId) {
		Integer maxOrderNo = categoryRepository.findMaxOrderNoByUserId(userId).orElse(0);
		Category category = Category.builder().name(dto.getName()).orderNo(maxOrderNo + 1)
				.user(User.builder().id(userId).build()).build();
		category = categoryRepository.save(category);
		return CategoryDto.fromEntity(category);
	}

	public List<CategoryDto> updateOrderNo(Long userId, ArrayList<Long> categoryId) {
		List<Category> category = new ArrayList<>();
		for(int i = 0; i < categoryId.size(); i++) {
			category.add(Category.builder().id(categoryId.get(i)).orderNo(i).build() );
		}
		category.forEach(dto -> categoryRepository.updateOrderNoById(dto.getOrderNo(), dto.getId()));
		return this.findValidAndOrderedCategory(userId);
	}

	public void updateDeletedTrue(ArrayList<Long> categoryIds) {
		if(categoryIds.isEmpty()) {
			throw new CategoryNotFoundException();
		}
		categoryIds.forEach(id -> {
			categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
			categoryRepository.updateDeletedTrue(id);
		});
	}

	public GetAllPostsResponse findParticularPosts(long categoryId, Long userId, Pageable pageable) {
		categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
		Page<Post> posts = categoryRepository.findPostsByIdAndUserId(categoryId, userId, pageable);
		GetAllPostsResponse re = GetAllPostsResponse.fromEntity(posts.getContent());
		re.addPageInfo(posts);
		return re;
	}

	public CategoryDto updateName(String name, Long id) {
		categoryRepository.updateName(name, id);
		Optional<Category> category = categoryRepository.findById(id);
		return category.map(CategoryDto::fromEntity).orElseThrow(CategoryNotFoundException::new);
	}
}
