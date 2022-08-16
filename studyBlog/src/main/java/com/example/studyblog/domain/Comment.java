package com.example.studyblog.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends TimeAndMember{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    private String content;

    @Builder
    public Comment(Long id, String content, Post post, Member member){
        this.id = id;
        this.content = content;
        this.post = post;
        this.member = member;
    }

    public void update(String content){
        this.content = content;
    }
}
