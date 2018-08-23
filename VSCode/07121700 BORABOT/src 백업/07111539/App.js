import React, { Component } from 'react';
import './App.css';
import ChartSelect from './components/ChartSelect';
import Sales from './components/Sales';
import NowTrading from './components/NowTrading';
import WalletInfo from './components/WalletInfo';
import Login from './components/Login';

class App extends Component {
  constructor(props){
    super(props);
    this.state = {
      id : "cksgh94a",
      listJ: new Array()
    };

  }

  render() {

    return (
      <div>
      <div className="App">
      <div className="wrapper">
        <div className="one"><NowTrading id = {this.state.id}/></div>
        <div className="three"><ChartSelect/></div>
        <div className="four"><WalletInfo/></div>
        <div className="five"><Sales id = {this.state.id} /></div>
      </div>
      </div>
      <Login/>      
      </div>
    );
  }
}

export default App;

