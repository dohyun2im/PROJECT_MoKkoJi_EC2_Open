package com.mokkoji.domain.entity.reply;

import com.mokkoji.domain.dto.ReplyDto;
import com.mokkoji.domain.entity.BaseEntity;
import com.mokkoji.domain.entity.board.BoardEntity;
import com.mokkoji.domain.entity.member.MemberEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "reply")
@Getter @Setter
@ToString @Builder
@NoArgsConstructor @AllArgsConstructor
public class ReplyEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rno;
    private String rcontent;
    private String rpassword;

    @ManyToOne
    @JoinColumn(name="mno")
    @ToString.Exclude
    private MemberEntity memberEntity;

    @ManyToOne
    @JoinColumn(name="bno")
    @ToString.Exclude
    private BoardEntity boardEntity;

    public ReplyDto toDto(){
        return ReplyDto.builder().rno(this.rno).rcontent(this.rcontent).rpassword(this.rpassword).build();
    }
}
