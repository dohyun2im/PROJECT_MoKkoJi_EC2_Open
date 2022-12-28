package com.mokkoji.domain.entity.member;

import com.mokkoji.domain.dto.MemberDto;
import com.mokkoji.domain.entity.*;
import com.mokkoji.domain.entity.board.BoardEntity;
import com.mokkoji.domain.entity.likes.LikesEntity;
import com.mokkoji.domain.entity.reply.ReplyEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Vector;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "member")
@Getter @Setter
@ToString @Builder
@NoArgsConstructor @AllArgsConstructor
public class MemberEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mno;

    @Column(nullable = false)
    private String memail;

    @Column(nullable = false)
    private String mdomain;

    @OneToMany(mappedBy = "memberEntity")
    @Builder.Default
    private List<BoardEntity> boardEntityList = new Vector<>();

    @OneToMany(mappedBy = "memberEntity")
    @Builder.Default
    private List<ReplyEntity> replyEntityList = new Vector<>();

    @OneToMany(mappedBy = "memberEntity")
    @Builder.Default
    private List<LikesEntity> likesEntityList = new Vector<>();

    public MemberDto toDto(){
        return MemberDto.builder().memail(this.memail).mdomain(this.mdomain).mno(this.mno).build();
    }

}
