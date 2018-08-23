import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import ChartSelect from './components/ChartSelect';
import Sales from './components/Sales';
import NowTrading from './components/NowTrading';
import Accounts from './components/Accounts';
import WalletInfo from './components/WalletInfo';
import WalletInfoChild from './components/WalletInfoChild';

import { Route } from 'react-router-dom';
import { Home, Auth } from './pages';
import HeaderContainer from './containers/Base/HeaderContainer';

import storage from './lib/storage';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as userActions from './redux/modules/user';
import * as baseActions from './redux/modules/base';

import styled from 'styled-components';

class App extends Component {

  render() {
    return (
      <div className="App">
        <div>
          <HeaderContainer/>
        </div>
        <div className="wrapper">
          <div className="one"><NowTrading id = "a"/></div>
          <div className="three"><ChartSelect/></div>
          <div className="four"><WalletInfo/></div>
          <div className="four_child"><WalletInfoChild/></div>
          <div className="five"><Sales id = "a"/></div>
        </div>
      </div>
    );
  }
}

export default connect(null, (dispatch) => ({
    UserActions: bindActionCreators(userActions, dispatch)
  })
)(App);
