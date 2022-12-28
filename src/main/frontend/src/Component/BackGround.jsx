import React from 'react';
import back from './../img/main_back.jpg';
import main from './../img/main_img.png';
import cloud from './../img/main_cloud.png';
import sun from './../img/main_sun.png';
import logo from './../img/teamlogo.png';
import login from './../img/loginbtn1.png';
import styles from '../CSS/BackGround.css';
import test from '../img/test.png';

export default function BackGround(){
    return (
        <>
            <div className="backbox">
                <img className="back" src={back} />
                <img className="main" src={main} />
                <img className="cloud" src={cloud} />
                <img className="sun" src={sun} />
                <div className="back_eff"></div>
                <table className="logobox">
                     <tr> <th> <img className="logo" src={logo}/> </th> </tr>
                    <tr><td><a href="/oauth2/authorization/kakao">
                    <img className="login_logo" src={login} />
                    </a></td></tr>
                </table>
            </div>
        </>
    );
}