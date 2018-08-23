// ====================
// 로그인 상태 저장 state
// ====================


const LOGIN = 'LOGIN';
const LOGOUT = 'LOGOUT';

export function login() {
	return {
		type: LOGIN
	};
}

export function logout() {
	return {
		type: LOGOUT
	};
}

const LoginStatus = {
	email: '',
	login: false
};

export const logInOut = (state = LoginStatus, action) => {
	switch(action.type) {
		case LOGIN:
			return Object.assign({}, state, {
				login: true
			});
		case LOGOUT:
			return Object.assign({}, state, {
				email: '',
				login: false
			});
		default:
			return state;
	}
};
