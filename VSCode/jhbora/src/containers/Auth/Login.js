import React, { Component } from 'react';
import { AuthContent, InputWithLabel, AuthButton, RightAlignedLink, AuthError } from '../../components/Auth';
import { connect } from 'react-redux';
import {bindActionCreators} from 'redux';
import * as authActions from '../../redux/modules/auth';
import * as userActions from '../../redux/modules/user';
import storage from '../../lib/storage';
import queryString from 'query-string';
import Background from '../../components/Auth/AuthWrapper';
import HeaderContainer from '../Base/HeaderContainer';
import Header, { LoginButton } from '../../components/Base/Header';

import { Route } from 'react-router-dom';
import { Home, Auth } from '../../pages';

import './Login.css';

const initialState = {
    login: {
        status: 'INIT'
    },
    status: {
        isLoggedIn: false,
        currentUser: '',
    }
}

class Login extends Component {
    constructor(props){
      super(props);
      this.state={
        email: "",
        password: "",
      };
    }

    componentDidMount() {
      const { location } = this.props;
      const query = queryString.parse(location.search);

      if(query.expired !== undefined) {
          this.setError('세션에 만료되었습니다. 다시 로그인 하세요.')
      }
    }

    handleChange = (e) => {
        const { AuthActions } = this.props;
        const { name, value } = e.target;

        AuthActions.changeInput({
            name,
            value,
            form: 'login'
        });
    }

    componentWillUnmount() {
        const { AuthActions } = this.props;
        AuthActions.initializeForm('login')
    }

    setError = (message) => {
        const { AuthActions } = this.props;
        AuthActions.setError({
            form: 'login',
            message
        });
        return false;
    }

    handleLocalLogin = async () => {
        const { form, AuthActions, UserActions, history } = this.props;
        const { email, password } = form.toJS();

        try {
            // await AuthActions.localLogin({email, password});
            console.log('b');
            const loggedInfo = this.props.result.toJS();

            UserActions.setLoggedInfo(loggedInfo);
            history.push('/');
            storage.set('loggedInfo', loggedInfo);

        } catch (e) {
            console.log('a');
            this.setError('잘못된 계정정보입니다.');
        }
    }

    render() {
      // form 에서 email 과 password 값을 읽어옴
        const { email, password } = this.props.form.toJS();
        const { handleChange, handleLocalLogin } = this;
        const { error } = this.props;


        return (
          <div>
            <div className="Logo">
                Logo Image
            </div>

            <AuthContent>
                <InputWithLabel
                    name="email"
                    placeholder="Username"
                    value={email}
                    onChange={handleChange}
                />
                <InputWithLabel
                    name="password"
                    placeholder="****"
                    type="password"
                    value={password}
                    onChange={handleChange}
                />
                {
                  error && <AuthError>{error}</AuthError>
                }
                <div className="loginButton">
                  <AuthButton onClick={handleLocalLogin}>로그인</AuthButton>
                </div>
            </AuthContent>
            <div className="Comments_1">
              회원이 아니신가요?
              <div className="Comments_2">
                간단한 회원가입으로 보라봇을 이용할 수 있습니다.
              </div>
            </div>
            <RightAlignedLink to="/auth/register">회원가입</RightAlignedLink>
          </div>
      );
  }
}

export default connect(
    (state) => ({
        form: state.auth.getIn(['login', 'form']),
        error: state.auth.getIn(['login', 'error']),
        result: state.auth.get('result')
    }),
    (dispatch) => ({
        AuthActions: bindActionCreators(authActions, dispatch),
        UserActions: bindActionCreators(userActions, dispatch)
    })
)(Login);
