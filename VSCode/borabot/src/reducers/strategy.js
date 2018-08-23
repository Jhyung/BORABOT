const sS = 'storeStrategy';

export function setStrategy(value) {
  return {
    type: sS,
    strategyList: value
  };
}

const Strategy = {
    strategyList: []
};

export const strategy = (state = Strategy, action) => {
  switch(action.type) {
    case sS:
      return Object.assign({}, state, {
        strategyList: action.strategyList
      });
    default:
      return state;
  }
};