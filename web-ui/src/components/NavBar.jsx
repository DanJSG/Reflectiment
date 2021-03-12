import React from 'react';
import { Link } from 'react-router-dom';
import UoYLogo from '../resources/uoy-logo.svg';


function NavBar() {

    return (
        <React.Fragment>
            <nav className="navbar sticky-top navbar-expand-md navbar-dark bg-dark">
                <a className="navbar-brand border-right border-secondary" href="./">
                    <img src={UoYLogo} alt="" width="180px"/>
                </a>
                <div className="navbar-nav" style={{fontSize: "1.2rem"}}>
                    <Link className="nav-item nav-link active" to="/">Analyser</Link>
                    <Link className="nav-item nav-link" to="/about">About</Link>
                </div>
            </nav>
        </React.Fragment>
    )

}

export default NavBar;
