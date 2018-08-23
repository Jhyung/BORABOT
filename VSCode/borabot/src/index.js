import React from 'react';
import ReactDOM from 'react-dom';
import { AppContainer } from 'react-hot-loader';
import { createStore } from 'redux';
import { Provider  } from 'react-redux';

import App from './App.js';
import reducers from './reducers';
import { loadState, saveState } from './reducers/localStorage';

import registerServiceWorker from './registerServiceWorker';

import './index.css';


const persistedState = loadState();
const store = createStore(reducers, persistedState);

store.subscribe(() => {
  saveState(store.getState())
})

const rootEl = document.getElementById('root');

ReactDOM.render(
  <AppContainer>
    <Provider store = {store}>
      <App />
    </Provider>
  </AppContainer>
  ,
  rootEl
);

if (module.hot) {
  module.hot.accept('./App', () => {
    const NextApp = require('./App').default; 
    ReactDOM.render(
      <AppContainer>
        <Provider store = {store}>
          <NextApp />
        </Provider>
      </AppContainer>,
      rootEl
    );
  });
}
  registerServiceWorker();
