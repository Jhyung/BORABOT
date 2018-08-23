import React from 'react';
import { BrowserRouter, Route } from 'react-router-dom';
import App from './App';
import { Provider } from 'react-redux';
import Login from './containers/Auth/Login';
import { Home, Auth } from './pages';

const Root = ({store}) => {
    return (
        // <Provider store={store}>
            <BrowserRouter>
                <div>
                  <Route exact path="/" component={App}/>
                  <Route path="/auth" component={Auth}/>
                  <Route path="/containers/auth/login" component={Login}/>
                </div>
            </BrowserRouter>
        // </Provider>
    );
};

export default Root;
