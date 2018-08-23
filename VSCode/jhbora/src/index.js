import React from 'react';
import ReactDOM from 'react-dom';
import Root from './Root';
import registerServiceWorker from './registerServiceWorker';
import './index.css';
import configureStore from './redux/configureStore';
import { AppContainer } from 'react-hot-loader';

const store = configureStore();

const render = Component => {
    ReactDOM.render(
        <AppContainer>
            <Component store={store}/>
        </AppContainer>,
        document.getElementById('root')
    );
};

render(Root);

if(module.hot) {
    module.hot.accept('./Root', () => render(Root));
}

registerServiceWorker();

const crypto = require('crypto');

const password = 'abc123';
const secret = 'MySecretKey1$1$234';

const hashed = crypto.createHmac('sha256', secret).update(password).digest('hex');

console.log(hashed);

const jwt = require('jsonwebtoken');
const token = jwt.sign({ foo: 'bar' }, 'secret-key', { expiresIn: '7d' }, (err, token) => {
    if(err) {
        console.log(err);
        return;
    }
    console.log(token);
});

const { jwtMiddleware } = require('./lib/token');

// app.use(bodyParser()); // 바디파서 적용, 라우터 적용코드보다 상단에 있어야합니다.
// app.use(jwtMiddleware);
// router.use('/api', api.routes()); // api 라우트를 /api 경로 하위 라우트로 설정
// app.use(router.routes()).use(router.allowedMethods());
//
// app.listen(port, () => {
//     console.log('heurm server is listening to port ' + port);
// });

require('dotenv').config(); //.env파일에서 환경변수 호출

const Koa = require('koa');
const Router = require('koa-router');

const app = new Koa();
const router = new Router();
const api = require('./api');
