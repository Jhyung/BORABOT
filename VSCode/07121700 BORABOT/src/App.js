import React, { Component } from 'react';
import { Route } from 'react-router-dom';
import { Initial } from './components/initial'
import { Main } from './components/main/Main'
import Header from './components/initial/Header'

class App extends Component {
  render() {

    return (
      <div>
        <Route exact path="/" component={Initial}/>
      </div>
    );
  }
}

export default App;

