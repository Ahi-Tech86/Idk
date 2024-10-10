import React from 'react';

export default function Header(props) {

    const handleLoginClick = () => {
    }

    return (
        <header className="App-header">
            <img src={props.logoSrc} className="App-logo" alt="logo" />
            <button className="App-login" onClick={handleLoginClick}>Login</button>
        </header>
    );
};