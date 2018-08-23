import React, { Component } from 'react';
import axios from 'axios';
import { connect } from 'react-redux';

import { setStrategy } from '../reducers/strategy';

import './Strategy.css';
import { isNullOrUndefined, isUndefined } from 'util';

var RSI = { indicator:'RSI', weight:1, period:14, buyIndex:30, sellIndex:70 }
var BollingerBand = { indicator:'BollingerBand', weight:1, period:20, mul:2 }
var CCI = { indicator:'CCI', weight:1, period:20, buyIndex:-100, sellIndex:100 }
var gdCross = { indicator:'gdCross', weight:1, longD:26, shortD:9, mT:1 }
var gdVCross = { indicator:'gdVCross', weight:1, longD:26, shortD:9, mT:1}
var MFI = { indicator:'MFI', weight:1, period:14, buyIndex:0, sellIndex:0 }
var StochOsc = { indicator:'StochOsc', weight:1, n:15, m:5, t:3 }
var VolumeRatio = { indicator:'VolumeRatio', weight:1, period:20, buyIndex:70, sellIndex:350 }
// var pCorr = { indicator:'pCorr', weight:1, period:15, cor:0 }

// 지표 기본값
var defaultRSI = { indicator:'RSI', weight:1, period:14, buyIndex:30, sellIndex:70 }
var defaultBollingerBand = { indicator:'BollingerBand', weight:1, period:20, mul:2 }
var defaultCCI = { indicator:'CCI', weight:1, period:20, buyIndex:-100, sellIndex:100 }
var defaultgdCross = { indicator:'gdCross', weight:1, longD:26, shortD:9, mT:1 }
var defaultgdVCross = { indicator:'gdVCross', weight:1, longD:26, shortD:9, mT:1}
var defaultMFI = { indicator:'MFI', weight:1, period:14, buyIndex:0, sellIndex:0 }
var defaultStochOsc = { indicator:'StochOsc', weight:1, n:15, m:5, t:3 }
var defaultVolumeRatio = { indicator:'VolumeRatio', weight:1, period:20, buyIndex:70, sellIndex:350 }
// var defaultpCorr = { indicator:'pCorr', weight:1, period:15, cor:0 }

var indicatorList = [RSI, BollingerBand, CCI, gdCross, gdVCross, MFI, StochOsc, VolumeRatio]
var defaultIndicatorList = [defaultRSI, defaultBollingerBand, defaultCCI, defaultgdCross, defaultgdVCross, defaultMFI, defaultStochOsc, defaultVolumeRatio]


class Strategy extends Component {
  constructor(){
    super();
    this.state={
      strategy_name: '',
      selectedIndicator: RSI, // 설정된 지표
      defaultIndicator: defaultRSI, // 설정된 지표의 기본값
      calculate: 'or', // 연산 방법
      buyC: '', // 구매 기준
      sellC: '',  // 판매 기준

      indicatorList: [],  // 설정된 지표 리스트
      expList: [],  // 지표 연산 방법 리스트
      savedCnt: 0,  // 설정된 지표 개수
      jsonString:'', // 서버에 보내기 위한 json

      selectedStrategy:'',
      isLoad: false  // 불러오기이면 true, 새로 만들기이면 false 
    }
  }

  // 현재 페이지에서 새로고침을 위해 메뉴를 다시 눌렀을 경우
  componentWillReceiveProps (nextProps) {
    (this.props.location.key !== nextProps.location.key) 
    && (window.location = "/strategy")
  }

  handleLoad = (e) => {
    // 새로 만들기 선택하면 기존에 만든 것 초기화
    if(e.target.value === '새로 만들기'){
      document.getElementById('strategy_name').value = ''
      this.setState({
        strategy_name: '',
        selectedIndicator: RSI, // 설정된 지표
        defaultIndicator: defaultRSI, // 설정된 지표의 기본값
        calculate: 'or', // 연산 방법
        buyC:'', // 구매 기준
        sellC:'',  // 판매 기준
  
        indicatorList: [],  // 설정된 지표 리스트
        expList: [],  // 지표 연산 방법 리스트
        savedCnt: 0,  // 설정된 지표 개수
        jsonString:'', // 서버에 보내기 위한 json
        isLoad: false
      })
    } else{
      document.getElementById('strategy_name').value = e.target.value
      this.setState({
        isLoad: true
      })
      this.props.strategyList.map((s) => {
        if(e.target.value === s.name) {
          this.setState({
            selectedStrategy: JSON.parse(s.data),
            buyC: JSON.parse(s.data).buyCriteria,
            sellC: JSON.parse(s.data).sellCriteria
          })
        }
      })
    }
  }

  handleCriteria = (e, bs) => {
    if(!isNaN(e.target.value) || e.target.value === '-'){
      (bs === 'buy')
        ? this.setState({
          buyC:e.target.value
        })
        : this.setState({
          sellC:e.target.value
        })
    }
  }

  // 지표 select box 변화에 따른 현재 선택된 지표와 기본값 state 변화
  handleIndicator = (e) => {
    indicatorList.map((idc) => {
      if(idc.indicator === e.target.value){
        this.setState({
          selectedIndicator: idc
        })
      }
    })
    defaultIndicatorList.map((didc) => {
      if(didc.indicator === e.target.value){
        this.setState({
          defaultIndicator: didc
        })
      }
    })
    this.initInput()
  }

  // 설정한 지표 저장 및 리스트, json 저장
  handleSave = (e) => {
    const { selectedIndicator, jsonString, savedCnt, expList, defaultIndicator, calculate } = this.state

    // 현재 설정된 지표 세부 설정 저장 (공백이면 기본값)
    if(document.getElementById('weight').value === '') selectedIndicator.weight = defaultIndicator.weight
    else if(!Number.isInteger(parseInt(document.getElementById('weight').value))){  // 정수 판별
      alert('정수를 입력하세요')
      return
    } else selectedIndicator.weight = document.getElementById('weight').value

    if(selectedIndicator.period === undefined) {} // 선택된 지표에 해당 값이 없으면 건너뜀
    else if(document.getElementById('period').value === '') selectedIndicator.period = defaultIndicator.period
    else if(!Number.isInteger(parseInt(document.getElementById('period').value))){
      alert('정수를 입력하세요')
      return
    } else selectedIndicator.period = document.getElementById('period').value

    if(selectedIndicator.buyIndex === undefined) {}
    else if(document.getElementById('buyIndex').value === '') selectedIndicator.buyIndex = defaultIndicator.buyIndex
    else if(!Number.isInteger(parseInt(document.getElementById('buyIndex').value))){
      alert('정수를 입력하세요')
      return
    } else selectedIndicator.buyIndex = document.getElementById('buyIndex').value

    if(selectedIndicator.sellIndex === undefined) {}
    else if(document.getElementById('sellIndex').value === '') selectedIndicator.sellIndex = defaultIndicator.sellIndex
    else if(!Number.isInteger(parseInt(document.getElementById('sellIndex').value))){
      alert('정수를 입력하세요')
      return
    } else selectedIndicator.sellIndex = document.getElementById('sellIndex').value

    if(selectedIndicator.mul === undefined) {}
    else if(document.getElementById('mul').value === '') selectedIndicator.mul = defaultIndicator.mul
    else if(!Number.isInteger(parseInt(document.getElementById('mul').value))){
      alert('정수를 입력하세요')
      return
    } else selectedIndicator.mul = document.getElementById('mul').value

    if(selectedIndicator.longD === undefined) {}
    else if(document.getElementById('longD').value === '') selectedIndicator.longD = defaultIndicator.longD
    else if(!Number.isInteger(parseInt(document.getElementById('longD').value))){
      alert('정수를 입력하세요')
      return
    } else selectedIndicator.longD = document.getElementById('longD').value

    if(selectedIndicator.shortD === undefined) {}
    else if(document.getElementById('shortD').value === '') selectedIndicator.shortD = defaultIndicator.shortD
    else if(!Number.isInteger(parseInt(document.getElementById('shortD').value))){
      alert('정수를 입력하세요')
      return
    } else selectedIndicator.shortD = document.getElementById('shortD').value

    if(selectedIndicator.mT === undefined) {}
    else if(document.getElementById('mT').value === '') selectedIndicator.mT = defaultIndicator.mT
    else if(!Number.isInteger(parseInt(document.getElementById('mT').value))){
      alert('정수를 입력하세요')
      return
    } else selectedIndicator.mT = document.getElementById('mT').value

    if(selectedIndicator.n === undefined) {}
    else if(document.getElementById('n').value === '') selectedIndicator.n = defaultIndicator.n
    else if(!Number.isInteger(parseInt(document.getElementById('n').value))){
      alert('정수를 입력하세요')
      return
    } else selectedIndicator.n = document.getElementById('n').value

    if(selectedIndicator.m === undefined) {}
    else if(document.getElementById('m').value === '') selectedIndicator.m = defaultIndicator.m
    else if(!Number.isInteger(parseInt(document.getElementById('m').value))){
      alert('정수를 입력하세요')
      return
    } else selectedIndicator.m = document.getElementById('m').value

    if(selectedIndicator.t === undefined) {}
    else if(document.getElementById('t').value === '') selectedIndicator.t = defaultIndicator.t
    else if(!Number.isInteger(parseInt(document.getElementById('t').value))){
      alert('정수를 입력하세요')
      return
    } else selectedIndicator.t = document.getElementById('t').value

    // if(selectedIndicator.cor === undefined) {}
    // else if(document.getElementById('cor').value === '') selectedIndicator.cor = defaultIndicator.cor
    // else selectedIndicator.cor = document.getElementById('cor').value

    var tempJson = jsonString

    if(savedCnt === 0) {
      this.setState({
        isLoad: false,
        indicatorList: indicatorList.concat(selectedIndicator),
        savedCnt: savedCnt + 1,
        jsonString: '"'+savedCnt+'":'+JSON.stringify(selectedIndicator)
      })
    } else {
      this.setState({
        strategy_name: document.getElementById('strategy_name').value,
        indicatorList: indicatorList.concat(selectedIndicator),
        expList: expList.concat(calculate),
        savedCnt: savedCnt + 1,
        jsonString: tempJson+',"'+savedCnt+'":'+JSON.stringify(selectedIndicator)
      })
    }
    this.initInput()
  }

  // 지표 input 초기화
  initInput = () => {
    if(!isNullOrUndefined(document.getElementById('weight'))) document.getElementById('weight').value = null
    if(!isNullOrUndefined(document.getElementById('period'))) document.getElementById('period').value = null
    if(!isNullOrUndefined(document.getElementById('buyIndex'))) document.getElementById('buyIndex').value = null
    if(!isNullOrUndefined(document.getElementById('sellIndex'))) document.getElementById('sellIndex').value = null
    if(!isNullOrUndefined(document.getElementById('mul'))) document.getElementById('mul').value = null
    if(!isNullOrUndefined(document.getElementById('longD'))) document.getElementById('longD').value = null
    if(!isNullOrUndefined(document.getElementById('shortD'))) document.getElementById('shortD').value = null
    if(!isNullOrUndefined(document.getElementById('mT'))) document.getElementById('mT').value = null
    if(!isNullOrUndefined(document.getElementById('n'))) document.getElementById('n').value = null
    if(!isNullOrUndefined(document.getElementById('m'))) document.getElementById('m').value = null
    if(!isNullOrUndefined(document.getElementById('t'))) document.getElementById('t').value = null
  }
  
  handleComplete = () => {
    // 항목 검증
    if(document.getElementById('strategy_name').value === '') {
      alert('전략 이름을 입력하세요')
      return
    }
    if(document.getElementById('strategy_name').value === '전략') {
      alert('불가능한 전략 이름입니다')
      return
    }
    if(document.getElementById('buyWeight').value === '') {
      alert('구매 기준치를 입력하세요')
      return
    }
    if(document.getElementById('sellWeight').value === '') {
      alert('판매 기준치를 입력하세요')
      return
    }
    if(this.state.jsonString === ''){
      alert('한개 이상의 지표를 저장하세요')
      return
    }
    
    if(window.confirm("전략을 저장하시겠습니까?")){
      var send = '{"indicatorList":{'+this.state.jsonString+'},"buyCriteria":'+this.state.buyC+',"sellCriteria":'+this.state.sellC+',"expList":"'+this.state.expList+'"}'
  
      // DB에 새로운 전략 저장
      axios.post(
        'Strategy', 
        'name='+this.state.strategy_name+'&data='+send, 
        { 'Content-Type': 'application/x-www-form-urlencoded' }
      )
      .then( response => {
        // 세션 검증
        if(response.data === 'sessionExpired') this.sessionExpired()
        else if(response.data === 1062) alert('중복된 전략 이름입니다')
        else if(!isUndefined(response.data.error)) alert('전략 생성 실패'+response.data.error)
        else {
          this.props.onStoreStrategy(response.data)
          alert('저장되었습니다')
          window.location = "/strategy"
        }
      }) 
      .catch( response => { console.log('err\n'+response); } ); // ERROR로
    }
  }

  // 세션 유효성 검증
  sessionExpired = () => {
    alert('세션이 종료되었습니다\n다시 로그인하세요')
    window.location = '/'
  }

  handleDelete = () => {    
    if(window.confirm("전략을 삭제하시겠습니까?")){
      // DB에 해당 전략 삭제
      axios.post(
        'Strategy', 
        'name='+this.props.strategyList[document.getElementById('serverStrategy').selectedIndex-1].name+'&data=delete', 
        { 'Content-Type': 'application/x-www-form-urlencoded' }
      )
      .then( response => {
        console.log(response.data)
        // 세션 검증
        if(response.data === 'sessionExpired') this.sessionExpired()
        else if(!isUndefined(response.data.error)) alert('전략 삭제 실패'+response.data.error)
        else {
          this.props.onStoreStrategy(response.data)
          alert('삭제되었습니다')
          window.location = "/strategy"
        }
      }) 
      .catch( response => { console.log('err\n'+response); } ); // ERROR로
    }
  }

  render() {
    const { selectedIndicator, isLoad, jsonString, defaultIndicator, selectedStrategy } = this.state
    return (
      <div class="strategy">
        <div class="strategy_1">
          <div class="strategy_1_contents">
            <div class="strategy_1_contents_top">
              <h4 class="strategy_1_contents_titles_1">불러오기</h4>
              <select id="serverStrategy" onChange={(e)=>this.handleLoad(e)}>
                <option class="strategy_1_contents_titles">새로 만들기</option>
                {
                  this.props.strategyList.map((e, i) => {
                  return (<option key={i}> {e.name} </option>)
                })
                
                }
              </select>
            </div>
            <div class="strategy_1_contents_bottom">
              <h4 class="strategy_1_contents_titles_2">전략 만들기</h4>
              <input placeholder="전략 이름" id="strategy_name" readOnly={isLoad}/>
              <h4 class="strategy_1_contents_titles_3">거래 세팅</h4>
              <input placeholder="구매 기준치" id="buyWeight" value={this.state.buyC} readOnly={isLoad} onChange={(e) => this.handleCriteria(e, 'buy')}/><br/>
              <input placeholder="판매 기준치" id="sellWeight" value={this.state.sellC} readOnly={isLoad} onChange={(e) => this.handleCriteria(e, 'sell')}/>

              <h4 class="strategy_1_contents_titles_4">지표 세팅</h4>
              <select id="indicator" onChange={(e)=>this.handleIndicator(e)}>
                {
                  indicatorList.map((e, i) => {
                  return (<option key={i}> {e.indicator} </option>)
                })
              }
              </select>
              <input placeholder={"weight: "+defaultIndicator.weight} id="weight"/>
              {selectedIndicator.period !== undefined && 
                (<input placeholder={"period: "+defaultIndicator.period} id="period"/>)}
              {selectedIndicator.buyIndex !== undefined && 
                (<input placeholder={"buyIndex: "+defaultIndicator.buyIndex} id="buyIndex"/>)}
              {selectedIndicator.sellIndex !== undefined && 
                (<input placeholder={"sellIndex: "+defaultIndicator.sellIndex} id="sellIndex"/>)}
              {selectedIndicator.mul !== undefined && 
                (<input placeholder={"mul: "+defaultIndicator.mul} id="mul"/>)}
              {selectedIndicator.longD !== undefined && 
                (<input placeholder={"longD: "+defaultIndicator.longD} id="longD"/>)}
              {selectedIndicator.shortD !== undefined && 
                (<input placeholder={"shortD: "+defaultIndicator.shortD} id="shortD"/>)}
              {selectedIndicator.mT !== undefined && 
                (<input placeholder={"mT: "+defaultIndicator.mT} id="mT"/>)}
              {selectedIndicator.n !== undefined && 
                (<input placeholder={"n: "+defaultIndicator.n} id="n"/>)}
              {selectedIndicator.m !== undefined && 
                (<input placeholder={"m: "+defaultIndicator.m} id="m"/>)}
              {selectedIndicator.t !== undefined && 
                (<input placeholder={"t: "+defaultIndicator.t} id="t"/>)}
              {/* {selectedIndicator.cor !== undefined && 
                (<input placeholder={"cor: "+selectedIndicator.cor} id="cor"/>)} */}
            </div>

            <div class="strategySaveButton">
              <button id="strategySaveButton" hidden={isLoad} onClick={this.handleSave}><img src={require('../img/common/btn_09.png')} /></button>
            </div>
          </div>
        </div>
        
        <div class="strategy_2_wrap">
          <h4 class="strategy_2_title">저장된 항목</h4>
          <h5 class="strategy_2_common">아래 지표들의 시그널값 합이 '구매기준치' 보다 높으면 매수, '판매기준치' 보다 작으면 매도를 하게 됩니다</h5>
        </div>
        <div class="strategy_2">
          <div class="strategy_2_grid">
            {isLoad === false
            ? (Object.keys(JSON.parse('{'+jsonString+'}')).map((idc, i) => {
                return (<div class="setting _1" key={i}>
                <table>
                  <thead>
                    <tr>
                      <p class="strategy_2_titles">{JSON.parse('{'+jsonString+'}')[idc].indicator}</p>
                    </tr>
                  </thead>
                  <tbody>
                    <div class="strategy_2_contents">
                      <div class="strategy_2_subtitles">
                        <tr class="strategy_2_subtitle">
                          <th class="strategy_2_sub_subtitle">
                            {JSON.parse('{'+jsonString+'}')[idc].weight !== undefined && (<th>weight &emsp;</th>)}
                            {JSON.parse('{'+jsonString+'}')[idc].period !== undefined && (<th>period &emsp;</th>)}
                            {JSON.parse('{'+jsonString+'}')[idc].buyIndex !== undefined && (<th>buyIndex &emsp;</th>)}
                            {JSON.parse('{'+jsonString+'}')[idc].sellIndex !== undefined && (<th>sellIndex&emsp; </th>)}
                            {JSON.parse('{'+jsonString+'}')[idc].mul !== undefined && (<th>mul &emsp;&emsp;</th>)}
                            {JSON.parse('{'+jsonString+'}')[idc].longD !== undefined && (<th>longD &emsp;</th>)}
                            {JSON.parse('{'+jsonString+'}')[idc].shortD !== undefined && (<th>shortD &emsp;</th>)}
                            {JSON.parse('{'+jsonString+'}')[idc].mT !== undefined && (<th>mT &emsp;</th>)}
                            {JSON.parse('{'+jsonString+'}')[idc].n !== undefined && (<th>n &emsp;&emsp;</th>)}
                            {JSON.parse('{'+jsonString+'}')[idc].m !== undefined && (<th>m &emsp;&emsp;</th>)}
                            {JSON.parse('{'+jsonString+'}')[idc].t !== undefined && (<th>t &emsp;&emsp;</th>)}
                            {/* {JSON.parse('{'+jsonString+'}')[idc].cor !== undefined && (<th>cor &emsp;</th>)} */}
                          </th>
                        </tr>
                      </div>
                      <div class="strategy_2_subcontents">
                        <tr class="strategy_2_subcontent">
                          <td class="strategy_2_sub_subcontent">
                            {JSON.parse('{'+jsonString+'}')[idc].weight !== undefined && (<td>{JSON.parse('{'+jsonString+'}')[idc].weight} &emsp;</td>)}
                            {JSON.parse('{'+jsonString+'}')[idc].period !== undefined && (<td>&emsp;{JSON.parse('{'+jsonString+'}')[idc].period} &emsp;</td>)}
                            {JSON.parse('{'+jsonString+'}')[idc].buyIndex !== undefined && (<td>&emsp;{JSON.parse('{'+jsonString+'}')[idc].buyIndex} &emsp;</td>)}
                            {JSON.parse('{'+jsonString+'}')[idc].sellIndex !== undefined && (<td>&emsp;{JSON.parse('{'+jsonString+'}')[idc].sellIndex} &emsp;</td>)}
                            {JSON.parse('{'+jsonString+'}')[idc].mul !== undefined && (<td>&emsp;{JSON.parse('{'+jsonString+'}')[idc].mul} &emsp;</td>)}
                            {JSON.parse('{'+jsonString+'}')[idc].longD !== undefined && (<td>&emsp;{JSON.parse('{'+jsonString+'}')[idc].longD} &emsp;</td>)}
                            {JSON.parse('{'+jsonString+'}')[idc].shortD !== undefined && (<td>&emsp;{JSON.parse('{'+jsonString+'}')[idc].shortD} &emsp;</td>)}
                            {JSON.parse('{'+jsonString+'}')[idc].mT !== undefined && (<td>&emsp;{JSON.parse('{'+jsonString+'}')[idc].mT} &emsp;</td>)}
                            {JSON.parse('{'+jsonString+'}')[idc].n !== undefined && (<td>{JSON.parse('{'+jsonString+'}')[idc].n} &emsp;</td>)}
                            {JSON.parse('{'+jsonString+'}')[idc].m !== undefined && (<td>{JSON.parse('{'+jsonString+'}')[idc].m} &emsp;</td>)}
                            {JSON.parse('{'+jsonString+'}')[idc].t !== undefined && (<td>{JSON.parse('{'+jsonString+'}')[idc].t} &emsp;</td>)}
                            {/* {JSON.parse('{'+jsonString+'}')[idc].cor !== undefined && (<td>&emsp;{JSON.parse('{'+jsonString+'}')[idc].cor} &emsp;</td>)} */}
                          </td>
                        </tr>
                      </div>
                      
                      {/* {i !== savedCnt-1 && (<button disabled>{expList[i]}</button>)} */}
                      
                    </div>
                  </tbody>
                </table>
                </div>);
              })) 

              : (Object.keys(selectedStrategy.indicatorList).map((idc, i) => {
                  return (<div class="setting _1" key={i}>
                  <table>
                    <thead>
                      <tr>
                        <p class="strategy_2_titles">{selectedStrategy.indicatorList[idc].indicator}</p>
                      </tr>
                    </thead>
                    <tbody>
                      <div class="strategy_2_contents">
                        <div class="strategy_2_subtitles">
                          <tr class="strategy_2_subtitle">
                            <th class="strategy_2_sub_subtitle">
                              {selectedStrategy.indicatorList[idc].weight !== undefined && (<th>weight &emsp;</th>)}
                              {selectedStrategy.indicatorList[idc].period !== undefined && (<th>period &emsp;</th>)}
                              {selectedStrategy.indicatorList[idc].buyIndex !== undefined && (<th>buyIndex &emsp;</th>)}
                              {selectedStrategy.indicatorList[idc].sellIndex !== undefined && (<th>sellIndex&emsp; </th>)}
                              {selectedStrategy.indicatorList[idc].mul !== undefined && (<th>mul &emsp;&emsp;</th>)}
                              {selectedStrategy.indicatorList[idc].longD !== undefined && (<th>longD &emsp;</th>)}
                              {selectedStrategy.indicatorList[idc].shortD !== undefined && (<th>shortD &emsp;</th>)}
                              {selectedStrategy.indicatorList[idc].mT !== undefined && (<th>mT &emsp;</th>)}
                              {selectedStrategy.indicatorList[idc].n !== undefined && (<th>n &emsp;&emsp;</th>)}
                              {selectedStrategy.indicatorList[idc].m !== undefined && (<th>m &emsp;&emsp;</th>)}
                              {selectedStrategy.indicatorList[idc].t !== undefined && (<th>t &emsp;&emsp;</th>)}
                              {/* {selectedStrategy.indicatorList[idc].cor !== undefined && (<th>cor &emsp;</th>)} */}
                            </th>
                          </tr>
                        </div>
                        <div class="strategy_2_subcontents">
                          <tr class="strategy_2_subcontent">
                            <td class="strategy_2_sub_subcontent">
                              {selectedStrategy.indicatorList[idc].weight !== undefined && (<td>{selectedStrategy.indicatorList[idc].weight} &emsp;</td>)}
                              {selectedStrategy.indicatorList[idc].period !== undefined && (<td>&emsp;{selectedStrategy.indicatorList[idc].period} &emsp;</td>)}
                              {selectedStrategy.indicatorList[idc].buyIndex !== undefined && (<td>&emsp;{selectedStrategy.indicatorList[idc].buyIndex} &emsp;</td>)}
                              {selectedStrategy.indicatorList[idc].sellIndex !== undefined && (<td>&emsp;{selectedStrategy.indicatorList[idc].sellIndex} &emsp;</td>)}
                              {selectedStrategy.indicatorList[idc].mul !== undefined && (<td>&emsp;{selectedStrategy.indicatorList[idc].mul} &emsp;</td>)}
                              {selectedStrategy.indicatorList[idc].longD !== undefined && (<td>&emsp;{selectedStrategy.indicatorList[idc].longD} &emsp;</td>)}
                              {selectedStrategy.indicatorList[idc].shortD !== undefined && (<td>&emsp;{selectedStrategy.indicatorList[idc].shortD} &emsp;</td>)}
                              {selectedStrategy.indicatorList[idc].mT !== undefined && (<td>&emsp;{selectedStrategy.indicatorList[idc].mT} &emsp;</td>)}
                              {selectedStrategy.indicatorList[idc].n !== undefined && (<td>{selectedStrategy.indicatorList[idc].n} &emsp;</td>)}
                              {selectedStrategy.indicatorList[idc].m !== undefined && (<td>{selectedStrategy.indicatorList[idc].m} &emsp;</td>)}
                              {selectedStrategy.indicatorList[idc].t !== undefined && (<td>{selectedStrategy.indicatorList[idc].t} &emsp;</td>)}
                              {/* {selectedStrategy.indicatorList[idc].cor !== undefined && (<td>&emsp;{selectedStrategy.indicatorList[idc].cor} &emsp;</td>)} */}
                            </td>
                          </tr>
                        </div>
                        
                        {/* {i !== savedCnt-1 && (<button disabled>{expList[i]}</button>)} */}
                        
                      </div>
                    </tbody>
                  </table>
                  </div>);
                })) 

            // : (Object.keys(selectedStrategy.indicatorList).map((idc, i) => {
            //     return (<div class="setting_2" key={i}>
            //       <b>{selectedStrategy.indicatorList[idc].indicator}</b><br/>
            //       <input value={"weight: "+selectedStrategy.indicatorList[idc].weight} readOnly/>
            //       {selectedStrategy.indicatorList[idc].period !== undefined && 
            //         (<input value={"period: "+selectedStrategy.indicatorList[idc].period} readOnly/>)}
            //       {selectedStrategy.indicatorList[idc].buyIndex !== undefined && 
            //         (<input value={"buyIndex: "+selectedStrategy.indicatorList[idc].buyIndex} readOnly/>)}
            //       {selectedStrategy.indicatorList[idc].sellIndex !== undefined && 
            //         (<input value={"sellIndex: "+selectedStrategy.indicatorList[idc].sellIndex} readOnly/>)}
            //       {selectedStrategy.indicatorList[idc].mul !== undefined && 
            //         (<input value={"mul: "+selectedStrategy.indicatorList[idc].mul} readOnly/>)}
            //       {selectedStrategy.indicatorList[idc].longD !== undefined && 
            //         (<input value={"longD: "+selectedStrategy.indicatorList[idc].longD} readOnly/>)}
            //       {selectedStrategy.indicatorList[idc].shortD !== undefined && 
            //         (<input value={"shortD: "+selectedStrategy.indicatorList[idc].shortD} readOnly/>)}
            //       {selectedStrategy.indicatorList[idc].mT !== undefined && 
            //         (<input value={"mT: "+selectedStrategy.indicatorList[idc].mT} readOnly/>)}
            //       {selectedStrategy.indicatorList[idc].n !== undefined && 
            //         (<input value={"n: "+selectedStrategy.indicatorList[idc].n} readOnly/>)}
            //       {selectedStrategy.indicatorList[idc].m !== undefined && 
            //         (<input value={"m: "+selectedStrategy.indicatorList[idc].m} readOnly/>)}
            //       {selectedStrategy.indicatorList[idc].t !== undefined && 
            //         (<input value={"t: "+selectedStrategy.indicatorList[idc].t} readOnly/>)}
            //       {selectedStrategy.indicatorList[idc].cor !== undefined && 
            //         (<input value={"cor: "+selectedStrategy.indicatorList[idc].cor} readOnly/>)}<br/>
            //       {i !== selectedStrategy.expList.split(',').length &&
            //         (<button disabled>{selectedStrategy.expList.split(',')[i]}</button>)}
            //     </div>);
            //   }))
            }
          </div>
          
        </div>
        <div class="strategyCompleteButton">
          <button id="strategyCompleteButton" hidden={isLoad} onClick={this.handleComplete}><img src={require('../img/common/btn_10.png')} /></button>
          <button id="strategyDeleteButton" hidden={!isLoad} onClick={this.handleDelete}>삭제</button>
        </div>
      </div>


      // css 입히기전 백업 (나중에 기능 이상 없으면 삭제)
      // <div>
      //   <h4>불러오기</h4>
      //   <select id="serverStrategy" onChange={(e)=>this.handleLoad(e)}>
      //     <option>새로 만들기</option>
      //     {
      //       this.props.strategyList.map((e, i) => {
      //       return (<option key={i}> {e.name} </option>)
      //     })
      //   }
      //   </select>
      //   <h4>전략 만들기</h4>
      //   전략 이름 : <input placeholder="이름" id="name"/>
      //   <h4>거래 세팅</h4>

      //   구매 기준치 : <input placeholder="구매 기준치" id="buyWeight" value={this.state.buyC} onChange={(e) => this.handleCriteria(e, 'buy')}/>
      //   판매 기준치 : <input placeholder="판매 기준치" id="sellWeight" value={this.state.sellC} onChange={(e) => this.handleCriteria(e, 'sell')}/>

      //   <h4>지표 세팅</h4>
      //   <select id="indicator" onChange={(e)=>this.handleIndicator(e)}>
      //     {
      //       indicatorList.map((e, i) => {
      //       return (<option key={i}> {e.indicator} </option>)
      //     })
      //   }
      //   </select>
      //   <input placeholder={"weight: "+selectedIndicator.weight} id="weight"/>
      //   {selectedIndicator.period !== undefined && 
      //     (<input placeholder={"period: "+selectedIndicator.period} id="period"/>)}
      //   {selectedIndicator.buyIndex !== undefined && 
      //     (<input placeholder={"buyIndex: "+selectedIndicator.buyIndex} id="buyIndex"/>)}
      //   {selectedIndicator.sellIndex !== undefined && 
      //     (<input placeholder={"sellIndex: "+selectedIndicator.sellIndex} id="sellIndex"/>)}
      //   {selectedIndicator.mul !== undefined && 
      //     (<input placeholder={"mul: "+selectedIndicator.mul} id="mul"/>)}
      //   {selectedIndicator.longD !== undefined && 
      //     (<input placeholder={"longD: "+selectedIndicator.longD} id="longD"/>)}
      //   {selectedIndicator.shortD !== undefined && 
      //     (<input placeholder={"shortD: "+selectedIndicator.shortD} id="shortD"/>)}
      //   {selectedIndicator.mT !== undefined && 
      //     (<input placeholder={"mT: "+selectedIndicator.mT} id="mT"/>)}
      //   {selectedIndicator.n !== undefined && 
      //     (<input placeholder={"n: "+selectedIndicator.n} id="n"/>)}
      //   {selectedIndicator.m !== undefined && 
      //     (<input placeholder={"m: "+selectedIndicator.m} id="m"/>)}
      //   {selectedIndicator.t !== undefined && 
      //     (<input placeholder={"t: "+selectedIndicator.t} id="t"/>)}
      //   {selectedIndicator.cor !== undefined && 
      //     (<input placeholder={"cor: "+selectedIndicator.cor} id="cor"/>)}
      //   <button disabled={isLoad} onClick={this.handleSave}>저장</button>

      //   <h4>저장된 항목</h4>
      //   {isLoad === false ? 
      //     (Object.keys(JSON.parse('{'+jsonString+'}')).map((idc, i) => {
      //       return (<div key={i}>
      //         <b>{JSON.parse('{'+jsonString+'}')[idc].indicator}</b><br/>
      //         <input value={"weight: "+JSON.parse('{'+jsonString+'}')[idc].weight} readOnly/>
      //         {JSON.parse('{'+jsonString+'}')[idc].period !== undefined && 
      //           (<input value={"period: "+JSON.parse('{'+jsonString+'}')[idc].period} readOnly/>)}
      //         {JSON.parse('{'+jsonString+'}')[idc].buyIndex !== undefined && 
      //           (<input value={"buyIndex: "+JSON.parse('{'+jsonString+'}')[idc].buyIndex} readOnly/>)}
      //         {JSON.parse('{'+jsonString+'}')[idc].sellIndex !== undefined && 
      //           (<input value={"sellIndex: "+JSON.parse('{'+jsonString+'}')[idc].sellIndex} readOnly/>)}
      //         {JSON.parse('{'+jsonString+'}')[idc].mul !== undefined && 
      //           (<input value={"mul: "+JSON.parse('{'+jsonString+'}')[idc].mul} readOnly/>)}
      //         {JSON.parse('{'+jsonString+'}')[idc].longD !== undefined && 
      //           (<input value={"longD: "+JSON.parse('{'+jsonString+'}')[idc].longD} readOnly/>)}
      //         {JSON.parse('{'+jsonString+'}')[idc].shortD !== undefined && 
      //           (<input value={"shortD: "+JSON.parse('{'+jsonString+'}')[idc].shortD} readOnly/>)}
      //         {JSON.parse('{'+jsonString+'}')[idc].mT !== undefined && 
      //           (<input value={"mT: "+JSON.parse('{'+jsonString+'}')[idc].mT} readOnly/>)}
      //         {JSON.parse('{'+jsonString+'}')[idc].n !== undefined && 
      //           (<input value={"n: "+JSON.parse('{'+jsonString+'}')[idc].n} readOnly/>)}
      //         {JSON.parse('{'+jsonString+'}')[idc].m !== undefined && 
      //           (<input value={"m: "+JSON.parse('{'+jsonString+'}')[idc].m} readOnly/>)}
      //         {JSON.parse('{'+jsonString+'}')[idc].t !== undefined && 
      //           (<input value={"t: "+JSON.parse('{'+jsonString+'}')[idc].t} readOnly/>)}
      //         {JSON.parse('{'+jsonString+'}')[idc].cor !== undefined && 
      //           (<input value={"cor: "+JSON.parse('{'+jsonString+'}')[idc].cor} readOnly/>)}<br/>
      //         {i !== savedCnt-1 &&
      //           (<button disabled>{expList[i]}</button>)} 
      //       </div>);
      //     })) :
      //     (Object.keys(selectedStrategy.indicatorList).map((idc, i) => {
      //       return (<div key={i}>
      //         <b>{selectedStrategy.indicatorList[idc].indicator}</b><br/>
      //         <input value={"weight: "+selectedStrategy.indicatorList[idc].weight} readOnly/>
      //         {selectedStrategy.indicatorList[idc].period !== undefined && 
      //           (<input value={"period: "+selectedStrategy.indicatorList[idc].period} readOnly/>)}
      //         {selectedStrategy.indicatorList[idc].buyIndex !== undefined && 
      //           (<input value={"buyIndex: "+selectedStrategy.indicatorList[idc].buyIndex} readOnly/>)}
      //         {selectedStrategy.indicatorList[idc].sellIndex !== undefined && 
      //           (<input value={"sellIndex: "+selectedStrategy.indicatorList[idc].sellIndex} readOnly/>)}
      //         {selectedStrategy.indicatorList[idc].mul !== undefined && 
      //           (<input value={"mul: "+selectedStrategy.indicatorList[idc].mul} readOnly/>)}
      //         {selectedStrategy.indicatorList[idc].longD !== undefined && 
      //           (<input value={"longD: "+selectedStrategy.indicatorList[idc].longD} readOnly/>)}
      //         {selectedStrategy.indicatorList[idc].shortD !== undefined && 
      //           (<input value={"shortD: "+selectedStrategy.indicatorList[idc].shortD} readOnly/>)}
      //         {selectedStrategy.indicatorList[idc].mT !== undefined && 
      //           (<input value={"mT: "+selectedStrategy.indicatorList[idc].mT} readOnly/>)}
      //         {selectedStrategy.indicatorList[idc].n !== undefined && 
      //           (<input value={"n: "+selectedStrategy.indicatorList[idc].n} readOnly/>)}
      //         {selectedStrategy.indicatorList[idc].m !== undefined && 
      //           (<input value={"m: "+selectedStrategy.indicatorList[idc].m} readOnly/>)}
      //         {selectedStrategy.indicatorList[idc].t !== undefined && 
      //           (<input value={"t: "+selectedStrategy.indicatorList[idc].t} readOnly/>)}
      //         {selectedStrategy.indicatorList[idc].cor !== undefined && 
      //           (<input value={"cor: "+selectedStrategy.indicatorList[idc].cor} readOnly/>)}<br/>
      //         {i !== selectedStrategy.expList.split(',').length &&
      //           (<button disabled>{selectedStrategy.expList.split(',')[i]}</button>)}
      //       </div>);
      //     }))
      //   }
      //   <button disabled={isLoad} onClick={this.handleComplete}>완료</button>
      // </div>
    );
  }
}

let mapDispatchToProps = (dispatch) => {
  return {
    onStoreStrategy: (value) => dispatch(setStrategy(value))
  }
}

let mapStateToProps = (state) => {
  return {
    strategyList: state.strategy.strategyList
  };
}

Strategy = connect(mapStateToProps, mapDispatchToProps)(Strategy);

export default Strategy;