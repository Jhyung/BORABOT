import React from 'react';
import styled from 'styled-components';
import oc from 'open-color';
import { Link } from 'react-router-dom';

const RightAligner = styled.div`
    margin-top: 1rem;
    text-align: right;
`;

const StyledLink = styled(Link)`
    color: white;
    &:hover {
        color: ${oc.gray[7]};
    }
`;

const RightAlignedLink = ({to, children}) => (
    <RightAligner>
        <StyledLink to={to}>{children}</StyledLink>
    </RightAligner>
);

export default RightAlignedLink;
