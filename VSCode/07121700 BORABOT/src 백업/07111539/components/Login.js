import React, { Component } from 'react';
import axios from 'axios';

class Login extends Component {

  render() {

    return (
      <div>
        <form action="http://localhost:8080/BORABOT/DoAuth" method="POST">회원가입<br/> 
            <input type="text" placeholder="email" name="email"/><br/>
            <input type="password" placeholder="비밀번호" name="password"/><br/>
            <input type="submit" value="회원가입"/>
        </form><br/>
        <form action="http://localhost:8080/BORABOT/DoLogin" method="POST">Login<br/>
            <input type="text" placeholder="email" name="email"/><br/>
            <input type="password" placeholder="비밀번호" name="password"/><br/>
            <input type="submit" value="로그인"/>
        </form><br/>
        <button type="button" onClick={this.getAxios}>SNT TEST</button>
      </div>      
    );
  }
}

export default Login;

