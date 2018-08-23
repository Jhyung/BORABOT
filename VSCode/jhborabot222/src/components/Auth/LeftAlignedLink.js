import React from 'react';
import styled from 'styled-components';
import oc from 'open-color';
import { Link } from 'react-router-dom';

const LeftAligner = styled.div`
    margin-top: 1rem;
    text-align: left;
`;

const StyledLink = styled(Link)`
    color: white;
    &:hover {
        color: ${oc.gray[7]};
    }
`

const LeftAlignedLink = ({to, children}) => (
    <LeftAligner>
        <StyledLink to={to}>{children}</StyledLink>
    </LeftAligner>
);

export default LeftAlignedLink;
