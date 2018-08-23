import React, { Component } from 'react';
import './Main.css';
import ChartSelect from './ChartSelect';
import Sales from './Sales';
import NowTrading from './NowTrading';
import WalletInfo from './WalletInfo';

class Main extends Component {
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
      </div>
    );
  }
}

export default Main;

