import React from 'react';
import styled from 'styled-components';
import oc from 'open-color';

const Title2 = styled.div`
  position: absolute;
   top: 50%;
   left: 50%;
   transform: translate(-50%, -50%);
`;

const AuthContent2 = ({title, children}) => (
    <div>
        <Title2>{title}</Title2>
        {children}
    </div>
);

export default AuthContent2;
