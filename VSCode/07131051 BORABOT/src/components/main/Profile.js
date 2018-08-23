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

  setError = (message) => {
       const { AuthActions } = this.props;
       AuthActions.setError({
           form: 'profile',
           message
       });
   }

   handleChange = (e,i) => {
        const { AuthActions } = this.props;
        const { name, number, account, SecretKey, APIKey } = e.target;

        AuthActions.changeInput({
            name,
            number,
            account,
            SecretKey,
            APIKey,
            form: 'profile'
        });
    }


    render() {
        return (
            <div>
              <Textfield
                name="name"
                placeholder="name"
                valude={this.state.name}
                onChange={e=>this.change(e)}
              />
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
                  {coinList.map((Exchange, i) => {
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
export default Profile;
