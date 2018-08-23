import React, { Component } from 'react';
import { AuthContent, InputWithLabel, AuthButton, RightAlignedLink, AuthError } from '../../components/Auth';
import { connect } from 'react-redux';
import {bindActionCreators} from 'redux';
import * as authActions from '../../redux/modules/auth';
import { isLength } from 'validator';
import debounce from 'lodash/debounce';
import * as userActions from '../../redux/modules/user';
import storage from '../../lib/storage';
import axios from 'axios';


const crypto = require('crypto');
const exchangeList = ["bithumb", "bittrex", "binance", "korbit", "coinone"]

var key = "tHis_iS_pRofILe_pRivaTe_Key";

class Profile extends Component {

  constructor(props) {
    super(props);
    this.state={
      profileInfo:
        {
          name: null,
          number: null,
          account: null,
          SecretKey: null,
          APIKey: null,
        }
    }
  }
  componentWillUnmount() {
      const { AuthActions } = this.props;
      AuthActions.initializeForm('profile')
  }

  setError = (message) => {
     const { AuthActions } = this.props;
     AuthActions.setError({
         form: 'profile',
         message
     });
  }

  handleChange = (e,i) => { //0:name, 1:number, 2:account, 3:SecretKey, 4:APIKey
    const { AuthActions } = this.props;
    const { name, value } = e.target;

    AuthActions.changeInput({
      name,
      value,
      form: 'profile'
    });

    const encrypt = (err, key) => {
       let cipher = crypto.createCipher('aes-256-cbc', key);
       let encipheredpw = cipher.update(err, 'utf-8', 'hex');
       // encipheredpw = cipher.setAutoPadding(auto_padding=true);
       encipheredpw += cipher.final('hex');
       return encipheredpw;
     }

    if(i===0) {
      let profile = this.state.profileInfo;
      profile.name=value;
      this.setState({profileInfo:profile});
    }
    else if(i===1) {
      let profile = this.state.profileInfo;
      profile.number=value;
      this.setState({profileInfo:profile});
    }
    else if(i===2) {
      let profile = this.state.profileInfo;
      profile.account=value;
      this.setState({profileInfo:profile});
    }
    else if(i===3) {
      let profile = this.state.profileInfo;
      profile.SecretKey=encrypt(value, key);
      this.setState({profileInfo:profile});
    }
    else if(i===4) {
      let profile = this.state.profileInfo;
      profile.APIKey=encrypt(value, key);
      this.setState({profileInfo:profile});
    }
  }

  handleLocalProfile = () => {
    let users = this.state.profileInfo;
    console.log(users);
    axios.post('/api/signup', users)
    .then(res => {
      console.log(res);
      if(res.data.redirect==="/")
        window.location.href = '/auth/profile';
    })
    .catch(err => console.log(err));


  }

    render() {
      const { error } = this.props;
      const { name, number, account, SecretKey, APIKey } = this.props.form.toJS();
      const { handleChange, handleLocalRegister } = this;

        return (
            <div>
              <AuthContent title="개인정보">
                  <InputWithLabel
                      name="name"
                      placeholder="이름"
                      value={name}
                      onChange={(e)=>handleChange(e,0)}
                  />
                  <InputWithLabel
                      name="number"
                      placeholder="핸드폰번호"
                      value={number}
                      onChange={(e)=>handleChange(e,1)}
                  />
                  <InputWithLabel
                      name="account"
                      placeholder="입금계좌"
                      value={account}
                      onChange={(e)=>handleChange(e,2)}
                  />
              </AuthContent>
              <AuthContent title="거래소 정보">
                <select className="ExchangeList" id="ExchangeList" size='1'>
                  {exchangeList.map((Exchange, i) => {
                    return (<option key={i}> {Exchange} </option>)
                  })}
                </select>
                <InputWithLabel
                    name="SecretKey"
                    placeholder="SecretKey"
                    value={SecretKey}
                    onChange={(e)=>handleChange(e,3)}
                />
                <InputWithLabel
                    name="APIKey"
                    placeholder="APIKey"
                    value={APIKey}
                    onChange={(e)=>handleChange(e,4)}
                />
              </AuthContent>
              <RightAlignedLink to="/auth/profile">완료</RightAlignedLink>
            </div>
          );
      }
}
export default connect(
    (state) => ({
        form: state.auth.getIn(['profile', 'form']),
        error: state.auth.getIn(['profile', 'error']),
        exists: state.auth.getIn(['profile', 'exists']),
        result: state.auth.get('result')
    }),
    (dispatch) => ({
        AuthActions: bindActionCreators(authActions, dispatch),
        UserActions: bindActionCreators(userActions, dispatch)
    }),

)(Profile);
