# Frontend part

This part used  React (with typescript) and Bootstrap

## Project structure
* api - AuthAPI.ts, StudentAPI.ts, WebSocketAPI.ts  - for api for server requests 
* components - Header, Login, Members
* help - TokenStorage - for manipulation with jwt. For example sessionStorage.getItem .
* models - Student 

## Header
Main functions
* logout()
* send() - send information via websocket

## Login
Main functions
* onSubmit - for submit login to server

## Members
It uses websocket connect and two subscribe (), one for changing hands and the other for adding a student.
