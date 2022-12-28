package com.mokkoji.domain.entity.board;

import com.mokkoji.domain.dto.BoardDto;
import com.mokkoji.domain.entity.BaseEntity;
import com.mokkoji.domain.entity.likes.LikesEntity;
import com.mokkoji.domain.entity.reply.ReplyEntity;
import com.mokkoji.domain.entity.member.MemberEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Vector;
import java.util.List;

@Entity
@Table(name = "board")
@Getter @Setter
@ToString @Builder
@NoArgsConstructor @AllArgsConstructor
public class BoardEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bno;            //PK
    @Column(nullable = false, columnDefinition = "text")
    private String bcontent;    // 내용
    @Column(nullable = false)
    private String bpassword;   // 비밀번호

    @Column(nullable = false)
    private String latitude;    // 경도
    @Column(nullable = false)
    private String longitude;   // 위도
    // 유정 조회수 추가
    @Column(nullable = false)
    private int bview;          // 조회수
    @Column(nullable = true)
    @ColumnDefault("없음")
    private String addr;

    @ManyToOne
    @JoinColumn(name="mno")
    @ToString.Exclude
    private MemberEntity memberEntity;

    @OneToMany(mappedBy = "boardEntity" , cascade = CascadeType.ALL)
    @Builder.Default
    private List<ReplyEntity> replyEntityList = new Vector<>();

    @OneToMany(mappedBy = "boardEntity" , cascade = CascadeType.ALL)
    @Builder.Default
    private List<LikesEntity> likesEntityList = new Vector<>();

    //1217 지웅 추가 좋아요-싫어요 계산 함수
    public int getRealLikes(){
        if(likesEntityList == null){
            return 0;
        }
        int countLikes = 0;
        int countDislikes = 0;

        for(int i = 0 ; i<likesEntityList.size(); i++){
            if(likesEntityList.get(i).isLikeInfo()){
                countLikes++;
            }else{
                countDislikes++;
            }
        }

        return countLikes - countDislikes;
    }

    public BoardDto toDto(){
        return BoardDto.builder().bno(this.bno).bpassword(this.bpassword).bcontent(this.bcontent)
                .latitude(this.latitude).addr(this.addr).longitude(this.longitude).bview(this.bview).build();
    }
}
