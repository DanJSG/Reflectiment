import { BrowserRouter, Switch, Route } from 'react-router-dom'
import './App.scss';
import HomePage from './pages/HomePage';

function App() {
    return (
        <BrowserRouter>
            <Switch>
                <Route exact path="/">
                    <HomePage />
                </Route>
            </Switch>
        </BrowserRouter>
    );
}

export default App;
