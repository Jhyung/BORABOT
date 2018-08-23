import React,{Component} from 'react';
import './WalletInfoChild.css';


class WalletInfoChild extends Component{

    state = {
        exchange : "None",
        content : "Please, Select Exchange"
    }

    render() {
        return(
            <div>
                <div className="WalletInfoChild">{this.state.exchange}<br/>{this.state.content}</div>
            </div>
        );
    }
}

export default WalletInfoChild;
