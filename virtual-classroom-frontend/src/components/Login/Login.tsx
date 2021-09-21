import React, {ChangeEvent, useState} from "react";
import {AuthAPI} from "../../api/AuthAPI";
import {Redirect} from "react-router-dom";
import classes from './Login.module.css'
import {Alert} from 'react-bootstrap';
import {TokenStorage} from "../../help/TokenStorage";


export const Login = (props: any) => {

    const [credentials, setCredentials] = useState({
        login: ''
    });

    const [errors, setErrors] = useState<string[]>([]);
    const [show, setShow] = useState(false);


    if (props.isAuth) {
        return <Redirect push to="/"/>
    }


    const onChange = ({target: {name, value}}: ChangeEvent<HTMLInputElement>) => {
        setCredentials({...credentials, [name]: value})
    };

    const onSubmit = (event?: React.FormEvent) => {
        if (event) {
            event.preventDefault()
        }
        AuthAPI.login(credentials.login).then(response => {
            if (response.data.token) {
                TokenStorage.saveToken(response.data.token)
                window.location.reload();
            }
            if (response.data.NotEmpty || response.data.Size) {
                if (!errors.includes(response.data.NotEmpty, 0) && !!response.data.NotEmpty) {
                    setErrors((prevState => [...prevState, response.data.NotEmpty]))
                }
                if (!errors.includes(response.data.Size, 0) && !!response.data.Size) {
                    setErrors((prevState => [...prevState, response.data.Size]))
                }
                setShow(true)
            }
        })
    };

    const onClose = () => {
        setShow(false)
        setErrors([])
    }

    return <>
        <div className="row justify-content-center">
            <div className={classes.margin}>
                <h2>Your name</h2>
                <form onSubmit={onSubmit}>
                    <input
                        className="form-control mt-3"
                        name="login"
                        type="text"
                        value={credentials.login}
                        onChange={onChange}/>
                    <button className="btn btn-primary mt-3" type="submit">Login</button>
                </form>

            </div>
            {
                show ? errors.map(err => {
                    return <Alert variant="danger" onClose={onClose} dismissible>{err}</Alert>
                }) : ''
            }
        </div>
    </>
}