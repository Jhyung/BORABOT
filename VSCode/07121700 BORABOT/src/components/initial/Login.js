import React, { Component } from 'react';

class Login extends Component {
  constructor(props) {
    super(props);
    this.state={
      email: null,
      password: null,
      isVal: null
    }
  }

  handleChange = (e) => {  
    const { email, password } = e.target; 
  }

  handleLogin = (e) => {
    // 이대로 하면 서버 올렸을 때 origin 같아서 cors 안생김 세션 다른거 상관 ㄴㄴ      
    const { email, password } = e.target; 
    
    alert(password);
    
    // fetch('http://localhost:8080/BORABOT/Login', {method: 'post'})
    // .then(res => res.json())
    // .then(
    //     (result) => {
    //         this.setState({
    //             listE: result
    //         })
    //     }
    // )

  }

  render() {
    return (
      <div>
        {/* <form onSubmit={(e)=>this.handleLogin(e)} onChange={(e=>this.handleChange(e))}>Login<br/> */}
        {/* <form action="Login" method="POST">Login<br/> */}
        <form action="http://localhost:8080/BORABOT/Login" method="POST">Login<br/>
            <input type="text" placeholder="email" name="email"/><br/>
            <input type="password" placeholder="비밀번호" name="password"/><br/>
            <input type="submit" value="로그인"/>
        </form><br/>
      </div>      
    );
  }
}

export default Login;

