import React, {useEffect, useState} from 'react';
import {Redirect, Route, Switch} from "react-router-dom";
import {Members} from "./components/Members";
import {Login} from "./components/Login";
import {useHistory} from "react-router";
import {StudentAPI} from "./api/StudentAPI";
import {Header} from "./components/Header";
import {TokenStorage} from "./help/TokenStorage";

const App = () => {
    const history = useHistory();
    const isAuth = !!TokenStorage.getToken();

    const [student, setStudent] = useState({
        id: 0,
        login: "",
        handUp: false
    })

    useEffect(() => {
        if (isAuth) {
            StudentAPI.getCurrentStudent().then(response => {
                setStudent(response.data)
            })
        }
    }, [isAuth])

    return (
        <>
            {
                isAuth ? <Header studentOb={student}/> : ''
            }
            <div className="container mt-3">
                <Switch>
                    <Route exact path="/members">
                        <Members isAuth={isAuth} history={history}/>
                    </Route>
                    <Route exact path="/login">
                        <Login isAuth={isAuth} history={history}/>
                    </Route>
                    <Route path="/">
                        <Redirect to="/members"/>
                    </Route>
                </Switch>
            </div>
        </>
    );
}

export default App;
