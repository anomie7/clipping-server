package com.depromeet.clippingserver.post.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Builder@Getter
public class GetAllPostsResponse {
    private List<PostDto> posts;

    public static GetAllPostsResponse fromEntity(List<Post> posts) {
        return GetAllPostsResponse.builder()
                .posts(posts.stream()
                        .map(PostDto::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
