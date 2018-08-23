import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';

import './Register.css';
import signWindowBackground from '../img/sign/sign_bg_02.png';
import signEmailImg from '../img/sign/sign_id_01.png';
import signEmailAuth from '../img/sign/sign_btn_i_01.png';
import signEmailKey from '../img/sign/sign_key_01.png';
import signPwImg from '../img/sign/sign_password_01.png';
import signPw2Img from '../img/sign/sign_password_02.png';
import signBtn from '../img/sign/sign_btn_01.png';
import signCheckBox from '../img/sign/sign_checkbox_01.png';
import signLogo from '../img/sign/sign_logo_01.png';

class Register extends Component {
  constructor(props) {
    super(props);
    this.state={
      email: null,  
      password: null,
      passwordC: null,

      eVal: false, // 이메일 유효성
      pVal: false, // 비밀번호 유효성
      ppVal: false // 비밀번호 일치 유효성
    }
  }

  handleChange = (e) => {  
    // 이메일 유효성 검사
    if(e.target.placeholder === "email"){
      if(e.target.value.match(/^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/)){
        this.setState({ email: e.target.value, eVal: true })        
      } else{
        this.setState({ email: e.target.value, eVal: false })
      }
    }

    // 비밀번호 유효성 검사 (영문(대소문자 구분),숫자,특수문자 ~!@#$%^&*()-_? 만 허용)
    else if(e.target.placeholder === "비밀번호"){
      if(e.target.value.match(/^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,16}$/)
        && (e.target.value.length >= 8) && (e.target.value.length <= 16)) {
        this.setState({ password: e.target.value, pVal: true })
      } else{
        this.setState({ password: e.target.value, pVal: false })
      }
    }

    // 비밀번호 확인
    else if(e.target.placeholder === "비밀번호 확인"){
      if(this.state.password === e.target.value) {
        this.setState({ passwordC: e.target.value, ppVal: true })
      } else{
        this.setState({ passwordC: e.target.value, ppVal: false })
      }
    }    
  }  

  handleAuth = () => {
    axios.post( 
      'Register', 
      'auth='+true+'&email='+this.state.email, 
      { 'Content-Type': 'application/x-www-form-urlencoded' }
    )
    alert('메일 확인후 키를 입력하여 주세요.\n(메일 수신에 1~2분 가량 소요될 수 있습니다.)')
  }

  handleRegister = (e) => {
    // 회원가입 조건 검증
    if(this.state.eVal && this.state.pVal && this.state.ppVal){
      axios.post( 
        'Register', 
        'auth='+false+
        '&email='+this.state.email+
        '&password='+this.state.password+
        '&key='+document.getElementById('key').value, 
        { 'Content-Type': 'application/x-www-form-urlencoded' }
      )
      .then( response => {
        if(response.data === 'dupError') alert('이미 존재하는 계정입니다.')
        else if(response.data === 'authError') alert('잘못된 인증키입니다.')
        else if(response.data === 'complete') {
          alert('회원가입이 완료되었습니다.')
          window.location = "/";
        }
        else alert(response.data)
      }) 
      .catch( response => { console.log('err\n'+response); } ); // ERROR
       
    }
    else{
      alert('양식을 확인해주세요')
    }
  }

  render() {
    const { email, password, passwordC, pVal, ppVal } = this.state

    const signWindow = {
      backgroundImage: `url(${signWindowBackground})`,
    }

    console.log(password, pVal)
    return (
      <div>
        <Link to="/"><img src = {signLogo} className = 'sign-logo'/></Link>
        
        <div style={signWindow} className = 'sign-windowBackground' >

          <div className = 'sign-header'>회원가입</div>
        
          <div className = "sign-email-box">
            <img src = {signEmailImg} className = "sign-email-img"/>
            <input type="text" placeholder="email" onChange={this.handleChange} className = 'sign-inputBox' />
            <div onClick={this.handleAuth} className = 'sign-auth-btn'><img src = {signEmailAuth}/></div>
          </div>

          <div className = 'sign-email-warning-text'>
            {((email !== null) && (email !== '') && !this.state.eVal) && <text>올바른 이메일 형식이 아닙니다.</text>}
          </div>

          <div className = "sign-authkey-box">
            <img src = {signEmailKey} className = "sign-email-key-img"/>
            <input type="text" id="key" placeholder="인증키 입력" onChange={this.handleChange} className = 'sign-inputBox' />     
          </div>

          <div className = "sign-password-box">
            <img src = {signPwImg} className = "sign-password-img"/>
            <input type="password" placeholder="비밀번호" onChange={this.handleChange} className = 'sign-inputBox' />
          </div>
          <text className = 'sign-password-text'>영문(대소문자 구분), 숫자, 특수문자를 포함하여 8~16자</text>
          <div className = "sign-password2-box">
            <img src = {signPw2Img} className = "sign-password2-img"/>
            <input type="password" placeholder="비밀번호 확인" onChange={this.handleChange} className = 'sign-inputBox' />        
          </div>

          <div className = 'sign-password-warning-text'>
            {((password !== '') && (password !== null) && !pVal) && <text>올바른 비밀번호 형식이 아닙니다.</text>}
            {((passwordC !== '') && pVal && !ppVal) && <text>비밀번호가 다릅니다.</text>}
          </div>
        
          <div className = 'sign-yakkwan-text'>
            <img src = {signCheckBox}/>&nbsp;이용약관, 개인정보보호정책에 동의합니다.
          </div>
        
          <div className = 'sign-19-text'>
            <img src = {signCheckBox}/>&nbsp;19세 이상입니다.
          </div>
    
          <div className = 'sign-sign-btn' onClick={this.handleRegister}>
            <img src = {signBtn}/>
          </div>
        </div>
      </div>
    );
  }
}

export default Register;

