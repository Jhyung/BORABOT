import React, { Component } from 'react';
import './NowTrading.css';
import TradingElement from './TradingElement';


// const ntHandle = new WebSocket("ws://localhost:8080/tass/nthandle");
const ntHandle = new WebSocket("ws://localhost:8080/wsSales/nthandle");
// const ntHandle = new WebSocket("ws://45.120.65.65/wsSales/nthandle");


var list = new Array()
list = [{name: "cksgh",  coin: "btc", exchange: "bithumb", profit: 100, startDate: new Date(), strategy: "bollingerPatternNaked"},
{name: "jh",  coin: "btc", exchange: "bithumb", profit: 100, startDate:new Date(),strategy: "bollingerPatternNaked"},
{name: "jhasdf",  coin: "btc", exchange: "bithumb", profit: 100, startDate:new Date(),strategy: "bollingerPatternNaked"},
]


class NowTrading extends Component {
    constructor(props) {
       super(props);

        this.state = {
            listJ: new Array()
        };
    
        ntHandle.onopen = (event) => {
            ntHandle.send(this.props.id)
        }
        
        ntHandle.onmessage = (event) => {
            if(event.data != "null"){   
                this.setState(
                    {listJ: JSON.parse(event.data)});
                }
        }
    }
    
    render() {
        return(
            <div >
                <div className = "NowTrading-elementList">
                    <div>
                    {list.map((nt) => {
                        return (<TradingElement id = {this.props.id} name = {nt.name} date = "Jun 14, 2018 10:46:06 AM" 
                            coin = {nt.coin} exchange = {nt.exchange}
                            profit = {nt.profit} strategy = {nt.strategy}
                                 />);
                    })}
                    </div>
                </div>
            </div>
        );
    }

}

export default NowTrading;