import React, { Component } from 'react';
import './ChartSelect.css';

const coinList = [
  {
    name: "ETH",
    img: "eth_img.jpg",
    link_whitepaper: "https://whitepaperbtc.com"
  },
  {
    name: "BTC",
    img: "btc_img.jpg",
    link_whitepaper: "https://whitepaperbtc.com"
  },
  {
    name: "BTG",
    img: "btg_img.jpg",
    link_whitepaper: "https://whitepaperbtc.com"
  },
  {
    name: "XRP",
    img: "xrp_img.jpg",
    link_whitepaper: "https://whitepaperbtc.com"
  },
  {
    name: "TRX",
    img: "trx_img.jpg",
    link_whitepaper: "https://whitepaperbtc.com"
  },
  {
    name: "LTC",
    img: "ltc_img.jpg",
    link_whitepaper: "https://whitepaperbtc.com"
  },
  {
    name: "DCT",
    img: "dct_img.jpg",
    link_whitepaper: "https://whitepaperbtc.com"
  },
  {
    name: "ETC",
    img: "etc_img.jpg",
    link_whitepaper: "https://whitepaperbtc.com"
  },
  {
    name: "QTUM",
    img: "qtum_img.jpg",
    link_whitepaper: "https://whitepaperbtc.com"
  }
]

const exchangeList = [
  {
    name: "BITTREX",
    //link?
  },
  {
    name: "BITHUMB"
  },
  {
    name: "BINANCE"
  },
  {
    name: "KORBIT"
  },
  {
    name: "COINONE"
  }
]

const unitList = [
  "5m", "10m", "15m", "30m", "1h", "6h", "1d", "1w", "1m", "1y"
]

class ChartSelect extends Component {

  state = {
    coin: coinList[0],
    exchange: exchangeList[0],
    unit: '5m',
    content: 'The base of this index is BTC, so there is no index of BTC-BTC. \n Choose another Cryptocurrency to display proper marketorder.'
  }

  componentDidMount() {
    this._call_API();
  }

  _call_API = () => {

    let base = 'https://bittrex.com/api/v1.1/public/getmarketsummary?market=btc-'
    let addr = base.concat(this.state.coin.name);
    console.log(addr);
    fetch(addr)
      .then(response => response.json())
      .then(json => {
        // Do what you want with your data
        this.setState({
          ...this.state.unit,
          content: (JSON.stringify(json.result[0]))
        })
      })
      .catch(err => console.log(err));

  }

  handleCoinSelect = () => {

    let e = document.getElementById("CS_coinSelectbox");
    let selected_coin = coinList[e.options[e.selectedIndex].index];
    this.setState({
      coin: selected_coin,
      ...this.state.content,
    })

    let base = 'https://bittrex.com/api/v1.1/public/getmarketsummary?market=btc-'
    let addr = base.concat(selected_coin.name);
    console.log(selected_coin.name + "->" + addr);
    fetch(addr)
      .then(response => response.json())
      .then(json => {
        // Do what you want with your data
        this.setState({
          ...this.state.unit,
          content: (JSON.stringify(json.result[0]))
        })
      })
      .catch(err => console.log(err));
  }

  handleExchangeSelect = () => {

    let e = document.getElementById("CS_exchangeSelectbox");
    let selected_exchange = exchangeList[e.options[e.selectedIndex].index];

    this.setState({
      coin: this.state.coin,
      exchange: selected_exchange,
      ...this.state.content
    })
  }

  handleUnitSelect = () => {
    let e = document.getElementById("CS_unitSelectbox");
    let selected_unit = unitList[e.options[e.selectedIndex].index];

    this.setState({
      ...this.state.exchange,
      unit: selected_unit,
      content: this.state.content
    })
  }

  // 컴포넌트 간 props 및 state 체인지
  handleSwtichRequest = () => {

    const newState = this.props;

  }

  render() {

    return (
      <div className="ChartSelect">
        <select id="CS_coinSelectbox" size='1' onChange={this.handleCoinSelect} style={{ marginRight: 10 }}>
          {coinList.map((coin, i) => {
            return (<option key={i}>{coin.name}</option>);
          })}
        </select>


        <select id="CS_exchangeSelectbox" size='1' onChange={this.handleExchangeSelect} style={{ marginRight: 10 }}>
          {exchangeList.map((exchange, i) => {
            return (<option key={i}> {exchange.name} </option>)
          })
          }          
          </select>

        <select id="CS_unitSelectbox" size='1' onChange={this.handleUnitSelect}>
          {unitList.map((unit, i) => {
            return(<option key = {i}> {unit} </option>)
          })}
        </select>

        <p style={{ color: 'white' }}>
          coin : {this.state.coin.name} --------- exchange : {this.state.exchange.name} --------- period unit : {this.state.unit}
        </p>
        <h3 style={{ color: 'white' }}>[ INDEX : MarketSummary of BTC-{this.state.coin.name} ]</h3>
        <p style={{ color: 'white', fontSize: '15px' }}>
          {this.state.content}
        </p>
        <img src="https://media.coindesk.com/uploads/2017/11/Bitcoin-daily.png" style={{ width: '800px', height: '450px' }} />



      </div>
    );
  }
}

export default ChartSelect;