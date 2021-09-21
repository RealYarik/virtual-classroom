import React, {useEffect, useState} from "react";
import {StudentAPI} from "../../api/StudentAPI";
import {Student} from "../../models/Student";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import classes from './Members.module.css'
import {TokenStorage} from "../../help/TokenStorage";

const SERVER_URL = 'http://localhost:8080/ws'
const TOPIC = '/queue/reply/';
const TOPIC_LOGIN = '/queue/newStudent/';

const webSocket = new SockJS(SERVER_URL);
const stompClient = Stomp.over(webSocket);

export const Members = (props: any) => {
    const [students, setStudents] = useState<Student[]>([])

    useEffect(() => {
        StudentAPI.getStudents().then(response => {
            setStudents(response.data)
            console.log(response.data)
        })
    }, [])

    if (!props.isAuth) {
        props.history.push('/login')
    }
    useEffect(() => {
        const token = TokenStorage.getToken();

        stompClient.connect({'Authorization': token}, function (frame) {
            stompClient.subscribe(TOPIC_LOGIN, (message) => {
                if (message.body) {
                    setStudents((prevState => [...prevState, JSON.parse(message.body)]))
                }
            }, {'Authorization': token});

            stompClient.subscribe(TOPIC, (message) => {
                if (message.body) {
                    const studentObj = JSON.parse(message.body)
                    const newList = students.map(o => {
                        const student = o as Student
                        if (student.login === studentObj.login) {
                            return studentObj;
                        }
                        return o;
                    });
                    setStudents(newList)
                }
            }, {'Authorization': token});
        });
    }, [students, stompClient])


    return <>
        <div className="row justify-content-center">
            <div className={classes.margin}>
                <h3>Class members</h3>
                <div className={classes.border}>
                    <table className="table table-borderless mt-3">
                        <thead>
                        <tr>
                            <th scope="col">Login</th>
                            <th scope="col">Hand</th>
                        </tr>
                        </thead>
                        <tbody>
                        {
                            students.map(data => {
                                const student = data as Student
                                return <tr key={student.id}>
                                    <td>{student.login}</td>
                                    <td>{student.handUp ? <img src={"/hand.png"}/> : ""}</td>
                                </tr>
                            })
                        }
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </>
}