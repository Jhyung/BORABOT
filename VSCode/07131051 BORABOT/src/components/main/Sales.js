import React, { Component } from 'react';
import './Sales.css';

// const mainHandle = new WebSocket("ws://localhost:8080/Auth/mainhandle");
// const mainHandle = new WebSocket("ws://localhost:8080/BORABOT/mainhandle");
// const mainHandle = new WebSocket("ws://45.120.65.65/BORABOT/mainhandle");
// const authHandle = new WebSocket("ws://45.120.65.65/Auth/authhandle");

const exchangeList = ["bithumb", "bittrex", "binance", "korbit", "coinone"]
const coinList = ["btc", "eth", "btg", "xrp", "eos", "ltc", "dog", "etc", "qtum"]

class Sales extends Component {

  handleStartbtn = () => {
    var sName = document.getElementById("SL_nameInputbox").value;
    var sPrice = document.getElementById("SL_priceInputbox").value;
    var sDeadline = document.getElementById("SL_deadlineInputbox").value;

    let SL_coinSelectbox = document.getElementById("SL_coinSelectbox");
    var sCoin = SL_coinSelectbox.options[SL_coinSelectbox.selectedIndex].text;

    let SL_exchangeSelectbox = document.getElementById("SL_exchangeSelectbox");
    var sExchange = SL_exchangeSelectbox.options[SL_exchangeSelectbox.selectedIndex].text;

    let SL_strategySelectbox = document.getElementById("SL_strategySelectbox");
    var sStrategy = SL_strategySelectbox.options[SL_strategySelectbox.selectedIndex].text;
    
    var jsonStart = {"id" : this.props.id, "name" : sName, "status" : true, "coin" : sCoin, "exchange" : sExchange, "strategy" : sStrategy, "price" : sPrice, "startDate" : new Date(), "period": sDeadline};

    let alertMsg = sCoin + '\n' + sExchange + '\n' + sStrategy + '\n' + sPrice + '\n' + sDeadline +  '\n이 맞습니까?';

    alert(alertMsg);

    //웹소켓으로 textMessage객체의 값을 보낸다.
    // mainHandle.send(JSON.stringify(jsonStart));
    console.log(jsonStart + '전송');
  }

  render() {
    return (
      <div>        
        <form action="TradeMain" method="POST">
        {/* <form action="http://localhost:8080/BORABOT/TradeMain" method="POST"> */}
          <h4 className="Sales-color">Sales configuration</h4>
          <input className="Sales-input" id="SL_nameInputbox" placeholder="이름" name="botname"/><br/>
          <select className="Sales-box" id="SL_coinSelectbox" size='1' name="coin">
            {coinList.map((coin, i) => {
              return (<option key={i}> {coin} </option>)
            })}
          </select><br/>
          <select className="Sales-box" id="SL_exchangeSelectbox" size='1' name="exchange">
            {exchangeList.map((exchange, i) => {
              return (<option key={i}> {exchange} </option>)
            })
            }
          </select><br/>
          <select className="Sales-box" id="SL_strategySelectbox" size='1' name="strategy">
            <option>bollingerPatternNaked</option>
            <option>Bollingertrade</option>
            <option>trendFollowing</option>
            <option>patterNakedTrade</option>
          </select><br/>
          <input className="Sales-input" id="SL_priceInputbox" placeholder="금액 (원)" name="asset"/><br/>
          <input className="Sales-input" id="SL_deadlineInputbox" placeholder="거래 기간 (일)" name="period"/><br/>
          <input name="status" value="true"/><br/><br/>
          <input type="submit" value="거래 시작"/>
        </form><br/>
        
        {/* <h4 className="Sales-color">
          Sales configuration
                </h4>
        <div>
          <input className="Sales-input" id="SL_nameInputbox" placeholder="이름" ></input>
        </div>

        <div>
          <select className="Sales-box" id="SL_coinSelectbox" size='1'>
            {coinList.map((coin, i) => {
              return (<option key={i}> {coin} </option>)
            })}
          </select>
        </div>

        <div>
          <select className="Sales-box" id="SL_exchangeSelectbox" size='1'>
            {exchangeList.map((exchange, i) => {
              return (<option key={i}> {exchange} </option>)
            })
            }
          </select>
        </div>

        <div>
          <select className="Sales-box" id="SL_strategySelectbox" size='1'>
            <option>bollingerPatternNaked</option>
            <option>Bollingertrade</option>
            <option>trendFollowing</option>
            <option>patterNakedTrade</option>
          </select>
        </div>

        <div>
          <input className="Sales-input" id="SL_priceInputbox" placeholder="금액 (원)" ></input>
        </div>
        <div>
          <input className="Sales-input" id="SL_deadlineInputbox" placeholder="거래 기간 (일)" ></input>
        </div>
        <div className="Sales-start-btn" id="Sale-start">
          <button id="Sale-start-btn" onClick={this.handleStartbtn} style={{ margin: '3px' }}>
            거래 시작
          </button>
        </div> */}
      </div>
    );
  }

}

export default Sales;