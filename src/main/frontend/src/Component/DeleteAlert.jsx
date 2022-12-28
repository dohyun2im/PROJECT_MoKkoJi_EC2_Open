import React, { useState } from 'react';
import Alert from 'react-bootstrap/Alert';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import axios from 'axios';

let deleteboard = '';
export default function DeleteAlert(props) {
    console.log(props.bno);
    const deleteClick = () =>{
        console.log(deleteboard);
        axios.delete("/board/deleteboard" , {params:{bno:props.bno , bpassword:deleteboard}})
            .then(res => {
                if(res.data == true){
                    props.close();
                    props.map();
                }else{
                    alert('실패')
                }
            });
    }
    return (
        <>
            <Alert variant="danger" >
                <div className="col">
                    <Form.Control onChange={(e)=>{deleteboard=e.target.value;}} type="password" placeholder="Password" />
                    <Button onClick={deleteClick}>Click</Button>
                </div>
            </Alert>
        </>
    );
}