import React, { Component } from 'react';
import { AuthContent, AuthContent2, InputWithLabel, AuthWrapper, AuthButton, RightAlignedLink, AuthError } from '../../components/Auth';
import { connect } from 'react-redux';
import {bindActionCreators} from 'redux';
import * as authActions from '../../redux/modules/auth';
import { isLength, isAlphanumeric } from 'validator';
import debounce from 'lodash/debounce';
import * as userActions from '../../redux/modules/user';
import storage from '../../lib/storage';
import axios from 'axios';
import './Profile.css';


const profileHandle = new WebSocket("ws://45.120.65.65/BORABOT/profilehandle");


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
          phone_number: null,
          // account: null,
          SecretKey: null,
          APIKey: null,
        }
    }
  }
  // componentWillUnmount() {
  //     const { AuthActions } = this.props;
  //     AuthActions.initializeForm('profile')
  // }

  // setError = (message) => {
  //    const { AuthActions } = this.props;
  //    AuthActions.setError({
  //        form: 'profile',
  //        message
  //    });
  // }

  // handleChange = (e,i) => { //0:name, 1:number, 2:account, 3:SecretKey, 4:APIKey
  //   const { AuthActions } = this.props;
  //   const { name, value } = e.target;
  //
  //   AuthActions.changeInput({
  //     name,
  //     value,
  //     form: 'profile'
  //   });
  //
  //   const encrypt = (err, key) => {
  //      let cipher = crypto.createCipher('aes-256-cbc', key);
  //      let encipheredpw = cipher.update(err, 'utf-8', 'hex');
  //      // encipheredpw = cipher.setAutoPadding(auto_padding=true);
  //      encipheredpw += cipher.final('hex');
  //      return encipheredpw;
  //    }
  //
  //   if(i===0) {
  //     let profile = this.state.profileInfo;
  //     profile.name=value;
  //     this.setState({profileInfo:profile});
  //   }
  //   else if(i===1) {
  //     let profile = this.state.profileInfo;
  //     profile.phone_number=value;
  //     this.setState({profileInfo:profile});
  //   }
  //   // else if(i===2) {
  //   //   let profile = this.state.profileInfo;
  //   //   profile.account=value;
  //   //   this.setState({profileInfo:profile});
  //   // }
  //   else if(i===2) {
  //     let profile = this.state.profileInfo;
  //     profile.SecretKey=encrypt(value, key);
  //     this.setState({profileInfo:profile});
  //   }
  //   else if(i===3) {
  //     let profile = this.state.profileInfo;
  //     profile.APIKey=encrypt(value, key);
  //     this.setState({profileInfo:profile});
  //   }
  // }
  // //
  // handleLocalProfile = () => {
  //   let users = this.state.profileInfo;
  //   console.log(users);
  //   axios.post('/api/signup', users)
  //   .then(res => {
  //     console.log(res);
  //     if(res.data.redirect==="/")
  //       window.location.href = '/auth/profile';
  //   })
  //   .catch(err => console.log(err));
  // }

    render() {
      const { error } = this.props;
      const { name, phone_number, /*account,*/ SecretKey, APIKey } = this.props.form.toJS();
      const { handleChange, handleLocalProfile } = this;

        return (
            <div className="Content">
              <AuthContent title="개인정보">
              <div className="Content_1">
                  <InputWithLabel
                      name="name"
                      placeholder="이름"
                      value={name}
                  />
                  <InputWithLabel
                      name="phone_number"
                      placeholder="핸드폰번호"
                      value={phone_number}
                  />

                <div className="Content_2">
                  <select className="ExchangeList" id="ExchangeList" size='1'>
                    {exchangeList.map((Exchange, i) => {
                      return (<option key={i}> {Exchange} </option>)
                    })}
                  </select>
                  <InputWithLabel
                      name="SecretKey"
                      placeholder="SecretKey"
                      value={SecretKey}
                  />
                  <InputWithLabel
                      name="APIKey"
                      placeholder="APIKey"
                      value={APIKey}
                  />
                  <div className="profileButton1">
                    <AuthButton onClick="/auth/profile">완료</AuthButton>
                  </div>
                  <div className="profileButton2">
                    <AuthButton onClick="/auth/login">수정</AuthButton>
                  </div>
                </div>
              </div>
            </AuthContent>
          </div>
        );
      }
}
// export default connect(
//     (state) => ({
//         form: state.auth.getIn(['profile', 'form']),
//         error: state.auth.getIn(['profile', 'error']),
//         exists: state.auth.getIn(['profile', 'exists']),
//         result: state.auth.get('result')
//     }),
//     (dispatch) => ({
//         AuthActions: bindActionCreators(authActions, dispatch),
//         UserActions: bindActionCreators(userActions, dispatch)
//     }),

// )(Profile);
export default Profile;