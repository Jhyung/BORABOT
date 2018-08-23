import React, { Component } from 'react';
import './App.css';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import Header from './components/initial/Header';
import Initial from './components/initial/Initial';
import Main from './main/index';

class App extends Component {

  render() {

    return (
      <BrowserRouter basename={process.env.REACT_APP_ROUTER_BASE || ''}>
        <div>
          <Header/>
          <Route path="/" component={Initial}/>
          <Route path="/main" component={Main}/>
        </div>
      </BrowserRouter>
    );
  }
}

export default App;

