import React from 'react';
import styled from 'styled-components';
import oc from 'open-color';
import { shadow } from '../../lib/styleUtils';

const Wrapper = styled.div`
    float: right;
    margin-top: 1rem;
    margin-bottom: 1rem;
    padding: 0.3rem;

    background: white;
    color: black;

    text-align: center;
    font-size: 1.25rem;
    font-weight: 500;

    cursor: pointer;
    user-select: none;
    transition: .2s all;

    &:active {
        background: ${oc.grape[6]};
    }

`;

const AuthButton = ({children, onClick}) => (
    <Wrapper onClick={onClick}>
        {children}
    </Wrapper>
);


export default AuthButton;
