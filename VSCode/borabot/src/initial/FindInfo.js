import React, { Component } from 'react';
import axios from 'axios';

import './FindInfo.css';

class FindInfo extends Component {
  constructor(props) {
    super(props);
    this.state={
      email: null
    }
  }

  handleChange = (e) => {  
    this.setState({ email: e.target.value })
  }

  handleTempPwd = () => {
    this.state.email !== null
      && axios.post( 
          'FindInfo', 
          'email='+this.state.email, 
          { 'Content-Type': 'application/x-www-form-urlencoded' }
        )
        .then( response => {
          if(response.data === 'emailError') alert('존재하지 않는 계정입니다.')
          else if(response.data === 'complete') {
            alert('약 1~2분 후 이메일 확인 후 비밀번호를 재설정해주세요\n(메일이 오지 않을 경우 다시 한 번 발송 버튼을 눌러주세요)')
          } 
          else alert(response.data)
        }) 
        .catch( response => { console.log('err\n'+response); } ) // ERROR
  }

  handleBackHome = () => {
    window.location = "/";
  }
  
  render() {
    console.log(this.state.email)
    return (
      <div>
        <a href="/"><img class="findInfo_logo" src={require('../img/common/logo_01.png')} /></a>
        <div class="findInfo">
          <h1 class="findInfo_title">비밀번호 찾기</h1>
          <input id="inputFindInfo" type="text" placeholder="이메일을 입력하세요." name="email" onChange={this.handleChange}/>
          <button id="tempPwd" onClick={this.handleTempPwd}><img src={require('../img/common/btn_17.png')} /></button>
        </div>
        <h5 class="tempPwdText">입력하신 이메일로 임시비밀번호가 발송됩니다.</h5>
        <button id="backToHome" onClick={this.handleBackHome}><img src={require('../img/common/btn_18.png')} /></button>
      </div>      
    );
  }
}

export default FindInfo;

