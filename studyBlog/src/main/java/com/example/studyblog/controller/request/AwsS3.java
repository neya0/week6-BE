package com.example.studyblog.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AwsS3 {
    private String key;
    private String imgUrl;

    public AwsS3() {

    }

    @Builder
    public AwsS3(String key, String imgUrl) {
        this.key = key;
        this.imgUrl = imgUrl;
    }
}
