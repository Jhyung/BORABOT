import React, { Component } from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import axios from 'axios';
import { connect } from 'react-redux';

import Header from './header/Header';
import Main from './main/Main';
import Profile from './profile/Profile';
import Board from './board/Board';
import BackTesting from './backTesting/BackTesting';
import Strategy from './strategy/Strategy';
import Log from './log/Log'

import Login from './initial/Login';
import Register from './initial/Register';
import FindInfo from './initial/FindInfo';

import { login, logout } from './reducers/logInOut';
import { setStrategy } from './reducers/strategy';

import './App.css'

class App extends Component {
  constructor(){
    super();
    this.state={
      initLogin: false
    }
  }

  componentDidMount() {    
    // 세션의 현재 로그인 여부 확인
    axios.get('Status')
    .then( response => {
      if(response.data.status) {
        this.props.onLogin()
        this.setState({ initLogin: true })
      }
      else{
        this.props.onLogout()
        this.setState({ initLogin: false })
      }
    }) 
    .catch( response => { console.log('err\n'+response); } ); // ERROR    

    axios.get( 'Strategy' )
    .then( response => {
      this.props.onSetStrategy(response.data)
    }) 
    .catch( response => { console.log('err\n'+response); } ); // ERROR

    // 앞단 테스트용
    // const data = [{"data":"{\"indicatorList\":{\"0\":{\"indicator\":\"RSI\",\"weight\":1,\"period\":14,\"buyIndex\":30,\"sellIndex\":70},\"1\":{\"indicator\":\"RSI\",\"weight\":1,\"period\":14,\"buyIndex\":30,\"sellIndex\":70},\"2\":{\"indicator\":\"RSI\",\"weight\":1,\"period\":14,\"buyIndex\":30,\"sellIndex\":70}},\"buyCriteria\":1,\"sellCriteria\":1,\"expList\":\"or,or\"}","name":"asdf"},{"data":"{\"indicatorList\":{\"0\":{\"indicator\":\"RSI\",\"weight\":1,\"period\":14,\"buyIndex\":30,\"sellIndex\":70},\"1\":{\"indicator\":\"RSI\",\"weight\":1,\"period\":14,\"buyIndex\":30,\"sellIndex\":70},\"2\":{\"indicator\":\"RSI\",\"weight\":1,\"period\":14,\"buyIndex\":30,\"sellIndex\":70},\"3\":{\"indicator\":\"RSI\",\"weight\":1,\"period\":14,\"buyIndex\":30,\"sellIndex\":70},\"4\":{\"indicator\":\"RSI\",\"weight\":1,\"period\":14,\"buyIndex\":30,\"sellIndex\":70},\"5\":{\"indicator\":\"RSI\",\"weight\":1,\"period\":14,\"buyIndex\":30,\"sellIndex\":70}},\"buyCriteria\":0,\"sellCriteria\":0,\"expList\":\"or,or,or,or,or\"}","name":"zxcvwer"}]
    // this.props.onSetStrategy(data)
    // 앞단 테스트용
  }

  render() { 
    return (
      <BrowserRouter basename={process.env.REACT_APP_ROUTER_BASE || ''}>
        {
        this.state.initLogin // 실제용
        // true // 앞단 테스트
        ? <div style={{height:"100%"}}>
            <div className = "header-location"><Header/></div>
            <div className="main">
              <Switch>
                <Route path="/profile" component={Profile}/>
                <Route path="/backtesting" component={BackTesting}/>
                <Route path="/board" component={Board}/>
                <Route path="/log" component={Log}/>
                <Route path="/strategy" component={Strategy}/>
                <Route path="/" component={Main}/>
              </Switch>
            </div>
          </div>
        : <Switch>
            <Route path="/register"><div className="main"><Register/></div></Route>
            <Route path="/findInfo"><div className="main"><FindInfo/></div></Route>
            <Route path="/"><div className="login"><Login/></div></Route>
          </Switch>
        }
      </BrowserRouter>
    );
  }
}

let mapDispatchToProps = (dispatch) => {
  return {
    onLogin: () => dispatch(login()),
    onLogout: () => dispatch(logout()),
    onSetStrategy: (value) => dispatch(setStrategy(value))
  }
}

let mapStateToProps = (state) => {
  return {
    login: state.logInOut.login,
    strategyList: state.strategy.strategyList
  };
}

App = connect(mapStateToProps, mapDispatchToProps)(App);

export default App;
