package com.example.studyblog.domain;


import com.example.studyblog.controller.request.PostRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)

    private String content;

    @Column(nullable = false)
    private String imgUrl;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Comment> comment;
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    public void update (PostRequestDto postRequestDto){
        this.title= postRequestDto.getTitle();
        this.content= postRequestDto.getContent();
    }

    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }


}
