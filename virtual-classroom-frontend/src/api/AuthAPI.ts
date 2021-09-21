import axios from "axios";

const instance = axios.create({
    baseURL: `http://localhost:8080/api/auth`,
})

export const AuthAPI = {

    login: (login: string) => {
        return instance.post(`/login`, {
            login: login
        })
    },
}
