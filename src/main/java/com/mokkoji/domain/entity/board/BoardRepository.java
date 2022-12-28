package com.mokkoji.domain.entity.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {
    //12/26 도현 sql 수정본 => likes 테이블에서 (추천-비추천)이 많은순으로 내림차순 정렬한 후 board 테이블에 right join 후 board 테이블 추출
    @Query(value = "select b.bno,b.cdate,b.udate,b.addr,b.bcontent,b.bpassword,b.bview,b.latitude,b.longitude,b.mno from board b " +
                   "right join (select l.bno , sum(l.like_info = 1) - sum(l.like_info = 0) as likecount from likes l " +
                   "group by l.bno having likecount>=0 order by likecount desc) l  on b.bno=l.bno;", nativeQuery = true)
    List<BoardEntity> findByPopular();
    //12/27 도현 최신순 10개만 뽑아오기
    @Query( value="SELECT * FROM board order by bno desc limit 10;" ,  nativeQuery = true )
    List<BoardEntity> findByLatest();
    
    // 1216 유정 조회수 많은 순 10개 출력
    @Query( value = "select * from board order by bview desc limit 10;" , nativeQuery = true)
    List<BoardEntity> findByBviewdesc();

    // 1216유정 오늘 글만 출력 , 1218 지웅  최근 글 검색 시 위에서 개수대로 자르면 되도록 내림차순 정렬(order by bno desc) 추가
    @Query( value="SELECT * FROM board WHERE cdate BETWEEN DATE_ADD(NOW(), INTERVAL -1 DAY ) AND NOW();" ,  nativeQuery = true )
    List<BoardEntity> findBytoday();

    @Query( value="SELECT * FROM board WHERE cdate BETWEEN DATE_ADD(NOW(), INTERVAL -7 DAY ) AND NOW();" ,  nativeQuery = true )
    List<BoardEntity> findByWeek();

    // 유정 내 글만 10개 출력
    @Query( value="select * from board where mno=:mno order by bno desc limit 10" , nativeQuery = true )
    List<BoardEntity> findBymyboard( int mno );
}
