import React, { Component } from 'react';
import logo from '../../logo.svg';
import './Term.css';
import ChartSelect from '../../components/ChartSelect';
import Sales from '../../components/Sales';
import NowTrading from '../../components/NowTrading';
import Accounts from '../../components/Accounts';
import WalletInfo from '../../components/WalletInfo';
import WalletInfoChild from '../../components/WalletInfoChild';

import { Login } from '../../containers/Auth';


import { Route } from 'react-router-dom';
import { Home, Auth } from '../../pages';

import storage from '../../lib/storage';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as userActions from '../../redux/modules/user';
import * as baseActions from '../../redux/modules/base';

import styled from 'styled-components';

class Term extends Component {
  initializeUserInfo = async () => {
    const loggedInfo = storage.get('loggedInfo');
    if (!loggedInfo) return;

    const { UserActions } = this.props;
    UserActions.setLoggedInfo(loggedInfo);
    try {
      await UserActions.checkStatus();
    } catch (e) {
      storage.remove('loggedInfo');
      window.location.href = '/auth/login?expired';
    }
  }

  componentDidMount() {
    this.initializeUserInfo();
  }

  render() {
    return (
      <div className="Term">
        이용약관1
      </div>
    );
  }
}

export default connect(null, (dispatch) => ({
    UserActions: bindActionCreators(userActions, dispatch)
  })
)(Term);
