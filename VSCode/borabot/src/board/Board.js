import React, { Component } from 'react';
import axios from 'axios';
import Post from './Post';

import './Board.css';
import onText from '../img/common/on_bg_01.png';
import offText from '../img/common/off_bg_01.png';
import toLeftBtn from '../img/common/pre_btn_01.png';
import toRightBtn from '../img/common/next_btn_01.png';

class Board extends Component {
  constructor(){
    super();
    this.state={
      post: false,  // true : 게시물 작성, 보기, false : 목록 표시
      write: false, // true : 게시물 작성, false : 게시물 보기 / 목록 표시

      post_num: 0,  // 현재 선택된 게시물 번호
      postList: [], // 현재 선택된 페이지의 10개의 게시물 리스트

      pageNum:1,  // 현재 선택된 페이지 번호
      pageNumList: [1] // 게시물의 전체 페이지 리스트

      // 앞단 테스트용 ============================================================================================================================= //
      // post_num: 0,  // 현재 선택된 게시물 번호
      // pageNum:1,  // 현재 선택된 페이지 번호
      // postList: [{"comment_count":"1","post_num":"247","title":"zxbasdfgweqtgw","email":"test","post_time":"2018-07-31 18:47:35"},{"comment_count":"2","post_num":"244","title":"ㅇㅊㅌㅍㅂㅁㅈㄷㄱ","email":"qwe","post_time":"2018-07-30 11:42:06"},{"comment_count":"2","post_num":"243","title":"ㅇㅊㅌㅍㅂㅁㅈㄷㄱ","email":"qwe","post_time":"2018-07-30 11:42:06"},{"comment_count":"2","post_num":"242","title":"ㅇㅊㅌㅍㅂㅁㅈㄷㄱ","email":"qwe","post_time":"2018-07-30 11:42:06"},{"comment_count":"2","post_num":"241","title":"ㅇㅊㅌㅍㅂㅁㅈㄷㄱ","email":"qwe","post_time":"2018-07-30 11:42:06"},{"comment_count":"2","post_num":"240","title":"ㅇㅊㅌㅍㅂㅁㅈㄷㄱ","email":"qwe","post_time":"2018-07-30 11:42:06"},{"comment_count":"2","post_num":"239","title":"ㅇㅊㅌㅍㅂㅁㅈㄷㄱ","email":"qwe","post_time":"2018-07-30 11:42:06"},{"comment_count":"2","post_num":"238","title":"ㅇㅊㅌㅍㅂㅁㅈㄷㄱ","email":"qwe","post_time":"2018-07-30 11:42:06"},{"comment_count":"2","post_num":"237","title":"ㅇㅊㅌㅍㅂㅁㅈㄷㄱ","email":"qwe","post_time":"2018-07-30 11:42:06"},{"comment_count":"2","post_num":"236","title":"ㅇㅊㅌㅍㅂㅁㅈㄷㄱ","email":"qwe","post_time":"2018-07-30 11:42:06"}],
      // pageNumList: [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33]
      // 앞단 테스트용 ============================================================================================================================= //
    }
  }

  componentWillMount() {
    this.getBoard(1)
  }

  // 현재 페이지에서 새로고침을 위해 메뉴를 다시 눌렀을 경우
  componentWillReceiveProps (nextProps) {
    (this.props.location.key !== nextProps.location.key) 
    && (window.location = "/board")
  }

  // 처음 게시판을 보거나 페이지가 바뀌었을 때 호출하여 10개의 게시물을 불러오고 페이지리스트 갱신
  getBoard = (i) => {
    axios.get( 'Board?pageNum='+i )
    .then( response => {
      if(response.data === 'sessionExpired') this.sessionExpired()
      else{
        var pNL = [1]  // state에 저장할 페이지리스트 생성
        for(var i = 2; i <= (response.data.count-1)/10+1; i++){
          pNL.push(i)
        }
        this.setState({
          postList: response.data.postList, // 10개의 게시물 저장
          pageNumList: pNL
        })
      }
    }) 
    .catch( response => { console.log('err\n'+response); } ); // ERROR
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
      ? pn = pageNum -(pageNum -1)%10 -1
      : pn = 1
    } else if(fbn === 'back'){
      (parseInt(pageNum/10, 10) !== parseInt(pageNumList.length/10, 10))
      ? pn = (pageNum -1) -(pageNum -1)%10 +11
      : pn = pageNumList.length
    } else pn = fbn
    
    this.setState({ pageNum: pn })
    this.getBoard(pn)
  }

  // 게시물을 선택하면 해당 게시물 표시
  selectPost = (i) => {
    this.setState({
      post: true,
      post_num: this.state.postList[i].post_num,
      write: false
    })
  }

  // 게시물에서 목록으로 돌아가는 버튼
  moveList = () => {
    this.setState({
      post: false,
      write: false,
      post_num: 0
    })
    this.getBoard(this.state.pageNum)
  }

  // 게시물 작성
  writePost = () => {
    this.setState({
      post: true,
      write: true,
      post_num: 0
    })
  }

  render() {
    const { post, post_num, write, postList, pageNum, pageNumList} = this.state

    const onTextBg = {
      backgroundImage : `url(${onText})`,
    }

    const offTextBg = {
      backgroundImage : `url(${offText})`,
    }
    
    return (
      <div class="board_total">
        <div class="board">
          <div class="board_title">전략 공유 게시판</div>
          { // 게시물 작성/보기일 경우엔 게시물 표시, 아니면 목록 표시
          post
          ? <div class="post_table">
              <Post post_num={post_num} write={write} toList={this.moveList}/>  
              {/*목록버튼*/}
              {/* <button id="listButton" onClick={this.moveList}><img src={require('../img/common/btn_13.png')} /></button> */}
            </div>

          : <div>
              <table className='table-tableContainer' >
                <thead>
                  <th className='table-headTr'>No</th>
                  <th className='table-headTr'>제목</th>
                  <th className='table-headTr'>작성자</th>
                  <th className='table-headTr'>날짜</th>
                </thead>

                <tbody className = 'table-tbodyContainer' >              
                  { // state에 저장된 게시물 리스트를 map 함수 통하여 표시
                  postList.map((p, i) => {
                    return (<tr key={i} className = 'table-tr' style={{borderBottom : "1px solid"}} >
                      <td className = 'table-td'>{p.post_num}</td>
                      <td className = 'table-td' onClick={() => this.selectPost(i)}>{p.title}{p.comment_count !== '0' &&' ('+p.comment_count+')'}</td>
                      <td className = 'table-td'>{p.email}</td>
                      <td className = 'table-td'>{p.post_time}</td>
                    </tr>)
                  })}
                </tbody>
              </table>
              

              <div className = "table-chooseBoxContainer">
                { /* 이전 10 페이지 이동 버튼*/ }
                <div className = "table-chooseLeft" onClick={() => this.selectPage('front')}> <img src = {toLeftBtn}/> </div>
                { // 현재 선택된 페이지의 근처 10개 페이지 표시
                pageNumList.slice(pageNum -(pageNum-1)%10 -1, pageNum -(pageNum-1)%10 +9).map((p, i) => {
                  return(<div  key ={i} onClick={() => this.selectPage(p)}>
                    {pageNum === p ? <div style={onTextBg} className = "table-chooseNumberSelected" >  {p}  </div> : <div style={offTextBg} className = "table-chooseNumber" >  {p}  </div>}
                  </div>)
                })}
                { /* 이후 10 페이지 이동 버튼*/ }
                <div className = "table-chooseRight" onClick={() => this.selectPage('back')}><img src = {toRightBtn}/></div>
              </div>
              {/*저장버튼*/}
              <button id="boardButton" onClick={this.writePost}><img src={require('../img/common/btn_11.png')} alt="btn_11" /></button>
            </div>
          }
        </div>
      </div>
    );
  }
}

export default Board;