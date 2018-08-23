import React, { Component } from 'react';
import Login from './Login';
import Register from './Register';

class Initial extends Component {

  render() {
    return (
        <div>
            <Login/>
            <Register/>
        </div>
    );
  }
}

export default Initial;
