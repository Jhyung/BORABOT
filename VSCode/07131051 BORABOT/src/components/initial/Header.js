import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import Main from '../main/Main';

class Header extends Component {

  render() {
    return (
        <div>
            <Link to path="/main" component={Main}>Borabot</Link>
        </div>
    );
  }
}

export default Header;

