import React, { Component } from 'react';
import axios from 'axios';
import { connect } from 'react-redux';

import moment from 'moment';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

import './BackTesting.css';
import toLeftBtn from '../img/common/pre_btn_01.png';
import toRightBtn from '../img/common/next_btn_01.png';
import onText from '../img/common/on_bg_01.png';
import offText from '../img/common/off_bg_01.png';
import startBtn from '../img/common/btn_08.png';
import calendar from '../img/common/calendar_01.png';

const hourList = []
for(var i=1;i<=24;i++) hourList.push(i-1) 

const periodLimit = [ "1일", "1주일", "15일", "3개월", "3개월", "3개월" ]

// 구매, 판매 설정 리스트
const buyingSetting = [ {key: '전액구매', value: 'buyAll'}, {key: '금액구매', value: 'buyCertainPrice'}, {key: '개수구매', value: 'buyCertainNum'} ]
const sellingSetting = [ {key: '전액판매', value: 'sellAll'}, {key: '금액판매', value: 'sellCertainPrice'}, {key: '개수판매', value: 'sellCertainNum'} ]

class BackTesting extends Component {
  constructor(){
    super();
    this.state={
      exchangeIndex: 0,
      baseIndex: 0,
      startDay: '',
      endDay: '',
      nowCash:'',
      
      buyDetail: false,
      sellDetail: false,
      buyUnit:'',
      sellUnit:'',
        
      pageNum:1,  // 현재 선택된 페이지 번호
      pageNumList: [1], // 게시물의 전체 페이지 리스트

      showList: [],
      resultList: [],
      result: {}
    }
  }

  // 현재 페이지에서 새로고침을 위해 메뉴를 다시 눌렀을 경우
  componentWillReceiveProps (nextProps) {
    (this.props.location.key !== nextProps.location.key)
    && (window.location = "/backtesting")
  }

  // stateRefresh = () => {
  //   this.setState({
  //     exchangeIndex: 0,
  //     baseIndex: 0,
  //     startDay: '',
  //     endDay: '',
  //     nowCash:'',
      
  //     buyDetail: false,
  //     sellDetail: false,
  //     buyUnit:'',
  //     sellUnit:'',
      
  //     pageNum:1,  // 현재 선택된 페이지 번호
  //     pageNumList: [1], // 게시물의 전체 페이지 리스트

  //     resultList: [],
  //     showList: [],
  //     result: {}
  //   })

  //   document.getElementById('bt_exchange').selectedIndex = 0
  //   document.getElementById('base').selectedIndex = 0
  //   document.getElementById('coin').selectedIndex = 0
  //   document.getElementById('interval').selectedIndex = 0
  //   document.getElementById('buyingSetting').selectedIndex = 0
  //   document.getElementById('sellingSetting').selectedIndex = 0
  //   document.getElementById('startHour').selectedIndex = 0
  //   document.getElementById('endHour').selectedIndex = 0
  //   document.getElementById('buyingDetail').value = ''
  //   document.getElementById('sellingDetail').value = ''
  // }

  handleIndex = (e) => {
    if (e.target.id === 'bt_exchange'){
      document.getElementById('base').selectedIndex = 0
      document.getElementById('coin').selectedIndex = 0
    } else if(e.target.id === 'base') document.getElementById('coin').selectedIndex = 0
    this.setState({
      exchangeIndex: document.getElementById('bt_exchange').selectedIndex,
      baseIndex: document.getElementById('base').selectedIndex
    })
  }

  handleSetting = (e) => {
    switch(e.target.value){
      case '전액구매':
        return( this.setState({ buyDetail: false }) )
      case '금액구매':
        return( this.setState({ buyDetail: true, buyUnit: '원' }) )
      case '개수구매':
        return( this.setState({ buyDetail: true, buyUnit: '개' }) )
      case '전액판매':
        return( this.setState({ sellDetail: false }) )
      case '금액판매':
        return( this.setState({ sellDetail: true, sellUnit: '원' }) )
      case '개수판매':
        return( this.setState({ sellDetail: true, sellUnit: '개' }) )
      default: break
    }
  }

  handleDayChange = (day, se) => {
    se === 'start'
    ? this.setState({ startDay: day })
    : this.setState({ endDay: day })
  }

  handleCash = (e) => {
    !isNaN(e.target.value) && this.setState({
      nowCash:e.target.value
      // nowCash:Number(e.target.value).toLocaleString('en')  // 3자리마다 , 찍는건데 숫자 형식이 아니라 안됨
    })
  }

  dateValidate = (sD, eD) =>{
    var diff = new Date(eD) - new Date(sD)
    switch(document.getElementById('interval').value) {
      case "5분":
        if( diff > 0 && diff < 1*24*60*60*1000 )
          return true
        else return false
      case "30분":
        if( diff > 0 && diff < 7*24*60*60*1000 )
          return true
        else return false
      case "1시간":
        if( diff > 0 && diff < 15*24*60*60*1000 )
          return true
        else return false
      default:
        if( diff > 0 && diff < 90*24*60*60*1000 )
          return true
        else return false
    }
  }

  // 데이터 유효성 검증
  validate = () => {
    // 거래소 검증
    if(document.getElementById('bt_exchange').value === '거래소'){
      alert('거래소를 설정해주세요')
      return false
    }
    // 기축통화 검증
    if(document.getElementById('base').value === '기축통화'){
      alert('기축통화를 설정해주세요')
      return false
    }
    // 코인 검증
    if(document.getElementById('coin').value === '코인'){
      alert('거래할 코인을 설정해주세요')
      return false
    }
    // 거래 간격 검증
    if(document.getElementById('interval').value === '거래 간격'){
      alert('거래 간격을 설정해주세요')
      return false
    }
    // 전략 검증
    if(document.getElementById('strategy').value === '전략'){
      alert('전략을 설정해주세요')
      return false
    }
    // 구매 방식 검증
    if(document.getElementById('buyingSetting').value === '구매 설정'){
      alert('구매 방식을 설정해주세요')
      return false
    }
    // 세부 구매 설정 검증
    if(document.getElementById('buyingDetail').value === '' && document.getElementById('buyingSetting').value !== '전액구매'){
      alert('구매할 금액(수량)을 설정해주세요')
      return false
    }
    // 판매 방식 검증
    if(document.getElementById('sellingSetting').value === '판매 설정'){
      alert('판매 방식을 설정해주세요')
      return false
    }
    // 세부 판매 설정 검증
    if(document.getElementById('sellingDetail').value === '' && document.getElementById('sellingSetting').value !== '전액판매'){
      alert('판매할 금액(수량)을 설정해주세요')
      return false
    }
    if(document.getElementById('nowCash').value === '') {
      alert('시작 금액을 입력하세요')
      return false
    }
    return true
  }

  handleStartbtn = () => {
    if(this.validate()){
      const { startDay, endDay } = this.state
      var startDate = startDay.format('YYYY-MM-DDT')+
        ("0"+document.getElementById('startHour').value.slice(0,-1)).slice(-2)+':00:00.000'
      var endDate = endDay.format('YYYY-MM-DDT')+
        ("0"+document.getElementById('endHour').value.slice(0,-1)).slice(-2)+':00:00.000'
  
      if(this.dateValidate(startDate, endDate)){
        axios.post( 
          'BackTest', 
          'exchange='+document.getElementById('bt_exchange').value+
          '&coin='+document.getElementById('coin').value+
          '&base='+document.getElementById('base').value+ 
          '&interval='+this.props.intervalList[document.getElementById('interval').selectedIndex].value+
          '&strategyName='+document.getElementById('strategy').value+
          '&buyingSetting='+buyingSetting[document.getElementById('buyingSetting').selectedIndex].value+
          '&sellingSetting='+sellingSetting[document.getElementById('sellingSetting').selectedIndex].value+
          '&buyingDetail='+document.getElementById('buyingDetail').value+
          '&sellingDetail='+document.getElementById('sellingDetail').value+
          '&startDate='+startDate+
          '&endDate='+endDate+
          '&nowCash='+document.getElementById('nowCash').value,
          { 'Content-Type': 'application/x-www-form-urlencoded' }
        )
        .then( response => {
          // 세션 검증
          if(response.data === 'sessionExpired') this.sessionExpired()
          // 백테스트 성공 여부
          else if(response.data.status === '성공'){
            var pNL = [1]  // state에 저장할 페이지리스트 생성
            for(var i = 2; i <= (response.data.log.length-1)/10+1; i++){
              pNL.push(i)
            }
            this.setState({
              resultList: response.data.log,
              result: response.data.result,
              pageNumList: pNL,
              pageNum: 1,
              showList: response.data.log.slice(0, 10),
              isResulted: true
            })
            alert('백테스팅에 성공하였습니다.')
          } else alert('백테스팅에 실패하였습니다. (' + response.data + ')')
        }) 
        .catch( response => { console.log('err\n'+response); } ); // ERROR
      } else alert(
        document.getElementById('interval').value +
        ' 간격의 거래는 ' +
        periodLimit[document.getElementById('interval').selectedIndex] +
        ' 이내의 기간으로 설정해주세요.')
    }
  }

  // 세션 유효성 검증
  sessionExpired = () => {
    alert('세션이 종료되었습니다\n다시 로그인하세요')
    window.location = '/'
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
    
    this.setState({
      pageNum: pn,
      showList: this.state.resultList.slice((pn -1)*10, (pn -1)*10+10)
    })
  }

  render() {
    const { exchangeList, intervalList , strategyList } = this.props
    const { exchangeIndex, baseIndex, isResulted, pageNum, pageNumList, showList, result } = this.state
    
    const onTextBg = {
      backgroundImage : `url(${onText})`,
    }

    const offTextBg = {
      backgroundImage : `url(${offText})`,
    }

    return (
      <div>
        {/* 거래 설정 영역 */}
        <div className = 'bt-settingContainer'>          
          <div className = 'bt-btSettingText'>백테스팅 설정</div>
            {/* 거래소 선택 */}
            <select className="bt-select" id="bt_exchange" placeholder="거래소" onChange={this.handleIndex}>
              {exchangeList.map((exchange, index) => {
                return (<option key={index} > {exchange.key} </option>)
              })}
              <option selected hidden disabled>거래소</option>
            </select><br/>
            {/* 기축통화 선택 */}
            <select className="bt-select" id="base" placeholder="기축통화" onChange={this.handleIndex}>
              {exchangeList[exchangeIndex].value.baseList.map((base, i) => {
                return (<option key={i}> {base} </option>)
              })}
              <option selected hidden disabled>기축통화</option>
            </select><br/>
            {/* 코인 선택 */}
            <select className="bt-select" id="coin" placeholder="코인">
              {exchangeList[exchangeIndex].value.coin[baseIndex].list.map((coin, i) => {
                return (<option key={i}> {coin} </option>)
              })}
              <option selected hidden disabled>코인</option>
            </select>
            {/* 거래 간격 선택 */}
            <select className="bt-select" id="interval" placeholder="거래 간격">
              {intervalList.map((int, i) => {
                return (<option key={i}> {int.key} </option>)
              })}
              <option selected hidden disabled>거래 간격</option>
            </select>
            {/* 전략 선택 */}
            <select className="bt-select" id="strategy" placeholder="전략">
              <option selected hidden disabled>전략</option>
              {strategyList.map((s, i) => {
                return (<option key={i}> {s.name} </option>)
              })}
              <option selected hidden disabled>전략</option>
            </select>
            <div class="bt-input">
              {/* 구매 설정 선택 */}
              <select className="bt-select" id="buyingSetting" onChange={this.handleSetting} className = "bt-select">
                {buyingSetting.map((b, i) => { return (<option key={i}> {b.key} </option>) })}
                <option selected hidden disabled>구매 설정</option>
              </select>
              <input id="buyingDetail" className = "bt-input-buySetting" hidden={!this.state.buyDetail}/>{this.state.buyDetail && this.state.buyUnit}
              {/* 판매 설정 선택 */}
              <select className="bt-select" id="sellingSetting" onChange={this.handleSetting} className = "bt-select">
                {sellingSetting.map((s, i) => { return (<option key={i}> {s.key} </option>) })}
                <option selected hidden disabled>판매 설정</option>
              </select>
              <input id="sellingDetail" className = "bt-input-sellSetting" hidden={!this.state.sellDetail}/>{this.state.sellDetail && this.state.sellUnit}
              {/* 시작일 선택 */}
              <div style = {{ maringTop : "12px", height : "42px"}}>
                <div style = {{ position:"absolute", borderBottom : "1px solid #9646a0", width : "81px", float : "left", marginLeft : "20px", marginTop : "4px"}}>
                  <DatePicker
                    selected={this.state.startDay}
                    onChange={(day) => this.handleDayChange(day, 'start')}
                    customInput={
                      <input 
                        style={{ 
                          width: '80px',
                          marginTop : "20px",
                          borderTop : 'transparent',
                          borderLeft : 'transparent',
                          borderRight : 'transparent',
                          borderBottom : 'transparent'}}
                        onClick={this.props.onClick}>
                        {this.props.value}
                      </input>
                    }
                    fixedHeight
                    placeholderText = "시작일"
                    dateFormat="YYYY/M/D"
                    maxDate={moment()}/>
                  <img src = {calendar} style = {{position : "absolute", top : '20px', left : '63px'}}/>
                </div>
                {/* 시작 시간 선택 */}
                <select className = "bt-select-hour" id="startHour">
                  {hourList.map((e, i) => {
                    return (<option key={i}> {e}시 </option>)
                  })}
                </select>
              </div>
              {/* 종료일 선택 */}
              <div style = {{ maringTop : "12px", height : "42px"}}>
                <div style = {{ position:"absolute", borderBottom : "1px solid #9646a0", width : "81px", float : "left", marginLeft : "20px", marginTop : "4px"}}>
                  <DatePicker
                    selected={this.state.endDay}
                    onChange={(day) => this.handleDayChange(day, 'end')}
                    customInput={
                      <input 
                        style={{
                          width: '80px',
                          marginTop : "20px",
                          borderTop : 'transparent',
                          borderLeft : 'transparent',
                          borderRight : 'transparent',
                          borderBottom : 'transparent'}}
                        onClick={this.props.onClick}>
                        {this.props.value}
                      </input>
                    }
                    fixedHeight
                    placeholderText = "종료일"
                    dateFormat="YYYY/M/D"
                    minDate={this.state.startDay}
                    maxDate={moment()}/>
                  <img src = {calendar} style = {{position : "absolute", top : '20px', left : '63px'}}/>
                </div>
                {/* 종료 시간 선택 */}
                <select className = "bt-select-hour" id="endHour">
                  {hourList.map((e, i) => {
                    return (<option key={i}> {e}시 </option>)
                  })}
                </select>
                {/* 시작 금액 선택 */}
                <input id="nowCash" placeholder="시작 금액을 입력하세요"  className = 'bt-startPrice' value={this.state.nowCash} onChange={this.handleCash}/><br/>
              </div>       
            </div>            
          {/* 시작 버튼 */}
          <div className = 'bt-start-btn' onClick={this.handleStartbtn}><img src = {startBtn}/></div>
        </div>

        {/* 거래 결과 기록 영역 */}
        <div className = 'bt-resultLogContainer'>
          <div>
            <div className = "bt-botIndivText" >
              매매 기록<text style={{color: "grey", fontSize : "12px", marginLeft: "765px"}}>대기는 기록되지 않습니다</text>
            </div>
            <table className='bt-tableContainer' >
              <thead>
                {/* <th className='bt-headTr'>성공 여부</th> */}
                <th className='bt-headTr'>시간</th>
                <th className='bt-headTr'>매매</th>
                <th className='bt-headTr'>가격<small>{result.base !== (null || undefined) && (result.base)}</small></th>
                <th className='bt-headTr'>코인 매매 수량<small>(개)</small></th>
                <th className='bt-headTr'>현재 보유 현금<small>(KRW)</small></th>
                <th className='bt-headTr'>현재 보유 코인수<small>(개)</small></th>
              </thead>

              <tbody className = 'bt-tbodyContainer' >              
                { // state에 저장된 게시물 리스트를 map 함수 통하여 표시
                showList.map((r, i) => {
                  return (<tr key={i} className="bt-tr" style={{borderBottom : "1px solid"}} >
                    {/* <td className = 'bt-td'>{r.success}</td> */}
                    <td className = 'bt-td'>{r.time}</td>
                    <td className = 'bt-td'>{r.saleAction}</td>
                    <td className = 'bt-td'>{r.coinCurrentPrice}</td>
                    <td className = 'bt-td'>{r.salingCoinNumber}</td>
                    <td className = 'bt-td'>{r.nowCash}</td>
                    <td className = 'bt-td'>{r.nowCoin}</td>
                  </tr>)
                })}
              </tbody>
            </table>

            <div className = "bt-chooseBoxContainer">
              { /* 이전 10 페이지 이동 버튼*/ }
              <div className = "bt-chooseLeft" onClick={() => this.selectPage('front')}> <img src = {toLeftBtn}/> </div>
              { // 현재 선택된 페이지의 근처 10개 페이지 표시
              pageNumList.slice(pageNum -(pageNum-1)%10 -1, pageNum -(pageNum-1)%10 +9).map((p, i) => {
                return(<div  key ={i} onClick={() => this.selectPage(p)}>
                  {pageNum === p ? <div style={onTextBg} className = "bt-chooseNumberSelected" >  {p}  </div> : <div style={offTextBg} className = "bt-chooseNumber" >  {p}  </div>}
                </div>)
              })}
              { /* 이후 10 페이지 이동 버튼*/ }
              <div className = "bt-chooseRight" onClick={() => this.selectPage('back')}><img src = {toRightBtn}/></div>
            </div>
          </div>
        </div>

        {/* 거래 결과 영역 */}
        <div className = 'bt-resultWindow'>
          <div style={{marginTop : '24px', marginLeft : '20px', fontSize : "24px", fontWeight:"bold", borderRight : '1px solid #9646a0', width : '172px', display: "inline-block"}}>
            백테스팅 결과
          </div>
          { isResulted
            && <div style={{display: "inline-block", marginLeft : '20px'}}>
                최종 수익률 : {result.finalProfit} % &nbsp; &nbsp; &nbsp; 최종 금액 : {result.finalAsset} KRW
              </div>
          }
        </div>
      </div>
    );
  }
}

let mapStateToProps = (state) => {
  return {
    strategyList: state.strategy.strategyList,
    exchangeList: state.exchange.exchangeList,
    intervalList: state.exchange.intervalList
  };
}

BackTesting = connect(mapStateToProps)(BackTesting);

export default BackTesting;