import React, { Component } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { connect } from 'react-redux';

import { login } from '../reducers/logInOut';

import './Login.css';
import loginBtn from '../img/login/login_btn_01.png';
import backGround from '../img/login/login_bg.png';
import loginWindow from '../img/login/login_bg02.png';
import loginEmailImg from '../img/login/icon_id_01.png';
import loginPwImg from '../img/login/icon_password_01.png';
import loginLogo from '../img/login/login_logo.png';

class Login extends Component {
  constructor(props) {
    super(props);
    this.state={
      email: null,
      password: null
    }
  }

  handleChange = (e) => {  
    if(e.target.placeholder === "아이디 (이메일)"){
      this.setState({
        email: e.target.value
      })
    }
    else{
      this.setState({
        password: e.target.value
      })
    }
  }

  handleLogin = () => {
    (this.state.email !== null && this.state.password !== null)
    ? axios.post( 
        'LogInOut', 
        'email='+this.state.email+'&password='+this.state.password, 
        { 'Content-Type': 'application/x-www-form-urlencoded' }
      )
      .then( response => {
        if(response.data === 'emailError') alert('존재하지 않는 계정입니다.')
        else if(response.data === 'pwError') alert('비밀번호가 일치하지 않습니다.')
        else if(response.data === 'complete') {
          this.props.onLogin()
          window.location = "/";
        } 
        else alert(response.data)
      }) 
      .catch( response => { console.log('err\n'+response); } ) : // ERROR
      alert('양식을 확인해주세요')
  }

  render() {

    const windowBg = {
      backgroundImage: `url(${loginWindow})`,
      width : '445px',
      height : '534px',
      position : 'absolute',
      left : '1300px',
      top : '320px'
    }

    return (
      <div>
        <img src = {loginLogo} className = 'login-logo' />
        <div style = {windowBg}>

          <div className = "login-email-box">
            <img src = {loginEmailImg} className = "login-email-img"/>
            <input type="text" placeholder="아이디 (이메일)" onChange={this.handleChange} className = 'login-inputBox' />        
          </div>

          <div className = "login-password-box">
            <img src = {loginPwImg} className = "login-pw-img"/>
            <input type="password" placeholder="비밀번호" onChange={this.handleChange} className = 'login-inputBox' />
          </div>

          <div onClick={this.handleLogin} className='login-button' >
            <img src = {loginBtn} />
          </div>
          <div className = "login-text" size = '18px'>회원이 아니신가요?</div>
          <div className = "login-text2" size = '18px'>간단한 회원가입으로 보라봇을 이용해 보세요.</div>
          <Link to="/register"><button className ="login-registerButton">회원가입</button></Link>
          <Link to="/findInfo"><button className ="login-findButton">비밀번호 찾기</button></Link>
        </div>
      </div>
    );
  }
}

let mapDispatchToProps = (dispatch) => {
  return {
    onLogin: () => dispatch(login())
  }
}

Login = connect(undefined, mapDispatchToProps)(Login);

export default Login;

