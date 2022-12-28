import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import Kakaomap from './Component/Kakaomap';
import Main from "./Component/Main";
import {createStore} from 'redux';
import {Provider} from 'react-redux';

function reducer(currentState , action){
    if(currentState===undefined){
        return {latitude:37.3084312076492 , longitude: 126.85083057973264,};
    };
    const newState = {...currentState};

    if(action.type==="changeXY"){
        newState.latitude=action.latitude;
        newState.longitude=action.longitude;
    }
    return newState;
}

const store = createStore(reducer);

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <Provider store={store}>
        <Main />
    </Provider>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
