import React, {useEffect, useState} from 'react';
import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';
import Toast from 'react-bootstrap/Toast';

function ReplyToast(props) {
    const [show, setShow] = useState(false);

    useEffect( () => {setShow(true);}, []);

    return (
        <Row>
            <Col xs={6}>
                <Toast onClose={()=>{setShow(false); props.ToastHandle(false);} } show={show} delay={3000} autohide>
                    <Toast.Header>
                        <strong className="me-auto">알림</strong>
                        <small>now</small>
                    </Toast.Header>
                    <Toast.Body>
                        {props.content}
                    </Toast.Body>
                </Toast>
            </Col>
        </Row>
    );
}

export default ReplyToast;