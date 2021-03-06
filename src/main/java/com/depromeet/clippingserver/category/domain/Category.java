package com.depromeet.clippingserver.category.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.depromeet.clippingserver.post.domain.Post;
import com.depromeet.clippingserver.user.domain.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter @NoArgsConstructor
@Builder @AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false)
    private String name;
    
    private int orderNo;
    
    @Builder.Default
    private boolean deleted = false;
    
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    
    @OneToMany(mappedBy = "category", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @Builder.Default
    private List<Post> posts = new ArrayList<>();

	public void setOrderNo(int i) {
		this.orderNo = i;
	}

	public void addPost(Post post) {
		if(!this.posts.contains(post)){
			this.posts.add(post);
		}
		post.addCategory(this);
	}
}
