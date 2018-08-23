import React,{Component} from 'react';
import axios from 'axios';
import { connect } from 'react-redux';

import './WalletInfo.css';

class WalletInfo extends Component{
  constructor(props) {
    super(props);

    this.state = {
      selectedWallet: [
        // 앞단 테스트용
        // {"balance":0.0,"base":"USDT"},{"balance":0.0,"base":"BTC"},{"balance":0.0,"base":"ETH"}
        // 앞단 테스트용
      ]
    };
  }

  componentDidMount() {    
    axios.get( 'WalletInfo?exchange=BINANCE' )
    .then( response => {
      if(response.data === 'sessionExpired') this.sessionExpired()
      else {
        this.setState({ selectedWallet: response.data })
      }
    })
    .catch( response => { console.log('err\n'+response); } ); // ERROR
  }

  handleExchange = (e) => {
    if(e.target.value !== '거래소'){
      axios.get( 'WalletInfo?exchange='+e.target.value )
      .then( response => {
        if(response.data === 'sessionExpired') this.sessionExpired()
        else {
          this.setState({ selectedWallet: response.data })
        }
      })
      .catch( response => { console.log('err\n'+response); } ); // ERROR
    }
  }

  // 세션 유효성 검증
  sessionExpired = () => {
    alert('세션이 종료되었습니다\n다시 로그인하세요')
    window.location = '/'
  }

  render() {
    const { exchangeList } = this.props

    return(
      <div className = "walletInfo-container" >
        <div style = {{marginTop : "20px", textAlign : "center", fontSize : "18px", fontWeight : "bold" }}>
          내 지갑
        </div>

        <div style = {{marginTop : "20px", marginBottom : '10px' }}>
          <select className = "wallet-select" id="WI_exchangeSelectbox" size = '1' onChange = {this.handleExchange} placeholder={'Select something'}>
            {exchangeList.map((exchange, i) => {
              return (<option key = {i}>{exchange.key}</option>)
            })}
            <option selected hidden disabled>거래소</option>
          </select>
        </div>

        {this.state.selectedWallet.map((w, i) => {
          return (
            <div style={{marginLeft : '20px', marginBottom : '10px', fontSize : '14px', fontWeight : 'bold' }}>{w.base} : {w.balance}<br/></div>
          )
        })}
      </div>
    );
  }
}

let mapStateToProps = (state) => {
  return {
    exchangeList: state.exchange.exchangeList,
  };
}

WalletInfo = connect(mapStateToProps)(WalletInfo);

export default WalletInfo;