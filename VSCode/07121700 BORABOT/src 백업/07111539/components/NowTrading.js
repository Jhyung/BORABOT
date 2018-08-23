import React, { Component } from 'react';
import './NowTrading.css';
import TradingElement from './TradingElement';
// import fetchJsonp from 'fetch-jsonp';
// import axios from 'axios';


// const ntHandle = new WebSocket("ws://localhost:8080/Auth/nthandle");
// const ntHandle = new WebSocket("ws://localhost:8080/BORABOT/nthandle");
// const ntHandle = new WebSocket("ws://45.120.65.65/BORABOT/nthandle");


class NowTrading extends Component {
    constructor(props) {
       super(props);

        this.state = {
            listE: []
        };
    
        // ntHandle.onopen = (event) => {
        //     ntHandle.send(this.props.id)
        // }
        
        // ntHandle.onmessage = (event) => {
        //     if(event.data != "null"){
        //         this.setState(
        //             {listJ: JSON.parse(event.data)});
        //         }
        // }

    }
    componentDidMount() {
        // fetch('http://localhost:8080/BORABOT/NowTrading',{
        //     credentials: 'include',
        //     mode: 'no-cors'
        // }).then((response)=>{console.log(response);
        //     console.log(response.json());
        // }).catch((e) => {console.log(e);})

        // 이대로 하면 서버 올렸을 때 origin 같아서 cors 안생김 세션 다른거 상관 ㄴㄴ
        fetch('http://localhost:8080/BORABOT/NowTrading',
        // {credentials: 'include'}
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