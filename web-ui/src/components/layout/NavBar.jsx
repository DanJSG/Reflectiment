import React, { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import uoyLogo from '../../resources/uoy-logo.svg';


function NavBar() {

    const currentLocation = useLocation();

    return (
        <React.Fragment>
            <nav className="navbar sticky-top navbar-expand-md navbar-dark bg-dark">
                <a className="navbar-brand border-right border-secondary pr-3" href="./">
                    <img src={uoyLogo} alt="University of York Logo" width="180px"/>
                </a>
                <div className="navbar-nav" style={{fontSize: "1.2rem"}}>
                    <Link className={`nav-item nav-link ${currentLocation.pathname === '/' ? 'active' : null}`} to="/">Analyzer</Link>
                    <Link className={`nav-item nav-link ${currentLocation.pathname === '/about' ? 'active' : null}`} to="/about">About</Link>
                </div>
            </nav>
        </React.Fragment>
    )

}

export default NavBar;
