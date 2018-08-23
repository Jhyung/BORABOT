import React, { Component } from 'react';
import './Main.css';
import ChartSelect from '../components/main/ChartSelect';
import Sales from '../components/main/Sales';
import NowTrading from '../components/main/NowTrading';
import WalletInfo from '../components/main/WalletInfo';

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
