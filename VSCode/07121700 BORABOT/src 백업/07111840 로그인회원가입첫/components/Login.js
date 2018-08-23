import React, { Component } from 'react';

class Login extends Component {

  render() {

    return (
      <div>
        <form action="Auth" method="POST">회원가입<br/> 
        {/* <form action="http://localhost:8080/BORABOT/Auth" method="POST">회원가입<br/>  */}
            <input type="text" placeholder="email" name="email"/><br/>
            <input type="password" placeholder="비밀번호" name="password"/><br/>
            <input type="submit" value="회원가입"/>
        </form><br/>
        <form action="Login" method="POST">Login<br/>
        {/* <form action="http://localhost:8080/BORABOT/Login" method="POST">Login<br/> */}
            <input type="text" placeholder="email" name="email"/><br/>
            <input type="password" placeholder="비밀번호" name="password"/><br/>
            <input type="submit" value="로그인"/>
        </form><br/>
      </div>      
    );
  }
}

export default Login;

