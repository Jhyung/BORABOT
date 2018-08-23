import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import { connect } from 'react-redux';

import { logout } from '../reducers/logInOut';

import './Header.css';
import mainLogo from '../img/common/logo_01.png'

class Header extends Component {
  HandleLogOut = () => {
    if(window.confirm('로그아웃하시겠습니까?')){
      axios.get( 'LogInOut')
      this.props.onLogout()
      window.location = '/'
    }
  }

  render() {
    return (
      <div className = "header-container">
        <Link to="/"><img src = {mainLogo} className = 'header-logo'/></Link>
        <Link to="/" className = 'header-main'>거래화면</Link>
        <Link to="/board" className = 'header-board'>전략공유게시판</Link>
        <Link to="/backtesting" className = 'header-bt'>백테스팅</Link>
        <Link to="/strategy" className = 'header-strategy'>전략</Link>
        <Link to="/log" className = 'header-log'>거래기록</Link>
        <Link to="/profile" className = 'header-profile'>회원정보관리</Link>
        {this.props.login && <button onClick={this.HandleLogOut} className = 'header-logout'>로그아웃</button>}
      </div>
    );
  }
}

let mapDispatchToProps = (dispatch) => {
  return {
    onLogout: () => dispatch(logout())
  }
}

let mapStateToProps = (state) => {
  return {
    login: state.logInOut.login
  };
}

Header = connect(mapStateToProps, mapDispatchToProps)(Header);

export default Header;