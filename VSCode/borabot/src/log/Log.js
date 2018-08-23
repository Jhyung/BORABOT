import React, { Component } from 'react';
import axios from 'axios';

import './log.css';
import mainBackground from '../img/sign/sign_bg_01.png';
import toLeftBtn from '../img/common/pre_btn_01.png';
import toRightBtn from '../img/common/next_btn_01.png';
import onText from '../img/common/on_bg_01.png';
import offText from '../img/common/off_bg_01.png';

class Log extends Component {
  constructor(){
    super();
    this.state={

      selectedTrade: {},
      selectedSalesAction: {},
      tradeList: [],
      logList: [], // 현재 선택된 페이지의 10개의 로그 리스트

      // 앞단 테스트용 ==================================================================================================================== //
      // tradeList: [{"exchange_name":"bithumb","bot_name":"asdfasdf","coin":"btckrw"},{"exchange_name":"bithumb","bot_name":"qweteaw","coin":"btckrw"},{"exchange_name":"bithumb","bot_name":"tqtqtqtq","coin":"btckrw"},{"exchange_name":"bithumb","bot_name":"xcbaqwesadg","coin":"btckrw"},{"exchange_name":"bithumb","bot_name":"zxbwefa","coin":"btckrw"},{"exchange_name":"bithumb","bot_name":"zxbwefaxzc","coin":"btckrw"},{"exchange_name":"bithumb","bot_name":"ㅋㅌ츚ㅂㄷㅅㅁㄴㅇㄹ","coin":"btckrw"}],
      // logList: [{"now_coin_number":"0.0","coin_intent":"0.0","now_balance":"0.0","trans_time":"2018-07-30 17:42:14","coin_price":"9175000.0","sales_action":"1"},{"now_coin_number":"0.0","coin_intent":"0.0","now_balance":"0.0","trans_time":"2018-07-30 17:47:12","coin_price":"9153000.0","sales_action":"1"}],
      // ================================================================================================================================ //

      pageNum:1,  // 현재 선택된 페이지 번호
      pageNumList: [1] // 게시물의 전체 페이지 리스트
    }
  }

  componentDidMount() {
    axios.get( 'Log' )
    .then( response => {
      if(response.data === 'sessionExpired') this.sessionExpired()
      else{
        this.setState({
          tradeList: response.data,
          pageNumList: [1]
        })
        // 거래 현황에서 거래 기록을 눌렀을 경우
        this.props.location.bot_name !== undefined
        && response.data.map((t, i) => {
          if(t.bot_name === this.props.location.bot_name){
            this.setState({
              selectedTrade: t
            })
            this.getLog(t.bot_name, 1, '매수/매도')
            document.getElementById('botName').selectedIndex=i+1
          }})
      }
    }) 
    .catch( response => { 
      console.log('err\n'+response); 
    }); // ERROR
  }

  // 세션 유효성 검증
  sessionExpired = () => {
    alert('세션이 종료되었습니다\n다시 로그인하세요')
    window.location = '/'
  }

  // 현재 페이지에서 새로고침을 위해 메뉴를 다시 눌렀을 경우
  componentWillReceiveProps (nextProps) {
    (this.props.location.key !== nextProps.location.key) 
    && (window.location = "/log")
  }

  handleChange = (e) => {
    const { tradeList } = this.state
    if(e.target.id === 'botName'){  // 봇 이름 선택일 때
      // 기본 인덱스 0일 때
      if ((document.getElementById('botName').selectedIndex) === 0){
        this.setState({
          selectedTrade: {},
          logList: []
         })
      } else{ // 봇을 선택했을 때
        this.setState({ selectedTrade: tradeList[document.getElementById('botName').selectedIndex-1] })
        this.getLog(
          tradeList[document.getElementById('botName').selectedIndex-1].bot_name,
          1,
          '매수/매도'
        )
      }
    } else {  // 매수/매도 선택일 
      document.getElementById('botName').selectedIndex !== 0
      && this.getLog(
          tradeList[document.getElementById('botName').selectedIndex-1].bot_name,
          1,
          document.getElementById('salesAction').value
        )
    }
  }

  getLog = (bn, pN, sa) => {
    axios.post('Log', 
      'bot_name='+bn+
      '&pageNum='+pN+
      '&sales_action='+sa,
      { 'Content-Type': 'application/x-www-form-urlencoded' }
    )
    .then( response => {
      if(response.data === 'sessionExpired') this.sessionExpired()
      else{
        var pNL = [1]  // state에 저장할 페이지리스트 생성
        for(var i = 2; i <= (response.data.count-1)/10+1; i++){
          pNL.push(i)
        }
        this.setState({
          logList: response.data.logList,
          pageNumList: pNL
        })
      }
    }) 
    .catch( response => { console.log('err\n'+response); } ); // ERROR
  }

  // 페이지를 선택하면 state 변화후 게시물을 새로 불러옴
  selectPage = (fbn) => {
    const { pageNum, pageNumList } = this.state
    var pn = 1  // 서버에 호출할 페이지 번호

    if(fbn === 'front'){
      (pageNum > 10)
      ? pn = pageNum -(pageNum-1)%10 -1
      : pn = 1
    } else if(fbn === 'back'){
      (parseInt(pageNum/10, 10) !== parseInt(pageNumList.length/10, 10))
      ? pn = (pageNum -1) -(pageNum -1)%10 +11
      : pn = pageNumList.length
    } else pn = fbn
    
    this.setState({ pageNum: pn })
    this.getLog(
      this.state.selectedTrade.bot_name,
      pn,
      document.getElementById('salesAction').value
    )
  }

  render() {
    const { tradeList, selectedTrade, logList, pageNum, pageNumList } = this.state
    
    const mainBgStyle = {
      backgroundImage: `url(${mainBackground})`,
    }

    const onTextBg = {
      backgroundImage : `url(${onText})`,
    }

    const offTextBg = {
      backgroundImage : `url(${offText})`,
    }

    return (
      <div style = {mainBgStyle} className = "log-bakcground" > 
        <div className = "log-leftBox">
          <div className = "log-botSelText">봇 선택</div>
          <select id="botName" className = "log-botSelect" onChange={this.handleChange}>
            <option selected hidden disabled>봇 이름</option>
            {tradeList.map((t, i) => {
              return (<option key={i}> {t.bot_name} </option>)
            })}
          </select><br/>
          <select id="salesAction" className = "log-saleSelect" onChange={this.handleChange}>
            <option>매수/매도</option><option>매수</option><option>매도</option>
          </select><br/>
        </div>

        <div className = "log-mainBox">
          <div className = "log-botTextContainer">
            <div className = "log-botIndivText">
              {selectedTrade.bot_name}의 거래 기록 
            </div> 
            <div className = "log-botBackground">
              거래소 : {selectedTrade.exchange_name}
            </div>  
            <div className = "log-botBackground">
              코인 : {selectedTrade.coin}
            </div>  
          </div>
          <table className='log-tableContainer'>
            <thead>
              <th className='log-headTr'>거래 신호 시간</th>
              <th className='log-headTr'>매매 행동</th>
              <th className='log-headTr'>개당 코인 가격</th>
              <th className='log-headTr'>코인 매매 수량<small>(개)</small></th>
              <th className='log-headTr'>현재 보유 현금<small>(KRW)</small></th>
              <th className='log-headTr'>현재 보유 코인수<small>(개)</small></th>
            </thead>

            <tbody className = 'log-tbodyContainer' >              
              { // state에 저장된 게시물 리스트를 map 함수 통하여 표시
              logList.map((l, i) => {
                return (<tr key={i} style={{borderBottom : "1px solid"}} >
                  <td className = 'log-td'>{l.trans_time}</td>
                  <td className = 'log-td'>{l.sales_action === "1" ? ("매수") : ( "매도" )}</td>
                  <td className = 'log-td'>{l.coin_price}</td>
                  <td className = 'log-td'>{l.coin_intent}</td>
                  <td className = 'log-td'>{l.now_balance}</td>
                  <td className = 'log-td'>{l.now_coin_number}</td>
                </tr>)
              })}
            </tbody>
          </table>

          <div className = "log-chooseBoxContainer">
            { /* 이전 10 페이지 이동 버튼*/ }
            <div className = "log-chooseLeft" onClick={() => this.selectPage('front')}> <img src = {toLeftBtn}/> </div>
            { // 현재 선택된 페이지의 근처 10개 페이지 표시
            pageNumList.slice(pageNum -(pageNum-1)%10 -1, pageNum -(pageNum-1)%10 +9).map((p, i) => {
              return(<div  key ={i} onClick={() => this.selectPage(p)}>
                {pageNum === p ? <div style={onTextBg} className = "log-chooseNumberSelected" >  {p}  </div> : <div style={offTextBg} className = "log-chooseNumber" >  {p}  </div>}
              </div>)
            })}
            { /* 이후 10 페이지 이동 버튼*/ }
            <div className = "log-chooseRight" onClick={() => this.selectPage('back')}><img src = {toRightBtn}/></div>
          </div>
        </div>          
      </div>
    );
  }
}

export default Log;

