import React, { Component } from 'react';
import './NowTrading.css';
import TradingElement from './TradingElement';


class NowTrading extends Component {
    constructor(props) {
       super(props);

        this.state = {
            listE: []
        };
    }
    componentDidMount() {
        // 이대로 하면 서버 올렸을 때 origin 같아서 cors 안생김 세션 다른거 상관 ㄴㄴ            
        fetch('http://localhost:8080/BORABOT/NowTrading' // vscode용 + 크롬 cors
        // fetch('NowTrading'              // 서버용
        // , {credentials: 'include'} // 서버용 
        )
        .then(res => res.json())
        .then(
            (result) => {
                this.setState({
                    listE: result
                })
            }
        )
    }

    render() {
        return(
            <div >
            <div className = "NowTrading-elementList">
                {this.state.listE.map((nt, i) => {
                    return (<TradingElement id = {this.props.id} name = {nt.name} endDate = {nt.endDate} 
                        coin = {nt.coin} exchange = {nt.exchange}
                        profit = {nt.profit} strategy = {nt.strategy} key = {i}
                                />);
                })}
            </div>
            </div>
        );
    }

}

export default NowTrading;