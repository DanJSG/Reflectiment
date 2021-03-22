import { BrowserRouter, Switch, Route } from 'react-router-dom'
import './App.scss';
import NavBar from './components/layout/NavBar';
import AboutPage from './pages/AboutPage';
import HomePage from './pages/HomePage';

/**
 * Main application entrypoint. Contains the global navigation header bar and 
 * maps the pages to specific browser routes.
 * 
 * @returns the DOM elements to render
 */
function App() {

    return (
        <BrowserRouter>
            <NavBar />
            <Switch>
                <Route exact path="/">
                    <HomePage />
                </Route>
                <Route exact path="/about">
                    <AboutPage />
                </Route>
            </Switch>
        </BrowserRouter>
    );
}

export default App;
