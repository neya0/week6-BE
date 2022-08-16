package com.example.studyblog.domain;


import com.example.studyblog.controller.request.PostRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    public void update (PostRequestDto postRequestDto){
        this.title= postRequestDto.getTitle();
        this.content= postRequestDto.getContent();
    }

//    public boolean validateMember(Member member) {
//        return !this.member.equals(member);
//    }


}
