# Backend part
The student and his authentication is written so that in the future it would be possible to customize and add roles and a password, for example

## Authentication with JWT
For authentication with jwt, we use some classes:
* JWTAuthenticationEntryPoint - for handling authorization errors and further use in web security settings
* JWTAuthenticationFilter - to authenticate users and filter requests
* JWTTokenProvider - to generate(generateToken) and validate tokens(validateToken), as well as identify the user by his token


## WebSocket
* Messages will be subscribed via "/ queue".
* In the method WebSocketConfig.configureClientInboundChannel, we configure the interception to authenticate the user

## AuthController
authenticateStudent() - a method that does the following:
* send an error if any
* create student if not exist
* student authentication
* convertAndSend to "/queue/newStudent/"  that student was added
* generate jwt
* send jwt

## StudentController
* getStudents - return List<StudentDto> ; getCurrentStudent - return StudentDto
* sendHand - used for WebSocket to send hand information
