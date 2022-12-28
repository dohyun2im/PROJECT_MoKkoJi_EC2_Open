package com.mokkoji.domain.dto;

import com.mokkoji.domain.entity.board.BoardEntity;
import com.mokkoji.domain.entity.reply.ReplyEntity;
import lombok.*;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ReplyDto {
    private int rno;
    private String rcontent;
    private String rpassword;
    private int bno;
    public ReplyEntity toEntity(){
        return ReplyEntity.builder().rno(this.rno).rcontent(this.rcontent).rpassword(this.rpassword).build();
    }
}
