package com.example.studyblog.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@Getter
public class Heart extends TimeAndMember{
    @EmbeddedId
    private HeartId id;

    @Builder
    public Heart(Post post, Comment comment, Member member){
        this.id = new HeartId(post, comment);
        this.member = member;
    }

    @Embeddable
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HeartId implements Serializable {
        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "post_id", nullable = false)
        private Post post;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "comment_id", nullable = false)
        private Comment comment;
    }
}
