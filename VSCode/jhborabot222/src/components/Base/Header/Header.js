import React from 'react';
import { Link } from 'react-router-dom';
import styled from 'styled-components';
import oc from 'open-color';
import { shadow, media } from '../../../lib/styleUtils';
import { AuthContent, InputWithLabel, AuthButton, RightAlignedLink, AuthError } from '../../Auth';

// 상단 고정
const Positioner = styled.div`
    display: flex;

    position: relative;
    top: 0px;
    width: 100%;
`;

// 내용 중간 정렬
const WhiteBackground = styled.div`
width: 100%;
    background: ${oc.grape[7]};
    display: flex;
    padding-left: 3rem;
    height: auto;
`;

// 해더의 내용
const HeaderContents = styled.div`
    width: 100%;
    min-width: 900px;
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
const BackTesting = styled.div`
  font-size: 1rem;
  color: white;
  font-family: 'Rajdhani';
  text_decoration: none;
`;

const Blog = styled.div`
  font-size: 1rem;
  color: white;
  font-family: 'Rajdhani';
  text_decoration: none;
`;

const Profile = styled.div`
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


const Header = ({children}) => {
    return (
        <Positioner>
            <WhiteBackground>
                <HeaderContents>
                  <Logo to="/">BoraBot</Logo>
                  <Spacer/>
                  <BackTesting> 백테스팅 </BackTesting>
                  <Spacer_1/>
                  <Blog> 전략 공유 게시판 </Blog>
                  <Spacer_1/>
                  <Profile> 회원정보관리 </Profile>
                  <Spacer_1/>
                  <LogOut to="../../../auth/login"> 로그아웃 </LogOut>
                  <Spacer_2/>
                </HeaderContents>
            </WhiteBackground>
        </Positioner>
    );
};

export default Header;
