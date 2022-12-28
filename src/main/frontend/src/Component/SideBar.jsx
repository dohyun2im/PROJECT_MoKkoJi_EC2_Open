import React, {useState, useEffect, useRef} from 'react';
import Button from 'react-bootstrap/Button';
import MyModal from "./MyModal";
import axios from "axios";
import styles from "./../CSS/SideBar.css"

export default function SideBar(props) {
    const [options,setOptions] = useState(1);        //도현 1:최신 ,2:추천, 3:조회수, 4:유정씨 커스텀
    const [isSide , setIsSide] = useState(true);     //도현 사이드바 열고닫기
    const [boardList , setBoardList] = useState([]); //가져온 게시물들
    const [bno,setBno] = useState(1);
    const [show, setShow] = useState(false);

    //모달 끌때 함수
    const handleChangeShow = () =>{setShow(false);};
    //모달 켤때 함수
    const handleShowModal = (bno) =>{
        //게시물 번호 변경
        setBno(bno);
        //모달 켜기
        setShow(true);
        //조회수 증가
        axios.post("/board/updateBview", {bno : bno}).then(res=>{console.log(res.data)});
    }
    //도현 사이드바 열고닫기 핸들러함수
    const handleSideBar = () =>{ setIsSide(true);}
    // 도현 최신 조회 좋아요순 게시물보기 핸들러함수
    const handleLatestOrder = () =>{setOptions(1)}
    const handleViewOrder = () =>{setOptions(2)}
    const handleLikeOrder = () =>{setOptions(3)}
    //1227 커스텀 함수(내 글 보기)
    const handlerMyContent = () =>{setOptions(4)}
    // 도현 최신 인기 조회수순 가져오기
    const getpopular = () => {
        axios.get("/board/getboardoptions", {params:{option:options}})
            .then( res => { setBoardList(res.data); console.log(res.data);} )
    }
    useEffect(getpopular , [options]);

    // 1223 지웅 수정
    // sidebar 트랜지션 적용 위해 일부 수정
    // 생명주기 컨트롤X -> props 값 통해 position 이동

    return(
        <>
            <div className={"sideBody"} style={{left:props.position}}>
                <div className={"listBody"}>
                    <div className={"listBtn"}>
                        <div className="threeWords" onClick={handleLatestOrder}>
                            <span>최신순</span>
                        </div>
                        <div className="threeWords" onClick={handleViewOrder}>
                            <span>조회순</span>
                        </div>
                        <div className="threeWords" onClick={handleLikeOrder}>
                            <span>추천순</span>
                        </div>
                        <div className="btntext" onClick={handlerMyContent}>
                            <span>내 글 목록</span>
                        </div>
                    </div>
                    <div className={"listTable"} style={{wordBreak: 'break-all'}}>
                        <table className="table table-striped popList">
                            <tr><th className='col-md-3'> 순위 </th> <th className='col-md-5'> 지역 </th><th className='col-md-4'> 내용 </th> </tr>
                            { boardList.length>0? (
                                boardList.map( ( e, i ) =>{
                                        return(<tr onClick={()=> handleShowModal(e.bno) }><th className='col-md-3'> {i+1} </th> <th className='col-md-5'>{e.addr}</th><th className='col-md-4'>{e.bcontent.substr(0, 5)}.. </th> </tr>)
                                    }
                                )
                            ):(null)
                            }
                        </table>
                    </div>
                    <div className={"closeBtnBox"}>
                        <Button onClick={ ()=>{props.onHide("-100%");} } variant="secondary" className={"closeBtn"}>
                            닫기
                        </Button>
                    </div>
                    {show && <MyModal close={handleChangeShow} show={show} bno={bno} />}
                </div>
            </div>
        </>
    )
}