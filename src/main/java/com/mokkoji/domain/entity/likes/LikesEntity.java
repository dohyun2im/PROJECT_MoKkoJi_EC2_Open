package com.mokkoji.domain.entity.likes;

import com.mokkoji.domain.dto.LikesDto;
import com.mokkoji.domain.entity.BaseEntity;
import com.mokkoji.domain.entity.board.BoardEntity;
import com.mokkoji.domain.entity.member.MemberEntity;
import lombok.*;

import javax.persistence.*;

@Entity @Table(name = "likes")
@Getter @Setter
@ToString @Builder
@NoArgsConstructor @AllArgsConstructor
public class LikesEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int lno;

    @Column(nullable = false)
    private boolean likeInfo;

    @ManyToOne
    @JoinColumn(name="mno")
    @ToString.Exclude
    private MemberEntity memberEntity;

    @ManyToOne
    @JoinColumn(name="bno")
    @ToString.Exclude
    private BoardEntity boardEntity;

    public LikesDto toDto(){
        return LikesDto.builder().lno(this.lno).likeInfo(this.likeInfo).build();
    }
}
