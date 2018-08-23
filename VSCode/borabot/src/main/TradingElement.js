import React, { Component } from 'react';
import axios from 'axios';

import './NowTrading.css';

class TradingElement extends Component {

    handleStopbtn = () => {
         alert(this.props.name + " 거래를 중지하시겠습니까?");

        axios.post( 
            'TradeMain', 
            'status='+false+
            '&botname='+this.props.name,
            { 'Content-Type': 'application/x-www-form-urlencoded' }
        )
    }
    
    render() {
        return(
            <div >
                <div className = "NowTrading-element" >
                    <b>{this.props.name}</b><br/>코인 : {this.props.coin}<br/>거래소 : {this.props.exchange}<br/>
                    전략 : {this.props.strategy}<br/>종료일 : {this.props.endDate}<br/>수익률 : {this.props.profit}%<br/>
                    <button id="Sale-stop-btn" onClick={this.handleStopbtn}>거래 종료</button>
                </div>
            </div>
        );
    }

}

export default TradingElement; 