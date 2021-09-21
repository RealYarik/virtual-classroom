import axios from "axios";
import {TokenStorage} from "../help/TokenStorage";

const token = TokenStorage.getToken();

const instance = axios.create({
    baseURL: `http://localhost:8080/api/students`,
    headers: {
        'Authorization': token
    }
})

export const StudentAPI = {

    getStudents: () => {
        return instance.get(`/`,)
    },
    getCurrentStudent: () => {
        return instance.get(`/current`,)
    },
    deleteStudent: (id: number) => {
        return instance.get(`/` + id,)
    }
}