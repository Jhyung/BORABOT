import React, { Component } from 'react';
import './NowTrading.css';


// const botHandle = new WebSocket("ws://localhost:8080/tass/bothandle");
const botHandle = new WebSocket("ws://localhost:8080/wsSales/bothandle");
// const botHandle = new WebSocket("ws://45.120.65.65/wsSales/bothandle");

// const ntHandle = new WebSocket("ws://localhost:8080/wsSales/nthandle");
class TradingElement extends Component {

    handleStopbtn = () => {
      alert(this.props.name + " 거래를 중지하시겠습니까?");
      var jsonStop = {"id" : this.props.id, "name" : this.props.name, "status" : false, "index" : this.props.key};
      botHandle.send(JSON.stringify(jsonStop));
    }
    
    render() {
        return(
            <div >
                <div className = "NowTrading-element" >
                    <b>{this.props.name}</b><br/>시작일 : {this.props.date}<br/>
                    코인 : {this.props.coin}<br/>거래소 : {this.props.exchange}<br/>
                    수익률 : {this.props.profit}%<br/>전략 : {this.props.strategy}<br/>
                    <button id="Sale-stop-btn" onClick={this.handleStopbtn}>
                    거래 종료
                    </button>
                </div>
            </div>
        );
    }

}

export default TradingElement;