const Joi = require('joi');
const Account = require('models/Account');


//pbkdf2
var bkfd2Password = require('pbkdf2-password');
var hasher = bkfd2Password();

// // login
// app.get('/auth/login', (req, res) => {
//   if(!req.session.displayName){
//     res.render('login');
//   } else {
//     req.session.save(() => {
//       res.redirect('/welcome');
//     });
//   }
// });
// app.post('/auth/login', (req, res) => {
//   var email = req.body.email;
//   var password = req.body.password;
//   for(var i=0; i {
//         if(hash === user.password) {
//           req.session.displayName = user.displayName;
//           req.session.save(() => {
//             res.redirect('/welcome');
//           });
//         } else {
//           res.render('notlogin');
//         }
//       });
//     }
//   res.render('notlogin');
// });
//
// // register
// app.get('/auth/register', (req, res) => {
//   res.render('register');
// });
// app.post('/auth/register', (req, res) => {
//   hasher({password: req.body.mypw}, (err, pass, salt, hash) => {
//     var user = {
//       email: req.body.email,
//       password: hash,
//       salt: salt,
//       displayName: req.body.displayName
//     }
//     users.push(user);
//     req.session.displayName = req.body.displayName;
//     req.session.save(() => {
//       res.redirect('/welcome');
//     });
//   });
// });


// 로컬 회원가입
exports.localRegister = async (ctx) => {
    ctx.body = 'register';

    // 계정 생성
   let account = null;
   try {
       account = await Account.localRegister(ctx.request.body);
   } catch (e) {
       ctx.throw(500, e);
   }

   let token = null;
   try {
       token = await account.generateToken();
   } catch (e) {
       ctx.throw(500, e);
   }

   ctx.cookies.set('access_token', token, { httpOnly: true, maxAge: 1000 * 60 * 60 * 24 * 7 });
   ctx.body = account.profile; // 프로필 정보로 응답합니다.
};

// 로컬 로그인
//
// exports.localLogin = async (ctx) => {
//     // 데이터 검증
//     const schema = Joi.object().keys({
//         email: Joi.string().email().required(),
//         password: Joi.string().required()
//     });
//
//     const result = Joi.validate(ctx.request.body, schema);
//
//     if(result.error) {
//         ctx.status = 400; // Bad Request
//         return;
//     }
//
//     const { email, password } = ctx.request.body;
//
//     let account = null;
//     try {
//         // 이메일로 계정 찾기
//         account = await Account.findByEmail(email);
//     } catch (e) {
//         ctx.throw(500, e);
//     }
//
//     if(!account || !account.validatePassword(password)) {
//     // 유저가 존재하지 않거나 || 비밀번호가 일치하지 않으면
//         ctx.status = 403; // Forbidden
//         return;
//     }
//
//     ctx.body = account.profile;
// };

// exports.localLogin = async (ctx) => {
//     ctx.body = 'login';
//
//     if(!account || !account.validatePassword(password)) {
//     // 유저가 존재하지 않거나 || 비밀번호가 일치하지 않으면
//         ctx.status = 403;
//         return;
//     }
//
//     let token = null;
//     try {
//         token = await account.generateToken();
//     } catch (e) {
//         ctx.throw(500, e);
//     }
//
//     ctx.cookies.set('access_token', token, { httpOnly: true, maxAge: 1000 * 60 * 60 * 24 * 7 });
//     ctx.body = account.profile; // 프로필 정보로 응답합니다.
// };

// 이메일 / 아이디 존재유무 확인
exports.exists = async (ctx) => {
  const { key, value } = ctx.params;
  let account = null;

  try {
      // key 에 따라 findByEmail 혹은 findByUsername 을 실행합니다.
      account = await (key === 'email' ? Account.findByEmail(value) : Account.findByUsername(value));
  } catch (e) {
      ctx.throw(500, e);
  }

  ctx.body = {
      exists: account !== null
  };
};

// 로그아웃
exports.logout = async (ctx) => {
  ctx.cookies.set('access_token', null, {
      maxAge: 0,
      httpOnly: true
  });
  ctx.status = 204;
};

exports.check = (ctx) => {
    const { user } = ctx.request;

    if(!user) {
        ctx.status = 403; // Forbidden
        return;
    }

    ctx.body = user.profile;
};
