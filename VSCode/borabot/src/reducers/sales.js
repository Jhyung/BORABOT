// ===================================================================
// 현재 거래 설정 저장 state (거래 시작, 차트 표시, 진행 중인 거래 선택에 사용)
// ===================================================================

const sS = 'setSales';
const sC = 'setChart';

export function setSales(value) {
	return {
		type: sS,
    sales: value.sales,
		exchangeIndex: value.exchangeIndex,
    baseIndex: value.baseIndex,
    coinIndex: value.coinIndex,
    intervalIndex: value.intervalIndex
	};
}

export function setChart(value) {
	return {
		type: sC,
    sales: value.sales
	};
}

const Sales = {
  sales: true,
  exchangeIndex: 0,
  baseIndex: 0,
  coinIndex: 0,
	intervalIndex: 0
};

export const sales = (state = Sales, action) => {
	switch(action.type) {
		case sS:
			return Object.assign({}, state, {
        sales: action.sales,
        exchangeIndex: action.exchangeIndex,
        baseIndex: action.baseIndex,
        coinIndex: action.coinIndex,
        intervalIndex: action.intervalIndex
			});
		case sC:
			return Object.assign({}, state, {
        sales: action.sales
			});
		default:
			return state;
	}
};