import React, { Component } from 'react';
import { Route, Switch } from 'react-router-dom';
import { Home, About } from 'pages';


class App extends Component {
    render() {
        return (
            <div>
                asdfasdf
                    <Route exact path="/" render={()=>(
                        <h3>exact path="/"</h3>
                    )}/>
                    <Route path="/main" render={()=>(
                        <h3>path="/main"</h3>
                    )}/>
                <Switch>
                    <Route exact path="/main" render={()=>(
                        <h3>exact path="/main"</h3>
                    )}/>
                    <Route exact path="http://localhost:8080/BORABOT/" render={()=>(
                        <h3>exact path="http://localhost:8080/BORABOT/"</h3>
                    )}/>
                    <Route exact path="http://localhost:8080/BORABOT/main" render={()=>(
                        <h3>exact path="http://localhost:8080/BORABOT/main"</h3>
                    )}/>
                    <Route exact path="/BORABOT" render={()=>(
                        <h3>exact path="/BORABOT"</h3>
                    )}/>
                    <Route exact path="/BORABOT/main" render={()=>(
                        <h3>exact path="/BORABOT/main"</h3>
                    )}/>
                    <Route path="/" render={()=>(
                        <h3>path="/"</h3>
                    )}/>
                    <Route path="http://localhost:8080/BORABOT/" render={()=>(
                        <h3>path="http://localhost:8080/BORABOT/"</h3>
                    )}/>
                    <Route path="http://localhost:8080/BORABOT/main" render={()=>(
                        <h3>path="http://localhost:8080/BORABOT/main"</h3>
                    )}/>
                    <Route path="/BORABOT" render={()=>(
                        <h3>path="/BORABOT"</h3>
                    )}/>
                    <Route path="/BORABOT/main" render={()=>(
                        <h3>path="/BORABOT/main"</h3>
                    )}/>
                    <Route render={()=>(
                        <h3>hihihi</h3>
                    )}/>
                </Switch>
            </div>
        );
    }
}

export default App;