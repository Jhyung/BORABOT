import React, { Component } from 'react';

import ChartSelect from './ChartSelect';
import Sales from './Sales';
import NowTrading from './NowTrading';
import WalletInfo from './WalletInfo';
import CoinRecommend from './CoinRecommend';

import './Main.css';

class Main extends Component {
  constructor(props) {
    super(props);

    this.state = {
      toggle: true  // Sales에서 거래가 시작되면 NowTrading에 알리기 위한 state
    };
  }

  // 현재 페이지에서 새로고침을 위해 메뉴를 다시 눌렀을 경우
  componentWillReceiveProps (nextProps) {
    (this.props.location.key !== nextProps.location.key)
    && (window.location = "/")
  }

  handleToggle = () => {  // Sales에서 거래가 시작되면 toggle을 변화
    this.setState({ toggle: !this.state.toggle })
  }
  
  render() {
    return (
      <div className="App">
        <div className = 'bg-recommend'><CoinRecommend/></div>
        <div className = 'bg-nowTrading'><NowTrading className = 'three-trading-obj' toggle={this.state.toggle}/></div>
        <div className = 'bg-chart'><ChartSelect/></div>
        <div className = 'bg-wallet'><WalletInfo/></div>
        <div className = 'bg-transaction'><Sales onToggle={this.handleToggle}/></div>
      </div>
    );
  }
}

export default Main;

