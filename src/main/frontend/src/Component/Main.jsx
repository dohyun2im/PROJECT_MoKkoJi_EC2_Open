import React,{useState,useEffect} from 'react';
import SideBar from './SideBar'
import Kakaomap from './Kakaomap'
import background from '../img/main.jpg'
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';
import BackGround from "./BackGround";
import logo from '../img/teamlogo.png'
import styles from '../CSS/Main.css'
import Button from "react-bootstrap/Button";
import Footer from "./Footer";

export default function Main(props) {
    const [isLogin , setIsLogin] = useState(null);
    // const [isSidebar, setIsSidebar] = useState("-100%");

    const loginHandler = () => {
        axios.get('/member/getLoginInfo').then( res => { setIsLogin(res.data);})
    };

    const sidebarHandler = ( value ) =>{
        const sidebody = document.querySelector('.sideBody');
        sidebody.style.left = value;
    }

    useEffect(loginHandler,[]);

    return(
        <>
            <Footer />
            {
                isLogin==''?
                    (
                        <>
                            <BackGround />
                        </>
                    )
                    :
                    (
                        <div>
                            <div className={"mainbox"}>
                                <div>
                                    <button onClick={ ()=>{ sidebarHandler(0); } } className={"sidebtn"} id={"sidebtn"} > 인기 게시물 </button>
                                </div>
                                <div className="sidebar">  <SideBar onHide={sidebarHandler} /></div>
                                <div className="mapbox">  <Kakaomap/>  </div>
                            </div>
                        </div>
                    )
            }
        </>
    );
}