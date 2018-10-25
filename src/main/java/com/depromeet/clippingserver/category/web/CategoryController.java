package com.depromeet.clippingserver.category.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.depromeet.clippingserver.category.domain.CategoryDto;
import com.depromeet.clippingserver.category.domain.CategoryService;
import com.depromeet.clippingserver.post.domain.GetAllPostsResponse;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class CategoryController {
	@Autowired
	private CategoryService categoryService;

	@GetMapping(path = "/categories")
	public ResponseEntity<List<CategoryDto>> getCategories(@RequestHeader(value = "UserId") Long userId) {
		List<CategoryDto> re = categoryService.findValidAndOrderedCategory(userId);
		return ResponseEntity.ok().body(re);
	}
	
	@ApiImplicitParams({
    	@ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
    			value = "Results page you want to retrieve (0..N)"),
    	@ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
    	value = "Number of records per page.")
    })
	@GetMapping(path ="/categories/{categoryId}/posts")
	public ResponseEntity<GetAllPostsResponse> getPostsUsingCategoryId(@RequestHeader(value = "UserId") Long userId, 
																	    @PathVariable(name="categoryId") Long categoryId,
																	    @ApiIgnore Pageable pageable){
		GetAllPostsResponse re = categoryService.findParticularPosts(categoryId, userId, pageable);
		return ResponseEntity.ok().body(re);
	}

	@PostMapping(path = "/categories")
	public ResponseEntity<CategoryDto> createCategory(@RequestHeader(value = "UserId") Long userId, @RequestBody CategoryDto category) {
		CategoryDto re = categoryService.saveNewCategory(category, userId);
		return ResponseEntity.status(HttpStatus.CREATED).body(re);
	}

	@PutMapping(path = "/categories")
	public ResponseEntity<List<CategoryDto>> updateCategoryOrderNo(@RequestHeader(value = "UserId") Long userId,@RequestBody CategoryIdList categoryId) {
		List<CategoryDto> category = categoryService.updateOrderNo(userId, categoryId.getCategoryId());
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(category);
	}
	
	@PutMapping(path = "/categories/{categoryId}")
	public ResponseEntity<CategoryDto> updateCategoryName(@RequestBody CategoryDto category, @PathVariable(name="categoryId") Long id){
		CategoryDto re = categoryService.updateName(category.getName(), id);
		return ResponseEntity.ok().body(re);
	}

	@DeleteMapping(path = "/categories")
	public ResponseEntity<String> deleteCategory(@RequestHeader(value = "UserId") Long userId,@RequestBody CategoryIdList categoryId) {
		categoryService.updateDeletedTrue(categoryId.getCategoryId());
		return ResponseEntity.status(HttpStatus.OK).body("delete success!");
	}
}
