import React, { Component } from 'react';
import { AuthContent, InputWithLabel, AuthButton, RightAlignedLink, AuthError } from '../../components/Auth';
// import { connect } from 'react-redux';
// import {bindActionCreators} from 'redux';
// import * as authActions from '../../redux/modules/auth';
// import * as userActions from '../../redux/modules/user';
// import storage from '../../lib/storage';
// import queryString from 'query-string';
// // import Background from '../../components/Auth/AuthWrapper';
// import Header, { LoginButton } from '../../components/Base/Header';
// import { Route } from 'react-router-dom';
// import { Home, Auth } from '../../pages';
import './Login.css';


const crypto = require('crypto');
// const loginHandle = new WebSocket("ws://45.120.65.65/BORABOT/loginhandle");


// var key = "tHis_iS_pRivaTe_Key";

// var serverpassword;
// var auto_padding;

// const initialState = {
//     login: {
//         status: 'INIT'
//     },
//     status: {
//         isLoggedIn: false,
//         currentUser: '',
//     }
// }


class Login extends Component {
    // constructor(props){
    //   super(props);
    //   this.state={
    //     loginInfo: {
    //       email: "",
    //       password: "",
    //     },
    //     serverpassword: ""
    //   }
    //   loginHandle.onmessage = (event) => {
    //     if(event.data != "null"){
    //         this.setState(
    //           {serverpassword: JSON.parse(event.data)});
    //     }
    //     console.log(this.state.serverpassword);
    //     if(this.state.loginInfo.password = this.state.serverpassword) {
    //       console.log('hello');
    //       console.log('serverpassword: ', this.state.serverpassword);
    //     }
    //   }
    // }

    encrypt = (err, key) => {
      var cipher = crypto.createCipher('aes-256-cbc', key);
      var encipheredpw = cipher.update(err, 'utf-8', 'hex');
      encipheredpw += cipher.final('hex');
      return encipheredpw;
    }

    decrypt = (err, key) => {
      var decipher = crypto.createDecipher('aes-256-cbc', key);
      var decipheredPlainpw = decipher.update(err, 'hex', 'utf-8');
      decipheredPlainpw += decipher.final('utf-8');
      return decipheredPlainpw;
    }

    // componentDidMount() {
    //   const { location } = this.props;
    //   const query = queryString.parse(location.search);

    //   if(query.expired !== undefined) {
    //       this.setError('세션에 만료되었습니다. 다시 로그인 하세요.')
    //   }
    // }

    // handleChange = (e) => {
    //     const { AuthActions } = this.props;
    //     const { name, value } = e.target;
    //
    //     AuthActions.changeInput({
    //         name,
    //         value,
    //         form: 'login'
    //     });
    // }

    // componentWillUnmount() {
    //     const { AuthActions } = this.props;
    //     AuthActions.initializeForm('login')
    // }

    // setError = (message) => {
    //     const { AuthActions } = this.props;
    //     AuthActions.setError({
    //         form: 'login',
    //         message
    //     });

    //     return false;
    // }

    // handleChange = (e,i) => { //0:email, 1:pw
    //      const { AuthActions } = this.props;
    //      const { name, value } = e.target;
    //
    //      AuthActions.changeInput({
    //          name,
    //          value,
    //          form: 'login'
    //      });
    //
    //     const encrypt = (err, key) => {
    //        let cipher = crypto.createCipher('aes-256-cbc', key);
    //        let encipheredpw = cipher.update(err, 'utf-8', 'hex');
    //        // encipheredpw = cipher.setAutoPadding(auto_padding=true);
    //        encipheredpw += cipher.final('hex');
    //        return encipheredpw;
    //      }
    //
    //     const decrypt = (err, key) => {
    //        let decipher = crypto.createDecipher('aes-256-cbc', key);
    //        // let decipheredpw = decipher.setAutoPadding(auto_padding=true);
    //        let decipheredpw = decipher.update(err, 'hex', 'utf-8');
    //        decipheredpw += decipher.final('utf-8');
    //        return decipheredpw;
    //      }
    //
    //
    //      if(i===0){
    //        let login=this.state.loginInfo;
    //        login.email=value;
    //        this.setState({loginInfo:login});
    //      }
    //      else if(i===1){ //단일성 암호화
    //        let login=this.state.loginInfo;
    //        // crypto.randomBytes(64, (err, buf) => {
    //        //   crypto.pbkdf2('value', buf.toString('base64'), 100000, 64, 'sha512', (err, key) => {
    //        //       login.password=key.toString('base64');
    //        //   });
    //        // });
    //        login.password=encrypt(value, key);
    //        this.setState({loginInfo:login});
    //      }
    //  }


    // handleLocalLogin = () => {
    //    const { form, AuthActions, UserActions, history } = this.props;
    //    const { email, password } = form.toJS();
    //    let users = this.state.loginInfo;
    //
    //    const decrypt = (err, key) => {
    //      let decipher = crypto.createDecipher('aes-256-cbc', key);
    //      // let decipheredpw = decipher.setAutoPadding(auto_padding=true);
    //      let decipheredpw = decipher.update(err, 'hex', 'utf-8');
    //      decipheredpw += decipher.final('utf-8');
    //      return decipheredpw;
    //    }
    //
    //    if(users.password = this.state.serverpassword) {
    //      AuthActions.localLogin({ email, password });
    //      console.log('AuthActions Error');
    //      const loggedInfo = this.props.result.toJS();
    //      UserActions.setLoggedInfo(loggedInfo);
    //      history.push('/');
    //      storage.set('loggedInfo', loggedInfo);
    //    }
    //    else {
    //      this.setError('잘못된 계정입니다.');
    //    }
    //    loginHandle.send(this.state.loginInfo.email);
    //
    //  }
    // handleLocalLogin = /*async*/ () => {
    //     const { form, AuthActions, UserActions, history } = this.props;
    //     const { email, password } = form.toJS();
    //     let users = this.state.loginInfo;
    //
    //     // await AuthActions.localLogin({email, password});
    //
    //     const decrypt = (err, key) => {
    //       let decipher = crypto.createDecipher('aes-256-cbc', key);
    //       // let decipheredpw = decipher.setAutoPadding(auto_padding=true);
    //       let decipheredpw = decipher.update(err, 'hex', 'utf-8');
    //       decipheredpw += decipher.final('utf-8');
    //       return decipheredpw;
    //     }
    //
    //     try {
    //       console.log('before await error');
    //         // await AuthActions.localLogin({ email, password });
    //         if(users.password = this.state.serverpassword) {
    //           AuthActions.localLogin({ email, password });
    //           console.log('after await error');
    //             const loggedInfo = this.props.result.toJS();
    //
    //             UserActions.setLoggedInfo(loggedInfo);
    //             // history.push('/');
    //             storage.set('loggedInfo', loggedInfo);
    //         }
    //       // console.log('after await error');
    //       //   const loggedInfo = this.props.result.toJS();
    //       //
    //       //   UserActions.setLoggedInfo(loggedInfo);
    //       //   history.push('/');
    //       //   storage.set('loggedInfo', loggedInfo);
    //
    //     } catch (e) {
    //         console.log('a');
    //         this.setError('잘못된 계정정보입니다.');
    //     }
    //     loginHandle.send(this.state.loginInfo.email);
    // }


    render() {
      // form 에서 email 과 password 값을 읽어옴
        const { email, password } = this.props.form.toJS();
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

                />
                <InputWithLabel
                    name="password"
                    placeholder="****"
                    type="password"
                    value={password}

                />
                {
                  error && <AuthError>{error}</AuthError>
                }
            </AuthContent>
            <div className="Comments_1">
              회원이 아니신가요?
              <div className="Comments_2">
                간단한 회원가입으로 보라봇을 이용할 수 있습니다.
              </div>
            </div>
            <div className="loginButton">
              <AuthButton onClick="/auth/login">로그인</AuthButton>
            </div>
            <div className="registerLink">
              <RightAlignedLink to="/auth/register">회원가입</RightAlignedLink>
            </div>
          </div>
      );
  }
}

// export default connect(
//     (state) => ({
//         form: state.auth.getIn(['login', 'form']),
//         error: state.auth.getIn(['login', 'error']),
//         result: state.auth.get('result')
//     }),
//     (dispatch) => ({
//         AuthActions: bindActionCreators(authActions, dispatch),
//         UserActions: bindActionCreators(userActions, dispatch)
//     })
// )(Login);

export default Login;