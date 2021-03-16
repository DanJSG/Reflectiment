import { BrowserRouter, Switch, Route } from 'react-router-dom'
import './App.scss';
import NavBar from './components/layout/NavBar';
import HomePage from './pages/HomePage';

function App() {

    return (
        <BrowserRouter>
            <NavBar />
            <Switch>
                <Route exact path="/">
                    <HomePage />
                </Route>
            </Switch>
        </BrowserRouter>
    );
}

export default App;
