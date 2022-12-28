import React,{useState, useEffect, useRef} from 'react';
import {Map, MapMarker, MarkerClusterer, useMap} from "react-kakao-maps-sdk";
import {useSelector , useDispatch} from 'react-redux';
import axios from "axios";
import MyModal from "./MyModal";
import WriteModal from "./WriteModal";
import 'bootstrap/dist/css/bootstrap.min.css';
import speech_bubble from "../img/speech_bubble2.png";
import logo from './../img/teamlogo.png' // 유정 추가
import styles from '../CSS/BackGround.css' // 유정 추가
import hot from '../img/hot.png'

let Clat = 37.3084312076492;
let Clng = 126.85083057973264;
let lastLevel = 3;
let clusters = null;

var options = {
    enableHighAccuracy: true,
    timeout: 5000,
    maximumAge: 0
};

function success(pos) {
    var crd = pos.coords;
    Clat = crd.latitude;
    Clng = crd.longitude;
}
navigator.geolocation.getCurrentPosition(success, error, options);

function error(err) {
    console.warn(`ERROR(${err.code}): ${err.message}`);
}

export default function Kakaomap(props) {
    const dispatch = useDispatch();
    async function getCurrentPosition(){
        await navigator.geolocation.getCurrentPosition(success, error, options);
    }
    useEffect( ()=>{
        getCurrentPosition().then(()=>{dispatch({type:"changeXY" , latitude: Clat, longitude: Clng});});
    }, [] );

    Clat = useSelector((state) => state.latitude );
    Clng = useSelector((state) => state.longitude );

    const [ mapdata, setPosition ] = useState([]);

    useEffect( ()=>{getlist();} , []);

    const map = useRef(null);

    async function getlist(){
        const resp = await axios.get("/board/getlist");
        const data = resp.data;

        let newPositions = [];

        data.forEach( d => {
            let el = {
                content : d.bcontent,
                latlng : { lat : Number(d.latitude), lng : Number(d.longitude) },
                bno : d.bno
            }
            newPositions.push(el);
            setPosition(newPositions);
        } )
    }

    const updateBview = (bno) => {
        axios.post("/board/updateBview", {bno : bno}).then(res=>{console.log(res.data)});
    }

    const EventMarkerContainer = ({ position, content, bno }) => {
        const [isVisible, setIsVisible] = useState(false);
        //12/14 도현 보기 모달이 꺼지면 마커도 꺼질수있도록 합니다.
        const changeIsVisible = () =>{
            setIsVisible(false);
        };

        return (
            <div>
                <MapMarker
                    position={position} // 마커를 표시할 위치
                    image={
                        {
                            src : speech_bubble,
                            size: { width: 50, height: 50}
                        }
                    }

                    onMouseOver={(marker)=>{

                    }}

                    // @ts-ignore
                    onClick={(marker) => {
                        updateBview(bno);
                        setIsVisible(!isVisible);
                    }
                }
            >
            </MapMarker>
            {isVisible && <MyModal close={changeIsVisible} show={isVisible} map={getlist} bno={bno}/>}
            </div>
        )
        {setIsVisible(!isVisible)}
    }

    const Main = () => {
        const [position, setMakers] = useState()
        //12/14 도현 writemodal에게 전해줄 함수
        const changePosition = () =>{
            //12/14 도현 마커는 사라지세요
            setMakers(null);
            //12/14 도현 썼든 안썼든 렌더링을 하세요 !
            // getlist();
        };
        return (
            <>
                <img src={logo} className="side_logo" />
                <Map className="map" // 지도를 표시할 Container
                    ref={map}
                    center={{
                        // 지도의 중심좌표
                        lat: Clat,
                        lng: Clng,
                    }}
                    style={{
                        // 지도의 크기
                        width: "100%",
                        height: "100%",
                    }}
                    level={lastLevel} // 지도의 확대 레벨
                    maxLevel={12}
                    onClick={(_t, mouseEvent) => {
                        Clat = mouseEvent.latLng.getLat();
                        Clng = mouseEvent.latLng.getLng();
                        setMakers({
                            lat: Clat,
                            lng: Clng,
                        })
                    }}
                     onZoomChanged={(t)=>{
                         lastLevel = t.getLevel();
                     }}
                >
                    <MarkerClusterer
                        averageCenter={true} // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정
                        minLevel={4} // 클러스터 할 최소 지도 레벨
                        onCreate={(_t) => {
                            clusters = _t;
                        }}
                        calculator={[10,20,30]}
                        texts={ (size)=>{
                            if(size<10){
                                return size;
                            }else if(size<20){
                                return size;
                            }else {
                                return "";
                            }
                        }}

                        styles={[
                            {
                                background : "url('/static/media/cluster1.png') no-repeat",
                                height : "50px",
                                width : "50px",
                                backgroundSize : "contain",
                                textAlign : "center",
                                fontSize : "25px",
                                fontWeight : "bold",
                                paddingTop : "12px",
                            },
                            {
                                background : "url('/static/media/cluster2.png') no-repeat",
                                width : "60px",
                                height : "60px",
                                backgroundSize : "contain",
                                textAlign : "center",
                                fontSize : "25px",
                                fontWeight : "bold",
                                paddingTop : "15px",
                            },
                            {
                                background : "url('/static/media/cluster3.png') no-repeat",
                                height : "80px",
                                width : "80px",
                                backgroundSize : "contain",
                                textAlign : "center",
                                fontSize : "25px",
                                fontWeight : "bold",
                                paddingTop : "15px",
                            },
                            ]}
                    >
                    {mapdata.map((value) => (
                        <EventMarkerContainer
                            key={`EventMarkerContainer-${value.latlng.lat}-${value.latlng.lng}`}
                            position={value.latlng}
                            content={value.content}
                            bno={value.bno}
                        />
                    ))}
                    {position && <MapMarker position={position} />}
                    </MarkerClusterer>
                </Map>
                {position && <WriteModal latitude={position.lat} longitude={position.lng} close={changePosition} getlist={getlist}/>}
            </>
        )
    }
    return(<Main/>)
}