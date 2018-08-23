import React from 'react';
import styled from 'styled-components';
import oc from 'open-color';
import { shadow, media } from '../../lib/styleUtils';
import { Link } from 'react-router-dom';
import { AuthContent, InputWithLabel, AuthButton, RightAlignedLink, AuthError } from '../Auth';
import { connect } from 'react-redux';
import {bindActionCreators} from 'redux';
import * as userActions from '../../redux/modules/user';


// 상단 고정
const Positioner = styled.div`
    position: relative;
    top: 0px;
    width: 100%;
`;

// 내용 중간 정렬
const WhiteBackground = styled.div`
    background: ${oc.grape[7]};
    display: flex;
    padding-left: 3rem;
    height: auto;
`;

// 해더의 내용
const HeaderContents = styled.div`
    width: 117rem;
    height: 55px;
    display: flex;

    align-items: center;

    padding-right: 1rem;
    padding-top: 0.5rem;


    ${media.wide`
        width: 992px;
    `}

    ${media.tablet`
        width: 100%;
        height: 100%;
    `}
`;

// 로고
const Logo = styled(Link)`
    left: 0px;
    font-size: 3rem;
    letter-spacing: 2px;
    color: white;
    font-family: 'Rajdhani';
    text-decoration: none;
`;


// 중간 여백
const Spacer = styled.div`
    flex-grow: 100;
`;

const Spacer_1 = styled.div`
    flex-grow: 10;
`;

const Spacer_2 = styled.div`
    flex-grow: 5;
`;


// 백테스팅, 전략 공유 게시판, 회원정보관리, 로그아웃
const BackTesting = styled(Link)`
  font-size: 1rem;
  color: white;
  font-family: 'Rajdhani';
  text_decoration: none;
`;

const Blog = styled(Link)`
  font-size: 1rem;
  color: white;
  font-family: 'Rajdhani';
  text_decoration: none;
`;

const Profile = styled(Link)`
  font-size: 1rem;
  color: white;
  font-family: 'Rajdhani';
  text_decoration: none;
`;

const LogOut = styled(Link)`
  font-size: 1rem;
  color: white;
  font-family: 'Rajdhani';
  text_decoration: none;
`;



// 화면 배치
const Positioner_1 = styled.div`
    position: absolute;
    align-items: center;
    justify-content: center;
    padding: 31.2%;
    padding-top: 0;
    height: 100%;
`;

const Positioner_2 = styled.div`
    position: absolute;
    padding: 83.2%;
    padding-top: 0;
    align-items: right;
    justify-content: right;
`;

// 로고
const LogoWrapper = styled.div`
    position: relative;
    background: ${oc.grape[9]};
    height: 100rem;
    display: flex;
    align-items: center;
    justify-content: center;
`;



// children 이 들어가는 곳
const Contents = styled.div`
    background: ${oc.grape[9]};
    border-color: ${oc.grape[9]};
    padding: 2rem;
    height: 30rem;
    text-align: left;
`;

const AuthWrapper = ({children}) => (
  <div>
    <Positioner>
        <WhiteBackground>
            <HeaderContents>
              <Logo to="/">BoraBot</Logo>
              <Spacer/>
              <BackTesting to="../../auth/backtesting"> 백테스팅 </BackTesting>
              <Spacer_1/>
              <Blog to="../../auth/blog"> 전략 공유 게시판 </Blog>
              <Spacer_1/>
              <Profile to="../../auth/profile"> 회원정보관리 </Profile>
              <Spacer_1/>
              <LogOut to="../../auth/login"> 로그아웃 </LogOut>
              <Spacer_2/>
            </HeaderContents>
        </WhiteBackground>
    </Positioner>
    <Positioner_1>
      <Positioner_2>
        <Contents>
          <div>
            {children}
          </div>
        </Contents>
      </Positioner_2>
    </Positioner_1>
  </div>
);

const Background = styled.div`
    backgournd: white;
`;

// export default connect(
//   null, (dispatch) => ({
//     UserActions: bindActionCreators(userActions, dispatch)
//   })
// )(AuthWrapper);

export default AuthWrapper;