import React,{useState,useEffect} from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import InputGroup from 'react-bootstrap/InputGroup';
import axios from 'axios';
import DeleteAlert from "./DeleteAlert";
import ReplyToast from "./ReplyToast";
import styles from '../CSS/MyModal.css'
import comments from '../img/speech_bubble.png';
import {useDispatch} from 'react-redux';

let deletepassword = '';
let ReplyAlert='';  // Toast에 들어갈 코멘트. setReply에서 세팅 후 props에서 추가
export default function MyModal(props) {
    const [board ,setBoard] = useState(null);
    const [reply ,setReply] = useState([]);
    const [show, setShow] = useState(props.show);
    const [alert, setAlert] = useState(false);
    const [viewComment, setviewComment] = useState(false);
    const [toast, setToast] = useState(false);
    const dispatch = useDispatch();

    const getBoard = () => {
        axios.get("/board/getBoard" , {params:{bno:props.bno}})
             .then(res => { setBoard(res.data);});
        axios.get("/board/getReplyList", {params:{bno:props.bno}})
             .then(res => { setReply( res.data  ) });
    }

    useEffect(getBoard , []);

    const handleClose = () => {
        setShow(false)
        props.close();
    };

    // 1221 지웅 추가 Toast 관련 함수
    const OpenToast = (comment) => {
        ReplyAlert=comment;
        setToast(true);
    }
    const ToastHandle = (state) => {
        setToast(state);
    }
    //12/22 도현 댓글 기능 추가
    const handleViewComment = ()=>{
        setviewComment(!viewComment);
    }
    const setreply = () =>{
        let Dto = {
            rcontent  : document.querySelector('.form1').value ,
            rpassword : document.querySelector('.form2').value ,
            bno :  props.bno
        };
        console.log(Dto)

        // 1221 지웅 추가
            // 내용 없을 시 toast출력 후 return
        if(Dto.rcontent==''){
            OpenToast("내용을 입력해 주세요.");
            return;
        }else if(Dto.rpassword==''){
            OpenToast("비밀번호를 입력해 주세요.");
            return;
        }

        axios.post('/board/setreply' , Dto)
             .then( res => { console.log("댓글작성 성공"+res.data); getBoard(); })
             .catch( err =>console.log("댓글작성 실패"+err))
        document.querySelector('.form1').value='';
        document.querySelector('.form2').value='';
    }

    const deletereply = (deleteno) =>{

        let Dto = {
            rno  : deleteno ,
            rpassword : deletepassword ,
            bno :  props.bno
        };
        console.log(Dto)
        axios.delete('/board/deletereply' , {params:{bno:props.bno,rpassword:deletepassword,rno:deleteno}})
             .then( res => { console.log("댓글삭제 성공"+res.data); getBoard(); })
             .catch( err =>console.log("댓글삭제 실패"+err))
    }

    const deletealert=() => {
        setAlert(!alert);
        getBoard();
    }

    const Like = (tf) => {
        console.log(tf);
        let dto = {
            bno:props.bno ,
            likeInfo:tf
        }
        axios.post('/board/setlikes' , dto )
            .then(  res => { console.log("좋아요 성공"+res.data); getBoard(); })
            .catch( err => console.log("좋아요 실패"+err))
    }

    return(
        <>
            <div>
                <Modal show={show} onHide={handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>
                            <div className={"modal_title"}>
                            조회수 :{board!=null?(board.bview):(null)}
                            <Button variant="outline-secondary" onClick={deletealert}> 삭제 </Button>
                            <Button variant="outline-secondary" onClick={ ()=>{dispatch({type:"changeXY" , latitude: board.latitude, longitude: board.longitude});} }> 이동 </Button>
                            </div>
                        </Modal.Title>
                    </Modal.Header>
                    <div>{alert?(<DeleteAlert map={props.map} close={handleClose} bno={props.bno}/>):(null)}</div>
                    <Modal.Body className={"modalbody_my"}>
                        <div>
                            <div>{toast?(<ReplyToast content={ReplyAlert} ToastHandle={ToastHandle} />):(null)}</div>
                            <div className="contentBox">
                                <div className="textBox">
                                    <strong>{board!=null?(board.bcontent):(null)}</strong>
                                </div>
                                <div className="likeBox">
                                    <button onClick={()=>{Like(true)}} className="likeBtn">  Like👍 {board!=null?(board.blikes):(null)}</button>
                                    <button onClick={()=>{Like(false)}} className="dislikeBtn"> Dislike👎 {board!=null?(board.bdislikes):(null)}</button>
                                </div>
                            </div>
                            <img src={comments} style={{width:30 , margin:20}} onClick={handleViewComment} className={"rViewBtn"} /> <span> {reply.length}개의 댓글 </span>
                            {viewComment&&
                            <>
                                <table className="table">
                                    <tr>
                                        <td className="col-md-7"><Form.Control className="form1 col-md-7" placeholder="메시지를 남겨주세요!" aria-describedby="basic-addon2"/></td>
                                        <td className="col-md-2"><Form.Control className="form2" type="password" placeholder="비밀번호" /></td>
                                        <td className="col-md-2"><Button onClick={setreply} variant="outline-secondary" id="button-addon2" className="col-md-12">등록</Button></td>
                                    </tr>
                                </table>
                                <strong>Comments</strong><br/>
                                <div id="replyList">
                                    <table className="table">
                                        {
                                            reply.length>0?
                                                (
                                                    reply.map(e=>
                                                        { return (
                                                            <tr>
                                                                <td className="col-md-7"><div className="col-md-7" type="text"/> {e.rcontent} </td>
                                                                <td className="col-md-2"><Form.Control onChange={(e)=>{deletepassword=e.target.value;}} type="password" placeholder="비밀번호" /></td>
                                                                <td className="col-md-2"><Button onClick={()=>deletereply(e.rno)} variant="outline-secondary" id="button-addon2">삭제</Button></td>
                                                            </tr>
                                                        )}
                                                    )
                                                )
                                                :(null)
                                        }
                                    </table>
                                </div>
                            </>
                            }
                        </div>
                    </Modal.Body>
                </Modal>
            </div>
        </>
    );
}