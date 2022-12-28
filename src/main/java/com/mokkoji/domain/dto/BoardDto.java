package com.mokkoji.domain.dto;

import com.mokkoji.domain.entity.board.BoardEntity;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString @Builder
public class BoardDto {
    private int bno;            // PK
    private String bcontent;    // 내용
    private String bpassword;   // 비밀번호
    private String latitude;    // 경도
    private String longitude;   // 위도
    private int mno;            // 도현 추가
    private int bview;          // 조회수 유정 추가
    private String addr;
    private int bLikes;         // 좋아요 수
    private int bDislikes;      // 싫어요 수

    public BoardEntity toEntity(){
        return BoardEntity.builder().bno(this.bno).bcontent(this.bcontent).bpassword(this.bpassword)
                .latitude(this.latitude).addr(this.addr).longitude(this.longitude).bview(this.bview).build();
    }
}
