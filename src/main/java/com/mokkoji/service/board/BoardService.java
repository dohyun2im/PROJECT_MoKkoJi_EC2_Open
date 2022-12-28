package com.mokkoji.service.board;

import com.mokkoji.domain.dto.BoardDto;
import com.mokkoji.domain.dto.LikesDto;
import com.mokkoji.domain.dto.ReplyDto;
import com.mokkoji.domain.entity.board.BoardEntity;
import com.mokkoji.domain.entity.board.BoardRepository;
import com.mokkoji.domain.entity.likes.LikesEntity;
import com.mokkoji.domain.entity.likes.LikesRepository;
import com.mokkoji.domain.entity.member.MemberEntity;
import com.mokkoji.domain.entity.member.MemberRepository;
import com.mokkoji.domain.entity.reply.ReplyEntity;
import com.mokkoji.domain.entity.reply.ReplyRepository;
import com.mokkoji.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class BoardService{

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;
    // 유정 추가
    @Autowired
    private LikesRepository likesRepository;


    // 글쓰기
    @Transactional
    public boolean setboard( BoardDto boardDto ) {
        Optional<MemberEntity> optional = memberRepository.findByMemail(memberService.getloginMemail());
        MemberEntity memberEntity = null;
        BoardEntity boardEntity = null;
        if(optional.isPresent()) {
            memberEntity = optional.get();
            boardDto.setMno(memberEntity.getMno());
            try{
                boardEntity = boardRepository.save(boardDto.toEntity());
                if(boardEntity.getBno() != 0) {
                    boardEntity.setMemberEntity(memberEntity);
                    memberEntity.getBoardEntityList().add(boardEntity);
                    return true;
                }
            }catch (Exception e){
                System.out.println("글 등록 오류" + e);
            }
        }
        return false;
    }

    public List<BoardDto> getList(){
        List<BoardDto> dtolist = new ArrayList<>();
        List<BoardEntity> entityList = boardRepository.findAll();

        if(entityList==null){return null;}

        for(BoardEntity entity : entityList){
            dtolist.add(entity.toDto());
        }
        return dtolist;
    }

    // 12.18 지웅 일부 수정
        // 조회수 변경 코드 삭제 -> 함수 별도 할당
    @Transactional
    public BoardDto getBoard(int bno){
        Optional<BoardEntity> optional = boardRepository.findById(bno); // 글 레코드 가져오고 - 이때 다 가져오는건 아닌가?? 따로 좋아요 싫어요를 추가해야하나?
        if(!optional.isPresent()){ return null; }                       // 암것도 없으면 안되고

        BoardDto boardDto = optional.get().toDto();
        int arr[] = likesCount( bno );
        boardDto.setBLikes(arr[0]);
        boardDto.setBDislikes(arr[1]);
        return boardDto;                                                // 그걸 그대로 리턴? 하면 끝인가?
    }

    @Transactional
    public boolean deleteboard(int bno , String bpassword ) {
        //12/14도현 멤버엔티티,보드엔티티 생성
        Optional<MemberEntity> optional1 = memberRepository.findByMemail(memberService.getloginMemail());
        Optional<BoardEntity> optional2 = boardRepository.findById(bno);
        //엔티티 null 확인
        if(optional1.isPresent() && optional2.isPresent()){
            MemberEntity memberEntity = optional1.get();
            BoardEntity boardEntity = optional2.get();
            //모든 엔티티 확인
            if(boardEntity.getBno()>0 && memberEntity.getMno()>0){
                if(boardEntity.getBpassword().equals(bpassword) && boardEntity.getMemberEntity()==memberEntity) {
                    try{
                        boardRepository.delete(boardEntity);
                    }catch (Exception e){
                        System.out.println("게시물 삭제 오류" + e);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /*
        20221215 지웅/유정
        게시물 열람 시 좋아요 숫자 계산 후 return
        getBoard에서 호출

        @Param bno
             게시물 번호
        @return int[2] arr
             arr[0] = 좋아요 수 / arr[1] = 싫어요 수
    */
    public int[] likesCount( int bno ){
        Optional<BoardEntity> optional = boardRepository.findById(bno);
        if( !optional.isPresent() ){ return null; }
        BoardEntity boardEntity = optional.get();

        List<LikesEntity> list = boardEntity.getLikesEntityList();

        int countLikes = 0;
        int countDislikes = 0;

        for(LikesEntity entity : list){
            if(entity.isLikeInfo()){
                countLikes++;
            }else{
                countDislikes++;
            }
        }
        int[] arr = new int[2];
        arr[0] = countLikes;
        arr[1] = countDislikes;

        return arr;
    }

    public List<ReplyDto> getReplyList(int bno){
        List<ReplyDto> dtoList = new ArrayList<>();

        Optional<BoardEntity> optional = boardRepository.findById(bno);
        if(!optional.isPresent()){ return null; }

        List<ReplyEntity> replyEntityList = optional.get().getReplyEntityList();

        for(ReplyEntity entity : replyEntityList){
            dtoList.add(entity.toDto());
        }
        return dtoList;
    }

    // 댓글쓰기
    @Transactional
    public boolean setreply(ReplyDto replyDto) {
        //12/14도현 멤버엔티티,보드엔티티,댓글엔티티 생성
        Optional<MemberEntity> optional1 = memberRepository.findByMemail(memberService.getloginMemail());
        Optional<BoardEntity> optional2 = boardRepository.findById(replyDto.getBno());

        //모든엔티티 null 확인
        if(optional1.isPresent() && optional2.isPresent()){
            MemberEntity memberEntity = optional1.get();
            BoardEntity boardEntity = optional2.get();
            //모든 엔티티 확인
            if(memberEntity.getMno()>0 && boardEntity.getBno()>0){
                try{
                    ReplyEntity replyEntity = replyRepository.save(replyDto.toEntity());
                    //교차 저장
                    replyEntity.setMemberEntity(memberEntity);
                    memberEntity.getReplyEntityList().add(replyEntity);
                    replyEntity.setBoardEntity(boardEntity);
                    boardEntity.getReplyEntityList().add(replyEntity);
                    return true;
                }catch (Exception e){
                    System.out.println("리플 등록 오류" + e);
                }
            }
        }
        return false;
    }
    // 유정 12/13 좋아요
    @Transactional
    public boolean setlikes(LikesDto likesDto){
        String memail = memberService.getloginMemail();
        int mno = memberService.getmno(memail);
        int bno = likesDto.getBno();
        boolean trueOrFalse = likesDto.isLikeInfo();
        Optional<LikesEntity> optional = likesRepository.findByRecords(bno, mno);

        //게시물번호 회원로그인 확인
        if(bno <= 0 && memail==null){return false;}

        //레코드를 찾을 수 없을 때 , 게시물 번호가 정상적이며 로그인이 되어 있을 때
        if(!optional.isPresent()){
            //엔티티 저장
            try{
                LikesEntity likesEntity= likesRepository.save(likesDto.toEntity());
                Optional<MemberEntity>Moptional= memberRepository.findById( mno );
                Optional<BoardEntity> Boptional= boardRepository.findById( bno );
                // null 확인
                if(Boptional.isPresent() && Moptional.isPresent()){
                    MemberEntity memberEntity = Moptional.get();
                    BoardEntity boardEntity   = Boptional.get();
                    //교차저장
                    memberEntity.getLikesEntityList().add(likesEntity);
                    boardEntity.getLikesEntityList().add(likesEntity);
                    likesEntity.setMemberEntity(memberEntity);
                    likesEntity.setBoardEntity(boardEntity);
                    return true;
                }
            }catch(Exception e){
                System.out.println("좋아요 set 오류" + e);
            }
        }
        //있던 레코드
        else{
            LikesEntity entity= optional.get();
            try{
                if(entity.isLikeInfo() == trueOrFalse){likesRepository.delete(entity);}
                else{entity.setLikeInfo(!entity.isLikeInfo());}
                return true;
            }catch (Exception e){
                System.out.println("좋아요 상태 변경 오류" + e);
            }
        }
        return false;
    }

    @Transactional
    public boolean deletereply(int bno ,int rno , String rpassword ) {
        //12/14도현 멤버엔티티,보드엔티티,댓글엔티티 생성
        Optional<MemberEntity> optional1 = memberRepository.findByMemail(memberService.getloginMemail());
        Optional<BoardEntity> optional2 = boardRepository.findById(bno);
        Optional<ReplyEntity> optional3 = replyRepository.findById(rno);
        //엔티티 null 확인
        if(optional1.isPresent() && optional2.isPresent() && optional3.isPresent()){
            MemberEntity memberEntity = optional1.get();
            BoardEntity boardEntity = optional2.get();
            ReplyEntity replyEntity = optional3.get();
            //모든 엔티티 확인
            if(replyEntity.getRno()>0 && boardEntity.getBno()>0 && memberEntity.getMno()>0){
                if(replyEntity.getRpassword().equals(rpassword)) {
                    try{
                        replyRepository.delete(replyEntity);
                    }catch (Exception e){
                        System.out.println("댓글 삭제 오류"+e);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    // 유정 조회수많은 순으로 출력
    public List<BoardDto> getBviewdesc(){
        List<BoardDto> dtoList = new ArrayList<>();                                      // dto 담는 리스트
        List<BoardEntity> entityList = new ArrayList<>();                                //entity 담는 리스트
        entityList = boardRepository.findByBviewdesc();                                  // 조회수순으로 10개 담아주고
        for(int i = 0 ; i<entityList.size(); i++){
            dtoList.add(entityList.get(i).toDto());
        }
        return dtoList;                                                                  // dto형태로 운반해야 하니까 리턴해줌
    }
    // 2022 12 17 지웅 게시물 관리 ORM 버전 함수 작성
    public List<BoardDto> getORMPopular(){
        List<BoardDto> list = new ArrayList<>();
        List<BoardEntity> entityList = boardRepository.findByPopular();
        // 최대 10개 리턴
        int size = 0;
        if(entityList.size()<10){size = entityList.size();}
        else{size = 10;}

        for(int i = 0; i<size; i++){
            list.add(entityList.get(i).toDto());
        }
        return list;
    }

    // 1218 지웅 조회수 변경 함수 추가
        // getboard시 bview++ 할 경우 좋아요/싫어요 댓글등록 등 렌더링 될 때마다 조회수 올라감
        // 게시물 클릭 시에만 오르도록 함수 별도 할당 및 KakaoMap 마커 onclick에 코드 추가
    @Transactional
    public boolean updateBview(int bno){
        Optional<BoardEntity> optional = boardRepository.findById(bno);
        if(optional.isPresent()){
            BoardEntity entity = optional.get();
            try{
                entity.setBview(entity.getBview()+1);
            }catch (Exception e){
                System.out.println("조회수 업데이트 오류"+e);
            }
            return true;
        }
        return false;
    }

    // 유정 범위내 글 구하기(하루)
    public List<BoardDto> gettimeboard(){
        List<BoardDto> dtolist = new ArrayList<BoardDto>();
        List<BoardEntity> entitylist = boardRepository.findBytoday();

        if(entitylist==null){return null;}

        for(BoardEntity entity : entitylist){
            dtolist.add(entity.toDto());
        }
        return dtolist;
    }

    // 12/27 도현 최신순으로 10개 가져오기 sql문 작성
    public List<BoardDto> orderByCurrent(){
        List<BoardDto> dtolist = new ArrayList<>();
        List<BoardEntity> entityList = boardRepository.findByLatest();
        int size = 0;
        if(entityList.size()<10){size = entityList.size();}
        else{size = 10;}

        for(int i = 0 ; i<size; i++){
            dtolist.add(entityList.get(i).toDto());
        }
        return dtolist;
    }


    // 유정~~ 내 최신글 10개 보기
    public List<BoardDto> myboard(){
        String memail = memberService.getloginMemail();
        int mno = memberService.getmno(memail);

        List<BoardDto> dtoList = new ArrayList<>();
        List<BoardEntity> entityList = boardRepository.findBymyboard( mno );
        int size = 0;
        if(entityList.size()<10){size = entityList.size();}
        else{size = 10;}

        for(int i = 0 ; i<size; i++){
            dtoList.add(entityList.get(i).toDto());
        }
        return dtoList;
    }
}