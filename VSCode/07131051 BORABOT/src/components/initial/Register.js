import React, { Component } from 'react';
import crypto from 'crypto';

var key = "tHis_iS_pRivaTe_Key";
const encrypt = (err, key) => {
  let cipher = crypto.createCipher('aes-256-cbc', key);
  let encipheredpw = cipher.update(err, 'utf-8', 'hex');
  // encipheredpw = cipher.setAutoPadding(auto_padding=true);
  encipheredpw += cipher.final('hex');
  return encipheredpw;
}

class Register extends Component {
  constructor(props) {
    super(props);
    this.state={
      email: null,
      password: null,
      isVal: true
    }
  }

  handleChange = (e) => {  
    if(e.target.placeholder === "email"){
      this.setState({
        email: e.target.value
      })
    }
    else{
      this.setState({
        password: e.target.value
      })
    }
    
    if(this.state.email != null && this.state.password !=null){
      this.setState({
        isVal: false
      })
    }
  }

  handleRegister = (e) => {
    this.setState({
      password: encrypt(this.state.password, key)
    })
  }

  render() {
    return (
      <div>
        <form action="http://localhost:8080/BORABOT/Register" method="POST">회원가입<br/> 
            <input type="text" placeholder="email" name="email" onChange={(e)=>this.handleChange(e)}/><br/>
            <input type="password" placeholder="비밀번호" name="password"onChange={(e)=>this.handleChange(e)} value={this.state.password}/><br/>
            <input disabled={this.state.isVal} type="submit" value="회원가입" onClick={this.handleRegister}/>
        </form><br/>
      </div>      
    );
  }
}

export default Register;

