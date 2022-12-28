package com.mokkoji.controller.board;

import com.mokkoji.domain.dto.BoardDto;
import com.mokkoji.domain.dto.LikesDto;
import com.mokkoji.domain.dto.ReplyDto;
import com.mokkoji.service.board.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/getlist")
    public List<BoardDto> getList() {
        return boardService.getList();
    }

    @GetMapping("/getBoard")
    public BoardDto getBoard(@PathParam("bno") int bno) {
        return boardService.getBoard(bno);
    }

    @DeleteMapping("/deleteboard")
    public boolean deleteboard(@RequestParam int bno, @RequestParam String bpassword) {
        return boardService.deleteboard(bno, bpassword);
    }

    @GetMapping("/getReplyList")
    public List<ReplyDto> getReplyList(@PathParam("bno") int bno) {
        return boardService.getReplyList(bno);
    }

    @PostMapping("/setboard")
    public boolean setboard(@RequestBody BoardDto boardDto) {
        return boardService.setboard(boardDto);
    }

    @PostMapping("/setreply")
    public boolean setreply(@RequestBody ReplyDto replyDto) {
        return boardService.setreply(replyDto);
    }

    // 유정 [12/13] 좋아요
    @PostMapping("/setlikes")
    public boolean setlikes(@RequestBody LikesDto likesDto) {
        return boardService.setlikes(likesDto);
    }

    @DeleteMapping("/deletereply")
    public boolean deletereply(@RequestParam int bno, @RequestParam int rno, @RequestParam String rpassword) {
        return boardService.deletereply(bno, rno, rpassword);
    }

    // 유정 [12/15] 조회수순 출력
    @GetMapping("/getbviewdesc")
    public List<BoardDto> bviewdesc() {
        return boardService.getBviewdesc();
    }

    // 지웅 [12/17]
    // 추천순 리턴 [ORM]
    @GetMapping("/getORMPopular")
    public List<BoardDto> getORMPopular() {
        return boardService.getORMPopular();
    }

    // 지웅 12/18 추가
    // 조회수 +1
    // RequestParam으로 int bno 받으니까 오류나서 body로 변경
    @PostMapping("/updateBview")
    public boolean updateBview(@RequestBody BoardDto boardDto) {
        return boardService.updateBview(boardDto.getBno());
    }

    // 유정 12/19 추가
    @GetMapping("/getboardoptions")
    public List<BoardDto> getboardoptions(@RequestParam int option) {
        if (option == 1) {
            return boardService.orderByCurrent();
        } else if (option == 2) {
            return boardService.getBviewdesc();
        } else if (option == 3) {
            return boardService.getORMPopular();
        }else if(option==4){
            return boardService.myboard();
        }
        return null;
    }

    // 유정 범위내 글 구하기
    @GetMapping("/gettimeboard")
    public List<BoardDto> gettimeboard() {
        return boardService.gettimeboard();
    }

    @GetMapping("/myboard")
    public List<BoardDto> myboard(){ return boardService.myboard(); }
}