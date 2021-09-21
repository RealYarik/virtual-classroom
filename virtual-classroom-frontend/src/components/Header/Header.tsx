import React, {useEffect, useState} from "react";
import {WebSocketAPI} from "../../api/WebSocketAPI";
import {Student} from "../../models/Student";
import {TokenStorage} from "../../help/TokenStorage";

type THeader = {
    studentOb: Student
}
export const Header: React.FC<THeader> = ({studentOb}) => {

    const [student, setStudent] = useState<Student>({
        id: 0,
        login: "",
        handUp: false
    })

    useEffect(() => {
        setStudent(studentOb)
    }, [studentOb])

    const logout = () => {
        TokenStorage.logout()
        WebSocketAPI._disconnect();
        window.location.reload()
    }

    const send = () => {
        WebSocketAPI._send(student)
    }

    return <>
        <nav className="navbar navbar-expand-lg navbar-light bg-light">
            <div className="container-fluid">
                <a className="navbar-brand" href="/">Virtual Classroom</a>

                <div className="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul className="navbar-nav me-auto">
                        <li className="nav-item dropdown">
                            <a className="nav-link dropdown-toggle" href="#"
                               id="navbarDropdownMenuLink" role="button"
                               data-bs-toggle="dropdown" aria-expanded="false">Actions</a>
                            <ul className="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                                <li>
                                    <button onClick={send} className="dropdown-item">Raise hand up/down</button>
                                </li>
                            </ul>
                        </li>
                    </ul>
                    <ul className="navbar-nav">
                        <li className="nav-item dropdown">
                            <a className="nav-link dropdown-toggle" href="#"
                               id="navbarDropdownMenuLink" role="button"
                               data-bs-toggle="dropdown" aria-expanded="false">
                                {student.login}
                            </a>
                            <ul className="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdownMenuLink">
                                <li>
                                    <button onClick={logout} className="dropdown-item">Logout</button>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    </>
}