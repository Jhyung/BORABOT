import React, { Component } from 'react';
import axios from 'axios';
import Slider from 'react-slick';

import './CoinRecommend.css';

class CoinRecommend extends Component {
  constructor(props) {
    super(props);

    this.state = {
      volumeHigh: {},
      priceHigh: {},
      biggestGap: {},
      exchangeList: []
    };
  }

  componentDidMount() {    
    axios.get('CoinRecommend')
    .then( response => {
      this.setState({
        volumeHigh: response.data.volumeHigh,
        priceHigh: response.data.priceHigh,
        biggestGap: response.data.biggestGap,
        exchangeList: response.data.exchangeList
      })
    }) 
    .catch( response => { console.log('err\n'+response); } ); // ERROR
  }
  
  render() {
    const { volumeHigh, priceHigh, biggestGap, exchangeList } = this.state

    const settings = {
      autoplay: true,
      autoplaySpeed:1000,
      vertical: true,
      arrows: false,
    };
    return (
      <Slider {...settings} >
        {exchangeList.map((e,i) => {
          return (
            <div style={{height:"100%"}}>
              {e} :: vH:{volumeHigh[e]} :: pH:{priceHigh[e]} :: bG:{biggestGap[e]}
            </div>
          )
        })}
      </Slider>
    );
  }

}

export default CoinRecommend;