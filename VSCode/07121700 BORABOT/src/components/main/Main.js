import React, { Component } from 'react';
import './Main.css';
import ChartSelect from './ChartSelect';
import Sales from './Sales';
import NowTrading from './NowTrading';
import WalletInfo from './WalletInfo';
import Login from '../initial/Login';
import Register from '../initial/Register';

class Main extends Component {
  constructor(props){
    super(props);
    this.state = {
      id : "cksgh94a",
    };

  }

  render() {

    return (
      <div>
      <div className="App">
      <div className="wrapper">
        <div className="one"><NowTrading/></div>
        <div className="three"><ChartSelect/></div>
        <div className="four"><WalletInfo/></div>
        <div className="five"><Sales/></div>
      </div>
      </div>
      <Register/><Login/>
      </div>
    );
  }
}

export default Main;

