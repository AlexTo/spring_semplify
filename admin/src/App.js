import React, {useState} from 'react';
import {Router} from 'react-router-dom';
import {renderRoutes} from 'react-router-config';
import {createBrowserHistory} from 'history';
import MomentUtils from '@date-io/moment';
import {Provider as StoreProvider} from 'react-redux';
import {ThemeProvider} from '@material-ui/styles';
import {MuiPickersUtilsProvider} from '@material-ui/pickers';
import 'react-perfect-scrollbar/dist/css/styles.css';
import {theme} from './theme';
import {configureStore} from './store';
import routes from './routes';
import GoogleAnalytics from './components/GoogleAnalytics';
import CookiesNotification from './components/CookiesNotification';
import ScrollReset from './components/ScrollReset';
import StylesProvider from './components/StylesProvider';
import './mixins/chartjs';
import './mixins/moment';
import './mixins/validate';
import './mixins/prismjs';
import './mock';
import './assets/scss/main.scss';
import * as Keycloak from "keycloak-js";
import {KeycloakProvider} from "@react-keycloak/web";

const keycloakHost = process.env.REACT_APP_KEYCLOAK_HOST;
const keycloakPort = process.env.REACT_APP_KEYCLOAK_PORT;

const initOptions = {
  url: `http://${keycloakHost}:${keycloakPort}/auth`,
  realm: 'semplify',
  clientId: 'semplify-app',
  onLoad: 'login-required'
};

const keycloak = Keycloak(initOptions);
const history = createBrowserHistory();
const store = configureStore();

function App() {
  return (
    <KeycloakProvider keycloak={keycloak} initConfig={{'onLoad': initOptions.onLoad}}>
      <StoreProvider store={store}>
        <ThemeProvider theme={theme}>
          <StylesProvider direction={'ltr'}>
            <MuiPickersUtilsProvider utils={MomentUtils}>
              <Router history={history}>
                <ScrollReset/>
                <GoogleAnalytics/>
                <CookiesNotification/>
                {renderRoutes(routes)}
              </Router>
            </MuiPickersUtilsProvider>
          </StylesProvider>
        </ThemeProvider>
      </StoreProvider>
    </KeycloakProvider>
  );
}

export default App;
