import React,{useState,useEffect} from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import FloatingLabel from 'react-bootstrap/FloatingLabel';
import ReplyToast from "./ReplyToast";
import styles from '../CSS/WriteModal.css' // 유정 추가
import {useDispatch} from "react-redux";
import InputGroup from 'react-bootstrap/InputGroup';
import axios from 'axios';

export default function WriteModal(props) {
    //로그인 확인
    const [isLogin , setIsLogin] = useState(null);
    const [addr , setAddr] = useState("없음");
    const [show, setShow] = useState(true);
    const [toast,setToast] = useState(false);
    const [msg , setMsg] = useState("");
    const dispatch = useDispatch();

    const loginHandler = () => {
        axios.get('/member/getmno').then( res => { setIsLogin(res.data);})
        const { kakao } = window;
        let geocoder = new kakao.maps.services.Geocoder();
        let coord = new kakao.maps.LatLng(props.latitude, props.longitude);
        let callback = function(result, status) {
            if (status === kakao.maps.services.Status.OK) {
              setAddr(result[0].address.address_name.split(" ")[1]+" "+result[0].address.address_name.split(" ")[2]);
            }
        }
        geocoder.coord2Address(coord.getLng(), coord.getLat(), callback);
    };
    useEffect(loginHandler,[]);

    const OpenToast = (comment) => {
        setMsg(comment);
        setToast(true);
    }
    const handleToast = (state) => {
        setToast(state);
    }
    //모달 중복가능
    const handleClose = () => {
        setShow(false);
        props.close();
    }

    const setboard = async () =>{
        if(isLogin==""){OpenToast("Please Login"); return;}

        let Dto = {
            bcontent  : document.querySelector('.form1').value ,
            bpassword : document.querySelector('.form2').value ,
            latitude :  props.latitude ,
            longitude: props.longitude ,
            addr : addr
        };
        if(Dto.bcontent==""){OpenToast("내용을 작성해주세요");    return;}
        if(Dto.bpassword==""){OpenToast("비밀번호를 입력해주세요");   return;}
        console.log(Dto);
        await axios.post('/board/setboard' , Dto)
             .then( res => { console.log("글작성 성공"+res.data); handleClose(); })
             .catch( err =>console.log("글작성 실패"+err))
        dispatch({type:"changeXY" , latitude: props.latitude, longitude: props.longitude})
        props.getlist();
    }
    return(
        <>
            <div>
                <Modal show={show} onHide={handleClose} backdrop="static">
                    <Modal.Header closeButton>
                        <Modal.Title>MoKKoji 우리동네 글 작성</Modal.Title>
                    </Modal.Header>
                    <Modal.Body className={"write_body"}>
                        <div>
                            <div>{toast?(<ReplyToast content={msg} ToastHandle={handleToast} />):(null)}</div>
                            <FloatingLabel controlId="floatingTextarea2" label="내용을 작성하세요 !">
                                    <Form.Control
                                      className="form1"
                                      as="textarea"
                                      placeholder="글을 작성해 주세요."
                                      style={{ height: '200px' , resize: 'none' }}
                                    />
                            </FloatingLabel><br/>
                            <InputGroup className="mb-3">
                                <Form.Control className="form2"
                                              type="password"
                                              placeholder="비밀번호"
                                />
                                <Button onClick={setboard} variant="outline-secondary" id="button-addon2">
                                    글 등록
                                </Button>
                            </InputGroup>
                        </div>
                    </Modal.Body>
                </Modal>
            </div>
        </>
    );
}