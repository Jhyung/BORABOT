import React, { Component } from 'react'

import Login from './Login';
import Register from './Register';
// import FindInfo from './FindInfo';

class Initial extends Component {
  constructor(props) {
    super(props);

    this.state = {
      action: 'login' // 사용자 접근 상태 (login, register, findInfo)
    };
  }

  handleAction = (a) => {
    this.setState({ action: a })
  }

  render() {
    switch(this.state.action){
      case 'register':
        return <Register/>
      // case 'findInfo':
      //   return <FindInfo/>
      default:
        return <Login action={this.state.action} handleAction={this.handleAction}/>
    }
  }
}

export default Initial;