import React, { Component } from 'react';
import TradingViewWidget, { Themes } from 'react-tradingview-widget';
import { connect } from 'react-redux';

import { setChart } from '../reducers/sales';

import './ChartSelect.css';

class ChartSelect extends Component {
  constructor(props) {
    super(props);

    // 차트 선택 화면에서 선택한 인덱스
    this.state = {
      exchangeIndex: 0,
      baseIndex: 0,
      coinIndex: 0,
      intervalIndex: 0
    };
  }

  // 거래 시작 화면에서 선택한 인덱스를 차트 선택 화면의 인덱스로 설정
  componentDidUpdate() {
    if(this.props.sales.sales){
      const { exchangeIndex, baseIndex, coinIndex, intervalIndex } = this.props.sales
      document.getElementById('chartExchange').selectedIndex = exchangeIndex
      document.getElementById('chartBase').selectedIndex = baseIndex
      document.getElementById('chartCoin').selectedIndex = coinIndex
      document.getElementById('chartInterval').selectedIndex = intervalIndex
    }
  }
  
  // 차트 선택 인덱스를 차트 데이터 인덱스로 설정
  handleIndex = (e) => {
    if (e.target.id === 'chartExchange'){
      document.getElementById('chartBase').selectedIndex = 0
      document.getElementById('chartCoin').selectedIndex = 0
    } else if(e.target.id === 'chartBase') document.getElementById('chartCoin').selectedIndex = 0
    this.props.onSetChart({
      sales: false
    })
    this.setState({
      exchangeIndex: document.getElementById('chartExchange').selectedIndex,
      baseIndex: document.getElementById('chartBase').selectedIndex,
      coinIndex: document.getElementById('chartCoin').selectedIndex,
      intervalIndex: document.getElementById('chartInterval').selectedIndex
    })
  }

  render() {
    const { exchangeList, intervalList, sales } = this.props
    let exchangeIndex = 0;  let baseIndex = 0;  let coinIndex = 0;  let intervalIndex = 0
        
    sales.sales 
      ? { exchangeIndex, baseIndex, coinIndex, intervalIndex } = this.props.sales // 거래 시작 화면에서 설정이 변경되면 차트는 해당 내용을 그림
      : { exchangeIndex, baseIndex, coinIndex, intervalIndex } = this.state // 차트 선택 화면에서 설정이 변경되면 차트는 해당 내용을 그림

    // console.log(exchangeList[exchangeIndex].key)
    // console.log(exchangeIndex[exchangeIndex].value.baseList[baseIndex])
    return (
      <div>
        <div className = 'CS-selectingChart'>

          <select  className = 'CS-selectEx' palceholder = '거래소' id="chartExchange" onChange={this.handleIndex}>
          {exchangeList.map((exchange, index) => {
            return (<option key={index} > {exchange.key} </option>)
          })}
          </select>

          <select id="chartBase" className = 'CS-select' onChange={this.handleIndex}>
            {exchangeList[exchangeIndex].value.baseList.map((base, i) => {
              return (<option key={i}>
                {(base === 'USD')
                  ? 'USDT'
                  : base }
                </option>)
            })}
          </select>

          <select id="chartCoin" className = 'CS-select' onChange={this.handleIndex}>
            {exchangeList[exchangeIndex].value.coin[baseIndex].list.map((coin, i) => {
              return (<option key={i}> {coin} </option>)
            })}
          </select>

          <select id="chartInterval" className = 'CS-select' onChange={this.handleIndex}>
            {intervalList.map((int, i) => {
              return (<option key={i}> {int.key} </option>)
            })}
          </select>
        </div>

        <TradingViewWidget
          symbol={exchangeList[exchangeIndex].key+":"+exchangeList[exchangeIndex].value.coin[baseIndex].list[coinIndex]+exchangeList[exchangeIndex].value.baseList[baseIndex]}
          theme={Themes.LIGHT}
          locale="kr"
          timezone="Asia/Seoul"
          width = "720px"
          height = "700px"
          // 트레이딩뷰에서 6시간과 12시간 데이터를 제공하지 않으므로 4시간으로 바꿔서 표시
          interval={((intervalList[intervalIndex].value/60 === 360) || (intervalList[intervalIndex].value/60 === 720))
            ? 240
            : intervalList[intervalIndex].value/60
          }
          // hide_top_toolbar
        />
      </div>
    );
  }
}

let mapDispatchToProps = (dispatch) => {
  return {
    onSetChart: (value) => dispatch(setChart(value))
  }
}

let mapStateToProps = (state) => {
  return {
    exchangeList: state.exchange.exchangeList,
    intervalList: state.exchange.intervalList,

    sales: state.sales
  };
}

ChartSelect = connect(mapStateToProps, mapDispatchToProps)(ChartSelect);


export default ChartSelect;