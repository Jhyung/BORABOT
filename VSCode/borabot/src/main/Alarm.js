import React, {Component} from 'react';
import axios from 'axios';

import './Alarm.css'

class Alarm extends Component {
  constructor(props) {
    super(props);

    this.state = {
      alarmList: [
        // 앞단 테스트용
        // {"exchange_name":"binance","coin":"ETHbtc","sales_action":"-1","coin_intent":"0.0","coin_price":"313500.0","trans_time":"2018-08-14 18:51:01"},
        // {"exchange_name":"bitthumb","coin":"BTCeth","sales_action":"1","coin_intent":"0.0","coin_price":"0.043949","trans_time":"2018-08-14 18:50:43"},
        // {"exchange_name":"binance","coin":"ETHbtc","sales_action":"-1","coin_intent":"0.0","coin_price":"313500.0","trans_time":"2018-08-14 18:51:01"},
        // {"exchange_name":"bitthumb","coin":"BTCeth","sales_action":"1","coin_intent":"0.0","coin_price":"0.043949","trans_time":"2018-08-14 18:50:43"},
        // {"exchange_name":"bitthumb","coin":"ETHbtc","sales_action":"-1","coin_intent":"0.0","coin_price":"313500.0","trans_time":"2018-08-14 18:51:01"},
        // {"exchange_name":"binance","coin":"BTCeth","sales_action":"1","coin_intent":"0.0","coin_price":"0.043949","trans_time":"2018-08-14 18:50:43"}
        // 앞단 테스트용
      ]
    };
  }

  componentDidMount() { 
    axios.get('Alarm')
    .then( response => {
      response.data === 'sessionExpired'
      ? this.sessionExpired()
      : this.setState({
          alarmList: response.data
        })
    }) 
    .catch( response => { console.log('err\n'+response); } ); // ERROR
    // this.props.handleAlarm()
  }

  // 세션 유효성 검증
  sessionExpired = () => {
    alert('세션이 종료되었습니다\n다시 로그인하세요')
    window.location = '/'
  }

  render() {
    return (
      <div>
        <div class="alarm-title">
          <th>
            <h2>&emsp;BORABOT 거래 알람</h2>
          </th>
          <th class="alarm-tableButton">
            <img onClick={()=>{this.props.close()}} src={require('../img/common/btn_06.png')} />
          </th>
        </div>
        <table class="alarm-table">
          <thead class="alarm-tableTitles" style={{width:'100%'}}>
            <th class="alarm-tableTitle1">거래소</th>
            <th class="alarm-tableTitle2">코인/기축통화</th>
            <th class="alarm-tableTitle3">매매</th>
            <th class="alarm-tableTitle4">수량</th>
            <th class="alarm-tableTitle5">가격</th>
            <th class="alarm-tableTitle6">시간</th>
          </thead>
          <tbody class="alarm-tableContents" style={{width:'100%'}}>
              { // state에 저장된 게시물 리스트를 map 함수 통하여 표시
              this.state.alarmList.map((l, i) => {
                return (<tr key={i} class="alarm-tableContent">
                  <td class="alarm-tableContent1">{l.exchange_name}</td>
                  <td class="alarm-tableContent2">{l.coin}</td>
                  <td class="alarm-tableContent3">{l.sales_action === "1" ? ("매수") : ( "매도" )}</td>
                  <td class="alarm-tableContent4">{l.coin_intent}</td>
                  <td class="alarm-tableContent5">{l.coin_price}</td>
                  <td class="alarm-tableContent6">{l.trans_time}</td>
                </tr>)
              })}
          </tbody>
        </table>
      </div>
    );
   }
}

export default Alarm;


// import React, {Component} from 'react';
// import axios from 'axios';

// // import './Alarm.css'

// class Alarm extends Component {
//   constructor(props) {
//     super(props);

//     this.state = {
//       // alarmList: []
//       // 앞단 테스트용
//       alarmList: [{"exchange_name":"binance","coin_intent":"0.0","trans_time":"2018-08-14 18:51:01","coin_price":"313500.0","sales_action":"-1","coin":"ETHbtc"},{"exchange_name":"binance","coin_intent":"0.0","trans_time":"2018-08-14 18:50:43","coin_price":"0.043949","sales_action":"1","coin":"ETHbtc"},{"exchange_name":"binance","coin_intent":"0.0","trans_time":"2018-08-14 18:51:01","coin_price":"313500.0","sales_action":"-1","coin":"ETHbtc"},{"exchange_name":"binance","coin_intent":"0.0","trans_time":"2018-08-14 18:50:43","coin_price":"0.043949","sales_action":"1","coin":"ETHbtc"},{"exchange_name":"binance","coin_intent":"0.0","trans_time":"2018-08-14 18:51:01","coin_price":"313500.0","sales_action":"-1","coin":"ETHbtc"},{"exchange_name":"binance","coin_intent":"0.0","trans_time":"2018-08-14 18:50:43","coin_price":"0.043949","sales_action":"1","coin":"ETHbtc"}]
//     };
//   }

//   componentDidMount() {    
//     axios.get('Alarm')
//     .then( response => {
//       this.setState({
//         alarmList: response.data
//       })
//     }) 
//     .catch( response => { console.log('err\n'+response); } ); // ERROR
//     // this.props.handleAlarm()
//   }

//   render() {
//     return (
//       <table>
//         <thead>
//           <th>거래소</th><th>코인</th><th>매매</th><th>수량</th><th>가격</th><th>시간</th>
//         </thead>
//         <tbody>
//             { // state에 저장된 게시물 리스트를 map 함수 통하여 표시
//             this.state.alarmList.map((l, i) => {
//               return (<tr key={i}>
//                 <td>{l.exchange_name}</td>
//                 <td>{l.coin}</td>
//                 <td>{l.sales_action === "1" ? ("매수") : ( "매도" )}</td>
//                 <td>{l.coin_intent}</td>
//                 <td>{l.coin_price}</td>
//                 <td>{l.trans_time}</td>
//               </tr>)
//             })}
//         </tbody>
//       </table>
//     );
// 	}
// }

// export default Alarm;