import React from 'react';
import { BrowserRouter } from 'react-router-dom';
// import App from './App';
import Header from './components/initial/Header'

const Root = () => (
    <div>
    <Header/>
    <iframe frameBorder="0"></iframe>
    </div>
    // <BrowserRouter>
    //     <App/>
    // </BrowserRouter>
);

export default Root;