import React, { Component } from 'react';
import './App.css';
import ChartSelect from './components/ChartSelect';
import Sales from './components/Sales';
import NowTrading from './components/NowTrading';
import WalletInfo from './components/WalletInfo';
import WalletInfoChild from './components/WalletInfoChild';

import Header from './components/Base/Header/Header';

// import { connect } from 'react-redux';
// import { bindActionCreators } from 'redux';
// import * as userActions from './redux/modules/user';

// import storage from './lib/storage';

class App extends Component {
  // initializeUserInfo = async () => {
  //     const loggedInfo = storage.get('loggedInfo'); // 로그인 정보를 로컬스토리지에서 가져오기.
  //     if(!loggedInfo) return; // 로그인 정보 없으면 중지.

  //     const { UserActions } = this.props;
  //     UserActions.setLoggedInfo(loggedInfo);
  //     try {
  //         await UserActions.checkStatus();
  //     } catch (e) {
  //         storage.remove('loggedInfo');
  //         window.location.href = '/auth/login?expired';
  //     }
  // }

  // componentDidMount() {
  //     this.initializeUserInfo();
  // }

  render() {
    return (
      <div className="App">
        <div>
          <Header/>
        </div>
        <div className="wrapper">
          <div className="one"><NowTrading email = "cksgh94a@gmail.com"/></div>
          <div className="three"><ChartSelect/></div>
          <div className="four"><WalletInfo/></div>
          <div className="four_child"><WalletInfoChild/></div>
          <div className="five"><Sales email = "cksgh94a@gmail.com"/></div>
        </div>
      </div>
    );
  }
}

// export default connect(null, (dispatch) => ({
//     UserActions: bindActionCreators(userActions, dispatch)
//   })
// )(App);

export default App;