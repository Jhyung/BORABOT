import React, { Component } from 'react';
import Header, { LoginButton } from '../../components/Base/Header';
import BackTesting from '../../components/Base/Header';
import Blog from '../../components/Base/Header';
import Profile from '../../components/Base/Header';
import LogOut from '../../components/Base/Header';
import { connect } from 'react-redux';
import * as userActions from '../../redux/modules/user';
import { bindActionCreators } from 'redux';
import storage from '../../lib/storage';


class HeaderContainer extends Component {

    handleLogout = async () => {
        const { UserActions } = this.props;
        try {
            await UserActions.logout();
        } catch (e) {
            console.log(e);
        }

        storage.remove('loggedInfo');
        window.location.href = '/'; // 홈페이지로 새로고침
    }

    render() {
        const { visible, user } = this.props;
        if(!visible) return null;

        return (
          <div>
            <Header>
                { user.get('logged')
                    ? (<div>
                        {user.getIn(['loggedInfo', 'username'])} <div onClick={this.handleLogout}>(로그아웃)</div>
                    </div> )
                    : <LoginButton/>
                }
            </Header>
          </div>
        );
    }
}


export default connect(
    (state) => ({
        visible: state.base.getIn(['header', 'visible']),
        user: state.user
    }),
    (dispatch) => ({
        UserActions: bindActionCreators(userActions, dispatch)
    })
)(HeaderContainer);
