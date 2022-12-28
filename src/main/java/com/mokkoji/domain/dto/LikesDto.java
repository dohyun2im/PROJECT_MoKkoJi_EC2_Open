package com.mokkoji.domain.dto;

import com.mokkoji.domain.entity.likes.LikesEntity;
import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class LikesDto {
    private int lno;
    private boolean likeInfo;
    private int bno;

    public LikesEntity toEntity(){
        return LikesEntity.builder().lno(this.lno).likeInfo(this.likeInfo).build();
    }
}
