import React from 'react';
import { BrowserRouter, Route } from 'react-router-dom';
import App from './App';
import { Provider } from 'react-redux';

import { Home, Auth } from './pages';

const Root = ({store}) => {
    return (
        <Provider store={store}>
            <BrowserRouter>
                <div>
                  <Route path="/" component={App}/>
                  <Route path="/auth" component={Auth}/>
                </div>
            </BrowserRouter>
        </Provider>
    );
};

export default Root;
