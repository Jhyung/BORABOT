import React from 'react';
import styled from 'styled-components';
import oc from 'open-color';
import { shadow } from '../../lib/styleUtils';
import { Link } from 'react-router-dom';

import { connect } from 'react-redux';
import {bindActionCreators} from 'redux';
import * as userActions from '../../redux/modules/user';

import HeaderContainer from '../../containers/Base/HeaderContainer';

// 화면 배치
const Positioner_1 = styled.div`
    position: absolute;
    align-items: center;
    justify-content: center;
`;

const Positioner_2 = styled.div`
    position: absolute;
    padding-left: 90rem;
    align-items: right;
    justify-content: right;
`;

// 로고
const LogoWrapper = styled.div`
    margin-top: 5rem;
    background: ${oc.grape[9]};
    height: 5rem;
    display: flex;
    align-items: center;
    justify-content: center;
`;

const Logo = styled(Link)`
    color: white;
    background: ${oc.grape[9]};
    font-family: 'Rajdhani';
    font-size: 2.4rem;
    letter-spacing: 5px;
    text-decoration: none;
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
    <Positioner_1>
      <HeaderContainer/>
    </Positioner_1>
    <Positioner_2>
      <LogoWrapper>
        <Logo to="/">BoraBot</Logo>
      </LogoWrapper>
      <Contents>
          {children}
      </Contents>
    </Positioner_2>
  </div>

);

const Background = styled.div`
    backgournd: white;
`;

export default connect(
  null, (dispatch) => ({
    UserActions: bindActionCreators(userActions, dispatch)
  })
)(AuthWrapper);
