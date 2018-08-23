import React,{Component} from 'react';
import './WalletInfo.css';


// SELECT API_KEY, SECRET_KEY, exchange FROM Exchange
// or
// SELECT API_KEY exchange, Hashed_KEY FROM Exchange
const exchange_registered = [
    {
        name : 'BITTREX',
        API_KEY : '485f69323ae844f99f2ef3ae81692a1e',
        SECRET_KEY : '3289895f108b435e8a4633df2b5cdf61',
        Hashed_KEY : ''
    },
    {
        name : 'COINONE',
        API_KEY : '',
        SECRET_KEY : '',
        Hashed_KEY : ''
    },
    {
        name : 'BITHUMB',
        API_KEY : '',
        SECRET_KEY : '',
        Hashed_KEY : ''
    },
]

const bittrex = require('../node.bittrex.api');
bittrex.options({ 
  'apikey' : exchange_registered[0].API_KEY, 
  'apisecret' : exchange_registered[0].SECRET_KEY, 
  'stream' : false, 
  'verbose' : false, 
  'cleartext' : false 
});

class WalletInfo extends Component{

    state = {
        exchange : "none",
        content : "please, select exchange"
    }

    callWalletAPI = (selected_exchange) => {
        const self = this;

        // 파라미터를 받아서 함수를 선택하는법?
        // coinone을 받으면 coinone()을 실행하는법 -> switch밖에없나?
        // 혹은 함수를 배열에 넣어서 인덱스로 선택?
        // 지금은 비트렉스만 있어서 이것만..
        bittrex.getbalances(function(data, err){
            if(err){
                console.log("error occurs : " + JSON.stringify(err));
                alert("error occurs : " + JSON.stringify(err.message));
            }
            else{
                console.log(data.success);
                self.setState({
                    exchange : selected_exchange,
                    content : "결과 : " + JSON.stringify(data.success) + "   ->   돈 : " + JSON.stringify(data.result)
                });
            }
        });

        // coinone.getbalances
        // bitthumb.getbalances
        // 등등
    }

    handleExchangeSelect = () => {
        // 여기서는 텍스트를 넘겨줌. 셀렉트박스의 텍스트를 그대로 callWalletAPI로 toss
        let e = document.getElementById("WI_exchangeSelectbox");
        let selected_exchange = e.options[e.selectedIndex].text;
        console.log("selected : " + selected_exchange);

        this.callWalletAPI(selected_exchange);
    }


    render() {
        return(
            <div>
                <div>
                    WalletInfo
                </div>
                <div>
                <select className = "WalletInfo-exchangeSelectbox" id="WI_exchangeSelectbox" size = '1' onChange = {this.handleExchangeSelect} 
                placeholder={'Select something'}> 
                <option selected hidden disabled>Choose exchange</option>
                {exchange_registered.map((exchange, i) => {
                    return (<option key = {i}>{exchange.name}</option>)
                })}
                </select>
                </div>
                <div style={{marginTop : '10px'}}>{this.state.exchange}<br/>{this.state.content}</div>
                


            </div>

        );
    }

}

export default WalletInfo;