import React, { Component } from 'react';
import { AuthContent, InputWithLabel, AuthButton, RightAlignedLink, AuthError } from '../../components/Auth';
import { connect } from 'react-redux';
import {bindActionCreators} from 'redux';
import * as authActions from '../../redux/modules/auth';
import {isEmail, isLength, isAlphanumeric} from 'validator';
import debounce from 'lodash/debounce';
import * as userActions from '../../redux/modules/user';
import storage from '../../lib/storage';
import axios from 'axios';
import './Register.css';



const regHandle = new WebSocket("ws://45.120.65.65/Auth/authandle");

class Register extends Component {

  constructor(props) {
    super(props);
    this.state={
      registerInfo:
        {
          email: null,
          password: null,
          name: null,
          number: null,
          account: null,
          checkBox1: (!!this.props.complete) || false,
          checkBox2: (!!this.props.complete) || false,
        }
    }
  }

  componentWillUnmount() {
      const { AuthActions } = this.props;
      AuthActions.initializeForm('register')
  }

   setError = (message) => {
        const { AuthActions } = this.props;
        AuthActions.setError({
            form: 'register',
            message
        });
    }

    //검증 작업
    validate = {
        email: (value) => {
            if(!isEmail(value)) {
                this.setError('잘못된 이메일 형식 입니다.');
                return false;
            }
            return true;
        },
        password: (value) => {
            if(!isLength(value, { min: 6 })) {
                this.setError('비밀번호를 6자 이상 입력하세요.');
                return false;
            }
            this.setError(null); // 이메일과 아이디는 에러 null 처리를 중복확인 부분에서 진행
            return true;
        },
        passwordConfirm: (value) => {
            if(this.props.form.get('password') !== value) {
                this.setError('비밀번호확인이 일치하지 않습니다.');
                return false;
            }
            this.setError(null);
            return true;
        }
    }

    checkEmailExists = debounce(async (email) => {
       const { AuthActions } = this.props;
       try {
           await AuthActions.checkEmailExists(email);
           if(this.props.exists.get('email')) {
               this.setError('이미 존재하는 이메일입니다.');
           } else {
               this.setError(null);
           }
       } catch (e) {
           console.log(e);
       }
   }, 300)

   checkTermBox = debounce(async (TermBox) => {
      const { AuthActions } = this.props;
      console.log("checkBox1");
      try {
          await AuthActions.checkTermBox(TermBox);
          if(this.props.complete.get('TermBox')) {
            console.log("checkBox2");
              this.setError('이용약관 동의를 하셔야 합니다.');
          } else {
            console.log("checkBox3");
              this.setError(null);
          }
      } catch (e) {
        console.log("checkBox4");
          console.log(e);
      }
  }, 300)

   handleChange = (e,i) => { //0:email, 1:pw, 2:checkBox1, 3:checkBox2
        const { AuthActions } = this.props;
        const { name, value } = e.target;

        AuthActions.changeInput({
            name,
            value,
            form: 'register'
        });

        // 검증작업 진행
        const validation = this.validate[name](value);

        // TODO: 이메일 중복 확인
        // name 에 따라 이메일체크할지 아이디 체크 할지 결정
        // 이메일 체크 및 이용약관 동의 체크
        const check = name === 'email' ? this.checkEmailExists : this.checkTermBox;
        check(value);

        const crypto = require('crypto');

        // crypto.createHash('sha512').update('value').digest('base64');
        // crypto.randomBytes(64, (err, buf) => {
        //   crypto.pbkdf2('value', buf.toString('base64'), 100000, 64, 'sha512', (err, key) => {
        //     console.log(key.toString('base64'));
        //   });
        // });

        if(i===0){
          let register=this.state.registerInfo;
          register.email=value;
          this.setState({registerInfo:register});
        }
        else if(i===1){ //단일성 암호화
          let register=this.state.registerInfo;
          register.password=crypto.createHash('sha512').update('value').digest('base64');
          this.setState({registerInfo:register});
        }
        else if(i===2){
          let register=this.state.registerInfo;
          register.checkBox1=value;
          this.setState({registerInfo:register});
        }
        else if(i===3){
          let register=this.state.registerInfo;
          register.checkBox2=value;
          this.setState({registerInfo:register});
        }
    }

    handleLocalRegister=()=>{
      let users=this.state.registerInfo;
      console.log(users);
      axios.post('/api/signup',users)
      .then(res=>{
        console.log(res);
        if(res.data.redirect==="/")
          window.location.href = '/auth/login';
      })
      .catch(err=>console.log(err));
    }


    render() {
        const { error } = this.props;
        const { email, password, passwordConfirm } = this.props.form.toJS();
        const { handleChange, handleLocalRegister } = this;

        return (
          <div>
            <AuthContent title="회원가입">
                <InputWithLabel
                    label="이메일"
                    name="email"
                    placeholder="이메일"
                    value={email}
                    onChange={(e)=>handleChange(e,0)}
                />
                <InputWithLabel
                    label="비밀번호"
                    name="password"
                    placeholder="비밀번호"
                    type="password"
                    value={password}
                    onChange={(e)=>handleChange(e,1)}
                />
                <InputWithLabel
                    label="비밀번호 확인"
                    name="passwordConfirm"
                    placeholder="비밀번호 확인"
                    type="password"
                    value={passwordConfirm}
                    onChange={handleChange}
                />

                <div className="Term1">
                  <input
                    type="checkbox"
                    checked={this.state.complete}
                    ref="complete"
                    // onChange={(e)=>handleChange(e,2)}
                    // onChange={this.handleChange}
                  /> 이용약관1
                </div>
                <div className="Term2">
                  <input
                    type="checkbox"
                    checked={this.state.complete}
                    ref="complete"
                    // onChange={(e)=>handleChange(e,3)}
                    // onChange={this.handleChange}
                  /> 이용약관2
                </div>
                {
                  error && <AuthError>{error}</AuthError>
                }
                <div className="registerButton">
                  <AuthButton onClick={ () => {

                    // if(this.state.checkBox1 && this.state.checkBox2 == "complete")
                    //   this.handleLocalRegister();
                    // else if(this.state.checkBox1 || this.state.checkBox2 != "complete")
                    //   this.checkTermBox;
                    this.handleLocalRegister();
                    }
                  }>회원가입</AuthButton>
                </div>
              </AuthContent>
            <RightAlignedLink to="/auth/login">로그인</RightAlignedLink>
          </div>
        );
    }
}


export default connect(
    (state) => ({
        form: state.auth.getIn(['register', 'form']),
        error: state.auth.getIn(['register', 'error']),
        exists: state.auth.getIn(['register', 'exists']),
        result: state.auth.get('result')
    }),
    (dispatch) => ({
        AuthActions: bindActionCreators(authActions, dispatch),
        UserActions: bindActionCreators(userActions, dispatch)
    }),

)(Register);
