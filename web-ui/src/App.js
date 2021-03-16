import { BrowserRouter, Switch, Route } from 'react-router-dom'
import './App.scss';
import NavBar from './components/layout/NavBar';
import AboutPage from './pages/AboutPage';
import HomePage from './pages/HomePage';

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
