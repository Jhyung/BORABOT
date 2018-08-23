import React, {Component} from 'react';
import './Accounts.css';


// From DB
const wallet = [{
    exchange : "BITTREX",
    balance : "300ETH",
}
]

class Accounts extends Component{

    render() {
        return(
            <div>
                <div>
                    Wallet<br></br>{wallet[0].exchange} &nbsp;/&nbsp; {wallet[0].balance}

                </div>
            </div>
        );
    }

}


export default Accounts;